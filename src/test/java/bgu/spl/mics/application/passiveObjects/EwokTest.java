package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.Future;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {

    private Ewok ewok;

    @BeforeEach
    void setUp() { ewok = new Ewok(1);}

    @AfterEach
    void tearDown() {
        ewok = null;
    }

    @Test
    void testAcquire() {
        assertTrue(ewok.available);
        try{
            ewok.acquire();
            assertFalse(ewok.available);
        }
        catch(InterruptedException e) {}
    }

    @Test
    void testRelease() {
        try {
            ewok.acquire();
            assertFalse(ewok.available);
            ewok.release();
            assertTrue(ewok.available);
        }
        catch (InterruptedException e) {}
    }

}
