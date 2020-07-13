package com.example.dumpstermobileapp.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.dumpstermobileapp.MainActivity;
import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the bluetooth connection and transactions.
 */
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

    /**
     * Creates a connection to the Smart Dumpster.
     */
    public void connectToDumpster() {
        ConnectionTask connectionTask = new ConnectionTask();
        connectionTask.execute();
    }

    /**
     * Checks if you are connected to the Smart Dumpster.
     *
     * @return True if you are connected, False otherwise.
     */
    public boolean btIsConnected() {
        return this.btIsConnected;
    }

    /**
     * Returns the socket of the bluetooth connection.
     *
     * @return The socket.
     */
    public AutoCloseable getSocket() {
        return this.socket;
    }

    /**
     * Sends a message to the Smart Dumpster.
     *
     * @param message The constant that represents the command.
     */
    public void sendMessage(int message) {
        if (this.btIsConnected()) {
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

    private void notifyConnected() {
        this.activity.connectionDone();
    }

    private class ConnectionTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter != null) {
                if (!bluetoothAdapter.isEnabled()) {
                    final Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    activity.startActivityForResult(enableBtIntent, C.Bluetooth.REQUEST_ENABLE_BT);
                } else {
                    final Map<String, BluetoothDevice> map = new HashMap<>();
                    for (BluetoothDevice bt : bluetoothAdapter.getBondedDevices()) {
                        map.put(bt.getName(), bt);
                    }
                    if (!map.keySet().contains(C.Bluetooth.DEVICE)) {
                        Log.e(LOG, "Dumpster not paired");
                        activity.showBluetoothPairingOption();
                    } else {
                        device = bluetoothAdapter.getRemoteDevice(map.get(C.Bluetooth.DEVICE).toString());
                        if (device != null) {
                            try {
                                socket = device.createRfcommSocketToServiceRecord(C.Bluetooth.uuid);
                                Log.e(LOG, "Socket done " + map.get(C.Bluetooth.DEVICE).toString());
                            } catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        if (socket != null) {
                            try {
                                socket.connect();
                                Log.e(LOG, "Connection done");
                                outputStream = socket.getOutputStream();
                                btIsConnected = true;
                            } catch (IOException e){
                                e.printStackTrace();
                                try {
                                    socket.close();
                                } catch (IOException cE){
                                    cE.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
            return btIsConnected;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                notifyConnected();
            }
        }
    }
}
