package com.team.splitapp.ui.group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.team.splitapp.R;

/**
 * Settle tab of {@link GroupActivity}. Owner: Abdullah.
 * Shows suggested payments from DebtSimplifier + mark paid.
 */
public class SettleFragment extends Fragment {

    private TextView textPlaceholder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the Fragment's UI from XML and return it (Lecture 7).
        View view = inflater.inflate(R.layout.fragment_settle, container, false);
        textPlaceholder = view.findViewById(R.id.text_placeholder);

        long groupId = ((GroupActivity) getActivity()).getGroupId();
        // TODO(Abdullah): load data for groupId on a background thread, then show it:
        // AppExecutors.runInBackground(() -> {
        //     ... query the database ...
        //     AppExecutors.runOnMainThread(() -> {
        //         if (isAdded()) { ... update the views ... }
        //     });
        // });
        return view;
    }
}
