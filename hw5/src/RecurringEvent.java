import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * RecurringEvent class, which implements Event interface and Comparable interface,
 * represents a recurring event.
 */
public class RecurringEvent implements Event, Comparable<RecurringEvent>{
    private String name;
    private LocalDate startingDate;
    private LocalDate endingDate;
    private ArrayList<Enum_Day> RecurringDays;
    private TimeInterval timeInterval;

    /**
     * Constructs a recurring event.
     * @param name - the name of the recurring event.
     * @param startingDate - the starting date of the recurring event.
     * @param endingDate - the ending date of the recurring event.
     * @param RecurringDays - the repeat days in every week.
     * @param timeInterval - the time interval of the recurring event.
     */
    public RecurringEvent(String name, LocalDate startingDate, LocalDate endingDate, ArrayList<Enum_Day> RecurringDays, TimeInterval timeInterval){
        this.name = name;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.RecurringDays = RecurringDays;
        this.timeInterval = timeInterval;
    }

    /**
     * Returns the name of the recurring event.
     * @return event name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the time interval of the recurring event.
     * @return time interval.
     */
    @Override
    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    /**
     * Returns the starting date of the recurring event.
     * @return starting date.
     */
    public LocalDate getStartingDate(){
        return startingDate;
    }

    /**
     * Returns the ending date of the recurring event.
     * @return ending date.
     */
    public LocalDate getendingDate(){
        return endingDate;
    }

    /**
     * Returns the repeat days in every week.
     * @return the repeat days.
     */
    public ArrayList<Enum_Day> getRecurringDays(){
        return RecurringDays;
    }

    /**
     * Turns the recurring event into some one-time events.
     * @return one-time events.
     */
    public ArrayList<OnetimeEvent> turnIntoOneTimeEvents(){
        ////private ArrayList<OnetimeEvent> onetimeEvents;
        ArrayList<OnetimeEvent> onetimeEvents = new ArrayList<OnetimeEvent>();
        for (LocalDate date = startingDate; date.isBefore(endingDate); date=date.plusDays(1)){
            for(Enum_Day day: RecurringDays){
                if(day.get_DayofWeek().equals (date.getDayOfWeek())){
                    OnetimeEvent onetimeEvent = new OnetimeEvent(getName(), date, getTimeInterval());
                    onetimeEvents.add(onetimeEvent);
                }
            }
        }
        return onetimeEvents;
    }

    /**
     * Compare two recurring events
     * @param otherEvent - a recurring event.
     * @return the compare result of two events.
     */
    @Override
    public int compareTo(RecurringEvent otherEvent) {
        if(getStartingDate().isBefore(otherEvent.getStartingDate()))
            return -1;
        else if (getStartingDate().isAfter(otherEvent.getStartingDate()))
            return 1;
        else
            return 0;
    }
}