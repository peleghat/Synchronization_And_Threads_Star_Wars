package bgu.spl.mics.application.passiveObjects;

import java.util.List;


/**
 * Passive data-object representing an attack object.
 * You must not alter any of the given public methods of this class.
 * <p>
 * YDo not add any additional members/method to this class (except for getters).
 */
public class Attack {
    final List<Integer> serials;
    final int duration;

    /**
     * Constructor.
     * @param serialNumbers is the required resources
     * @param duration is the duration of the attack received (in MS)
     */
    public Attack(List<Integer> serialNumbers, int duration) {
        this.serials = serialNumbers;
        this.duration = duration;
    }


    // Getters
    public List<Integer> getSerials() {
        return serials;
    }

    public int getDuration() {
        return duration;
    }
}
