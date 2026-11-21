# Test matrix (T1–T12)

Owner: Ahmed (QA lead). Run every test on **2 devices** (one Android 13+ phone, one older or an emulator).
Fill in the Result columns with Pass / Fail. For a failure, open a GitHub Issue and put its number in Notes.

Devices:
- Device A: _model, Android version_
- Device B: _model, Android version_

| ID | Test | Steps | Expected | Covers | Device A | Device B | Notes |
|---|---|---|---|---|---|---|---|
| T1 | Create group, 4 members, 6 expenses (equal + custom) | Create group "Test trip"; add Ahmed, Salam, Hassan, Abdullah; add 3 equal and 3 custom-split expenses | No error; every list updates right away | Criterion 1 | | | |
| T2 | Balances vs hand spreadsheet (3 scenarios) | Enter scenario 1–3 from the spreadsheet; compare the Balances tab | Every balance matches to the halala; balances sum to 0 | Criterion 2 | | | |
| T3 | Settle vs hand calculation | Open the Settle tab for scenarios 1–3 | Same totals as by hand; at most n − 1 payments | Criterion 3 | | | |
| T4 | Data survives restart | Turn on airplane mode; kill the app from Recents; reopen | All groups, members, expenses and payments still there | Criterion 4 | | | |
| T5 | Input validation | In Add expense enter 0, −5, 12.345, an empty amount, an empty description, no participants; custom shares that do not add up; a duplicate member name | A field error for each case; nothing is saved | Input validation | | | |
| T6 | Rotation | Half-type an expense; rotate; also repeat with Developer options → "Don't keep activities" on and switch apps | Typed input is still there; no crash | Rotation / backgrounding | | | |
| T7 | Monkey test | `adb shell monkey -p com.team.splitapp -v 5000` | 0 crashes, 0 ANRs | No crashes | | | |
| T8 | Share without WhatsApp | Uninstall or disable WhatsApp; tap Share summary and Send on WhatsApp | Chooser opens / fallback message; no exception | Intents (resolve test) | | | |
| T9 | Permissions denied | Deny location and contacts permissions; tap Use my location and Import from contacts | App explains why and keeps working | Robustness | | | |
| T10 | Reminder after reboot | Set a reminder 1 minute ahead; reboot the device | Notification still appears and opens the right group | Broadcast Receivers | | | |
| T11 | CSV export of a big group | Seed a group with 200 expenses; export CSV | UI stays responsive; CSV file created; notification shown | Services, Threads | | | |
| T12 | Arabic | Switch the phone language to Arabic; visit every screen | Text translated, layout mirrored, nothing cut off | Readable text, RTL | | | |

## Monkey test output

Paste the last 20 lines of the monkey output here (T7).

```
```
