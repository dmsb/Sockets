package tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import nio.NioClient;

@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EchoTest {

	protected static ExecutorService service;
	protected static List<NioClient> clients;
	protected static int clientsNumber;
	protected static int iterations;	
	protected static Long milliSeconds;

	@BeforeClass
	public static void configure() throws IOException, InterruptedException, ExecutionException, TimeoutException {
		clients = new ArrayList<>();
		clientsNumber = 200;
		service = Executors.newWorkStealingPool();
	}
	
	@AfterClass
	public static void teardown() throws IOException, InterruptedException {
		service.awaitTermination(500, TimeUnit.MILLISECONDS);
	}

    @Test
    public void test01SmallChargeTest() {
    	iterations = 100;
    }
    
    @Test
    public void test02AverageChargeTest() {
    	iterations = 1000;
    }
    
    @Test
    public void test03HeavyChargeTest() {
    	iterations = 2500;
    	
    }
    
    public void runMultiplesMessagesWithMultiplesClients(long ammountOfExecutions) {
    	clients.parallelStream().forEach(p -> {
    		for(int i = 0; i < ammountOfExecutions; i++) {
    			p.writeMessage();
    			i++;
    		}
    	});
    }
}
