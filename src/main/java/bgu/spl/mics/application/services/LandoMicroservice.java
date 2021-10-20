package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.concurrent.CountDownLatch;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {

    private long duration;
    private CountDownLatch countDownLatch;

    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    /**
     * initialize - subscribes the microservice to the required messages and handles them with callbacks.
     */
    protected void initialize() {
        Diary d = Diary.getInstance();
        subscribeEvent(BombDestroyerEvent.class,(c)->{
            Thread.sleep(duration);
            complete(c,true);
            sendBroadcast(new TerminateBroadcast());

        });

        subscribeBroadcast(TerminateBroadcast.class,(c)->{
            d.setLandoTerminate();
            terminate();
        });
        countDownLatch.countDown();
       }
}
