package com.blazingmuffin.health.mdmsystem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 10/21/2017.
 */

public class ResidentFragment extends Fragment implements ResidentAdapter.IRecyclerViewClickListener {
    private RecyclerView mRecyclerView;
    private ResidentAdapter mResidentAdapter;

    private FragmentManager mManager;

    private MainActivity mMainActivity;

    private ResidentRepository mResidentRepository;

//    private Button  mAdd,
//                    mEdit,
//                    mDelete;
    private Disposable mAddClicks;
    private Disposable mQueryChanges;
    private Disposable mDatabaseChanges;
    private LiveQuery mQuery ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resident_layout, container, false);
        mRecyclerView = view.findViewById(R.id.rv_resident_fragment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMainActivity = (MainActivity) getActivity();

        mResidentAdapter = new ResidentAdapter(this, mMainActivity.getResidentRepository());
        mRecyclerView.setAdapter(mResidentAdapter);

        mAddClicks = RxView.clicks(view.findViewById(R.id.btn_resident_add))
                .subscribe(x -> {
                            ResidentFragment residentFragment = new ResidentFragment();
                            FragmentTransaction transaction = mManager.beginTransaction();
                            transaction.replace(R.id.linear_fragment_container, residentFragment, getString(R.string.tag_resident_fragment));
                            transaction.addToBackStack(getString(R.string.tag_resident_details_fragment_backstack));
                            transaction.commit();

                            ResidentCreateFragment residentCreateFragment = new ResidentCreateFragment();
                            FragmentTransaction fragmentTransaction = mManager.beginTransaction();
                            fragmentTransaction.add(R.id.linear_fragment_container, residentCreateFragment, getString(R.string.tag_resident_create_fragment));
                            fragmentTransaction.commit();
//                            ResidentRepository residentRepository = new ResidentRepository(MDMContext.Instance(getActivity()));
//                            ResidentEntity residentEntity = new ResidentEntity();
//                            residentEntity.setFirstName("Mycar");
//                            residentEntity.setMiddleName("Pena");
//                            residentEntity.setLastName("Chu");
//                            residentEntity.setBirthdate("12/05/1994");
//                            residentEntity.setGender("Male");
//                            residentRepository.create(residentEntity);
                        }
                );
        mManager = ((MainActivity) getActivity()).getManager();

//        mEdit = (Button) view.findViewById(R.id.btn_resident_edit);
//        mDelete = (Button) view.findViewById(R.id.btn_resident_delete);

        mResidentRepository = new ResidentRepository(MDMContext.Instance(getActivity()));

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
//            Toast.makeText(getActivity(), "DB CHANGE", Toast.LENGTH_SHORT).show();

        });

        mQuery = cview.createQuery().toLiveQuery();
        mQueryChanges = Observable.<LiveQuery.ChangeEvent>create(s -> {
            mQuery.addChangeListener(s::onNext);
            mQuery.start();
        }).subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread())
        .subscribe(e -> {
            Toast.makeText(getActivity(), "Updated..." + e.getRows().getCount(), Toast.LENGTH_LONG).show();
            mResidentAdapter.setResidents(e.getRows());
        });

        return view;
    }

    @Override
    public void onDestroy() {
        mAddClicks.dispose();
        mQueryChanges.dispose();
        mDatabaseChanges.dispose();
        mQuery.stop();
        super.onDestroy();
    }

    @Override
    public void onRecyclerViewClickListener(ResidentEntity residentEntity) {
        mMainActivity.setResidentEntity(residentEntity);

        Fragment fragment = mManager.findFragmentByTag(getString(R.string.tag_resident_fragment));
        FragmentTransaction transaction = mManager.beginTransaction();
        transaction.replace(R.id.linear_fragment_container, fragment, getString(R.string.tag_resident_fragment));
        transaction.addToBackStack(getString(R.string.tag_resident_details_fragment_backstack));
        transaction.commit();

        ResidentDetailsFragment residentDetailsFragment = new ResidentDetailsFragment();
        FragmentTransaction fragmentTransaction = mManager.beginTransaction();
        fragmentTransaction.add(R.id.linear_fragment_container, residentDetailsFragment,
                getString(R.string.tag_resident_details_fragment));
        fragmentTransaction.addToBackStack(getString(R.string.tag_resident_details_fragment_backstack));
        fragmentTransaction.commit();
    }
}
