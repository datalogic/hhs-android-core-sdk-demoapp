package com.datalogic.hhs.core_sdk_demoapp;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.datalogic.hhs.core_sdk.DatalogicDevice;

import java.util.List;

public class DatalogicDeviceListAdapter extends ArrayAdapter<DatalogicDevice> {

    List<DatalogicDevice> list;

    public DatalogicDeviceListAdapter(Context context, int layoutResource, List<DatalogicDevice> list) {
        super(context, layoutResource, list);
        this.list = list;
    }


    @Override
    public void add(DatalogicDevice device) {
        if (list.contains(device)) {
            list.remove(device);
        }
        list.add(device);
        notifyDataSetChanged();
    }
}
