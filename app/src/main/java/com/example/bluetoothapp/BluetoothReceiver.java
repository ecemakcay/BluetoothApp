package com.example.bluetoothapp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

public class BluetoothReceiver extends BroadcastReceiver {
    private FirebaseAnalytics mFirebaseAnalytics;
    private Context mContext;

    public BluetoothReceiver(Context context) {
        mContext = context;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.d("BluetoothReceiver", "Device connected: " + device.getName() + " (" + device.getAddress() + ")");
            sendDeviceInfoToFirebase(device.getName(), device.getAddress());
            displayDeviceInfo(device.getName(), device.getAddress());
        } else if (action.equals(BluetoothDevice.ACTION_PAIRING_REQUEST)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.d("BluetoothReceiver", "Pairing request received from: " + device.getName() + " (" + device.getAddress() + ")");
        }
    }

    private void sendDeviceInfoToFirebase(String deviceName, String deviceAddress) {
        Bundle bundle = new Bundle();
        bundle.putString("device_name", deviceName);
        bundle.putString("mac", deviceAddress);
        mFirebaseAnalytics.logEvent("bluetooth_connect", bundle);
    }

    private void displayDeviceInfo(String deviceName, String deviceAddress) {
        String message = "Bluetooth device connected: " + deviceName + " (" + deviceAddress + ")";
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}

