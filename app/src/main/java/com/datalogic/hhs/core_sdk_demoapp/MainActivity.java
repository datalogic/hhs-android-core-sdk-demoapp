package com.datalogic.hhs.core_sdk_demoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.datalogic.hhs.core_sdk.DatalogicCommand;
import com.datalogic.hhs.core_sdk.DatalogicReceivedData;
import com.datalogic.hhs.core_sdk.DatalogicTransmittedData;
import com.datalogic.hhs.core_sdk.DatalogicCoreSDK;
import com.datalogic.hhs.core_sdk.DatalogicDevice;
import com.datalogic.hhs.core_sdk.DatalogicDeviceDiscoveryActivity;
import com.datalogic.hhs.core_sdk.DatalogicDeviceResponse;
import com.datalogic.hhs.core_sdk_demoapp.activity.BatchFragment;
import com.datalogic.hhs.core_sdk_demoapp.activity.ConfigurationFragment;
import com.datalogic.hhs.core_sdk_demoapp.activity.FragmentDrawer;
import com.datalogic.hhs.core_sdk_demoapp.activity.HealthCareFragment;
import com.datalogic.hhs.core_sdk_demoapp.activity.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private DatalogicCoreSDK mDatalogicCoreSDK;

    private DatalogicDeviceListAdapter bluetoohDeviceSdkViewAdapter;
    private List<DatalogicDevice> devicesList = new ArrayList<>();

    public static DatalogicDevice mDatalogicDevice;
    private ArrayAdapter<String> mConversationArrayAdapter;
    private ArrayAdapter<String> mConversationArrayAdapter2;

    private String myString = "";
    public Fragment mFragment;


    private String sBatteryValue = "";
    private String deviceName = "";
    private String myconnect = "disconnect";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatalogicCoreSDK = DatalogicCoreSDK.get(this, mCallback);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);

        mConversationArrayAdapter2 = new ArrayAdapter<String>(this, R.layout.message);


        /* Left menu fragment */
        FragmentDrawer drawerFragment = (FragmentDrawer)
                                        getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);


        if (!mDatalogicCoreSDK.isBluetoothEnabled()) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Bluetooth not enable.\nDo you want to enable it ?");

            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mDatalogicCoreSDK.enableBluetooth();
                }
            });

            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                    return;
                }
            });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

        devicesList.addAll(mDatalogicCoreSDK.getBondedDevices());
        bluetoohDeviceSdkViewAdapter = new DatalogicDeviceListAdapter(this, R.layout.list_devices_view, devicesList);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mDatalogicCoreSDK.stop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_connect:
                Intent serverIntent = new Intent(MainActivity.this, DatalogicDeviceDiscoveryActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

            case R.id.action_battery:
                setBatteryIcon();
                break;

            case R.id.action_search:
                GetDeviceName();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private DatalogicDevice.Callback mDeviceCallback = new DatalogicDevice.Callback() {

        @Override
        public void onConnected() {
            if (mDatalogicDevice != null) {
                statusMessage(mDatalogicDevice.isSdkProtocolEnabled() ? getResources().getString(R.string.connectedMainOverSpp) : getResources().getString(R.string.connectedMainNotOverSpp));
                Toast.makeText(MainActivity.this, mDatalogicDevice.getName() + " connected ..", Toast.LENGTH_SHORT).show();

                myconnect = " connected ";
                getSupportActionBar().setTitle(myconnect.toUpperCase());
            }
        }

        @Override
        public void onConnecting() {
            Toast.makeText(MainActivity.this, mDatalogicDevice.getName() + " on connecting", Toast.LENGTH_SHORT).show();
            myconnect = " on connecting ";
            getSupportActionBar().setTitle(myconnect.toUpperCase());
        }

        @Override
        public void onDisconnected() {
            Toast.makeText(MainActivity.this, mDatalogicDevice.getName() + " disconnected", Toast.LENGTH_SHORT).show();
            myconnect = " disconnected ";
            getSupportActionBar().setTitle(myconnect.toUpperCase());
        }

        @Override
        public void onConnectionFailed() {
            Toast.makeText(MainActivity.this, R.string.connecteFailedMain, Toast.LENGTH_SHORT).show();
        }


        public void onDataReceived(DatalogicReceivedData datalogicReceivedData) {

            if (datalogicReceivedData.isLabel()) {

                String msgView = datalogicReceivedData.getPayload().trim();

                myString = msgView;

                setMydata(msgView);

                if (mFragment instanceof HomeFragment) {

                    TextView text = (TextView) findViewById(R.id.textViewBarcodeShowHome);
                    text.setText("  " + myString + " ");

                    int qt = Inventory.getQuantity(myString);

                    text = (TextView) findViewById(R.id.textViewSetqtHome);

                    int total = qt - 1;
                    Inventory.setQuantity(myString, total);
                    int newqt = Inventory.getQuantity(myString);
                    text.setText(Integer.toString(newqt));

                    text = (TextView) findViewById(R.id.textViewDescrptionShow);
                    text.setText(Inventory.getDescription(msgView));
                    setInventoryImg(Inventory.getImageName(myString));

                    text = (TextView) findViewById(R.id.editTextPrice);
                    text.setText(Inventory.getPrice(myString));

                }
                if (mFragment instanceof BatchFragment) {
                    ListView mConversationView1 = (ListView) findViewById(R.id.listViewBatch);
                    mConversationView1.setAdapter(mConversationArrayAdapter);
                    int qt = Inventory.getQuantity(myString);
                    String des = Inventory.getDescription(myString);
                    mConversationArrayAdapter.add(" Barcode : " + myString + " Description : " + des + " Quantity = " + qt);
                }

                if (mFragment instanceof HealthCareFragment) {
                    ListView mConversationView2 = (ListView) findViewById(R.id.listViewHealth);
                    mConversationView2.setAdapter(mConversationArrayAdapter2);
                    int qt = Inventory.getQuantity(myString);
                    String des = Inventory.getDescription(myString);
                    mConversationArrayAdapter2.add(" Barcode : " + myString + " Description :  " + des + " Quantity = " + qt);
                }

                if (mFragment instanceof ConfigurationFragment) {
                    TextView text = (TextView) findViewById(R.id.brconfiguration);
                    text.setText(myString);
                }
            }
        }

        public void setMydata(String data) {
            myString = data;
        }

        public void onDataTransmitted(final DatalogicTransmittedData dataTransmitted) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String msgView = "Data transmitted: " + dataTransmitted.toString();
                }
            });
        }

    };


    public String getMyData() {
        return myString;
    }

    private void statusMessage(String msg) {
        String msgView = "Change Status: " + msg;
    }


    /**
     * Fragment Drawer Listener implementation
     */
    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.title_all);

        String dev = "";
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home) + " " + myconnect.toUpperCase();
                ;
                break;
            case 1:
                fragment = new BatchFragment();
                title = getString(R.string.title_friends) + " " + myconnect.toUpperCase();
                break;
            case 2:
                fragment = new HealthCareFragment();
                title = getString(R.string.title_messages) + " " + myconnect.toUpperCase();
                break;
            case 3:
                fragment = new ConfigurationFragment();
                title = getString(R.string.title_configuration) + " " + myconnect.toUpperCase();
                ;
                break;
            default:
                break;

        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            getSupportActionBar().setTitle(title);
        }
    }


    private void GetDeviceName() {
        if (mDatalogicDevice != null) {
            if (mDatalogicDevice.isConnected()) {
                mDatalogicDevice.sendCommand(DatalogicCommand.SCANNER_NAME, new DatalogicDeviceResponse.Callback() {
                    @Override
                    public void onResponse(DatalogicDeviceResponse datalogicDeviceResponse) {
                        if (!datalogicDeviceResponse.isOnError()) {
                            deviceName = (String) datalogicDeviceResponse.getValue();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getSupportActionBar().setTitle(deviceName.substring(0, 13) + myconnect.toUpperCase());
                                }
                            });

                        } else {
                            Toast.makeText(MainActivity.this, "Read scanner name error: " + datalogicDeviceResponse.getErrorCode(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }


    private void setBatteryIcon() {

        sBatteryValue = "0";

        if (mDatalogicDevice != null) {
            if (mDatalogicDevice.isConnected()) {

                MainActivity.mDatalogicDevice.sendCommand(DatalogicCommand.BATTERY_CHECK, new DatalogicDeviceResponse.Callback() {
                    @Override
                    public void onResponse(DatalogicDeviceResponse datalogicDeviceResponse) {
                        if (!datalogicDeviceResponse.isOnError()) {
                            final Integer progress = (Integer) datalogicDeviceResponse.getValue();
                            sBatteryValue = progress.toString();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!sBatteryValue.isEmpty()) {

                                        if (progress > 50) {
                                            getSupportActionBar().setIcon(R.drawable.fullbattery);
                                        }

                                        if (progress < 50) {
                                            getSupportActionBar().setIcon(R.drawable.halfchargedbattery);
                                        }

                                        if (progress < 25) {
                                            getSupportActionBar().setIcon(R.drawable.lowbattery);
                                        }

                                        if (progress == 0) {
                                            getSupportActionBar().setIcon(R.drawable.nobattery);
                                        }

                                        getSupportActionBar().setTitle(progress + "%");
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connectWith
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(DatalogicDeviceDiscoveryActivity.EXTRA_DEVICE_ADDRESS);
                    DatalogicDevice datalogicDevice = mDatalogicCoreSDK.getDeviceByAddress(address);
                    if (datalogicDevice != null) {
                        devicesList.clear();
                        devicesList.add(datalogicDevice);

                        mDatalogicDevice = mDatalogicCoreSDK.getDeviceByAddress(address);
                        if (mDatalogicDevice != null) {
                            mDatalogicDevice.connect(mDeviceCallback);
                        } else {
                            Toast.makeText(this, "Device with address '" + address + "' not exists", Toast.LENGTH_SHORT).show();
                        }

                        bluetoohDeviceSdkViewAdapter.notifyDataSetChanged();
                    }

                }
                break;
        }
    }


    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private DatalogicCoreSDK.Callback mCallback = new DatalogicCoreSDK.Callback() {

        @Override
        public void onBondChange(DatalogicDevice device) {

            bluetoohDeviceSdkViewAdapter.notifyDataSetChanged();
        }


        @Override
        public void onBluetoothEvent(Intent intent) {
            String action = intent.getAction();
            toast("Bluetooth Event:" + action);
        }
    };


    private void setInventoryImg(String picture) {

        ImageView whoamiwith = (ImageView) findViewById(R.id.imageView6);

        String sFileName = picture; //  this is image file name
        String PACKAGE_NAME = getApplicationContext().getPackageName();
        int imgId = getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + sFileName, null, null);
        System.out.println("IMG ID :: " + imgId);
        System.out.println("PACKAGE_NAME :: " + PACKAGE_NAME);

        whoamiwith.setImageResource(imgId);
    }

}