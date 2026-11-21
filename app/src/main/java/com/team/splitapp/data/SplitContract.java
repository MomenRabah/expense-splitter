package com.team.splitapp.data;

/**
 * Table and column names for the SplitApp database. Owner: Fadhlallah.
 * Always use these constants in SQL; never type a table or column name as a raw string.
 *
 * Every amount column is an INTEGER number of halalas (12.50 SAR = 1250).
 */
public class SplitContract {

    public static final String DATABASE_NAME = "splitapp.db";
    public static final int DATABASE_VERSION = 1;

    /** Primary key column used by every table that has one. */
    public static final String COL_ID = "_id";

    /** groups(_id, name, type, currency, archived, created_at) */
    public static class Groups {
        public static final String TABLE = "groups";
        public static final String COL_NAME = "name";
        public static final String COL_TYPE = "type";
        public static final String COL_CURRENCY = "currency";
        public static final String COL_ARCHIVED = "archived";
        public static final String COL_CREATED_AT = "created_at";
    }

    /** members(_id, group_id, name, phone) */
    public static class Members {
        public static final String TABLE = "members";
        public static final String COL_GROUP_ID = "group_id";
        public static final String COL_NAME = "name";
        public static final String COL_PHONE = "phone";
    }

    /** expenses(_id, group_id, payer_id, amount, description, date, split_mode, lat, lng, place_name, created_at) */
    public static class Expenses {
        public static final String TABLE = "expenses";
        public static final String COL_GROUP_ID = "group_id";
        public static final String COL_PAYER_ID = "payer_id";
        public static final String COL_AMOUNT = "amount";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_DATE = "date";
        public static final String COL_SPLIT_MODE = "split_mode";
        public static final String COL_LAT = "lat";
        public static final String COL_LNG = "lng";
        public static final String COL_PLACE_NAME = "place_name";
        public static final String COL_CREATED_AT = "created_at";

        public static final String SPLIT_EQUAL = "equal";
        public static final String SPLIT_CUSTOM = "custom";
    }

    /** expense_shares(expense_id, member_id, share) */
    public static class ExpenseShares {
        public static final String TABLE = "expense_shares";
        public static final String COL_EXPENSE_ID = "expense_id";
        public static final String COL_MEMBER_ID = "member_id";
        public static final String COL_SHARE = "share";
    }

    /** settlements(_id, group_id, from_member, to_member, amount, status, paid_at, remind_at) */
    public static class Settlements {
        public static final String TABLE = "settlements";
        public static final String COL_GROUP_ID = "group_id";
        public static final String COL_FROM_MEMBER = "from_member";
        public static final String COL_TO_MEMBER = "to_member";
        public static final String COL_AMOUNT = "amount";
        public static final String COL_STATUS = "status";
        public static final String COL_PAID_AT = "paid_at";
        public static final String COL_REMIND_AT = "remind_at";

        public static final String STATUS_PENDING = "pending";
        public static final String STATUS_PAID = "paid";
    }
}
