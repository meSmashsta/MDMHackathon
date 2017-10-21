package com.blazingmuffin.health.mdmsystem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blazingmuffin.health.mdmsystem.other.models.MDMContext;
import com.blazingmuffin.health.mdmsystem.other.models.ResidentEntity;
import com.blazingmuffin.health.mdmsystem.other.repositories.ResidentRepository;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.Mapper;

import java.util.Map;

/**
 * Created by lenovo on 10/21/2017.
 */

public class ResidentFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resident_layout, container, false);

        return view;
    }
}
