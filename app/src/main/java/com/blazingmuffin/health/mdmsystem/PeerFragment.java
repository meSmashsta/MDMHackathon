package com.blazingmuffin.health.mdmsystem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blazingmuffin.health.mdmsystem.other.adapter.PeerAdapter;
import com.blazingmuffin.health.mdmsystem.other.models.Peer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ListIterator;

import de.mannodermaus.rxbonjour.BonjourEvent;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by lenovo on 10/21/2017.
 */

public class PeerFragment extends Fragment {
    private static String TAG = "JEEPERS : Peer Activity";

    private RecyclerView mRecyclerView;
    private PeerAdapter mPeerAdapter;
    private ArrayList<Peer> peers = new ArrayList<Peer>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "--------------- ON CREATE ---------------------");
        View view = inflater.inflate(R.layout.fragment_peer_layout, container, false);
        mRecyclerView = view.findViewById(R.id.rv_peer_fragment_peers);

        // initialize dummy peers
        mPeerAdapter = new PeerAdapter(this.getContext(), peers);
        mRecyclerView.setAdapter(mPeerAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return view;
    }

    @Override
    public void onStart() {
        Log.d(TAG, "--------------- ON RESUME ---------------------");
        peers.clear();
        ((MainActivity) getActivity()).startDiscover()
                .subscribe(new Observer<BonjourEvent>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        Log.d(TAG,"DISCOVER : onSubscribe");
                        //peers.clear();
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull final BonjourEvent bonjourEvent) {
                        Log.d(TAG,"DISCOVER : onNext");
                        final String ipadd = bonjourEvent.getService().getHost().getHostAddress();
                        int port = bonjourEvent.getService().getPort();
                        try {
                            final URL url = new URL("http", ipadd, port, "/" + getString(R.string.general_database_name));
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    Log.d(TAG, url.toString());
                                    String urlString= url.toString();
                                    if(BonjourEvent.Added.class == bonjourEvent.getClass()){
                                        // remove old peer with url then add
                                        ListIterator<Peer> iter = peers.listIterator();
                                        while(iter.hasNext()){
                                            if(iter.next().getName().contains(ipadd)){
                                                iter.remove();
                                            }
                                        }
                                        peers.add(new Peer(url.toString()));
                                    } else if(BonjourEvent.Removed.class == bonjourEvent.getClass()) {
                                        ListIterator<Peer> iter = peers.listIterator();
                                        while(iter.hasNext()){
                                            if(iter.next().getName().compareTo(urlString) == 0){
                                                iter.remove();
                                            }
                                        }
                                    }
                                    mPeerAdapter.notifyDataSetChanged();
                                }
                            });
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.d(TAG,"DISCOVER : onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG,"DISCOVER : onComplete");
                    }
                });

        super.onStart();
    }

    @Override
    public void onStop() {
        peers.clear();
        mPeerAdapter.notifyDataSetChanged();
        super.onStop();
    }
}
