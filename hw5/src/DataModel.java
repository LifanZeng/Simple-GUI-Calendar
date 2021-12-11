import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * This class is the data model of the MVC pattern.
 */
public class DataModel {
    private ArrayList<Integer> datas;
    private ArrayList<ChangeListener> listeners;
    private MyCalendar calendar;// = new MyCalendar("events_modified.txt");
    private ArrayList<OnetimeEvent> onetimeEvents; //= calendar.getOnetimeEvents();
    private ArrayList<RecurringEvent> recurringEvents; // = calendar.getRecurringEvents();
    private ArrayList<OnetimeEvent> allEvents; // = calendar.getAllEvents();

    /**
     * To construct a data model.
     * @param datas
     * @param allEvents
     * @throws IOException
     */
    public DataModel(ArrayList<Integer> datas, ArrayList<OnetimeEvent> allEvents) throws IOException {
        this.datas = datas;
        this.allEvents = allEvents;
        listeners = new ArrayList<ChangeListener>();
        calendar = new MyCalendar("events_modified.txt");
        onetimeEvents = calendar.getOnetimeEvents();
        recurringEvents = calendar.getRecurringEvents();
        //allEvents = calendar.getAllEvents();
    }

    /**
     * Accessor for date data.
     * @return
     */
    public ArrayList<Integer> getDatas(){
        return (ArrayList<Integer>) datas.clone();
    }

    /**
     * Mutator for date data.
     * @param index
     * @param value
     */
    public void update(int index, int value){
        datas.set(index, new Integer(value));
        for(ChangeListener l: listeners){
            l.stateChanged(new ChangeEvent(this));
        }
    }

    /**
     * Accessor for events data.
     * @return
     */
    public ArrayList<OnetimeEvent> getAllEvents(){
        return (ArrayList<OnetimeEvent>) allEvents.clone();
    }

    /**
     * Mutator for events data.
     * @param newEvent
     * @throws IOException
     */
    public void addOneEvent(OnetimeEvent newEvent ) throws IOException {
        int nCheckconflict=100;
        for(OnetimeEvent test: allEvents){
            nCheckconflict=test.compareTo(newEvent);
            if(nCheckconflict==0){
                JOptionPane.showMessageDialog(null,"Fail to create schedule! Conflict with other event.");
                break;
            }
        }
        if(nCheckconflict!=0){
            onetimeEvents.add(newEvent);
            allEvents.add(newEvent);
            Collections.sort(allEvents);
            Collections.sort(onetimeEvents);
            calendar.saveData(); ////
        }

        for(ChangeListener l: listeners){
            l.stateChanged(new ChangeEvent(this));
        }
    }

    /**
     * To notify the views.
     * @param c - a ChangeListener.
     */
    public void addChangeListener(ChangeListener c){
        listeners.add(c);
    }
}
