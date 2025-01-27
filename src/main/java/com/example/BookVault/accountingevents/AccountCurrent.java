package com.example.BookVault.accountingevents;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record AccountCurrent(String aMessage) {
}
