package com.blazingmuffin.health.mdmsystem;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private FragmentManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mManager = getSupportFragmentManager();
//
//        ResidentFragment residentFragment = new ResidentFragment();
//        FragmentTransaction transaction = mManager.beginTransaction();
//        transaction.add(R.id.linear_fragment_container, residentFragment, "resident");
//        transaction.commit();


        SettingsFragment settingsFragment = new SettingsFragment();
        FragmentTransaction transaction = mManager.beginTransaction();
        transaction.add(R.id.linear_fragment_container, settingsFragment, "resident");
        transaction.commit();

    }
}
