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
import com.google.android.material.snackbar.Snackbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initViews();
        this.bluetoothManager = new BluetoothManager(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        final AutoCloseable socket = this.bluetoothManager.getSocket();
        if (socket != null) {
            try {
                this.bluetoothManager.getSocket().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

        this.setButtonsClickable(false);

        this.connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDumpsterAvailable()) {
                    statusTextView.setText(R.string.dumpster_not_available);
                } else {
                    bluetoothManager.connectToDumpster();
                    if (bluetoothManager.btIsConnected()) {
                        statusTextView.setText(R.string.dumpster_connected);
                        setButtonsClickable(true);
                    }
                }
            }
        });

        this.aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothManager.sendMessage(A_BUTTON);
            }
        });

        this.bButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothManager.sendMessage(B_BUTTON);
            }
        });

        this.cButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothManager.sendMessage(C_BUTTON);
            }
        });

        this.timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothManager.sendMessage(MORE_TIME_BUTTON);
            }
        });
    }

    private void setButtonsClickable(Boolean clickable) {
        this.aButton.setClickable(clickable);
        this.bButton.setClickable(clickable);
        this.cButton.setClickable(clickable);
        this.timeButton.setClickable(clickable);
    }

    private boolean isDumpsterAvailable() {
        return true;
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

    public void showPairingOption() {
        Snackbar.make(findViewById(R.id.activity_main), "The dumpster has not ben paired yet", Snackbar.LENGTH_INDEFINITE)
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

    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
