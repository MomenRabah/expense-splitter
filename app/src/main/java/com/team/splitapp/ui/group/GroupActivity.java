package com.team.splitapp.ui.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.team.splitapp.R;
import com.team.splitapp.util.Extras;

/**
 * One group. A row of four tab buttons at the bottom swaps the Fragment shown in
 * {@code fragment_container} using a FragmentTransaction (Lecture 7 part 2, dynamic fragments).
 * The action bar has an Up arrow and an "Add expense" menu item (Lecture 2, menus).
 */
public class GroupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int TAB_EXPENSES = 0;
    private static final int TAB_BALANCES = 1;
    private static final int TAB_SETTLE = 2;
    private static final int TAB_MEMBERS = 3;

    /** Key used to remember the selected tab across rotation (onSaveInstanceState). */
    private static final String KEY_SELECTED_TAB = "selected_tab";

    private long groupId;
    private int selectedTab = TAB_EXPENSES;
    private Button[] tabButtons;

    /** Explicit intent (Lecture 5) carrying the group id as an extra. */
    public static Intent newIntent(Context context, long groupId) {
        Intent intent = new Intent(context, GroupActivity.class);
        intent.putExtra(Extras.GROUP_ID, groupId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupId = getIntent().getLongExtra(Extras.GROUP_ID, Extras.NO_ID);
        if (groupId == Extras.NO_ID) {
            // Opened without a group: nothing to show. Never crash on a bad intent.
            Toast.makeText(this, R.string.error_no_group, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_group);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tabButtons = new Button[]{
                findViewById(R.id.tab_expenses),
                findViewById(R.id.tab_balances),
                findViewById(R.id.tab_settle),
                findViewById(R.id.tab_members)
        };
        for (Button button : tabButtons) {
            button.setOnClickListener(this);
        }

        if (savedInstanceState == null) {
            // First launch: add the first tab.
            showTab(TAB_EXPENSES);
        } else {
            // After rotation the FragmentManager re-creates the visible fragment by itself,
            // so we only restore which button is highlighted.
            selectedTab = savedInstanceState.getInt(KEY_SELECTED_TAB, TAB_EXPENSES);
            updateTabButtons();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SELECTED_TAB, selectedTab);
    }

    /** Fragments call this to know which group to load. */
    public long getGroupId() {
        return groupId;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int tab;
        if (id == R.id.tab_balances) {
            tab = TAB_BALANCES;
        } else if (id == R.id.tab_settle) {
            tab = TAB_SETTLE;
        } else if (id == R.id.tab_members) {
            tab = TAB_MEMBERS;
        } else {
            tab = TAB_EXPENSES;
        }
        if (tab != selectedTab) {
            showTab(tab);
        }
    }

    private void showTab(int tab) {
        Fragment fragment;
        if (tab == TAB_BALANCES) {
            fragment = new BalancesFragment();
        } else if (tab == TAB_SETTLE) {
            fragment = new SettleFragment();
        } else if (tab == TAB_MEMBERS) {
            fragment = new MembersFragment();
        } else {
            fragment = new ExpensesFragment();
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        // No addToBackStack: Back leaves the group instead of stepping through tabs.
        transaction.commit();

        selectedTab = tab;
        updateTabButtons();
    }

    /** The selected tab's button is disabled so it looks pressed and cannot be clicked again. */
    private void updateTabButtons() {
        for (int i = 0; i < tabButtons.length; i++) {
            tabButtons[i].setEnabled(i != selectedTab);
        }
        // "Add expense" is only shown on the Expenses tab (see onPrepareOptionsMenu).
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem addExpense = menu.findItem(R.id.action_add_expense);
        if (addExpense != null) {
            addExpense.setVisible(selectedTab == TAB_EXPENSES);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish(); // Up arrow: back to the groups list
            return true;
        } else if (id == R.id.action_add_expense) {
            // TODO(Hassan): open AddExpenseFragment for groupId.
            Toast.makeText(this, R.string.msg_add_expense_coming, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
