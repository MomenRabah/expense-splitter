# SplitApp: Group Expense Splitting

CS 526 Mobile Application Programming, Team_Moamen_Hicham_Rabah (Section 02).
Java · minSdk 24 · targetSdk 34 · AppCompat · package `com.team.splitapp`.

We only use techniques taught in the CS 526 lectures: Activities and their lifecycle, explicit and implicit Intents, Fragments with `FragmentManager`/`FragmentTransaction`, XML layouts (LinearLayout, RelativeLayout, ConstraintLayout), options menus, `Toast`, Threads with a `Handler`, SQLite (`SQLiteOpenHelper`), Content Providers, Broadcast Receivers and Maps/Location. Do not add ViewModel, LiveData, Room, RecyclerView, Material components or other libraries without asking Moamen first.

## Getting started

1. Clone the repo and open the folder in Android Studio.
2. Let Gradle sync, then run the `app` configuration.
3. Check out `develop` and branch from it: `git checkout -b feature/<your-task>`.

`local.properties` is not in Git. Salam: put the Maps API key there as `MAPS_API_KEY=...`.

## Branch and merge rules

- `main` holds only submitted builds. Nobody pushes to it directly.
- `develop` is the integration branch. Open every pull request into `develop`; Moamen merges daily before the 9 pm call.
- Use one branch per task, for example `feature/settle-share` or `fix/expense-rotation`.
- Pull `develop` every morning and push before 9 pm every day, even if unfinished.

## Project layout

```
com.team.splitapp
├── ui/main/        MainActivity: groups list (Mohammad)
├── ui/group/       GroupActivity: 4 tab buttons swap fragments in fragment_container
│   ├── ExpensesFragment   (Hassan)
│   ├── BalancesFragment   (Abdullah)
│   ├── SettleFragment     (Abdullah)
│   └── MembersFragment    (Mohammad)
├── logic/          Money, BalanceCalculator, DebtSimplifier, Transfer (plain Java)
├── util/           AppExecutors (background thread + Handler), Extras (intent keys)
├── data/           (Fadhlallah) SplitContract, SplitDbHelper, DAOs, SplitProvider
├── location/       (Salam)
└── notify/         (Ahmed) receivers, BackupService
```

Create the `data`, `location` and `notify` packages in your own branch.

## Rules everyone follows

### Money is a `long` of halalas

12.50 SAR is stored as `1250`. Never use `double` or `float` for an amount.

```java
long halalas = Money.parse(amountText);        // "12.50" -> 1250
if (halalas == Money.INVALID || halalas == 0) {
    amountInput.setError(getString(R.string.error_amount));
}
String label = Money.format(halalas, "SAR");   // 1250 -> "12.50 SAR"
```

Equal splits must use `BalanceCalculator.splitEqually(total, participantIds)`. It gives any leftover halalas to the lowest member ids (10000 / 3 -> 3334, 3333, 3333), so the shares always add up to the total.

### No database or algorithm work on the UI thread

Do the slow work on a worker thread, then post the result back to the UI thread with the Handler inside `AppExecutors`:

```java
AppExecutors.runInBackground(new Runnable() {
    public void run() {
        final ArrayList<Expense> expenses = dao.getExpenses(groupId);   // worker thread
        AppExecutors.runOnMainThread(new Runnable() {
            public void run() {
                if (isAdded()) {
                    showExpenses(expenses);                            // UI thread
                }
            }
        });
    }
});
```

### Code style: keep it basic Java

Use classes, fields with getters, `if`/`else`, `for` loops, arrays, `ArrayList` and `HashMap`. Use anonymous classes (`new View.OnClickListener() { ... }`) for listeners and `Runnable`s. Avoid lambdas, streams, `Optional` and other advanced Java features.

Check `isAdded()` in a fragment before touching views: the user may have switched tabs while the query was running.

### Fragments (every tab)

- Inflate the layout in `onCreateView` and find your views there.
- Get the open group with `((GroupActivity) getActivity()).getGroupId()`.
- Load data every time `onCreateView` runs. The tab is re-created when the user switches back to it, and after rotation, so the database is the source of truth.

### Rotation and half-typed input

- In `onCreate`, only add fragments when `savedInstanceState == null`; after a rotation the FragmentManager restores them itself.
- Save anything the user typed but hasn't saved in `onSaveInstanceState(Bundle)` and read it back in `onCreate` / `onCreateView`. Views with an `android:id` (like `EditText`) keep their text automatically.
- Test every screen with Developer options -> "Don't keep activities" switched on.

### Opening a group

```java
startActivity(GroupActivity.newIntent(context, groupId));   // explicit intent, group_id extra
```

Use the keys in `Extras` for intent extras; never type the key as a raw string.

### Resources

- No hard-coded strings, colours or dimensions in layouts. Put them in `res/values` (`strings.xml`, `colors.xml`, `dimens.xml`).
- Text is at least 14sp (`@dimen/text_body`); amounts are 18sp bold (`@dimen/text_amount`).
- Show short messages with `Toast`; confirmations with a `DialogFragment`.
- Use `start`/`end`, never `left`/`right`, so the Arabic RTL layout mirrors correctly.

### Definition of done

A task is done when it validates its input, survives rotation, has no Lint errors and has a screenshot in the report folder.

## Checks before a pull request

```
./gradlew assembleDebug    # must build
./gradlew lintDebug        # must have 0 errors
```
