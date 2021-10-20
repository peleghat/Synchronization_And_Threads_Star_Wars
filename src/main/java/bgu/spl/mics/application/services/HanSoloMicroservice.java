package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {

    private CountDownLatch countDownLatch;

    public HanSoloMicroservice() {
        super("Han");
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
        subscribeEvent(AttackEvent.class,(AttackEvent event)->{
            Ewoks ewoks = Ewoks.getInstance();
            Attack att = event.getData();
            List<Integer> resources = att.getSerials();
            resources.sort(Integer::compareTo); // sort resources to ask for ewoks in a logical order.
            for(Integer i: resources){ //acquire resources
                ewoks.useEwok(i);
            }
            Thread.sleep(att.getDuration()); // perform attack
            for(Integer i: resources){//release resources
                ewoks.freeEwok(i);
            }
            complete(event,true);
            d.setAttack();


            if(d.getTotalAttacks().get()==attackSize.get()-1){
                d.setHanSoloFinish();
               }
            if (d.getTotalAttacks().get()==attackSize.get()){
                d.setHanSoloFinish();
                sendEvent(new DeactivationEvent());
                DeactivationEvent ev = new DeactivationEvent();

                sendEvent(ev);
                if (isEventCompleted(ev)){ // Will always be True because it calls the future get first so it waits.
                    sendEvent(new BombDestroyerEvent());
                }



            }
        });

        subscribeBroadcast(TerminateBroadcast.class,(c)->{
            d.setHanSoloTerminate();
            terminate();
        });
        countDownLatch.countDown();
    }

}

