package io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class IoServerSocket {

	private static final Logger LOGGER = Logger.getLogger("IoServerSocket");
	
	public IoServerSocket( final Integer port) {
		startServerSocket(port);
	}
	
	public void startServerSocket(final Integer port) {
		
		try (final ServerSocket serverSocket = new ServerSocket(port);) {
			while (true) {
				final Socket clientSocket = serverSocket.accept();
				IoClient cliThread = new IoClient(clientSocket);
				cliThread.start();
			}
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
		}
	}
}
