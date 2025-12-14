-- V2__billing_invoices_payment_attempts.sql
-- Add invoice fields and payment_attempts table used by billing jobs.

BEGIN;

-- Add invoice columns if missing
ALTER TABLE invoices
    ADD COLUMN IF NOT EXISTS amount_cents BIGINT,
    ADD COLUMN IF NOT EXISTS currency TEXT,
    ADD COLUMN IF NOT EXISTS issued_at TIMESTAMP WITH TIME ZONE,
    ADD COLUMN IF NOT EXISTS due_at TIMESTAMP WITH TIME ZONE,
    ADD COLUMN IF NOT EXISTS status TEXT; -- e.g. 'open','paid','failed'

-- Create payment_attempts table to record retries
CREATE TABLE IF NOT EXISTS payment_attempts (
                                                id BIGSERIAL PRIMARY KEY,
                                                invoice_id BIGINT NOT NULL REFERENCES invoices(id) ON DELETE CASCADE,
    tenant_id BIGINT NOT NULL,
    attempt_number INT NOT NULL DEFAULT 1,
    status TEXT NOT NULL, -- pending, success, failed
    last_error TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    last_attempt_at TIMESTAMP WITH TIME ZONE,
    next_attempt_at TIMESTAMP WITH TIME ZONE
                                                                                                       );

CREATE INDEX IF NOT EXISTS idx_payment_attempts_invoice_id ON payment_attempts(invoice_id);
CREATE INDEX IF NOT EXISTS idx_payment_attempts_next_attempt_at ON payment_attempts(next_attempt_at);

COMMIT;
