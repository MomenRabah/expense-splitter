package com.team.splitapp.model;

/** A group of people sharing expenses (a trip, a flat, ...). Owner: Fadhlallah. */
public class Group {

    private long id; // _id
    private String name;
    private String type; // "trip", "home" or "other"
    private String currency; // for example "SAR"
    private boolean archived;
    private long createdAt; // milliseconds since 1970

    public Group() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return name;
    }
}
