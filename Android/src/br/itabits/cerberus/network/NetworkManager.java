/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package br.itabits.cerberus.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;
import br.itabits.cerberus.R;

/**
 * a manager that tracks every network change allowing different behaviors for each transition.<br>
 * (only Wi-Fi is tracked in this class)
 * 
 * @author Marcelo
 */
public class NetworkManager {
	public static final String WIFI = "Wi-Fi";

	// Whether there is a Wi-Fi connection.
	private static boolean wifiConnected = false;
	// Whether the display should be refreshed.
	public static boolean refreshDisplay = true;

	// The BroadcastReceiver that tracks network connectivity changes.
	private NetworkReceiver receiver = new NetworkReceiver();
	private NetworkResponse response;

	// All the registers need to be done in a specific context
	private Context context;

	public NetworkManager(Context applicationContext) {
		context = applicationContext;
	}

	public void onCreate(Bundle savedInstanceState) {
		// Register BroadcastReceiver to track connection changes.
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		receiver = new NetworkReceiver();
		context.registerReceiver(receiver, filter);

	}

	// Refreshes the display if the network connection allow it.
	public void onStart() {
		updateConnectedFlags();
	}

	public void onDestroy() {
		if (receiver != null) {
			context.unregisterReceiver(receiver);
		}
	}

	public boolean getWIFIConnected() {
		return wifiConnected;
	}

	// Checks the network connection and sets the wifiConnected
	// variables accordingly.
	private void updateConnectedFlags() {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
		if (activeInfo != null && activeInfo.isConnected()) {
			wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
		} else {
			wifiConnected = false;
		}
	}

	/**
	 * 
	 * This BroadcastReceiver intercepts the android.net.ConnectivityManager.CONNECTIVITY_ACTION, which indicates a
	 * connection change. It checks whether the type is TYPE_WIFI. If it is, it checks whether Wi-Fi is connected and
	 * sets the wifiConnected flag in the main activity accordingly.
	 * 
	 */
	public class NetworkReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

			// Checks the network connection. Based on the result, decides whether
			// to refresh the display or keep the current display.
			if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				// If device has its Wi-Fi connection, sets refreshDisplay
				// to true. This causes the display to be refreshed when the user
				// returns to the app.
				refreshDisplay = true;
				response.onChangesDetected(refreshDisplay);
				Toast.makeText(context, R.string.wifi_connected, Toast.LENGTH_SHORT).show();

				// If the setting is ANY network and there is a network connection
				// (which by process of elimination would be mobile), sets refreshDisplay to true.
			} else {
				refreshDisplay = false;
				response.onChangesDetected(refreshDisplay);
				Toast.makeText(context, R.string.lost_connection, Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void setResponse(NetworkResponse response) {
		this.response = response;
	}
}
