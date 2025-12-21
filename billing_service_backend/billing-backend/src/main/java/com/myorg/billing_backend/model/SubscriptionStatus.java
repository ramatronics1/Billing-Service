package com.myorg.billing_backend.model;

public enum SubscriptionStatus {
    SCHEDULED,   // starts in future
    ACTIVE,      // billing active
    PAUSED,      // temporarily stopped
    CANCELED,   // cancelled immediately or end-of-cycle
    EXPIRED      // naturally ended
}
