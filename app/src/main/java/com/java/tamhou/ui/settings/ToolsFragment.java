package com.java.tamhou.ui.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.java.tamhou.R;
import com.java.tamhou.SharedPref;

public class ToolsFragment extends Fragment {

    //private ToolsViewModel toolsViewModel;
    private Switch dayNightSwitch;
    SharedPref sharedPref;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //toolsViewModel = ViewModelProviders.of(this).get(ToolsViewModel.class);
        Log.d("enterTools", "Enter Settings");
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        dayNightSwitch = root.findViewById(R.id.nightModeSwitch);
        sharedPref = new SharedPref(this.getContext());
        if (sharedPref.loadNightModeState())
            dayNightSwitch.setChecked(true);

        dayNightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("modeChange", "Night Mode Changed");
                if (isChecked) {
                    sharedPref.setNightModeState(true);
                } else {
                    sharedPref.setNightModeState(false);
                }
                getActivity().recreate();
            }
        });
        return root;
    }
}