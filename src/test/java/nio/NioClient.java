package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {

	private SocketChannel client;

	public void startClient(final String hostname, final Integer port) throws IOException, InterruptedException {
		final InetSocketAddress hostAddress = new InetSocketAddress(hostname, port);
		client = SocketChannel.open(hostAddress);
	}

	public void writeMessage() {
		final byte[] message = new String("Hello World Hello World Hello World\n").getBytes();
		final ByteBuffer buffer = ByteBuffer.wrap(message);
		try {
			client.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
