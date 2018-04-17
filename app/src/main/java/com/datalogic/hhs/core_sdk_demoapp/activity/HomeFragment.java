package com.datalogic.hhs.core_sdk_demoapp.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.datalogic.hhs.core_sdk.DatalogicCommand;
import com.datalogic.hhs.core_sdk.DatalogicDeviceResponse;
import com.datalogic.hhs.core_sdk_demoapp.Inventory;
import com.datalogic.hhs.core_sdk_demoapp.MainActivity;
import com.datalogic.hhs.core_sdk_demoapp.R;


public class HomeFragment extends Fragment  implements View.OnClickListener  {

    public int addTotal = 0;


    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((MainActivity) getActivity()).mFragment = this;

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Button add = (Button) rootView.findViewById(R.id.buttonIncrease);

        add.setOnClickListener(new View.OnClickListener() {
            MainActivity activity = (MainActivity) getActivity();

            public void onClick(View v) {
                String barcode = activity.getMyData();
                if (MainActivity.mDatalogicDevice != null) {
                    EditText  tv = (EditText)rootView.findViewById(R.id.editTextQt);
                    if (tv.getText().length() == 0) {
                        addTotal = 0;
                    } else {
                        addTotal = Integer.valueOf(tv.getText().toString());

                        if (tv.getText().length() > 0) {
                            int  qt = Inventory.getQuantity(barcode);
                            int total = addTotal + qt ;
                            Inventory.setQuantity(barcode, total);
                            int newQuantity = Inventory.getQuantity(barcode);
                            TextView txtViewQt = (TextView) rootView.findViewById(R.id.textViewSetqtHome);
                            txtViewQt.setText(String.valueOf(newQuantity));
                            tv.getText().clear();
                        }
                    }
                }
            }
        });



        Button del = (Button) rootView.findViewById(R.id.buttonDecrease);
        del.setOnClickListener(new View.OnClickListener() {
            MainActivity activity = (MainActivity) getActivity();

            public void onClick(View v) {
                if (MainActivity.mDatalogicDevice != null) {
                    String barcode = activity.getMyData();
                    EditText  tv = (EditText)rootView.findViewById(R.id.editTextQt);
                    if (tv.getText().length() == 0) {
                        addTotal = 0;
                    } else {
                        addTotal = Integer.valueOf(tv.getText().toString());

                        if (tv.getText().length() > 0) {
                            int  qt = Inventory.getQuantity(barcode);
                            int total = qt - addTotal  ;
                            Inventory.setQuantity(barcode, total);
                            int newQuantity = Inventory.getQuantity(barcode);
                            TextView txtViewQt = (TextView) rootView.findViewById(R.id.textViewSetqtHome);
                            txtViewQt.setText(String.valueOf(newQuantity));
                            tv.getText().clear();
                        }
                    }
                }
            }
        });


        Button enable = (Button) rootView.findViewById(R.id.buttonEnable);
        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mDatalogicDevice != null) {
                    MainActivity.mDatalogicDevice.sendCommand(DatalogicCommand.ENABLE_SCANNER);
                }
            }
        });


        Button disable = (Button) rootView.findViewById(R.id.buttonDisable);
        disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mDatalogicDevice != null) {
                    MainActivity.mDatalogicDevice.sendCommand(DatalogicCommand.DISABLE_SCANNER);
                }
            }
        });


        Button trigger = (Button) rootView.findViewById(R.id.buttonTrigger);
        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mDatalogicDevice != null) {
                    MainActivity.mDatalogicDevice.sendCommand(DatalogicCommand.SOFTWARE_TRIGGER_ACTIVE) ;
                }
            }
        });


        Button triggerOff = (Button) rootView.findViewById(R.id.buttonTriggerOff);
        triggerOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mDatalogicDevice != null) {
                    MainActivity.mDatalogicDevice.sendCommand(DatalogicCommand.SOFTWARE_TRIGGER_RELEASE);
                }
            }
        });


        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Power OFF", Snackbar.LENGTH_SHORT).setAction("Confirm", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MainActivity.mDatalogicDevice != null) {
                            MainActivity.mDatalogicDevice.sendCommand(DatalogicCommand.POWER_OFF, new DatalogicDeviceResponse.Callback() {
                                @Override
                                public void onResponse(DatalogicDeviceResponse datalogicDeviceResponse) {
                                    if (!datalogicDeviceResponse.isOnError()) {
                                        Toast.makeText(getActivity(), "Power Off: ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Error: " + datalogicDeviceResponse.getErrorCode(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }).show();
            }
        });

        return rootView;
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View view) { }

}
