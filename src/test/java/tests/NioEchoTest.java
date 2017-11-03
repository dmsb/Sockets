package tests;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.BeforeClass;

import nio.NioClient;
import nio.NioServerSocket;
import tests.util.ExecutionTimer;

public class NioEchoTest extends EchoTest {
    
    @BeforeClass
    public static void setup() throws IOException, InterruptedException, ExecutionException, TimeoutException {
    	service.submit(() -> new NioServerSocket("localhost", 5454));
        for(int i = 0; i < clientsNumber; i++) {
        	final NioClient newNioClient = new NioClient();
    		newNioClient.startClient("localhost", 5454);
    		clients.add(newNioClient);
    	}
    }
    
    @After
    public void afterTest() {
    	milliSeconds = ExecutionTimer.time(() -> runMultiplesMessagesWithMultiplesClients(iterations));
    	final StringBuilder infoTextBuilder = new StringBuilder();
		infoTextBuilder
			.append("Unique thread with ")
			.append(clientsNumber)
			.append(" clients sending ")
			.append(iterations)
			.append(" messages: ")
			.append(milliSeconds)
			.append(" miliseconds.");
		
    	System.out.println(infoTextBuilder.toString());
    }
}
