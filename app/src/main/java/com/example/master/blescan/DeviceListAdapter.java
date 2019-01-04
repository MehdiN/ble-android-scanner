package com.example.master.blescan;

import android.bluetooth.BluetoothAdapter;
import android.app.Activity;
import java.util.ArrayList;
import java.util.List;
import android.R.layout;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;

/**
 * Created by master on 13/06/16.
 */
public class DeviceListAdapter extends ArrayAdapter<DeviceItem> {


    private Context context;
    private BluetoothAdapter mAdapter;
 //   private DeviceItem deviceItem;
//    private ArrayAdapter DeviceList;

    public DeviceListAdapter(Context context, List items) {
        super(context,android.R.layout.simple_list_item_1, items); //Error Unknown
        this.context = context;

    }


    private class ViewHolder{
        TextView titleText;
        protected TextView device_address;
        protected  TextView device_name;
        protected  TextView device_rssi;
    }

    public View getView(int position,View convertView, ViewGroup parent){

        if(convertView == null) {
            DeviceItem item = (DeviceItem) getItem(position);
            final String name = item.getDeviceName();

            // LayoutInflater class is used to instantiate layout XML file into its corresponding View objects.
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.listitem_device, null);
            final ViewHolder viewHolder = new ViewHolder();

            viewHolder.device_name = (TextView) convertView.findViewById(R.id.device_name);
            viewHolder.device_address = (TextView) convertView.findViewById(R.id.device_address);
            viewHolder.device_rssi = (TextView) convertView.findViewById(R.id.device_rssi);
            convertView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if(!isEmpty()){
            DeviceItem device = getItem(position);
            viewHolder.device_name.setText(device.getDeviceName());
            viewHolder.device_rssi.setText(String.valueOf(device.getRssi()));
            viewHolder.device_address.setText("[" + device.getAddress() + "]");
        }


        return convertView;

    }



}


