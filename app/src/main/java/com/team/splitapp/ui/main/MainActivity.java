package com.team.splitapp.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.team.splitapp.R;
import com.team.splitapp.notify.NotificationHelper;
import com.team.splitapp.ui.group.GroupActivity;

/**
 * Launcher screen: the list of groups.
 * TODO(Mohammad): groups list + create/edit/archive/delete, loaded from the database.
 */
public class MainActivity extends AppCompatActivity {

    /** Skeleton only: lets everyone reach the tabs before the groups list exists. */
    private static final long DEMO_GROUP_ID = 1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Notifications (Ahmed): the channel must exist before any reminder is shown,
        // and Android 13+ asks the user once for permission.
        NotificationHelper.createChannel(this);
        NotificationHelper.requestPermission(this);

        Button openDemo = findViewById(R.id.button_open_demo_group);
        openDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(GroupActivity.newIntent(MainActivity.this, DEMO_GROUP_ID));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_group) {
            // TODO(Mohammad): show the create-group DialogFragment.
            Toast.makeText(this, R.string.msg_create_group_coming, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
