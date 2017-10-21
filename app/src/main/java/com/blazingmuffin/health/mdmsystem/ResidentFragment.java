package com.blazingmuffin.health.mdmsystem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blazingmuffin.health.mdmsystem.other.adapter.ResidentAdapter;
import com.blazingmuffin.health.mdmsystem.other.models.MDMContext;
import com.blazingmuffin.health.mdmsystem.other.models.ResidentEntity;
import com.blazingmuffin.health.mdmsystem.other.repositories.ResidentRepository;
import com.couchbase.lite.Database;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.Mapper;

import java.util.Map;

import com.couchbase.lite.Query;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 10/21/2017.
 */

public class ResidentFragment extends Fragment{
    private ResidentAdapter mResidentAdapter;

    private Disposable mAddClicks;
    private Disposable mQueryChanges;
    private Disposable mDatabaseChanges;
    private LiveQuery mQuery ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resident_layout, container, false);
        RecyclerView mRecyclerView = view.findViewById(R.id.rv_resident_fragment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mResidentAdapter = new ResidentAdapter();
        mRecyclerView.setAdapter(mResidentAdapter);

        mAddClicks = RxView.clicks(view.findViewById(R.id.btn_resident_add))
                .subscribe(x -> {
                            ResidentRepository residentRepository = new ResidentRepository(MDMContext.Instance(getActivity()));
                            ResidentEntity residentEntity = new ResidentEntity();
                            residentEntity.setFirstName("Mycar");
                            residentEntity.setMiddleName("Pena");
                            residentEntity.setLastName("Chu");
                            residentEntity.setBirthdate("12/05/1994");
                            residentEntity.setGender("Male");
                            residentRepository.create(residentEntity);
                        }
                );

        Database db = MDMContext.Instance(getContext());
        com.couchbase.lite.View cview = db.getView(ResidentEntity.VIEW);
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

        mDatabaseChanges = Observable.<Database.ChangeEvent>create(s -> {
            db.addChangeListener(s::onNext);
        }).observeOn(AndroidSchedulers.mainThread())
        .subscribe(e ->{
            Toast.makeText(getActivity(), "DB CHANGE", Toast.LENGTH_SHORT).show();
        });

        mQuery = cview.createQuery().toLiveQuery();
        mQueryChanges = Observable.<LiveQuery.ChangeEvent>create(s -> {
            mQuery.addChangeListener(s::onNext);
            mQuery.start();
        }).observeOn(AndroidSchedulers.mainThread())
        .subscribe(e -> {
            Toast.makeText(getActivity(), "Updated..." + e.getRows().getCount(), Toast.LENGTH_LONG).show();
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAddClicks.dispose();
        mQueryChanges.dispose();
        mDatabaseChanges.dispose();
        mQuery.stop();
    }
}
