package com.datalogic.hhs.core_sdk_demoapp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.datalogic.hhs.core_sdk.DatalogicCommand;
import com.datalogic.hhs.core_sdk.DatalogicDeviceResponse;
import com.datalogic.hhs.core_sdk_demoapp.MainActivity;
import com.datalogic.hhs.core_sdk_demoapp.R;




public class HealthCareFragment extends Fragment {


    public HealthCareFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((MainActivity) getActivity()).mFragment = this;

        final   View rootView = inflater.inflate(R.layout.fragment_healthcare, container, false);

        final Button silentOn = (Button) rootView.findViewById(R.id.buttonSilentOn);
        final Button silentOff = (Button) rootView.findViewById(R.id.buttonSilentOff);

        silentOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mDatalogicDevice != null) {
                    silentOn.setEnabled(true);
                    silentOff.setEnabled(false);
                    MainActivity.mDatalogicDevice.sendCommand(DatalogicCommand.SILENT_OFF, new DatalogicDeviceResponse.Callback() {
                        @Override
                        public void onResponse(DatalogicDeviceResponse datalogicDeviceResponse) {
                            if (!datalogicDeviceResponse.isOnError()) {
                                Toast.makeText(getActivity(), "silent mode off: ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Error: " + datalogicDeviceResponse.getErrorCode(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }



        });



        silentOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mDatalogicDevice != null) {
                    silentOff.setEnabled(true);
                    silentOn.setEnabled(false);
                    MainActivity.mDatalogicDevice.sendCommand(DatalogicCommand.SILENT_ON, new DatalogicDeviceResponse.Callback() {
                        @Override
                        public void onResponse(DatalogicDeviceResponse datalogicDeviceResponse) {
                            if (!datalogicDeviceResponse.isOnError()) {
                                Toast.makeText(getActivity(), "silent mode on : ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Error: " + datalogicDeviceResponse.getErrorCode(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });



        Button beep = (Button) rootView.findViewById(R.id.buttonBeep);
        beep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mDatalogicDevice != null) {
                    MainActivity.mDatalogicDevice.sendCommand(DatalogicCommand.BEEP);
                }
            }
        });


        Button restoreDefault = (Button) rootView.findViewById(R.id.buttonRestoreDefault);
        restoreDefault.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (MainActivity.mDatalogicDevice != null) {
                    MainActivity.mDatalogicDevice.sendCommand(DatalogicCommand.RESTORE_DEFAULT, new DatalogicDeviceResponse.Callback() {
                        @Override
                        public void onResponse(DatalogicDeviceResponse datalogicDeviceResponse) {
                            if (!datalogicDeviceResponse.isOnError()) {
                                Toast.makeText(getActivity(), "restore default: ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Error: " + datalogicDeviceResponse.getErrorCode(), Toast.LENGTH_SHORT).show();
                            }
                            silentOn.setEnabled(true);
                            silentOff.setEnabled(false);
                        }
                    });
                }
            }
        });


        return rootView;
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }

}
