package com.team.splitapp.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Turns balances into a short list of payments that settles everyone.
 *
 * <p>Greedy algorithm: creditors (balance &gt; 0) and debtors (balance &lt; 0) go into two
 * max-heaps. Repeatedly the largest debtor pays the largest creditor the smaller of the two
 * amounts; whoever still has something left goes back into its heap. Each step clears at
 * least one member, so there are at most n − 1 payments for n members.
 *
 * <p>This gives few payments but not always the theoretical minimum (that problem is NP-hard);
 * see "Known limitations" in the report. Run it on {@code AppExecutors.diskIO()}.
 */
public final class DebtSimplifier {

    private DebtSimplifier() {
    }

    /**
     * @param balances member id → balance in halalas (from {@link BalanceCalculator}); must sum to 0.
     * @return payments to make, largest first. Empty if everyone is already settled.
     */
    public static List<Transfer> simplify(Map<Long, Long> balances) {
        long sum = 0;
        // Ties are broken by lower member id so results are the same on every run.
        PriorityQueue<long[]> creditors = new PriorityQueue<>(DebtSimplifier::largestFirst);
        PriorityQueue<long[]> debtors = new PriorityQueue<>(DebtSimplifier::largestFirst);
        for (Map.Entry<Long, Long> e : balances.entrySet()) {
            long balance = e.getValue();
            sum = Math.addExact(sum, balance);
            if (balance > 0) {
                creditors.add(new long[]{e.getKey(), balance});
            } else if (balance < 0) {
                debtors.add(new long[]{e.getKey(), -balance});
            }
        }
        if (sum != 0) {
            throw new IllegalArgumentException("Balances must sum to 0, got " + sum);
        }

        List<Transfer> transfers = new ArrayList<>();
        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            long[] creditor = creditors.poll();
            long[] debtor = debtors.poll();
            long amount = Math.min(creditor[1], debtor[1]);
            transfers.add(new Transfer(debtor[0], creditor[0], amount));
            creditor[1] -= amount;
            debtor[1] -= amount;
            if (creditor[1] > 0) creditors.add(creditor);
            if (debtor[1] > 0) debtors.add(debtor);
        }
        return transfers;
    }

    /** Orders {id, amount} pairs by amount descending, then id ascending. */
    private static int largestFirst(long[] a, long[] b) {
        int byAmount = Long.compare(b[1], a[1]);
        return byAmount != 0 ? byAmount : Long.compare(a[0], b[0]);
    }
}
