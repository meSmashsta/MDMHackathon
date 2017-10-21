package com.blazingmuffin.health.mdmsystem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.blazingmuffin.health.mdmsystem.other.models.ResidentEntity;
import com.blazingmuffin.health.mdmsystem.other.models.resident.update.HouseholdUpdatable;
import com.blazingmuffin.health.mdmsystem.other.repositories.ResidentRepository;
import com.blazingmuffin.health.mdmsystem.utils.WidgetUtils;

/**
 * Created by lenovo on 10/22/2017.
 */

public class HouseholdFragment extends Fragment {
    private MainActivity mMainActivity;
    private ResidentRepository mResidentRepository;

    private ResidentEntity mResidentEntity;

    private EditText mNoOfFamily,
                     mNoOfPeople;

    private RadioGroup m4Ps;

    private Button mSave;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house_hold_layout, container, false);
        mMainActivity = (MainActivity) getActivity();
        mResidentRepository = mMainActivity.getResidentRepository();

        mResidentEntity = mMainActivity.getResidentEntity();

        mNoOfFamily = view.findViewById(R.id.et_house_hold_no_of_family);
        mNoOfPeople = view.findViewById(R.id.et_house_hold_no_of_people);
        m4Ps = view.findViewById(R.id.rg_house_hold_is_4ps);

        mSave = view.findViewById(R.id.btn_house_hold_save);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResidentEntity.Household household = mResidentEntity.getHousehold();
                household.setNoOfFamilyInHousehold(Integer.parseInt(mNoOfFamily.getText().toString()));
                household.setNoOfPeopleInHousehold(Integer.parseInt(mNoOfPeople.getText().toString()));
                household.setFamily4PsMember(Boolean.valueOf(WidgetUtils.getText(m4Ps)));
                HouseholdUpdatable householdUpdatable = new HouseholdUpdatable(mMainActivity.getDatabase());
                mResidentRepository.setIUpdatable(householdUpdatable);
                mResidentRepository.update(mResidentEntity);
            }
        });
        return view;
    }
}
