package com.team.splitapp.model;

/** A person in a group. Owner: Fadhlallah. */
public class Member {

    private long id; // _id
    private long groupId;
    private String name; // unique inside the group
    private String phone; // may be null

    public Member() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /** ArrayAdapter shows this text in lists and spinners. */
    @Override
    public String toString() {
        return name;
    }
}
