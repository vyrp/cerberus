package br.itabits.cerberus.database;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * This class encapsulates the access to the database. The access is made through HTTP requests (GET and POST).
 * 
 * @author Croata
 */
public class DataBaseManager {

	/* * Fields * */

	private String server;
	private String deviceName;

	/* * Constructors * */

	/**
	 * Instantiates a DataBaseManager with a server. The device name should be passed with
	 * {@link DataBaseManager#createDevice()}.
	 * 
	 * @param server
	 *            The url of the server
	 */
	public DataBaseManager(String server) {
		this.server = checkEnding(server);
		this.deviceName = null;
	}

	/**
	 * Instantiates a DataBaseManager with a server and a device.
	 * 
	 * @param server
	 *            The url of the server
	 * @param deviceName
	 *            The name of the device
	 * @throws UnsupportedEncodingException
	 */
	public DataBaseManager(String server, String deviceName) throws UnsupportedEncodingException {
		this.server = checkEnding(server);
		this.deviceName = URLEncoder.encode(deviceName, "UTF-8");
	}

	/* * Methods * */

	/**
	 * Gets all the transactions of the device currently associated with this manager.
	 * 
	 * @return The transactions, as a serialized string.
	 * @throws IOException
	 */
	public String getAll() throws IOException {
		if (deviceName == null)
			throw new DeviceNotSetException("No device has been set for this manager.");

		HttpURLConnection conn = null;
		BufferedReader reader = null;
		try {
			conn = (HttpURLConnection) new URL(server + "get?device=" + deviceName).openConnection();
			conn.setRequestMethod("GET");
			conn.setDoInput(true);

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new IOException("Connection not accepted.");
			}

			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			return reader.readLine();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	/**
	 * Makes a request to create a device.
	 * 
	 * @param deviceName
	 *            The name of the device
	 * @throws IOException
	 */
	public void createDevice(String deviceName) throws IOException {
		this.deviceName = URLEncoder.encode(deviceName, "UTF-8");
		post("device=" + this.deviceName);
	}

	/**
	 * Adds a transaction to the database.
	 * 
	 * @param name
	 *            The name of the borrower
	 * @throws IOException
	 */
	public void put(String name) throws IOException {
		if (deviceName == null)
			throw new DeviceNotSetException("No device has been set for this manager.");

		post("device=" + deviceName + "&borrower=" + URLEncoder.encode(name, "UTF-8"));
	}

	/**
	 * Updates ( = returning ) the last transaction.
	 * 
	 * @return The name of the last borrower
	 * @throws IOException
	 */
	public String update() throws IOException {
		if (deviceName == null)
			throw new DeviceNotSetException("No device has been set for this manager.");

		return post("device=" + deviceName + "&update=true");
	}

	// Private Methods
	private String post(String message) throws IOException {
		HttpURLConnection conn = null;
		DataOutputStream writer = null;
		BufferedReader reader = null;
		try {
			conn = (HttpURLConnection) new URL(server + "post").openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			writer = new DataOutputStream(conn.getOutputStream());
			writer.writeBytes(message);
			writer.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new IOException("Connection not accepted.");
			}

			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			return reader.readLine();
		} finally {
			if (writer != null) {
				writer.close();
			}
			if (reader != null) {
				reader.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	private String checkEnding(String serverName) {
		if (serverName.endsWith("/"))
			return serverName;
		return serverName + "/";
	}
}
