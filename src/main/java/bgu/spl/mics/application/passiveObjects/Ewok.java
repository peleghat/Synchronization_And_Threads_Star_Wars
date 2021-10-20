package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Ewok {
	int serialNumber;
	boolean available;

    /**
     * Constructor
     * @param serialNumber is the serial number of the ewok
     */
	public Ewok (int serialNumber) {
	    this.serialNumber=serialNumber;
	    available=true;
    }
	
  
    /**
     * Acquires an Ewok - if is not available, wait until its available.
     */
    public synchronized void acquire() throws InterruptedException {
        while (!available) {
            wait();
        }
		this.available = false;
    }

    /**
     * release an Ewok - notify for waiting threads that the shared resource is released.
     */
    public synchronized void release() {
        if (available) {
            throw new IllegalArgumentException("Ewok is already Available");
        }
        this.available = true;
        notifyAll();
    }

    public boolean isAvailable() {
        return available;
    }

}
