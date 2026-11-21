package com.team.splitapp.logic;

/** One suggested payment: the member fromMemberId pays toMemberId the amount (in halalas). */
public class Transfer {

    private long fromMemberId;
    private long toMemberId;
    private long amount;

    public Transfer(long fromMemberId, long toMemberId, long amount) {
        this.fromMemberId = fromMemberId;
        this.toMemberId = toMemberId;
        this.amount = amount;
    }

    public long getFromMemberId() {
        return fromMemberId;
    }

    public long getToMemberId() {
        return toMemberId;
    }

    public long getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return fromMemberId + " pays " + toMemberId + " " + Money.format(amount);
    }
}
