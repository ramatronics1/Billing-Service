-- V2__seed_data.sql - seed demo data for local development
-- Inserts a tenant, a user (password placeholder), a plan, a customer, a subscription and a payment.

INSERT INTO tenants (name, contact_email)
VALUES ('Acme Corp', 'billing@acme.example')
    ON CONFLICT DO NOTHING;

-- Insert a user for Acme (password_hash is placeholder; don't store plaintext in real apps)
INSERT INTO users (tenant_id, email, password_hash, role)
SELECT t.id, 'admin@acme.example', 'placeholder-hash', 'admin'
FROM tenants t WHERE t.name = 'Acme Corp'
    ON CONFLICT (tenant_id, email) DO NOTHING;

-- Insert a basic plan
INSERT INTO plans (tenant_id, name, price_cents, billing_cycle, features_json, active)
SELECT t.id, 'Basic', 999, 'monthly', '{"max_users":1}', true
FROM tenants t WHERE t.name = 'Acme Corp'
    ON CONFLICT DO NOTHING;

-- Insert a customer
INSERT INTO customers (tenant_id, email, external_id)
SELECT t.id, 'alice@acme.example', 'cust-acme-alice'
FROM tenants t WHERE t.name = 'Acme Corp'
    ON CONFLICT DO NOTHING;

-- Insert a subscription (ties to the first plan & customer for that tenant)
INSERT INTO subscriptions (tenant_id, customer_id, plan_id, start_date, status, auto_renew, metadata)
SELECT t.id, c.id, p.id, CURRENT_DATE, 'active', true, '{"seeded":true}'
FROM tenants t
         JOIN customers c ON c.tenant_id = t.id
         JOIN plans p ON p.tenant_id = t.id
WHERE t.name = 'Acme Corp'
    LIMIT 1;

-- Insert a payment for the subscription
INSERT INTO payments (tenant_id, subscription_id, amount_cents, currency, paid_at, status, gateway_txn_id)
SELECT t.id, s.id, p.price_cents, 'USD', now(), 'succeeded', concat('seed-', (random()*100000)::int)
FROM tenants t
         JOIN subscriptions s ON s.tenant_id = t.id
         JOIN plans p ON p.tenant_id = t.id
WHERE t.name = 'Acme Corp'
    LIMIT 1;
