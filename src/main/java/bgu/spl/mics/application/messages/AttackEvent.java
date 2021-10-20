package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;


/**
 * this event will be sent by leia and will be handled by HanSolo and C3PO simultaneously
 */
public class AttackEvent implements Event<Boolean> {
	private Attack data;

    /**
     * Constructor
     * @param data is the attack related to the attack event
     */
	public AttackEvent(Attack data){
	    this.data=data;
    }

    public Attack getData() {
        return data;
    }
}
