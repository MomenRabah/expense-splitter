package com.team.splitapp.logic;

import java.util.Objects;

/** One suggested payment: {@code fromMemberId} pays {@code toMemberId} {@code amount} halalas. */
public final class Transfer {

    public final long fromMemberId;
    public final long toMemberId;
    public final long amount;

    public Transfer(long fromMemberId, long toMemberId, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be > 0, was " + amount);
        }
        if (fromMemberId == toMemberId) {
            throw new IllegalArgumentException("A member cannot pay themselves");
        }
        this.fromMemberId = fromMemberId;
        this.toMemberId = toMemberId;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transfer)) return false;
        Transfer t = (Transfer) o;
        return fromMemberId == t.fromMemberId && toMemberId == t.toMemberId && amount == t.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromMemberId, toMemberId, amount);
    }

    @Override
    public String toString() {
        return fromMemberId + " -> " + toMemberId + " : " + Money.format(amount);
    }
}
