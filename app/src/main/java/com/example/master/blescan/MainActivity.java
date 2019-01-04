package com.example.master.blescan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Button buttonScan;
    private ListView bleDeviceList;
    private BluetoothAdapter mBluetoothAdapter;
    public static int REQUEST_ENABLE_BT = 1;
    private boolean enable = false;
    private BluetoothLeScanner mLEScanner;
    private boolean mScanning;
    private Handler mHandler;
    private DeviceListAdapter mDeviceListAd;
    private ArrayList<DeviceItem> DeviceList;
    private File logFile;
    private FileOutputStream fo;



    public static final SimpleDateFormat SDF_TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd'  'HH:mm:ss.SSS");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler();
        DeviceList = new ArrayList<DeviceItem>();
        mDeviceListAd = new DeviceListAdapter(this,DeviceList);
        bleDeviceList = (ListView) findViewById(R.id.listView);
        bleDeviceList.setAdapter(mDeviceListAd);

        String pathname = Environment.getExternalStorageDirectory().getPath() + "/dobots/fingerprinting";
//

    }

    protected void onStart() {
        super.onStart();

        //Enable bluetooth
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        //set up the ble
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(
                Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

        final int rssi = result.getRssi();
        final BluetoothDevice device = result.getDevice();
        final DeviceItem mdeviceItem = new DeviceItem(device.getName(),device.getAddress(),rssi);
        //mdeviceItem.getAddress()  ;
          runOnUiThread(new Runnable() {
              @Override
              public void run() {
                 // TODO
                  mDeviceListAd.add(mdeviceItem);

                  //Write the log

                  try {

                      String address = "None";
                      String name = "None";
                      int rssiL = 0;
                      address = device.getAddress();
                      name = device.getName();
                      rssiL = rssi;
                      Date timestamp = new Date();
                      String line = String.format("%s  [%s]  RSSI: %d ",SDF_TIMESTAMP.format(timestamp),address,rssiL)+"\n";
                      fo.write(line.getBytes());
                      fo.flush();
                  } catch (IOException e) {
                      e.printStackTrace();

                  }
              }
          });
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    protected void onResume(){
        super.onResume();

    }


    //Scan Duration
    private static final long SCAN_PERIOD = 60000*60;


    public void scanLeDevice(final boolean enable){

        if (enable){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning=false;
                    mLEScanner.stopScan(mScanCallback);

                }
            },SCAN_PERIOD);
            mScanning = true;
            mLEScanner.startScan(mScanCallback);

        } else {
            mScanning = false;
            mLEScanner.stopScan(mScanCallback);

        }
    }

    public void onScanResult(int callbackType, ScanResult result){
        final int rssi = result.getRssi();
        final BluetoothDevice device = result.getDevice();
        final DeviceItem mdeviceItem = new DeviceItem(device.getName(),device.getAddress(),rssi);
        //mdeviceItem.getAddress()  ;
          runOnUiThread(new Runnable() {
              @Override
              public void run() {
                 // TODO
                  mDeviceListAd.add(mdeviceItem);
              }
          });
    }

    public void startScan(View view){
        //Start the scan
        if (!enable){
            enable = true;
        }
        else { enable = false;}

        Calendar c = Calendar.getInstance();
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        String filename = "LogFile_"+c.get(Calendar.DATE)+"_"+c.get(Calendar.MONTH)+"_"+c.get(Calendar.YEAR)+"_"+c.get(Calendar.HOUR_OF_DAY)+"_"+c.get(Calendar.MINUTE)+"_"+c.get(Calendar.SECOND);
//        File path = new File(pathname);
        path.mkdirs();
        logFile = new File(path, filename);
        try {
            fo = new FileOutputStream(logFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        scanLeDevice(enable);
    }

    public void quitApp(View view){
        Process.killProcess(Process.myPid());
        System.exit(1);
    }


}
