package com.blazingmuffin.health.mdmsystem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.blazingmuffin.health.mdmsystem.other.adapter.ResidentAdapter;
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

public class ResidentFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ResidentAdapter mResidentAdapter;

    private ResidentRepository mResidentRepository;

    private Button mAdd;

//    private Button  mAdd,
//                    mEdit,
//                    mDelete;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resident_layout, container, false);
        mRecyclerView = view.findViewById(R.id.rv_resident_fragment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mResidentAdapter = new ResidentAdapter();
        mRecyclerView.setAdapter(mResidentAdapter);

        mAdd = view.findViewById(R.id.btn_resident_add);
//        mEdit = (Button) view.findViewById(R.id.btn_resident_edit);
//        mDelete = (Button) view.findViewById(R.id.btn_resident_delete);

        mResidentRepository = new ResidentRepository(MDMContext.Instance(getActivity()));

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResidentEntity residentEntity = new ResidentEntity();
                residentEntity.setFirstName("Mycar");
                residentEntity.setMiddleName("Pena");
                residentEntity.setLastName("Chu");
                residentEntity.setBirthdate("12/05/1994");
                residentEntity.setGender("Male");
                mResidentRepository.create(residentEntity);
            }
        });
//
//        mEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ResidentEntity residentEntity = mResidentRepository.get("45e693d0-833e-45c4-85e6-1734e36a8312");
//                residentEntity.setFirstName("Arvin");
//                BasicUpdatable basicUpdatable = new BasicUpdatable(MDMContext.Instance(getActivity()));
//                mResidentRepository.setIUpdatable(basicUpdatable);
//                mResidentRepository.update(residentEntity);
//            }
//        });
//
//        mDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ResidentEntity residentEntity = mResidentRepository.get("45e693d0-833e-45c4-85e6-1734e36a8312");
//                mResidentRepository.delete(residentEntity);
//            }
//        });

        com.couchbase.lite.View cview = MDMContext.Instance(getContext()).getView(ResidentEntity.VIEW);
        if (cview.getMap() == null) {
            cview.setMap(new Mapper() {
                @Override
                public void map(Map<String, Object> document, Emitter emitter) {
                    if (document.get(ResidentEntity.TYPE).toString().equalsIgnoreCase(ResidentEntity.TYPE_VALUE)) {
                        emitter.emit(document, document.get(ResidentEntity.FIRST_NAME));
                    }
                }
            }, ResidentEntity.VIEW_VERSION);
        }
        LiveQuery liveQuery = cview.createQuery().toLiveQuery();
        liveQuery.addChangeListener(new LiveQuery.ChangeListener() {
            @Override
            public void changed(final LiveQuery.ChangeEvent event) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mResidentAdapter.setResidents(event.getRows());

                        Toast.makeText(getActivity(), "Updated...", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        liveQuery.start();


        return view;
    }
}
