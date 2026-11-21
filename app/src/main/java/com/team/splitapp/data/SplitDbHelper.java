package com.team.splitapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.team.splitapp.data.SplitContract.ExpenseShares;
import com.team.splitapp.data.SplitContract.Expenses;
import com.team.splitapp.data.SplitContract.Groups;
import com.team.splitapp.data.SplitContract.Members;
import com.team.splitapp.data.SplitContract.Settlements;

/**
 * Creates and upgrades the SQLite database (Lecture 8). Owner: Fadhlallah.
 *
 * Five tables. Deleting a group deletes its members, expenses and settlements
 * (ON DELETE CASCADE); deleting an expense deletes its shares.
 */
public class SplitDbHelper extends SQLiteOpenHelper {

    private static SplitDbHelper instance;

    /** One helper for the whole app, so every screen shares the same open database. */
    public static synchronized SplitDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SplitDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    private SplitDbHelper(Context context) {
        super(context, SplitContract.DATABASE_NAME, null, SplitContract.DATABASE_VERSION);
    }

    /** Foreign keys are off by default in SQLite; turn them on every time the database opens. */
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createGroups = "CREATE TABLE " + Groups.TABLE + " ("
                + SplitContract.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Groups.COL_NAME + " TEXT NOT NULL, "
                + Groups.COL_TYPE + " TEXT DEFAULT 'trip', "
                + Groups.COL_CURRENCY + " TEXT DEFAULT 'SAR', "
                + Groups.COL_ARCHIVED + " INTEGER DEFAULT 0, "
                + Groups.COL_CREATED_AT + " INTEGER NOT NULL)";

        String createMembers = "CREATE TABLE " + Members.TABLE + " ("
                + SplitContract.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Members.COL_GROUP_ID + " INTEGER NOT NULL REFERENCES " + Groups.TABLE
                + "(" + SplitContract.COL_ID + ") ON DELETE CASCADE, "
                + Members.COL_NAME + " TEXT NOT NULL, "
                + Members.COL_PHONE + " TEXT, "
                + "UNIQUE(" + Members.COL_GROUP_ID + ", " + Members.COL_NAME + "))";

        String createExpenses = "CREATE TABLE " + Expenses.TABLE + " ("
                + SplitContract.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Expenses.COL_GROUP_ID + " INTEGER NOT NULL REFERENCES " + Groups.TABLE
                + "(" + SplitContract.COL_ID + ") ON DELETE CASCADE, "
                + Expenses.COL_PAYER_ID + " INTEGER NOT NULL REFERENCES " + Members.TABLE
                + "(" + SplitContract.COL_ID + "), "
                + Expenses.COL_AMOUNT + " INTEGER NOT NULL CHECK(" + Expenses.COL_AMOUNT + " > 0), "
                + Expenses.COL_DESCRIPTION + " TEXT NOT NULL, "
                + Expenses.COL_DATE + " INTEGER, "
                + Expenses.COL_SPLIT_MODE + " TEXT DEFAULT 'equal', "
                + Expenses.COL_LAT + " REAL, "
                + Expenses.COL_LNG + " REAL, "
                + Expenses.COL_PLACE_NAME + " TEXT, "
                + Expenses.COL_CREATED_AT + " INTEGER)";

        String createShares = "CREATE TABLE " + ExpenseShares.TABLE + " ("
                + ExpenseShares.COL_EXPENSE_ID + " INTEGER NOT NULL REFERENCES " + Expenses.TABLE
                + "(" + SplitContract.COL_ID + ") ON DELETE CASCADE, "
                + ExpenseShares.COL_MEMBER_ID + " INTEGER NOT NULL REFERENCES " + Members.TABLE
                + "(" + SplitContract.COL_ID + "), "
                + ExpenseShares.COL_SHARE + " INTEGER NOT NULL, "
                + "PRIMARY KEY(" + ExpenseShares.COL_EXPENSE_ID + ", " + ExpenseShares.COL_MEMBER_ID + "))";

        String createSettlements = "CREATE TABLE " + Settlements.TABLE + " ("
                + SplitContract.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Settlements.COL_GROUP_ID + " INTEGER NOT NULL REFERENCES " + Groups.TABLE
                + "(" + SplitContract.COL_ID + ") ON DELETE CASCADE, "
                + Settlements.COL_FROM_MEMBER + " INTEGER NOT NULL REFERENCES " + Members.TABLE
                + "(" + SplitContract.COL_ID + "), "
                + Settlements.COL_TO_MEMBER + " INTEGER NOT NULL REFERENCES " + Members.TABLE
                + "(" + SplitContract.COL_ID + "), "
                + Settlements.COL_AMOUNT + " INTEGER NOT NULL CHECK(" + Settlements.COL_AMOUNT + " > 0), "
                + Settlements.COL_STATUS + " TEXT DEFAULT 'pending', "
                + Settlements.COL_PAID_AT + " INTEGER, "
                + Settlements.COL_REMIND_AT + " INTEGER)";

        db.execSQL(createGroups);
        db.execSQL(createMembers);
        db.execSQL(createExpenses);
        db.execSQL(createShares);
        db.execSQL(createSettlements);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO(Fadhlallah, day 5): migrate step by step instead of dropping the data.
        db.execSQL("DROP TABLE IF EXISTS " + Settlements.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ExpenseShares.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Expenses.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Members.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Groups.TABLE);
        onCreate(db);
    }
}
