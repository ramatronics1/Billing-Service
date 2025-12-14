package com.myorg.billing_backend.service;

import com.myorg.billing_backend.model.*;
import com.myorg.billing_backend.repo.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BillingService {

    private final SubscriptionRepository subscriptionRepo;
    private final PlanRepository planRepo;
    private final InvoiceRepository invoiceRepo;
    private final PaymentAttemptRepository attemptRepo;

    private static final int MAX_RETRIES = 3;

    public BillingService(SubscriptionRepository subscriptionRepo,
                          PlanRepository planRepo,
                          InvoiceRepository invoiceRepo,
                          PaymentAttemptRepository attemptRepo) {
        this.subscriptionRepo = subscriptionRepo;
        this.planRepo = planRepo;
        this.invoiceRepo = invoiceRepo;
        this.attemptRepo = attemptRepo;
    }

    /**
     * Main scheduled entry point.
     * Runs every minute for development. Adjust cron for production.
     */
    @Scheduled(fixedDelayString = "${billing.scheduler.delay:60000}")
    public void runBillingCycle() {
        try {
            processInvoiceGeneration();
            processPaymentAttempts();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 1) Generate invoices for eligible subscriptions.
     * Simple rule: active subscriptions with no 'open' invoice.
     */
    @Transactional
    public void processInvoiceGeneration() {
        List<Subscription> subs = subscriptionRepo.findByTenantId(null == null ? 0L : 0L); // placeholder to get all? We'll fetch differently below
        // We will fetch all subscriptions and filter in Java for active - simpler and safe for small scale.
        subs = subscriptionRepo.findAll(); // WARNING: ok for small data sets — replace with query for production

        LocalDate today = LocalDate.now();

        for (Subscription s : subs) {
            if (!"active".equalsIgnoreCase(s.getStatus())) continue;
            // check if there's any open invoice for this subscription's tenant for now
            // Simplification: we create invoice per subscription if there is no OPEN invoice in DB with same tenant and similar amount.
            boolean hasOpen = invoiceRepo.findByTenantIdAndStatus(s.getTenantId(), "open").stream()
                    .anyMatch(inv -> inv.getInvoiceNo() != null && inv.getInvoiceNo().contains("SUB-" + s.getId()));
            if (hasOpen) continue;

            // Get plan price
            Optional<Plan> maybePlan = planRepo.findById(s.getPlanId());
            if (maybePlan.isEmpty()) continue;
            Plan plan = maybePlan.get();

            // create invoice
            Invoice inv = new Invoice();
            inv.setTenantId(s.getTenantId());
            inv.setAmountCents(plan.getPriceCents());
            inv.setCurrency("INR"); // default; replace later
            inv.setIssuedAt(OffsetDateTime.now(ZoneOffset.UTC));
            inv.setDueAt(inv.getIssuedAt().plusDays(7)); // net 7
            inv.setStatus("open");
            // invoice_no encodes subscription for traceability
            inv.setInvoiceNo("SUB-" + s.getId() + "-" + UUID.randomUUID().toString().substring(0,8));
            inv.setPdfPath(null);
            Invoice saved = invoiceRepo.save(inv);

            // create initial payment attempt (pending) and try immediate
            PaymentAttempt pa = new PaymentAttempt();
            pa.setInvoiceId(saved.getId());
            pa.setTenantId(saved.getTenantId());
            pa.setAttemptNumber(1);
            pa.setStatus("pending");
            pa.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
            pa.setNextAttemptAt(OffsetDateTime.now(ZoneOffset.UTC)); // attempt now
            attemptRepo.save(pa);
        }
    }

    /**
     * 2) Process pending payment attempts that are due.
     */
    @Transactional
    public void processPaymentAttempts() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        List<PaymentAttempt> due = attemptRepo.findByNextAttemptAtBeforeAndStatus(now, "pending");
        for (PaymentAttempt pa : due) {
            try {
                pa.setLastAttemptAt(now);
                // simulate payment (replace with gateway call)
                boolean success = simulatePayment(pa);
                if (success) {
                    pa.setStatus("success");
                    attemptRepo.save(pa);

                    // update invoice -> paid
                    Invoice invoice = invoiceRepo.findById(pa.getInvoiceId()).orElseThrow();
                    invoice.setStatus("paid");
                    invoice.setIssuedAt(invoice.getIssuedAt() == null ? OffsetDateTime.now(ZoneOffset.UTC) : invoice.getIssuedAt());
                    invoiceRepo.save(invoice);
                } else {
                    // failed
                    pa.setStatus("failed");
                    pa.setLastError("simulated failure");
                    attemptRepo.save(pa);

                    // determine retry
                    int nextAttempt = pa.getAttemptNumber() + 1;
                    if (nextAttempt <= MAX_RETRIES) {
                        PaymentAttempt next = new PaymentAttempt();
                        next.setInvoiceId(pa.getInvoiceId());
                        next.setTenantId(pa.getTenantId());
                        next.setAttemptNumber(nextAttempt);
                        next.setStatus("pending");
                        next.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
                        // exponential backoff in minutes
                        long backoffMinutes = (long) Math.pow(2, nextAttempt - 1);
                        next.setNextAttemptAt(OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(backoffMinutes));
                        attemptRepo.save(next);
                    } else {
                        // mark invoice failed after max retries
                        Invoice invoice = invoiceRepo.findById(pa.getInvoiceId()).orElseThrow();
                        invoice.setStatus("failed");
                        invoiceRepo.save(invoice);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                pa.setLastError(ex.getMessage());
                pa.setStatus("failed");
                attemptRepo.save(pa);
            }
        }
    }

    /**
     * Simulate payment outcome. Replace this method with a gateway call,
     * ensuring idempotency and proper error handling.
     */
    private boolean simulatePayment(PaymentAttempt attempt) {
        // deterministic simulation for dev: succeed for attemptNumber == 1 for odd invoiceId else random
        if (attempt.getAttemptNumber() == 1) {
            return Math.random() > 0.3; // 70% success on first attempt
        } else {
            return Math.random() > 0.5; // 50% on retries
        }
    }
}
