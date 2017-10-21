package com.blazingmuffin.health.mdmsystem.other.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blazingmuffin.health.mdmsystem.R;
import com.blazingmuffin.health.mdmsystem.other.models.Peer;

import java.util.List;

/**
 * Created by lenovo on 10/21/2017.
 */

public class PeerAdapter extends RecyclerView.Adapter<PeerAdapter.PeerViewHolder> {

    private List<Peer> mPeers;
    private Context mContext;

    // Define listener member variable
    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Pass in the contact array into the constructor
    public PeerAdapter(Context context, List<Peer> peers) {
        mPeers = peers;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // inflating layout from xml and returning the holder
    @Override
    public PeerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View peerView = inflater.inflate(R.layout.item_peer_layout, parent, false);

        PeerViewHolder viewholder = new PeerViewHolder(peerView);
        return viewholder;
    }


    // populating data through the holder
    @Override
    public void onBindViewHolder(PeerViewHolder holder, int position) {
        Peer peer = mPeers.get(position);

        TextView textView = holder.mPeerTextView;
        textView.setText(peer.getName());
        Button button = holder.mConnectButton;
        button.setEnabled(true);
    }

    @Override
    public int getItemCount() {
        return mPeers.size();
    }

    public  class PeerViewHolder extends RecyclerView.ViewHolder{
        public TextView mPeerTextView;
        public Button mConnectButton;

        public PeerViewHolder(View itemView) {
            super(itemView);

            mPeerTextView = itemView.findViewById(R.id.peer_textview);
            mConnectButton = itemView.findViewById(R.id.connect_button);
            mConnectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }
}
