package com.blazingmuffin.health.mdmsystem.other.models;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by bchantiu on 10/21/17.
 */

public class Peer {
    private String mName;
    private URL mURL;
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

    public URL getURL() throws MalformedURLException {
        mURL = new URL(mName);
        return mURL;
    }
}
