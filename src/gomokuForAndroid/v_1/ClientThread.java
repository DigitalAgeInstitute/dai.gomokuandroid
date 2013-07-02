package gomokuForAndroid.v_1;

//make necessary imports

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/*
 *  this class hold everything to do with connecting to the server,reading,writing to the server
 */
public class ClientThread extends Thread {

	// below set ip_address and port for socket connection
	static String SERVER_IP = "10.0.2.2";
	static  int SERVER_PORT = 4010;

	// initialise variables
	Handler handler;
	static String responseFromServer;
	static Socket socket;
	Context context;

	// create object for reading and writing streams for socket
	PrintWriter out;
	BufferedReader br;

	// constructor initialises the handler and context
	public ClientThread(Handler handler, Context context) {
		this.handler = handler;
		this.context = context;
	}

	// ---create a thread to create the connection,read and write in sockets---
	public void run() {
		Looper.prepare();
		try {
			
			// ---get the inetAddress---
			Log.e("At ClientThread: ", "getByName: " + SERVER_IP);
			InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
			Log.e("At ClientThread: ", "success:=> getting InetAddress");

			// ---in the socket constructor pass the ip_address and the port for the server---
			Log.e("At ClientThread: ",
					"onging:=> client conntecting to server:...............");
			socket = new Socket(serverAddr, SERVER_PORT);
			Log.e("At ClientThread: ",
					"success:=> connection to server established: "
							+ socket.getRemoteSocketAddress().toString());

			try {
				// ---get an OutputStream object to send to the server---
				Log.e("At ClientThread: ",
						"ongoing:=> initialinzing the printwirter:out");
				out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				Log.e("At ClientThread: ",
						"success:=> out has been initialized");

				Log.e("At ClientThread: ",
						"ongoing:=> initialinzing the BufferedReader:br");
				// ---get an InputStream object to read from the server---
				br = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				Log.e("At ClientThread: ", "success:=> br has been initialized");

				try {
					// ---read all incoming data terminated with a \n
					// char---

					Log.e("At ClientThread: ",
							"ongoing:=> starting to read from the server..................");
					String line = "";
					while ((line = br.readLine()) != null) {
						Log.e("At ClientThread: ",
								"ongoing:=> reading incoming data from the server: "
										+ line);
						
						// ---test if the string has the required data i.e data between [STARTJSON] and [ENDJSON]
						if (line.equals("[STARTJSON]")) {
							responseFromServer = "";
							while ((line = br.readLine()) != null) {
								if (line.equals("[ENDJSON]")) {
									Log.e("At ClientThread: ",
											"ongoing:=> reading incoming data from the server: "
													+ line);
									break;
								}
								
								// ---set string to ---
								responseFromServer += line;
								
								// ---create a bundle to hold the message from the server the send the message through at the handler
								Bundle  bundle = new Bundle();
								Log.e("AT Response handler", "getting"+responseFromServer.toString());
								bundle.putString("responseFromServer", responseFromServer.toString());
								Message message = new Message();
								message.setData(bundle);
								handler.sendMessage(message);
								Log.e("At Client: ",
										"success:=> json to be processed: "
												+ responseFromServer);
							}
							
						}				
						

						// ---disconnected from the server---

					}
					
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("At ClientThread: ",
							"error:=> while reading data from server: "
									+ responseFromServer);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Log.e("At ClientThread: ",
						"error:=> while initializing outputstream and inputstream");

			}
			Looper.loop();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("At ClientThread: ", "error:=> while connecting to server:"
					+ socket.getRemoteSocketAddress().toString());
		}
	}

	// ---sends message passed to the server---
	public void sendMessage(String message) {
		Log.e("At ClientThread: ", "ongoing:=> sending message called");
		Log.e("At ClientThread: ",
				"ongoing:=> checking for error in connections...");
		if (out != null && !out.checkError()) {
			Log.e("At ClientThread: ",
					"ongoing:=> processing json server client protocol");
			String msg = "\n[STARTJSON]\n" + message + "\n[ENDJSON]\n";
			Log.e("json", msg);
			Log.e("At ClientThread: ",
					"success:=> processing protocol for server");

			Log.e("At ClientThread: ", "ongoing:=> writing msg into out");
			out.write(msg);
			Log.e("At ClientThread: ", "success:=> writing msg into out");

			Log.e("At ClientThread: ",
					"ongoing:=> trying to flush the msg in out");
			out.flush();
			Log.e("At ClientThread: ", "success:=> flushing");
		}
	}

	// ---stop/shutdown all the connections to the socket
	protected void onStop() {
		try {
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("At ClientThread: ", "shuting down failed");
		}
	}

}