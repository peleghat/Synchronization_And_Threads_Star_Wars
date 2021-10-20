package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import java.util.concurrent.CountDownLatch;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    private long duration;
    private CountDownLatch countDownLatch;

    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration=duration;
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
        subscribeEvent(DeactivationEvent.class,(c)->{
            Thread.sleep(duration);
            complete(c,true); // Updating the Future Object to Resolved
            d.setR2D2Deactivate();
        });


        subscribeBroadcast(TerminateBroadcast.class,(c)->{
            d.setR2D2Terminate();
            terminate();
        });
    countDownLatch.countDown();
    }

}
