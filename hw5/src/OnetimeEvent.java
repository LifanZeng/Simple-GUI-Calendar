import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * OnetimeEvent class, which implements Event interface and Comparable interface,
 * represents an one-time event.
 */
public class OnetimeEvent implements Event, Comparable<OnetimeEvent>{
    private String name;
    private LocalDate onetimeDate;
    private TimeInterval timeInterval;

    /**
     * Constructs an one-time event.
     * @param name - the name of the event.
     * @param onetimeDate - the date of the the event.
     * @param timeInterval - the time interval of the event.
     */
    public OnetimeEvent(String name, LocalDate onetimeDate, TimeInterval timeInterval){
        this.name = name;
        this.onetimeDate = onetimeDate;
        this.timeInterval = timeInterval;
    }

    /**
     * Returns the name of the one-time event.
     * @return the event name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the time interval of the one-time event.
     * @return time interval.
     */
    @Override
    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    /**
     * Returns the date of the one-time event.
     * @return the date of the event.
     */
    public LocalDate getDate(){
        return onetimeDate;
    }

    /**
     * Returns the starting datetime of the one-time event.
     * @return starting datetime.
     */
    public LocalDateTime getStartingDateTime(){
        LocalDateTime dateTime = LocalDateTime.of(onetimeDate.getYear(), onetimeDate.getMonth(),
                onetimeDate.getDayOfMonth(), timeInterval.getStartingTime().getHour(),
                timeInterval.getStartingTime().getMinute(), timeInterval.getStartingTime().getSecond());
        return dateTime;
    }

    /**
     * Returns the ending datetime of the one-time event.
     * @return ending datetime.
     */
    public LocalDateTime getEndingDateTime(){
        LocalDateTime dateTime = LocalDateTime.of(onetimeDate.getYear(), onetimeDate.getMonth(),
                onetimeDate.getDayOfMonth(), timeInterval.getEndingTime().getHour(),
                timeInterval.getEndingTime().getMinute(), timeInterval.getEndingTime().getSecond());
        return dateTime;
    }

    /**
     * Compare two one-time events
     * @param otherEvent - an one-time event.
     * @return the compare result of two events.
     */
    @Override
    public int compareTo(OnetimeEvent otherEvent) {
        if(getEndingDateTime().isBefore(otherEvent.getStartingDateTime()))
            return -1;
        else if (getStartingDateTime().isAfter(otherEvent.getEndingDateTime()))
            return 1;
        else{
            System.out.println(otherEvent.getName()+" CONFLICT with other event at the interval:  Start time:"+
                    otherEvent.getStartingDateTime()+", Ending time:"+otherEvent.getEndingDateTime());
            return 0;}
    }
}