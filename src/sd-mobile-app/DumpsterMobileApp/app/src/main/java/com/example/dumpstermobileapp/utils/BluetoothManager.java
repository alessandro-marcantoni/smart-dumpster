package com.example.dumpstermobileapp.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.example.dumpstermobileapp.MainActivity;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.UUID;

import unibo.btlib.BluetoothChannel;
import unibo.btlib.BluetoothUtils;
import unibo.btlib.ConnectToBluetoothServerTask;
import unibo.btlib.ConnectionTask;
import unibo.btlib.RealBluetoothChannel;
import unibo.btlib.exceptions.BluetoothDeviceNotFound;

/**
 * Manages the bluetooth connection and transactions.
 */
public class BluetoothManager {
    private static final String LOG = "BLUETOOTH LOG";
    private static final Map<Integer, String> MESSAGE_MAP;

    private MainActivity activity;
    private boolean btIsConnected = false;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothChannel bluetoothChannel;

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
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            this.activity.showBluetoothPairingOption();
        }
    }

    /**
     * Creates a connection to the Smart Dumpster.
     */
    public void connectToDumpster() throws BluetoothDeviceNotFound {
        final BluetoothDevice bluetoothDevice = BluetoothUtils.getPairedDeviceByName(C.Bluetooth.DEVICE);
        final UUID uuid = BluetoothUtils.getEmbeddedDeviceDefaultUuid();
        new ConnectToBluetoothServerTask(bluetoothDevice, uuid, new ConnectionTask.EventListener() {
            @Override
            public void onConnectionActive(BluetoothChannel channel) {
                btIsConnected = true;
                notifyConnected();
                bluetoothChannel = channel;
                bluetoothChannel.registerListener(new RealBluetoothChannel.Listener() {

                    @Override
                    public void onMessageReceived(String receivedMessage) {
                        if (receivedMessage.trim().equals(C.DONE_DEPOSIT)) {
                            activity.readyToDump();
                            activity.dumpSuccessful();
                        }
                    }

                    @Override
                    public void onMessageSent(String sentMessage) {}
                });
            }

            @Override
            public void onConnectionCanceled() {
                btIsConnected = false;
                bluetoothChannel = null;
            }
        }).execute();
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
     * Sends a message to the Smart Dumpster.
     *
     * @param message The constant that represents the command to send.
     */
    public void sendMessage(int message) {
        if (this.btIsConnected()) {
            String msg = this.MESSAGE_MAP.get(message);
            if (this.bluetoothChannel != null) {
                this.bluetoothChannel.sendMessage(msg);
            }
        }
    }

    private void notifyConnected() {
        this.activity.connectionDone();
    }


}
