package com.datalogic.hhs.core_sdk_demoapp.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.datalogic.hhs.core_sdk.BarcodeBSdk;
import com.datalogic.hhs.core_sdk.DatalogicCommand;
import com.datalogic.hhs.core_sdk.DatalogicDeviceResponse;
import com.datalogic.hhs.core_sdk_demoapp.MainActivity;
import com.datalogic.hhs.core_sdk_demoapp.R;
import com.google.zxing.BarcodeFormat;


public class ConfigurationFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private ImageView myImage ;
    private DatalogicCommand mDatalogicCommand;
    private BarcodeBSdk mBarcodeSdk;
    private BarcodeFormat mBarcodeFormat;


    public ConfigurationFragment() {
    }


    public static ConfigurationFragment newInstance(String param1, String param2) {
        ConfigurationFragment fragment = new ConfigurationFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity()).mFragment = this;

        final View  rootView = inflater.inflate(R.layout.fragment_configuration, container, false);

        Button sendButton = (Button) rootView.findViewById(R.id.button_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(rootView.getContext().INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                TextView t = (TextView) rootView.findViewById(R.id.text_to_send);
                String message = t.getText().toString();
                MainActivity.mDatalogicDevice.send(message);
                t.setText("");
            }
        });


        Button generateBarcodeButton = (Button) rootView.findViewById(R.id.generateBarcode);
        generateBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(rootView.getContext().INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                TextView t = (TextView) rootView.findViewById(R.id.text_to_send);
                String message = t.getText().toString();
                generateBarcode(message);
                t.setText("");
            }
        });


        Button generateBarcodeButtonFNC3 = (Button) rootView.findViewById(R.id.generateBarcodeFNC3);
        generateBarcodeButtonFNC3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(rootView.getContext().INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                TextView t = (TextView) rootView.findViewById(R.id.text_to_send);
                String message = t.getText().toString() + BarcodeBSdk.FNC3;
                generateBarcode(message);
                t.setText("");
            }
        });


        myImage = (ImageView) rootView.findViewById(R.id.imageBarcode);

        Spinner commandSpinner = (Spinner) rootView.findViewById(R.id.spinnerCommand);
        ArrayAdapter<CharSequence> cmdAdapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.commands_array,
                                                                                android.R.layout.simple_spinner_item);
        cmdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        commandSpinner.setAdapter(cmdAdapter);
        commandSpinner.setOnItemSelectedListener(this);

        Spinner barcodeSpinner = (Spinner) rootView.findViewById(R.id.spinnerBarcode);
        ArrayAdapter<CharSequence> barcodeAdapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.barcodes_array,
                                                                                    android.R.layout.simple_spinner_item);
        barcodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        barcodeSpinner.setAdapter(barcodeAdapter);
        barcodeSpinner.setOnItemSelectedListener(this);


        Spinner barcodeKindSpinner = (Spinner) rootView.findViewById(R.id.genBarcodeSpinner);
        ArrayAdapter<CharSequence> barcodeKindAdapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.barcodes_kind,
                                                                                        android.R.layout.simple_spinner_item);
        barcodeKindAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        barcodeKindSpinner.setAdapter(barcodeKindAdapter);
        barcodeKindSpinner.setOnItemSelectedListener(this);


        Button sendCommandBtn = (Button) rootView.findViewById(R.id.sndCommandBtn);
        sendCommandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mDatalogicDevice != null) {
                    MainActivity.mDatalogicDevice.sendCommand(mDatalogicCommand, new DatalogicDeviceResponse.Callback() {
                        @Override
                        public void onResponse(DatalogicDeviceResponse datalogicDeviceResponse) {
                            if (!datalogicDeviceResponse.isOnError()) {
                                String resp = datalogicDeviceResponse.getValue().toString();
                                if (resp.startsWith("$") || resp.startsWith("~")) {
                                    Toast.makeText(getActivity(), mDatalogicCommand.getLabel(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), resp, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Error: " + datalogicDeviceResponse.getErrorCode(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "No Device connected.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        Button showBarcodeBtn = (Button) rootView.findViewById(R.id.showBarcodeButton);
        showBarcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarcodeBSdk.showImage(rootView.getContext(), mBarcodeSdk, null, new BarcodeBSdk.Listener() {
                    @Override
                    public void onOk() { }

                    @Override
                    public void onCancel() { }
                });
            }
        });

        return rootView;
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner parentSpinner = (Spinner) parent;
        if (parentSpinner.getId() == R.id.spinnerCommand) {
            mDatalogicCommand = DatalogicCommand.getCommandAt(position);
        } else {
            if (parentSpinner.getId() == R.id.spinnerBarcode) {
                mBarcodeSdk = BarcodeBSdk.getBarcodeAt(position);
            } else {
                if (parentSpinner.getId() == R.id.genBarcodeSpinner) {
                    switch (position) {
                        case 0:
                            mBarcodeFormat = BarcodeFormat.CODE_39;
                            break;
                        case 1:
                            mBarcodeFormat = BarcodeFormat.CODE_128;
                            break;
                        case 2:
                            mBarcodeFormat = BarcodeFormat.PDF_417;
                            break;
                        case 3:
                            mBarcodeFormat = BarcodeFormat.QR_CODE;
                            break;
                    }
                }
            }
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void generateBarcode(String qrInputText) {

        if (qrInputText == "") {
            return;
        }

        WindowManager manager = (WindowManager) getActivity().getSystemService((Context.WINDOW_SERVICE));
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        Bitmap bitmap = BarcodeBSdk.generateBarcode(qrInputText, mBarcodeFormat, width);
        myImage.setImageBitmap(bitmap);
    }


}
