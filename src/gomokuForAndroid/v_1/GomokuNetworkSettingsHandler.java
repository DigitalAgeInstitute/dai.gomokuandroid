package gomokuForAndroid.v_1;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;

public class GomokuNetworkSettingsHandler {

	//create instance of the variables
	static Context context;

	public GomokuNetworkSettingsHandler(Context context) {
		// below the constructor set the content to the content passed in the
		// parameters
		this.context = context;
	}

	// method to set the ip_address and port number
	public void promptForSettingsInput() {
		// below call for the network setting prompt
		new NetworkSettings().networkGameSettingsDialog(context);
	}

	// this method now sets the ip_address and port in the clientThread
	public void setIp_Port(String ip, int port) {

		// below the ip_address and port numbers are set
		ClientThread.SERVER_IP = ip;
		ClientThread.SERVER_PORT = port;

	}

	// below method tests if the ip_address address is valid thus the client
	// can try to establish a connection
	public static boolean isValidInetAddress(final String address) {
		
		//checks if the ip_address input is a valid ip_address
		if (InetAddressUtils.isIPv4Address(address) == false
				&& InetAddressUtils.isIPv6Address(address) == false) {
			return false;
		} else {
			return true;
		}

	}

}
