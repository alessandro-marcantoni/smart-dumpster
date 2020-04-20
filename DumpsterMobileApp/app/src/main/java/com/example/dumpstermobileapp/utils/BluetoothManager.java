package com.example.dumpstermobileapp.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

import com.example.dumpstermobileapp.MainActivity;
import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class BluetoothManager {
    private static final String LOG = "BLUETOOTH LOG";
    private static final Map<Integer, String> MESSAGE_MAP;

    private MainActivity activity;
    private boolean btIsConnected = false;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private BluetoothDevice device;
    private OutputStream outputStream;

    static {
        MESSAGE_MAP = ImmutableMap.of(
                MainActivity.A_BUTTON, "W_A",
                MainActivity.B_BUTTON, "W_B",
                MainActivity.C_BUTTON, "W_C",
                MainActivity.MORE_TIME_BUTTON, "M_T"
        );
    }

    public BluetoothManager(MainActivity activity) {
        this.activity = activity;
    }

    public void connectToDumpster() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                final Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                this.activity.startActivityForResult(enableBtIntent, C.Bluetooth.REQUEST_ENABLE_BT);
            } else {
                final Map<String, BluetoothDevice> map = new HashMap<>();
                for (BluetoothDevice bt : bluetoothAdapter.getBondedDevices()) {
                    map.put(bt.getName(), bt);
                }
                if (!map.keySet().contains(C.Bluetooth.DEVICE)) {
                    Log.e(this.LOG, "Dumpster not paired");
                    this.activity.showPairingOption();
                } else {
                    this.device = bluetoothAdapter.getRemoteDevice(map.get(C.Bluetooth.DEVICE).toString());
                    if (this.device != null) {
                        try {
                            this.socket = this.device.createRfcommSocketToServiceRecord(C.Bluetooth.uuid);
                            Log.e(this.LOG, "Socket done " + map.get(C.Bluetooth.DEVICE).toString());
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if (this.socket != null) {
                        try {
                            this.socket.connect();
                            Log.e(this.LOG, "Connection done");
                            this.outputStream = this.socket.getOutputStream();
                            this.btIsConnected = true;
                        } catch (IOException e){
                            e.printStackTrace();
                            try {
                                this.socket.close();
                            } catch (IOException cE){
                                cE.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean btIsConnected() {
        return this.btIsConnected;
    }

    public AutoCloseable getSocket() {
        return this.socket;
    }

    public void sendMessage(int message) {
        if (this.outputStream != null) {
            byte[] msgBuffer = this.MESSAGE_MAP.get(message).getBytes();
            try {
                outputStream.write(msgBuffer);
            } catch (IOException e) {
                e.printStackTrace();
                this.activity.showError("Message could not be send");
            }
        }

    }
}
