package com.blazingmuffin.health.mdmsystem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by lenovo on 10/21/2017.
 */

public class SettingsFragment extends Fragment {

    private Button mUploadButton;
    private EditText mServerUrlText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_layout, container, false);

        String serverUrlString = getString(R.string.general_default_sgw);
        mServerUrlText = view.findViewById(R.id.server_url_text);
        mServerUrlText.setText(serverUrlString);
        mUploadButton = view.findViewById(R.id.upload_button);
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).setSyncURLString(serverUrlString);
                ((MainActivity) getActivity()).startCBSyncing();
            }
        });

        return view;
    }
}
