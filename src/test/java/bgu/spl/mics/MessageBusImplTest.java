package bgu.spl.mics;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import bgu.spl.mics.application.services.LeiaMicroservice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageBusImplTest {

    private MessageBusImpl msgbus;
    private DeactivationEvent deactivationEvent;
    private TerminateBroadcast terminateBroadcast;
    private LeiaMicroservice leia;
    private HanSoloMicroservice hanSolo;
    private C3POMicroservice c3po;

    @BeforeEach
    void setUp() {
        msgbus = MessageBusImpl.getInstance();
        deactivationEvent = new DeactivationEvent();
        terminateBroadcast = new TerminateBroadcast();
        leia = new LeiaMicroservice(new Attack[]{});
        hanSolo = new HanSoloMicroservice();
        c3po = new C3POMicroservice();
        msgbus.register(leia);
        msgbus.register(hanSolo);
        msgbus.register(c3po);
    }

    @AfterEach
    void tearDown() {
        msgbus.unregister(leia);
        msgbus.unregister(hanSolo);
        msgbus.unregister(c3po);
        leia = null;
        hanSolo = null;
        c3po = null;
        deactivationEvent = null;
        terminateBroadcast = null;
        msgbus = null;
    }


    @Test
    void testSendBroadcast() {
        msgbus.subscribeBroadcast(terminateBroadcast.getClass(),c3po);
        msgbus.subscribeBroadcast(terminateBroadcast.getClass(),hanSolo);
        leia.sendBroadcast(terminateBroadcast);
        Message b2 = msgbus.awaitMessage(c3po);
        Message b3 = msgbus.awaitMessage(hanSolo);
        assertEquals(terminateBroadcast,b2);
        assertEquals(terminateBroadcast,b3);
    }

    @Test
    void testSendEvent() {
        msgbus.subscribeEvent(deactivationEvent.getClass(),c3po);
        leia.sendEvent(deactivationEvent);
        Message e2 = msgbus.awaitMessage(c3po);
        assertEquals(deactivationEvent,e2);
    }

    @Test
    void testComplete() {
        msgbus.subscribeEvent(deactivationEvent.getClass(),hanSolo);
        Future<Boolean> res = leia.sendEvent(deactivationEvent);
        assertFalse(res.isDone());
        assertEquals(res.get(50, TimeUnit.MILLISECONDS),null);
        hanSolo.complete(deactivationEvent,true);
        assertTrue(res.isDone()); // Complete calls the resolve method
        assertEquals(res.get(),true); // Checks if result was updated
    }

    @Test
    void testAwaitMessage() {
    }

    @Test
    void testSubscribeEvent() {
    }

    @Test
    void testSubscribeBroadcast() {
    }

    @Test
    void testRegister() {
    }

    @Test
    void testUnregister() {
    }

}
