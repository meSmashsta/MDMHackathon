package com.blazingmuffin.health.mdmsystem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.blazingmuffin.health.mdmsystem.other.models.ResidentEntity;

/**
 * Created by lenovo on 10/21/2017.
 */

public class ResidentDetailsFragment extends Fragment {
    private ResidentEntity mResidentEntity;
    private MainActivity mMainActivity;

    private TextView mFullName,
                     mGender;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resident_details_layout, container, false);
        mMainActivity = (MainActivity) getActivity();
        mResidentEntity = mMainActivity.getResidentEntity();
        mFullName = (TextView) view.findViewById(R.id.tv_resident_details_full_name);
        mGender = (TextView) view.findViewById(R.id.tv_resident_details_gender);
        mFullName.setText(mResidentEntity.getFullName());
        mGender.setText(mResidentEntity.getGender());
        return view;
    }
}
