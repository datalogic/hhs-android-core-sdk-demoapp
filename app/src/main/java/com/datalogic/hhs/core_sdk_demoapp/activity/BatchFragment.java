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




public class BatchFragment extends Fragment {


    public BatchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity()).mFragment = this;
        final View rootView = inflater.inflate(R.layout.fragment_batch, container, false);


        Button batchOn = (Button) rootView.findViewById(R.id.buttonEnableBatch);
        batchOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mDatalogicDevice != null) {
                    MainActivity.mDatalogicDevice.sendCommand(DatalogicCommand.MANUAL_BATCH_ENABLE, new DatalogicDeviceResponse.Callback() {
                        @Override
                        public void onResponse(DatalogicDeviceResponse datalogicDeviceResponse) {
                            if (!datalogicDeviceResponse.isOnError()) {
                                Toast.makeText(getActivity(), "Batch enabled ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Error: " + datalogicDeviceResponse.getErrorCode(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        Button batchOff = (Button) rootView.findViewById(R.id.buttonDisableBatch);
        batchOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mDatalogicDevice != null) {
                    MainActivity.mDatalogicDevice.sendCommand(DatalogicCommand.MANUAL_BATCH_DISABLE, new DatalogicDeviceResponse.Callback() {
                        @Override
                        public void onResponse(DatalogicDeviceResponse datalogicDeviceResponse) {
                            if (!datalogicDeviceResponse.isOnError()) {
                                Toast.makeText(getActivity(), "Batch disabled ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Error: " + datalogicDeviceResponse.getErrorCode(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        Button dump = (Button) rootView.findViewById(R.id.buttonDumpBatch);
        dump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mDatalogicDevice != null) {
                    MainActivity.mDatalogicDevice.sendCommand(DatalogicCommand.BATCH_DUMPING, new DatalogicDeviceResponse.Callback() {
                        @Override
                        public void onResponse(DatalogicDeviceResponse datalogicDeviceResponse) {
                            if (!datalogicDeviceResponse.isOnError()) {
                                Toast.makeText(getActivity(), "dump batch: ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Error: " + datalogicDeviceResponse.getErrorCode(), Toast.LENGTH_SHORT).show();
                            }
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
