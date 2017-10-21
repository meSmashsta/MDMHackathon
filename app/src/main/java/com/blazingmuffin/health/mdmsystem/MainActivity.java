package com.blazingmuffin.health.mdmsystem;

import android.annotation.SuppressLint;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.blazingmuffin.health.mdmsystem.other.models.MDMContext;
import com.blazingmuffin.health.mdmsystem.other.models.ResidentEntity;
import com.blazingmuffin.health.mdmsystem.other.repositories.ResidentRepository;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseOptions;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.listener.LiteListener;
import com.couchbase.lite.replicator.Replication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import de.mannodermaus.rxbonjour.BonjourBroadcastConfig;
import de.mannodermaus.rxbonjour.BonjourEvent;
import de.mannodermaus.rxbonjour.RxBonjour;
import de.mannodermaus.rxbonjour.drivers.jmdns.JmDNSDriver;
import de.mannodermaus.rxbonjour.platforms.android.AndroidPlatform;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static String TAG = "JEEPERS : MainActivity";
    private ResidentRepository mResidentRepository;
    private ResidentEntity mResidentEntity;

    private FragmentManager mManager;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationDrawer;

    private String mSyncURLString = "";
    private String mRESTURLString;

    // couchbase
    private static Manager mCBManager;
    private Database mDatabase;
    private LiteListener mRESTListener;
    public static final String designDocName = "resident-view";
    public static final String byDateViewName = "byDate";
    private Boolean mIsCBSyncRunning = false;
    private Replication mPullReplication;
    private Replication mPushReplication;

    // service discovery and registration
    protected RxBonjour mRxBonjour;

    public RxBonjour getRxBonjour() {
        return mRxBonjour;
    }

    public void setSyncURLString(String syncURLString) {
        mSyncURLString = syncURLString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mToolbar = findViewById(R.id.toolbar_general_layout);
        setSupportActionBar(mToolbar);

        Database database = MDMContext.Instance(MainActivity.this);
        mResidentRepository = new ResidentRepository(database);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mNavigationDrawer = findViewById(R.id.navigation_views);
        mNavigationDrawer.setNavigationItemSelectedListener(this);

        startServices();

        mManager = getSupportFragmentManager();

        ResidentFragment residentFragment = new ResidentFragment();
        FragmentTransaction transaction = mManager.beginTransaction();
        transaction.add(R.id.linear_fragment_container, residentFragment, "resident");
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction fragmentTransaction = mManager.beginTransaction();
        switch (item.getItemId()) {
            case R.id.item_residents:
                ResidentFragment residentFragment = new ResidentFragment();
                fragmentTransaction.replace(R.id.linear_fragment_container, residentFragment, getString(R.string.tag_resident_fragment));
                break;
            case R.id.item_peers:
                PeerFragment peerFragment = new PeerFragment();
                fragmentTransaction.replace(R.id.linear_fragment_container, peerFragment, "peer");

                break;
            case R.id.item_settings:
                SettingsFragment settingsFragment = new SettingsFragment();
                fragmentTransaction.replace(R.id.linear_fragment_container, settingsFragment, getString(R.string.tag_settings_fragment));
                break;
        }
        fragmentTransaction.commit();
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mActionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public FragmentManager getManager() {
        return this.mManager;
    }

    public ResidentRepository getResidentRepository() {
        return this.mResidentRepository;
    }

    public void setResidentEntity(ResidentEntity residentEntity) {
        this.mResidentEntity = residentEntity;
    }

    public ResidentEntity getResidentEntity() {
        return this.mResidentEntity;
    }

    private void startServices(){
        try {
            startCouchbaseLite();
            startCBSyncing();
            startRESTListener();
            //mRESTURLString = getRESTListenerURL();
            //mExperimentUrlText.setText(mRESTURLString);
            mRxBonjour = (new RxBonjour.Builder()).platform(AndroidPlatform.create(this)).driver(JmDNSDriver.create()).create();
            broadcastService();
            // call discover on Peer Fragment
            //startDiscover();
        } catch (CouchbaseLiteException e){
            Toast.makeText(getApplicationContext(), "DB : Get Database went wrong.", Toast.LENGTH_LONG);
            Log.d(TAG, "Can not create initialize CB Lite");
        }  catch (IOException e){
            Toast.makeText(getApplicationContext(), "DB : Initialization went wrong.", Toast.LENGTH_LONG);
            Log.d(TAG, "Can not create initialize CB Lite");
        }
        Log.d(TAG, "DONE!");
    }


    private void startCouchbaseLite() throws IOException, CouchbaseLiteException {
        mCBManager = MDMContext.Manager(this);
        mDatabase = MDMContext.Instance(this);

        /*
        Manager.enableLogging(TAG, Log.VERBOSE);
        mCBManager = new Manager(new AndroidContext(getApplicationContext()), Manager.DEFAULT_OPTIONS);
        DatabaseOptions dbOptions = new DatabaseOptions();
        dbOptions.setCreate(true);
        String db = getString(R.string.general_database_name);
        mDatabase = mCBManager.openDatabase(db, dbOptions);
        // TODO: 10/21/17 PUT Create View here
        com.couchbase.lite.View viewItemsByDate = mDatabase.getView(String.format("%s/%s", designDocName, byDateViewName));
        viewItemsByDate.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                Object createdAt = document.get("created_at");
                if (createdAt != null) {
                    emitter.emit(createdAt.toString(), null);
                }
            }
        }, "1.0");
        */
    }

    protected void startCBSyncing(){
        URL syncURL;
        stopRunningSync();
        if(!mSyncURLString.isEmpty()) {
            try {
                syncURL = new URL(mSyncURLString);
                initPULLReplication(syncURL);
                initPUSHReplication(syncURL);
                mIsCBSyncRunning = true;
                Log.d(TAG, "CB SYNC RUNNING ................");
            } catch (MalformedURLException e) {
                // TODO: 10/21/17 Catch Error on URL
                Log.e(TAG, "Malformed URL");
                //throw new RuntimeException(e);
            } catch (Exception e) {
                // TODO: 10/21/17 Catch Error on URL
                Log.e(TAG, "Error on CB Syncing");
            }
        }
    }

    private void initPULLReplication(URL syncURL){
        mPullReplication = mDatabase.createPullReplication(syncURL);
        mPullReplication.setContinuous(true);
        mPullReplication.start();
        mPullReplication.addChangeListener(new Replication.ChangeListener() {
            @Override
            public void changed(Replication.ChangeEvent event) {
                // TODO: 10/21/17 PUT PULL Replication Event handler here
                Log.d(TAG, "PULL : change detected!");
            }
        });
    }

    private void initPUSHReplication(URL syncURL){
        mPushReplication = mDatabase.createPushReplication(syncURL);
        mPushReplication.setContinuous(true);
        mPushReplication.start();
        mPushReplication.addChangeListener(new Replication.ChangeListener() {
            @Override
            public void changed(Replication.ChangeEvent event) {
                // TODO: 10/21/17 PUT PUSH Replication Event handler here
                Log.d(TAG, "PUSH : change detected!");
            }
        });
    }

    private void startRESTListener(){
        mRESTListener = new LiteListener(mCBManager, 5432, null);
        Thread thread = new Thread(mRESTListener);
        thread.start();
    }

    private String getRESTListenerURL() throws MalformedURLException {
        String restUrlString = "";
        String ipadd = getIPAddressDeprecated();
        int port = mRESTListener.getListenPort();
        URL url = new URL("http", ipadd, port, "/" + getString(R.string.general_database_name));
        return url.toString();
    }

    private String getIPAddress(){
        String IPAddress = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress())  {
                        IPAddress = inetAddress.getHostAddress().toString();
                        break;
                    }

                }
            }
        } catch (SocketException ex) {
            Log.e("LOG_TAG", ex.toString());
            IPAddress = "";
        }
        return IPAddress;
    }

    private String getIPAddressDeprecated(){
        @SuppressLint("WifiManagerLeak") WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;
    }

    private void broadcastService(){
        HashMap<String, String> mss1 = new HashMap<String, String>();
        mss1.put("my.record","my value");
        mss1.put("other.record","0815");
        String serviceName = getString(R.string.general_database_name);
        BonjourBroadcastConfig config = new BonjourBroadcastConfig("_http._tcp",
                serviceName, null, mRESTListener.getListenPort(), mss1);
        mRxBonjour.newBroadcast(config)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d("CHAN", "reg : SUBSCRIBE");
                    }
                    @Override
                    public void onComplete() {
                        Log.d("CHAN", "reg : COMPLETE");
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("CHAN", "reg : ERROR");
                    }
                });
    }

    protected Observable<BonjourEvent> startDiscover (){
        return mRxBonjour.newDiscovery("_http._tcp")
                .filter(
                        new Predicate<BonjourEvent>() {
                            @Override
                            public boolean test(@io.reactivex.annotations.NonNull BonjourEvent bonjourEvent) throws Exception {
                                Boolean is_diff_address = bonjourEvent.getService().getHost().getHostAddress().compareTo(getIPAddressDeprecated()) != 0;
                                Boolean is_correct_service = bonjourEvent.getService().getName().startsWith(getString(R.string.general_database_name));
                                return is_correct_service && is_diff_address;
                            }
                        }
                );
                //.filter(e -> e.getService().getHost().getHostAddress().compareTo(getIPAddressDeprecated()) == -1)
                //.filter(e -> e.getService().getName().startsWith(getString(R.string.general_database_name)))
                //.subscribeOn(Schedulers.io());
    }//--startDiscover

    protected void stopRunningSync(){
        if(mIsCBSyncRunning){
            mPullReplication.stop();
            mPushReplication.stop();
            mIsCBSyncRunning = false;
        }
    }
}
