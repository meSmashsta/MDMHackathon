package com.blazingmuffin.health.mdmsystem.other.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blazingmuffin.health.mdmsystem.R;
import com.couchbase.lite.QueryEnumerator;

/**
 * Created by lenovo on 10/21/2017.
 */

public class ResidentAdapter extends RecyclerView.Adapter<ResidentAdapter.ResidentViewHolder> {
    private QueryEnumerator mResidents;

    @Override
    public ResidentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParent = false;
        View view = layoutInflater.inflate(R.layout.item_resident_layout, parent, shouldAttachToParent);
        return new ResidentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResidentViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (mResidents != null) return mResidents.getCount();
        return 0;
    }

    public void setResidents(QueryEnumerator residents) {
        this.mResidents = residents;
        notifyDataSetChanged();
    }

    public QueryEnumerator getResidents() {
        return this.mResidents;
    }

    public class ResidentViewHolder extends RecyclerView.ViewHolder {
        public ResidentViewHolder(View view) {
            super(view);
        }
    }
}
