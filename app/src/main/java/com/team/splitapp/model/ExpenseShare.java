package com.team.splitapp.model;

/** How much of one expense one member owes. Share is in halalas. Owner: Fadhlallah. */
public class ExpenseShare {

    private long expenseId;
    private long memberId;
    private long share; // halalas

    public ExpenseShare() {
    }

    public long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(long expenseId) {
        this.expenseId = expenseId;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public long getShare() {
        return share;
    }

    public void setShare(long share) {
        this.share = share;
    }
}
