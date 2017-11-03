package tests;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.BeforeClass;

import io.IoServerSocket;
import nio.NioClient;
import tests.util.ExecutionTimer;

public class IoEchoTest extends EchoTest {

	@BeforeClass
	public static void setup() throws IOException, InterruptedException, ExecutionException, TimeoutException {
		service.submit(() -> new IoServerSocket(8189));
		for (int i = 0; i < clientsNumber; i++) {
			final NioClient client = new NioClient();
			client.startClient("localhost", 8189);
			clients.add(client);
		}
	}
	
	@After
    public void afterTest() {
    	milliSeconds = ExecutionTimer.time(() -> runMultiplesMessagesWithMultiplesClients(iterations));
    	final StringBuilder infoTextBuilder = new StringBuilder();
		infoTextBuilder
			.append("Mutiples threads with ")
			.append(clientsNumber)
			.append(" clients sending ")
			.append(iterations)
			.append(" messages: ")
			.append(milliSeconds)
			.append(" miliseconds.");
		
    	System.out.println(infoTextBuilder.toString());
    }
}
