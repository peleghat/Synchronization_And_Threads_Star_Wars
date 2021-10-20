package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.atomic.AtomicInteger;

/**
 int totalAttacks - the total number of attacks executed by HanSolo and C3PO.
 can also be of AtomicInteger type. Stamped only by HanSolo or C3PO (!!).
 ● long HanSoloFinish - a timestamp indicating when HanSolo finished the
 execution of all his attacks.
 ● long C3POFinish - a timestamp indicating when C3PO finished the execution of
 all his attacks.
 ● long R2D2Deactivate - a timestamp indicating when R2D2 finished deactivation
 the shield generator.
 ● long LeiaTerminate - a time stamp that Leia puts in right before termination.
 ● long HanSoloTerminate - a time stamp that HanSolo puts in right before
 termination.
 ● long C3POTerminate - a time stamp that C3PO puts in right before termination.
 ● long R2D2Terminate - a time stamp that R2d2 puts in right before termination.
 ● long LandoTerminate - a time stamp that Lando puts in right before termination.
 ● To get those timestamps, simply use System.currentTimeMillis().
 ● We will check that your timestamps make sense.
 ● Each timestamp is recorded by the specified name, e.g. only C3PO is allowed to
 set the value of C3POFinish. The totalAttacks member is recorded only by
 HanSolo or C3PO
 */
public class Diary {

    private AtomicInteger totalAttacks;
    private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3POTerminate;
    private long R2D2Terminate;
    private long LandoTerminate;
    private static Diary instance = null;

    // Constructor
    private Diary() {
        totalAttacks = new AtomicInteger(0);
    }

    /**
     * @return instance of the diary (because it is implemented as a singleton)
     */
    public static Diary getInstance () { // Singleton "Constructor"
        return DiaryHolder.instance;
    }


    private static class DiaryHolder {
        private static Diary instance = new Diary();
    }


    /**
     * will be resetting all the data members of this diary instance. will be used for test purposes only.
     */
    public void resetDiary () {
        instance = null; // for Test purposes
        AtomicInteger totalAttacks = new AtomicInteger(0);
        long HanSoloFinish=0;
        long C3POFinish=0;
        long R2D2Deactivate=0;
        long LeiaTerminate=0;
        long HanSoloTerminate=0;
        long C3POTerminate=0;
        long R2D2Terminate=0;
        long LandoTerminate=0;
    }

    //Setters
    public void setAttack() {
        totalAttacks.incrementAndGet();
    }

    public void setHanSoloFinish() {
    HanSoloFinish = System.currentTimeMillis();
    }

    public void setC3POFinish() {
    C3POFinish = System.currentTimeMillis();
    }

    public void setR2D2Deactivate() {
    R2D2Deactivate = System.currentTimeMillis();
    }

    public void setR2D2Terminate() {
    R2D2Terminate = System.currentTimeMillis();
    }

    public void setLeiaTerminate() {
    LeiaTerminate = System.currentTimeMillis();
    }

    public void setHanSoloTerminate() {
    HanSoloTerminate = System.currentTimeMillis();
    }

    public void setLandoTerminate() {
    LandoTerminate = System.currentTimeMillis();
    }

    public void setC3POTerminate() {
        C3POTerminate = System.currentTimeMillis();
    }


    //Getters
    public AtomicInteger getTotalAttacks() {
        return totalAttacks;
    }

    public long getHanSoloFinish() {
        return HanSoloFinish;
    }

    public long getC3POFinish() {
        return C3POFinish;
    }

    public long getC3POTerminate() {
        return C3POTerminate;
    }

    public long getR2D2Deactivate() {
        return R2D2Deactivate;
    }

    public long getLeiaTerminate() {
        return LeiaTerminate;
    }

    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }

    public long getLandoTerminate() {
        return LandoTerminate;
    }

    public long getR2D2Terminate() {
        return R2D2Terminate;
    }

    public void resetNumberAttacks(){
        this.totalAttacks=new AtomicInteger(0);
    }
}



