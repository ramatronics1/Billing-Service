-- src/main/resources/db/migration/V1__init.sql
-- Initial schema for multi-tenant subscription-tracking app
-- Postgres SQL: creates TENANTS, USERS, PLANS, CUSTOMERS, SUBSCRIPTIONS,
-- PAYMENTS, INVOICES, USAGE_COUNTERS, API_KEYS with constraints and helpful indexes.

-- === Safety: fail early if not Postgres ===
-- (works in modern Postgres versions)
-- Create a transaction so Flyway rolls back on error
BEGIN;

-- ---------------------------
-- TENANTS
-- ---------------------------
CREATE TABLE IF NOT EXISTS tenants (
                                       id BIGSERIAL PRIMARY KEY,
                                       name TEXT NOT NULL,
                                       contact_email TEXT
);

-- ---------------------------
-- USERS
-- ---------------------------
CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     tenant_id BIGINT NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    email TEXT NOT NULL,
    password_hash TEXT,
    role TEXT,
    CONSTRAINT users_tenant_email_unique UNIQUE (tenant_id, email)
    );

CREATE INDEX IF NOT EXISTS idx_users_tenant_id ON users(tenant_id);

-- ---------------------------
-- PLANS
-- ---------------------------
CREATE TABLE IF NOT EXISTS plans (
                                     id BIGSERIAL PRIMARY KEY,
                                     tenant_id BIGINT NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    name TEXT NOT NULL,
    price_cents BIGINT NOT NULL DEFAULT 0,
    billing_cycle TEXT,
    features_json JSONB,
    active BOOLEAN NOT NULL DEFAULT TRUE
    );

CREATE INDEX IF NOT EXISTS idx_plans_tenant_id ON plans(tenant_id);
CREATE INDEX IF NOT EXISTS idx_plans_active ON plans(active);

-- ---------------------------
-- CUSTOMERS
-- ---------------------------
CREATE TABLE IF NOT EXISTS customers (
                                         id BIGSERIAL PRIMARY KEY,
                                         tenant_id BIGINT NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    email TEXT,
    external_id TEXT
    );

CREATE INDEX IF NOT EXISTS idx_customers_tenant_id ON customers(tenant_id);
CREATE INDEX IF NOT EXISTS idx_customers_external_id ON customers(external_id);

-- ---------------------------
-- SUBSCRIPTIONS
-- ---------------------------
CREATE TABLE IF NOT EXISTS subscriptions (
                                             id BIGSERIAL PRIMARY KEY,
                                             tenant_id BIGINT NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    customer_id BIGINT NOT NULL REFERENCES customers(id) ON DELETE RESTRICT,
    plan_id BIGINT NOT NULL REFERENCES plans(id) ON DELETE RESTRICT,
    start_date DATE,
    end_date DATE,
    status TEXT,
    auto_renew BOOLEAN DEFAULT TRUE,
    metadata JSONB
    );

CREATE INDEX IF NOT EXISTS idx_subscriptions_tenant_id ON subscriptions(tenant_id);
CREATE INDEX IF NOT EXISTS idx_subscriptions_customer_id ON subscriptions(customer_id);
CREATE INDEX IF NOT EXISTS idx_subscriptions_plan_id ON subscriptions(plan_id);
CREATE INDEX IF NOT EXISTS idx_subscriptions_status ON subscriptions(status);

-- ---------------------------
-- PAYMENTS
-- ---------------------------
CREATE TABLE IF NOT EXISTS payments (
                                        id BIGSERIAL PRIMARY KEY,
                                        tenant_id BIGINT NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    subscription_id BIGINT REFERENCES subscriptions(id) ON DELETE SET NULL,
    amount_cents BIGINT NOT NULL,
    currency TEXT,
    paid_at TIMESTAMP WITH TIME ZONE,
                                                                                             status TEXT,
                                                                                             gateway_txn_id TEXT
                                                                                             );

CREATE INDEX IF NOT EXISTS idx_payments_tenant_id ON payments(tenant_id);
CREATE INDEX IF NOT EXISTS idx_payments_subscription_id ON payments(subscription_id);
CREATE UNIQUE INDEX IF NOT EXISTS uidx_payments_gateway_txn_id ON payments(gateway_txn_id) WHERE gateway_txn_id IS NOT NULL;

-- ---------------------------
-- INVOICES
-- ---------------------------
CREATE TABLE IF NOT EXISTS invoices (
                                        id BIGSERIAL PRIMARY KEY,
                                        tenant_id BIGINT NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    payment_id BIGINT REFERENCES payments(id) ON DELETE SET NULL,
    invoice_no TEXT,
    pdf_path TEXT
    );

CREATE INDEX IF NOT EXISTS idx_invoices_tenant_id ON invoices(tenant_id);
CREATE UNIQUE INDEX IF NOT EXISTS uidx_invoices_invoice_no ON invoices(invoice_no) WHERE invoice_no IS NOT NULL;

-- ---------------------------
-- USAGE_COUNTERS
-- ---------------------------
CREATE TABLE IF NOT EXISTS usage_counters (
                                              id BIGSERIAL PRIMARY KEY,
                                              tenant_id BIGINT NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    customer_id BIGINT NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    metric_name TEXT NOT NULL,
    value BIGINT NOT NULL DEFAULT 0,
    last_reset TIMESTAMP WITH TIME ZONE
                                                                                                   );

CREATE INDEX IF NOT EXISTS idx_usage_tenant_id ON usage_counters(tenant_id);
CREATE INDEX IF NOT EXISTS idx_usage_customer_id ON usage_counters(customer_id);
CREATE INDEX IF NOT EXISTS idx_usage_metric ON usage_counters(metric_name);

-- ---------------------------
-- API_KEYS
-- ---------------------------
CREATE TABLE IF NOT EXISTS api_keys (
                                        id BIGSERIAL PRIMARY KEY,
                                        tenant_id BIGINT NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    key_hash TEXT NOT NULL,
    scopes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    CONSTRAINT api_keys_tenant_key_hash_unique UNIQUE (tenant_id, key_hash)
    );

CREATE INDEX IF NOT EXISTS idx_api_keys_tenant_id ON api_keys(tenant_id);

-- ---------------------------
-- Example: helpful view (tenant summary)
-- ---------------------------
CREATE OR REPLACE VIEW tenant_subscription_summary AS
SELECT
    t.id AS tenant_id,
    t.name AS tenant_name,
    COUNT(DISTINCT c.id) AS customer_count,
    COUNT(DISTINCT s.id) FILTER (WHERE s.status = 'active') AS active_subscriptions,
    COALESCE(SUM(p.amount_cents),0) AS total_collected_cents
FROM tenants t
         LEFT JOIN customers c ON c.tenant_id = t.id
         LEFT JOIN subscriptions s ON s.tenant_id = t.id
         LEFT JOIN payments p ON p.tenant_id = t.id
GROUP BY t.id, t.name;

-- ---------------------------
-- Finalize
-- ---------------------------
COMMIT;
