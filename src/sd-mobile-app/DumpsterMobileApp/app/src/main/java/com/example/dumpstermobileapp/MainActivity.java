/**
 * This is the "Dumpster Mobile App" that connects to
 * the Smart Dumpster and makes it possible to send commands to it.
 *
 * @author alessandr.marcanton2@studio.unibo.it
 */

package com.example.dumpstermobileapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dumpstermobileapp.utils.BluetoothManager;
import com.example.dumpstermobileapp.utils.C;
import com.example.dumpstermobileapp.utils.HttpManager;
import com.google.android.material.snackbar.Snackbar;

import unibo.btlib.exceptions.BluetoothDeviceNotFound;

public class MainActivity extends AppCompatActivity {
    public static final int A_BUTTON = 1;
    public static final int B_BUTTON = 2;
    public static final int C_BUTTON = 3;
    public static final int MORE_TIME_BUTTON = 4;

    private TextView statusTextView;
    private Button connectButton;
    private Button aButton;
    private Button bButton;
    private Button cButton;
    private Button timeButton;

    private BluetoothManager bluetoothManager;
    private HttpManager httpManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initViews();
        this.bluetoothManager = new BluetoothManager(this);
        this.httpManager = new HttpManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.httpManager.registerNetworkCallback();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.httpManager.unregisterNetworkCallback();
    }

    public void connectionDone() {
        if (bluetoothManager.btIsConnected()) {
            statusTextView.setText(R.string.dumpster_connected);
            readyToDump();
        }
    }

    public void onResponseAvailability() {
        if (!isDumpsterAvailable()) {
            statusTextView.setText(R.string.dumpster_not_available);
        } else {
            try {
                bluetoothManager.connectToDumpster();
            } catch (BluetoothDeviceNotFound bluetoothDeviceNotFound) {
                bluetoothDeviceNotFound.printStackTrace();
            }
        }
    }

    public void dumpSuccessful() {
        this.httpManager.throwSuccess();
    }

    /**
     * Init the UI and the listeners.
     */
    private void initViews() {
        this.statusTextView = findViewById(R.id.statusTextView);
        this.connectButton = findViewById(R.id.connectButton);
        this.aButton = findViewById(R.id.aButton);
        this.bButton = findViewById(R.id.bButton);
        this.cButton = findViewById(R.id.cButton);
        this.timeButton = findViewById(R.id.timeButton);

        this.connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //httpManager.checkConnection();
                if (httpManager.isNetworkConnected()) {
                    httpManager.checkAvailable();
                }
            }
        });

        this.aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothManager.sendMessage(A_BUTTON);
                dumping();
            }
        });

        this.bButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothManager.sendMessage(B_BUTTON);
                dumping();
            }
        });

        this.cButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothManager.sendMessage(C_BUTTON);
                dumping();
            }
        });

        this.timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothManager.sendMessage(MORE_TIME_BUTTON);
            }
        });

        this.notConnected();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == C.Bluetooth.REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            Log.e("LOG", "Bluetooth enabled");
        }
        if (requestCode == C.Bluetooth.REQUEST_ENABLE_BT && resultCode == RESULT_CANCELED) {
            statusTextView.setText(R.string.dumpster_error);
            Log.e("LOG", "Bluetooth not enabled");
        }
    }

    public void showBluetoothPairingOption() {
        Snackbar.make(findViewById(R.id.activity_main), "The dumpster has not been paired yet", Snackbar.LENGTH_INDEFINITE)
                .setAction("SETTINGS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                }).show();
    }

    public void showHttpPairingOption() {
        Snackbar.make(findViewById(R.id.activity_main), "You are not connected to the internet", Snackbar.LENGTH_INDEFINITE)
                .setAction("SETTINGS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_WIRELESS_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                }).show();
    }

    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void notConnected() {
        this.aButton.setClickable(false);
        this.bButton.setClickable(false);
        this.cButton.setClickable(false);
        this.timeButton.setClickable(false);
    }

    public void readyToDump() {
        this.aButton.setClickable(true);
        this.bButton.setClickable(true);
        this.cButton.setClickable(true);
        this.timeButton.setClickable(false);
    }

    private void dumping() {
        this.aButton.setClickable(false);
        this.bButton.setClickable(false);
        this.cButton.setClickable(false);
        this.timeButton.setClickable(true);
    }

    private boolean isDumpsterAvailable() {
        return this.httpManager.isDumpsterAvailable();
    }
}
