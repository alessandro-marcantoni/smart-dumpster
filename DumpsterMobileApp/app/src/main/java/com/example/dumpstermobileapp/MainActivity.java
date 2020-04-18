package com.example.dumpstermobileapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

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
                    }
                }
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
}
