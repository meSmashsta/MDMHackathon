package com.blazingmuffin.health.mdmsystem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blazingmuffin.health.mdmsystem.other.models.ResidentEntity;

/**
 * Created by lenovo on 10/21/2017.
 */

public class ResidentDetailsFragment extends Fragment {
    private ResidentEntity mResidentEntity;
    private MainActivity mMainActivity;

    private FragmentManager mManager;

    private LinearLayout mHousehold;

    private TextView mFullName,
                     mGender;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resident_details_layout, container, false);
        mMainActivity = (MainActivity) getActivity();
        mMainActivity.hideFloatingActionButton();
        mResidentEntity = mMainActivity.getResidentEntity();
        mManager = mMainActivity.getManager();

        mHousehold = view.findViewById(R.id.linear_resident_details_household_info);

        mFullName = view.findViewById(R.id.tv_resident_details_full_name);
        mGender = view.findViewById(R.id.tv_resident_details_gender);
        mFullName.setText(mResidentEntity.getFullName());
        mGender.setText(mResidentEntity.getGender());

        mHousehold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HouseholdFragment householdFragment = new HouseholdFragment();
                FragmentTransaction transaction = mManager.beginTransaction();
                transaction.add(R.id.linear_fragment_container, householdFragment, getString(R.string.tag_resident_household_save));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}
