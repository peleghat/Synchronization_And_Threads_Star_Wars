package bgu.spl.mics.application.passiveObjects;

import java.util.Vector;


/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */

public class Ewoks {

    private static Ewoks instance = null; // Singleton
    private Vector<Ewok> ewokVector;

    /**
     * @return instance of the Ewok (because implemented as a singleton)
     */
    public static Ewoks getInstance(){
        return EwoksHolder.instance;
    }

    private static class EwoksHolder {
        private static Ewoks instance = new Ewoks();
    }

    private Ewoks(){  // Private Constructor
        ewokVector = new Vector<>();
    }


    /**
     *
     * this method is used for initialization only.
     */
    public void insertEwok (Ewok ewok){
        ewokVector.add(ewok);
    }

    /**
     * used by HanSolo and C3po - to manage resources
     */
    public Ewok useEwok (int ewok) throws InterruptedException {
        ewokVector.elementAt(ewok-1).acquire();
        return ewokVector.elementAt(ewok-1);
    }

    /**
     * used by HanSolo and C3po - to manage resources
     */
    public void freeEwok (int ewok) {
        ewokVector.elementAt(ewok-1).release();
    }

    /**
     *  for test purposes only - resetting all the data members
     */
    public void resetEwoks () {
        ewokVector = new Vector<>();
        instance = null; // for Test purposes
    }
}
