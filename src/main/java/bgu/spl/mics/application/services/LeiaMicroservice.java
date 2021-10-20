package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
	
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
    }

    @Override
    /**
     * initialize - sends the attack events.
     * subscribes to the terminateBroadcast.
     */
    protected void initialize() {
        Diary d = Diary.getInstance();

        subscribeBroadcast(TerminateBroadcast.class,(c)->{
            d.setLeiaTerminate();
            terminate();
        });

        for(Attack a: attacks){
            AttackEvent e = new AttackEvent(a);
            sendEvent(e); // Send attack events to the messageBus
        }

    }

}
