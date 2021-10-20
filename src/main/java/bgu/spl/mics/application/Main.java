package bgu.spl.mics.application;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) throws IOException, InterruptedException {

		// ** Creating the input from json ** //
		String path = args[0]  ; // "/home/spl211/IdeaProjects/Assignment2_Spl/input.json"
		Input newInput = JsonInputReader.getInputFromJson(path);
		Attack[] attacks = newInput.getAttacks();
		MicroService.setAttackSize(attacks.length);
		long R2D2 = newInput.getR2D2();
		long Lando = newInput.getLando();
		int ewoks = newInput.getEwoks();
		Ewoks ewoks1 = Ewoks.getInstance(); // Singleton
		for(int i=1; i <= ewoks ; i++)
			ewoks1.insertEwok(new Ewok(i));

		// ** The Battle - Initialize CountDownLatch ** //
		CountDownLatch latch = new CountDownLatch(4);
		// ** Initialize MicroServices ** //
		LeiaMicroservice leiaMicroservice = new LeiaMicroservice(attacks);
		R2D2Microservice r2D2Microservice = new R2D2Microservice(R2D2);
		LandoMicroservice landoMicroservice = new LandoMicroservice(Lando);
		HanSoloMicroservice hanSoloMicroservice = new HanSoloMicroservice();
		C3POMicroservice c3POMicroservice = new C3POMicroservice();

		r2D2Microservice.setCountDownLatch(latch);
		landoMicroservice.setCountDownLatch(latch);
		hanSoloMicroservice.setCountDownLatch(latch);
		c3POMicroservice.setCountDownLatch(latch);
		// ** Initialize Threads ** //
		Thread leiaThread = new Thread(leiaMicroservice);
		Thread r2d2Thread = new Thread(r2D2Microservice);
		Thread landoThread = new Thread(landoMicroservice);
		Thread hanSoloThread = new Thread(hanSoloMicroservice);
		Thread c3poThread = new Thread(c3POMicroservice);
		// ** Let the 4 threads start and only then let leia start (for registration and subscription purposes) ** //
		r2d2Thread.start();
		landoThread.start();
		hanSoloThread.start();
		c3poThread.start();
		latch.await();
		leiaThread.start();
		// ** Let all threads finish their Execution ** //
		landoThread.join();
		r2d2Thread.join();
		hanSoloThread.join();
		c3poThread.join();
		leiaThread.join();

		// ** Creating the output as a json file ** //
		Diary diary = Diary.getInstance();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileWriter writer = new FileWriter(args[1]); // "./output.json"
		gson.toJson(diary,writer);
		writer.flush();
		writer.close();

		// ** Resetting all of Our resources - for test purposes only! ** //
		diary.resetDiary();
		ewoks1.resetEwoks();
		MessageBusImpl.getInstance().resetMsgBus();
		leiaMicroservice = null;
		r2D2Microservice = null;
		landoMicroservice = null;
		hanSoloMicroservice = null;
		c3POMicroservice = null;
	}
}
