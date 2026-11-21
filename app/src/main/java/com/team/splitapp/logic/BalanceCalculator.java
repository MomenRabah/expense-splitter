package com.team.splitapp.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Computes each member's balance in a group.
 *
 *   balance = total paid - total share + payments sent - payments received
 *
 * Positive balance: the group owes the member money.
 * Negative balance: the member owes the group money.
 * All balances in a group add up to 0.
 *
 * How to use it (on a background thread):
 *
 *   BalanceCalculator calc = new BalanceCalculator(memberIds);
 *   for each expense:         calc.addPaid(payerId, amount);
 *   for each expense share:   calc.addShare(memberId, share);
 *   for each PAID settlement: calc.addSettlement(fromMember, toMember, amount);
 *   long b = calc.getBalance(memberId);
 */
public class BalanceCalculator {

    private ArrayList<Long> memberIds;
    private HashMap<Long, Long> balances;

    /** memberIds: every member of the group, so members with no expenses still show 0. */
    public BalanceCalculator(ArrayList<Long> memberIds) {
        this.memberIds = memberIds;
        this.balances = new HashMap<>();
        for (int i = 0; i < memberIds.size(); i++) {
            balances.put(memberIds.get(i), 0L);
        }
    }

    /** The member paid this amount for an expense. */
    public void addPaid(long memberId, long amount) {
        add(memberId, amount);
    }

    /** The member's share of an expense. */
    public void addShare(long memberId, long amount) {
        add(memberId, -amount);
    }

    /** A paid settlement: fromMemberId paid toMemberId this amount. */
    public void addSettlement(long fromMemberId, long toMemberId, long amount) {
        add(fromMemberId, amount);
        add(toMemberId, -amount);
    }

    public long getBalance(long memberId) {
        Long value = balances.get(memberId);
        if (value == null) {
            return 0;
        }
        return value;
    }

    public ArrayList<Long> getMemberIds() {
        return memberIds;
    }

    public HashMap<Long, Long> getBalances() {
        return balances;
    }

    private void add(long memberId, long amount) {
        balances.put(memberId, getBalance(memberId) + amount);
    }

    /**
     * Splits total equally between the participants.
     * Leftover halalas go one each to the participants with the smallest ids,
     * so 10000 split over 3 gives 3334, 3333, 3333. The shares always add up to total.
     *
     * Returns a map: member id -> share.
     */
    public static HashMap<Long, Long> splitEqually(long total, ArrayList<Long> participantIds) {
        ArrayList<Long> ids = new ArrayList<>(participantIds);
        Collections.sort(ids);

        long count = ids.size();
        long baseShare = total / count;
        long leftover = total % count;

        HashMap<Long, Long> shares = new HashMap<>();
        for (int i = 0; i < ids.size(); i++) {
            long share = baseShare;
            if (i < leftover) {
                share = share + 1;
            }
            shares.put(ids.get(i), share);
        }
        return shares;
    }
}
