package io;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class IoClient extends Thread {

	private static final Logger LOGGER = Logger.getLogger("EchoClient");
	private Socket clientSocket;

	public IoClient(final Socket socket) throws UnknownHostException, IOException {
		clientSocket = socket;
	}

	@Override
	public void run() {
		try {
			final OutputStream outputStream = clientSocket.getOutputStream();
			final InputStream inputStream = clientSocket.getInputStream();
			final DataOutputStream out = new DataOutputStream(outputStream);			
			final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			while (true) {
				String line;
				if ((line  = in.readLine()) == null || line.equalsIgnoreCase("QUIT")) {
					clientSocket.close();
                    break;
                } else {
                    out.writeBytes(line);
                    out.flush();
                }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() throws IOException {
		clientSocket.close();
	}

	public void echoMessage(final String message) {
		
		try {
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			out.println(message);
			out.write(message);
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
		}
	}
}
