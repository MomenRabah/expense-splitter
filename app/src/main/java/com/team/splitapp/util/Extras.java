package com.team.splitapp.util;

/** Intent extra / argument keys shared between screens. Use these, never raw strings. */
public final class Extras {

    /** long: the _id of the group a screen is showing. */
    public static final String GROUP_ID = "group_id";

    /** long: the _id of an expense being edited. */
    public static final String EXPENSE_ID = "expense_id";

    /** Value used when no id was passed. */
    public static final long NO_ID = -1L;

    private Extras() {
    }
}
