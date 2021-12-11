import java.sql.Time;
import java.time.LocalTime;

/**
 * TimeInterval class represents an interval of time, suitable for events.
 */
public class TimeInterval {
    private LocalTime startingTime;
    private LocalTime endingTime;

    /**
     * Constructs a time interval with a specified starting time and ending time.
     * @param startingTime - the starting time.
     * @param endingTime - the ending time.
     */
    public TimeInterval(LocalTime startingTime, LocalTime endingTime){
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }

    /**
     * Returns the starting time of the time interval.
     * @return the starting time.
     */
    public LocalTime getStartingTime(){
        return startingTime;
    }

    /**
     * Returns the ending time of the time interval.
     * @return the ending time.
     */
    public LocalTime getEndingTime(){
        return endingTime;
    }
}
