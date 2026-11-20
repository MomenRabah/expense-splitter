package com.team.splitapp.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Computes each member's balance in a group.
 *
 * <pre>balance = total paid − total share + payments sent − payments received</pre>
 *
 * <p>Positive means the group owes the member money; negative means the member owes.
 * Only PAID settlements are fed in. Balances in a group always sum to exactly 0.
 *
 * <p>Pure Java with no Android or database types. The
 * screen code reads rows from SQLite and feeds them in with the add* methods:
 * <pre>
 * BalanceCalculator calc = new BalanceCalculator(memberIds);
 * for (expense)  calc.addPaid(expense.payerId, expense.amount);
 * for (share)    calc.addShare(share.memberId, share.share);
 * for (paid settlement) calc.addSettlement(s.fromMember, s.toMember, s.amount);
 * Map&lt;Long, Long&gt; balances = calc.getBalances();
 * </pre>
 * Not thread-safe; create one per calculation on the background thread.
 */
public final class BalanceCalculator {

    /** Sorted by member id so output order is stable (and matches "first by id" rules). */
    private final TreeMap<Long, Long> balances = new TreeMap<>();

    /** @param memberIds every member of the group, so members with no activity show 0. */
    public BalanceCalculator(Collection<Long> memberIds) {
        for (Long id : memberIds) {
            balances.put(id, 0L);
        }
    }

    /** {@code memberId} paid {@code amount} for an expense. */
    public BalanceCalculator addPaid(long memberId, long amount) {
        requireNonNegative(amount);
        add(memberId, amount);
        return this;
    }

    /** {@code memberId}'s share of an expense is {@code amount}. */
    public BalanceCalculator addShare(long memberId, long amount) {
        requireNonNegative(amount);
        add(memberId, -amount);
        return this;
    }

    /** A PAID settlement: {@code from} paid {@code to} {@code amount}. */
    public BalanceCalculator addSettlement(long fromMemberId, long toMemberId, long amount) {
        requireNonNegative(amount);
        add(fromMemberId, amount);
        add(toMemberId, -amount);
        return this;
    }

    /**
     * Member id → balance in halalas, ordered by member id.
     *
     * @throws IllegalStateException if the balances do not sum to 0, which means an expense's
     *         shares do not add up to its amount (a data bug we want to catch early).
     */
    public Map<Long, Long> getBalances() {
        long sum = 0;
        for (long b : balances.values()) {
            sum = Math.addExact(sum, b);
        }
        if (sum != 0) {
            throw new IllegalStateException("Balances sum to " + sum + " halalas, expected 0");
        }
        return Collections.unmodifiableMap(new LinkedHashMap<>(balances));
    }

    private void add(long memberId, long delta) {
        Long current = balances.get(memberId);
        balances.put(memberId, Math.addExact(current == null ? 0L : current, delta));
    }

    private static void requireNonNegative(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must not be negative: " + amount);
        }
    }

    /**
     * Splits {@code total} equally between participants. Remainder halalas go one each to the
     * participants with the lowest ids, so 10000 over 3 → 3334, 3333, 3333. The shares always
     * add up to exactly {@code total}.
     *
     * @return member id → share, ordered by member id.
     */
    public static Map<Long, Long> splitEqually(long total, Collection<Long> participantIds) {
        if (total <= 0) {
            throw new IllegalArgumentException("Total must be > 0");
        }
        List<Long> ids = new ArrayList<>(new TreeSet<>(participantIds));
        if (ids.isEmpty()) {
            throw new IllegalArgumentException("At least one participant is required");
        }
        long base = total / ids.size();
        long remainder = total % ids.size();
        Map<Long, Long> shares = new LinkedHashMap<>();
        for (int i = 0; i < ids.size(); i++) {
            shares.put(ids.get(i), base + (i < remainder ? 1 : 0));
        }
        return shares;
    }
}
