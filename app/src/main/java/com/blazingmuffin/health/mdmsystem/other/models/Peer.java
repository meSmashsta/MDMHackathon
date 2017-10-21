package com.blazingmuffin.health.mdmsystem.other.models;

import java.util.ArrayList;

/**
 * Created by bchantiu on 10/21/17.
 */

public class Peer {
    private String mName;

    public Peer(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public static ArrayList<Peer> createExamplePeerList(int numPeers) {
        ArrayList<Peer> peers = new ArrayList<Peer>();

        for (int i = 1; i <= numPeers; i++) {
            peers.add(new Peer("http://blablabla/" + i));
        }
        return peers;
    }
}
