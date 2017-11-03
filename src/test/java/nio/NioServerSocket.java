package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NioServerSocket {

	private static final Logger LOGGER = Logger.getLogger("NioEchoServer");
	
	private ServerSocketChannel serverSocket;
	
	public NioServerSocket(final String hostname, final Integer port) {
		startNioServer(hostname, port);
	}
	
	public void startNioServer(final String hostname, final Integer port) {
		Selector selector;
		try {
			selector = Selector.open();
			serverSocket = buildServerSocketChannel(selector, hostname, port);
			while (true) {
				selector.select();
				processClientRequest(selector, serverSocket);
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Fail to start NIO server", e);
		}
	}

	private void processClientRequest(final Selector selector, final ServerSocketChannel serverSocket) 
			throws IOException, ClosedChannelException {
		
		final Set<SelectionKey> selectedKeys = selector.selectedKeys();
		final Iterator<SelectionKey> selectionKeyIterator = selectedKeys.iterator();
		while (selectionKeyIterator.hasNext()) {
			
			final SelectionKey currentKey = selectionKeyIterator.next();
			selectionKeyIterator.remove();
			
			if(Boolean.FALSE.equals(currentKey.isValid())) {
				continue;
			}
			if (currentKey.isAcceptable()) {
				acceptClient(selector, serverSocket);
			}
			if (currentKey.isReadable()) {
				processClientMessage(currentKey);
			}
		}
	}

	private ServerSocketChannel buildServerSocketChannel(final Selector selector, 
			 final String hostname, final Integer port)
			throws IOException, ClosedChannelException {
		final ServerSocketChannel serverSocket = ServerSocketChannel.open();
		serverSocket.bind(new InetSocketAddress(hostname, port));
		serverSocket.configureBlocking(false);
		serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		return serverSocket;
	}

	private void acceptClient(final Selector selector, final ServerSocketChannel serverSocket)
			throws IOException, ClosedChannelException {
		final SocketChannel client = serverSocket.accept();
		client.configureBlocking(false);
		client.register(selector, SelectionKey.OP_READ);
	}
	
	private void processClientMessage(final SelectionKey readableKey) throws IOException {
		final ByteBuffer buffer = ByteBuffer.allocate(512);
		final SocketChannel client = (SocketChannel) readableKey.channel();
		final int readPosition = client.read(buffer);
		
		if(readPosition == -1) {
			client.socket().close();
			readableKey.cancel();
			return;
		} else {
			buffer.flip();
			client.write(buffer);
			buffer.clear();
		}
	}
	
	public void close() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Fail to close server socket", e);
		}
	}
	
}
