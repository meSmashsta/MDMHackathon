package com.blazingmuffin.health.mdmsystem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blazingmuffin.health.mdmsystem.other.models.ResidentEntity;
import com.blazingmuffin.health.mdmsystem.other.models.resident.update.HouseholdUpdatable;
import com.blazingmuffin.health.mdmsystem.other.repositories.ResidentRepository;
import com.blazingmuffin.health.mdmsystem.utils.WidgetUtils;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;

/**
 * Created by lenovo on 10/22/2017.
 */

public class HouseholdFragment extends Fragment {
    private MainActivity mMainActivity;
    private FragmentManager mManager;
    private ResidentRepository mResidentRepository;

    private ResidentEntity mResidentEntity;

    private EditText mNoOfFamily,
                     mNoOfPeople;

    private RadioGroup m4Ps;

    private RadioButton m4PsYes,
                        m4PsNo;

    private Button mSave;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house_hold_layout, container, false);
        mMainActivity = (MainActivity) getActivity();
        mResidentRepository = mMainActivity.getResidentRepository();

        mResidentEntity = mMainActivity.getResidentEntity();
        mManager = mMainActivity.getManager();

        mNoOfFamily = (EditText) view.findViewById(R.id.et_house_hold_no_of_family);
        mNoOfPeople = (EditText) view.findViewById(R.id.et_house_hold_no_of_people);
        m4Ps = (RadioGroup) view.findViewById(R.id.rg_house_hold_is_4ps);
        m4PsYes = (RadioButton) view.findViewById(R.id.rb_house_hold_is_4ps);
        m4PsNo = (RadioButton) view.findViewById(R.id.rb_house_hold_not_4ps);

        Document document = mMainActivity.getDatabase().getDocument(mResidentEntity.getId());
        if (mResidentEntity.getProperty(document, ResidentEntity.HOUSEHOLD) != null) {
            mResidentEntity = new ResidentEntity(document);
            ResidentEntity.Household household = mResidentEntity.getHousehold();
            mNoOfFamily.setText(Integer.toString(household.getNoOfFamilyInHousehold()));
            mNoOfPeople.setText(Integer.toString(household.getNoOfPeopleInHousehold()));
            check4Ps(household.isFamily4PsMember());
        }

        mSave = (Button) view.findViewById(R.id.btn_house_hold_save);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValid()) {
                    return;
                }

                ResidentEntity.Household household = mResidentEntity.getHousehold();
                household.setNoOfFamilyInHousehold(Integer.parseInt(mNoOfFamily.getText().toString()));
                household.setNoOfPeopleInHousehold(Integer.parseInt(mNoOfPeople.getText().toString()));
                household.setFamily4PsMember(is4Ps());
                HouseholdUpdatable householdUpdatable = new HouseholdUpdatable(mMainActivity.getDatabase());
                mResidentRepository.setIUpdatable(householdUpdatable);
                mResidentRepository.update(mResidentEntity);

                ResidentFragment residentFragment = new ResidentFragment();
                FragmentTransaction transaction = mManager.beginTransaction();
                transaction.replace(R.id.linear_fragment_container, residentFragment, getString(R.string.tag_resident_fragment));
                transaction.commit();
            }
        });
        return view;
    }

    private boolean isValid () {
        boolean noError = true;
        if (mNoOfFamily.getText().toString().trim().equals("")) {
            mNoOfFamily.setError(getString(R.string.message_error_required));
            noError = false;
        }
        if (mNoOfPeople.getText().toString().trim().equals("")) {
            mNoOfPeople.setError(getString(R.string.message_error_required));
            noError = false;
        }

        if (m4Ps.getCheckedRadioButtonId() == -1) {
            m4PsYes.setError(getString(R.string.message_error_select));
            noError = false;

        }
        return noError;
    }

    private boolean is4Ps() {
        int checked = m4Ps.getCheckedRadioButtonId();
        switch (checked) {
            case R.id.rb_house_hold_is_4ps:
                return true;
            case R.id.rb_house_hold_not_4ps:
                return false;
            default:
                return false;
        }
    }

    private void check4Ps(boolean yes) {
        if (yes) {
            m4PsYes.setChecked(true);
        } else {
            m4PsNo.setChecked(true);
        }
    }
}
