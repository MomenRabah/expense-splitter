package com.team.splitapp.logic;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Turns balances into a short list of payments that settles everyone.
 *
 * Greedy algorithm: repeatedly take the member who owes the most (largest debtor)
 * and the member who is owed the most (largest creditor). The debtor pays the creditor
 * the smaller of the two amounts. Each step clears at least one member, so there are
 * at most n - 1 payments for n members.
 *
 * Example: Ahmed +200, Salam -55, Hassan -145
 *   step 1: Hassan pays Ahmed 145  (Ahmed +55, Hassan 0)
 *   step 2: Salam pays Ahmed 55    (everyone 0)
 */
public class DebtSimplifier {

    public static ArrayList<Transfer> simplify(ArrayList<Long> memberIds, HashMap<Long, Long> balances) {
        int n = memberIds.size();

        // Copy the balances into an array so we can change them.
        long[] amounts = new long[n];
        for (int i = 0; i < n; i++) {
            Long value = balances.get(memberIds.get(i));
            if (value != null) {
                amounts[i] = value;
            }
        }

        ArrayList<Transfer> transfers = new ArrayList<>();
        while (true) {
            // Find the largest creditor (most positive) and largest debtor (most negative).
            int creditor = -1;
            int debtor = -1;
            for (int i = 0; i < n; i++) {
                if (amounts[i] > 0 && (creditor == -1 || amounts[i] > amounts[creditor])) {
                    creditor = i;
                }
                if (amounts[i] < 0 && (debtor == -1 || amounts[i] < amounts[debtor])) {
                    debtor = i;
                }
            }

            // Nobody owes or nobody is owed: everyone is settled.
            if (creditor == -1 || debtor == -1) {
                break;
            }

            long pay = Math.min(amounts[creditor], -amounts[debtor]);
            transfers.add(new Transfer(memberIds.get(debtor), memberIds.get(creditor), pay));
            amounts[creditor] = amounts[creditor] - pay;
            amounts[debtor] = amounts[debtor] + pay;
        }
        return transfers;
    }
}
