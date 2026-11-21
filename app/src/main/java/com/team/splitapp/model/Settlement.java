package com.team.splitapp.model;

/** A payment between two members. Only "paid" settlements change balances. Owner: Fadhlallah. */
public class Settlement {

    private long id; // _id
    private long groupId;
    private long fromMember; // member who pays
    private long toMember; // member who receives
    private long amount; // halalas, always > 0
    private String status; // "pending" or "paid"
    private long paidAt; // 0 if not paid
    private long remindAt; // 0 if no reminder

    public Settlement() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getFromMember() {
        return fromMember;
    }

    public void setFromMember(long fromMember) {
        this.fromMember = fromMember;
    }

    public long getToMember() {
        return toMember;
    }

    public void setToMember(long toMember) {
        this.toMember = toMember;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(long paidAt) {
        this.paidAt = paidAt;
    }

    public long getRemindAt() {
        return remindAt;
    }

    public void setRemindAt(long remindAt) {
        this.remindAt = remindAt;
    }
}
