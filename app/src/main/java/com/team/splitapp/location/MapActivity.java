package com.team.splitapp.location;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.team.splitapp.R;
import com.team.splitapp.util.Extras;

/**
 * Trip map: one marker per expense that has a location (Lecture 13). Owner: Salam.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    /** Where the camera starts before any expense is shown: Dammam. */
    private static final LatLng DEFAULT_CENTER = new LatLng(26.4207, 50.0888);
    private static final float DEFAULT_ZOOM = 11f;

    private GoogleMap map;
    private long groupId;

    public static Intent newIntent(Context context, long groupId) {
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra(Extras.GROUP_ID, groupId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        groupId = getIntent().getLongExtra(Extras.GROUP_ID, Extras.NO_ID);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_CENTER, DEFAULT_ZOOM));
        // TODO(Salam, day 4): add a marker per located expense of groupId:
        // map.addMarker(new MarkerOptions().position(latLng).title(description).snippet(amount));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
