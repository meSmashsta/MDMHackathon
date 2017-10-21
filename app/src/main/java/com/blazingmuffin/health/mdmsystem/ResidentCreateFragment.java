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
import android.widget.RadioGroup;

import com.blazingmuffin.health.mdmsystem.other.models.ResidentEntity;
import com.blazingmuffin.health.mdmsystem.other.repositories.ResidentRepository;
import com.blazingmuffin.health.mdmsystem.utils.WidgetUtils;

/**
 * Created by lenovo on 10/21/2017.
 */

public class ResidentCreateFragment extends Fragment {
    private MainActivity mMainActivity;
    private ResidentRepository mResidentRepository;

    private FragmentManager mManager;

    private EditText mFullName,
//                     mMiddleName,
//                     mLastName,
                     mBirthdate;

    private RadioGroup mGender;

    private Button mCreate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resident_create_layout, container, false);

        mMainActivity = (MainActivity) getActivity();
        mManager = mMainActivity.getManager();
        mResidentRepository = mMainActivity.getResidentRepository();

        mGender = view.findViewById(R.id.rg_resident_create_gender);

        mFullName = view.findViewById(R.id.et_resident_create_full_name);
        mBirthdate = view.findViewById(R.id.et_resident_create_birthdate);

        mCreate = view.findViewById(R.id.btn_resident_create_create);
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResidentEntity residentEntity = new ResidentEntity();
                residentEntity.setFullName(WidgetUtils.getText(mFullName));
//                residentEntity.setMiddleName(WidgetUtils.getText(mMiddleName));
//                residentEntity.setLastName(WidgetUtils.getText(mLastName));
                residentEntity.setBirthdate(WidgetUtils.getText(mBirthdate));
                residentEntity.setGender(WidgetUtils.getText(mGender));
                mResidentRepository.create(residentEntity);

                ResidentFragment residentFragment = new ResidentFragment();
                FragmentTransaction fragmentTransaction = mManager.beginTransaction();
                fragmentTransaction.replace(R.id.linear_fragment_container, residentFragment,
                        getString(R.string.tag_resident_fragment));
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}
