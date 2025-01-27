package com.example.BookVault.time;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

class SettableClock extends Clock {

    private Clock delegate;

    public void setDelegate(Clock delegate) {
        this.delegate = delegate;
    }

    SettableClock(Clock delegate) {
        this.delegate = delegate;
    }

    @Override
    public ZoneId getZone() {
        return delegate.getZone();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return delegate.withZone(zone);
    }

    @Override
    public Instant instant() {
        return delegate.instant();
    }
}
