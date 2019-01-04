package com.example.master.blescan;

/**
 * Created by master on 13/06/16.
 */
public class DeviceItem {


    private String deviceName;
    private String address;
    private int rssi;


    public String getDeviceName(){
        return deviceName;
    }

    public String getAddress(){
    return address;
    }

    public int getRssi(){
        return rssi;
    }

    public void setDeviceName(String deviceName){
        this.deviceName = deviceName;
    }

    public DeviceItem(String name,String address,int signal){
        this.deviceName = name;
        this.address = address;
        this.rssi = signal;
    }

}
