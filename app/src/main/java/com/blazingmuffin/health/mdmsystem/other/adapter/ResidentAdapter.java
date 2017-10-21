package com.blazingmuffin.health.mdmsystem.other.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blazingmuffin.health.mdmsystem.R;
import com.blazingmuffin.health.mdmsystem.other.models.ResidentEntity;
import com.blazingmuffin.health.mdmsystem.other.repositories.ResidentRepository;
import com.couchbase.lite.Document;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;

/**
 * Created by lenovo on 10/21/2017.
 */

public class ResidentAdapter extends RecyclerView.Adapter<ResidentAdapter.ResidentViewHolder> {
    private final IRecyclerViewClickListener mRecyclerViewClickListener;
    private final ResidentRepository mResidentRepository;
    private QueryEnumerator mResidents;

    public ResidentAdapter(IRecyclerViewClickListener iRecyclerViewClickListener, ResidentRepository residentRepository) {
        this.mRecyclerViewClickListener = iRecyclerViewClickListener;
        this.mResidentRepository = residentRepository;
    }

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
        QueryRow row = mResidents.getRow(position);
        ResidentEntity residentEntity = new ResidentEntity(row.getDocument());
        String display = String.format("%1$s %2$d", residentEntity.getFirstName(), position);
        holder.mFullName.setText(display);
//        holder.mFullName.setText(residentEntity.getId().toString());
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

    public class ResidentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mFullName;
        public ResidentViewHolder(View view) {
            super(view);
            mFullName = view.findViewById(R.id.tv_item_resident_full_name);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int itemIndex = getAdapterPosition();
            Document residentDocument = mResidents.getRow(itemIndex).getDocument();
//            ResidentEntity residentEntity = mResidentRepository.get(Integer.toString(itemIndex));
            ResidentEntity residentEntity = new ResidentEntity(residentDocument);
            mRecyclerViewClickListener.onRecyclerViewClickListener(residentEntity);
        }
    }

    public interface IRecyclerViewClickListener {
        void onRecyclerViewClickListener(ResidentEntity residentEntity);
    }
}
