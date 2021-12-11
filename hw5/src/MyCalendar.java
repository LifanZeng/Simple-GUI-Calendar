import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * MyCalendar class defines an underlying data structure to hold events.
 */
public class MyCalendar {
    private String filename;
    private ArrayList<OnetimeEvent> onetimeEvents = new ArrayList<OnetimeEvent>();
    private ArrayList<RecurringEvent> recurringEvents = new ArrayList<RecurringEvent>();
    private ArrayList<OnetimeEvent> allEvents = new ArrayList<OnetimeEvent>();

    /**
     * Constructs a calendar with the specified events.
     * @param filename - a file holding the initial events.
     * @throws IOException if error occurs when reading data from the file.
     */
    public MyCalendar(String filename) throws IOException{
        this.filename = filename;
        String eventName;
        LocalDate date_onetime;
        TimeInterval timeInterval;
        LocalDate date_start;
        LocalDate date_end;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        List<String> Lines = Files.readAllLines(Paths.get(filename));
        for (int i=0; i<Lines.size(); i=i+2){
            eventName = Lines.get(i);
            String[] words = Lines.get(i+1).split(" ");
            if (words.length==3){
                //words[0] date; words[1] starting time; words[2] ending time.
                date_onetime = LocalDate.parse(words[0], formatter);
                timeInterval = new TimeInterval(LocalTime.parse(words[1]), LocalTime.parse(words[2]));
                OnetimeEvent oneEvent = new OnetimeEvent(eventName, date_onetime, timeInterval);
                onetimeEvents.add(oneEvent);
            }
            //*****************************************************************************************
            else if(words.length==5){
                //words[0] ArrayList<Enum_Day>; words[1] starting time; words[2] ending time.
                //words[3] starting date; words[4] ending date.
                char[] Days = words[0].toCharArray();
                ArrayList<Enum_Day> EDays=new ArrayList<Enum_Day>();
                for(char d: Days){
                    String sd = "" + d;
                    EDays.add(Enum_Day.valueOf(sd));
                }

                date_start = LocalDate.parse(words[3], formatter);
                date_end =LocalDate.parse(words[4], formatter);
                timeInterval = new TimeInterval(LocalTime.parse(words[1]), LocalTime.parse(words[2]));

                RecurringEvent recurringEvent = new RecurringEvent(eventName, date_start, date_end, EDays, timeInterval);
                recurringEvents.add(recurringEvent);
            }
        }

        for(OnetimeEvent o: onetimeEvents){

            allEvents.add(o);
        }
        for(RecurringEvent r: recurringEvents){
            ArrayList<OnetimeEvent> oneTimeEvents = r.turnIntoOneTimeEvents();
            for(OnetimeEvent o: oneTimeEvents){
                allEvents.add(o);
            }
        }
        Collections.sort(allEvents);
        Collections.sort(onetimeEvents);
        Collections.sort(recurringEvents);
        //System.out.println("Loading is done!");
    }

    /**
     * Returns the one time events of this calendar.
     * @return the one time events.
     */
    public ArrayList<OnetimeEvent> getOnetimeEvents(){
        return onetimeEvents;
    }

    /**
     * Returns the recurring events of this calendar.
     * @return the recurring events.
     */
    public ArrayList<RecurringEvent> getRecurringEvents(){
        return recurringEvents;
    }

    /**
     * Returns all the events of this calendar.
     * @return all the events.
     */
    public ArrayList<OnetimeEvent> getAllEvents(){
        return allEvents;
    }

    /**
     * The mutator of the calendar.
     * @throws IOException if error occurs when writing data to the file.
     */
    public void saveData() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");

        FileWriter fileWriter = new FileWriter(filename,false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for(OnetimeEvent o: onetimeEvents){
            printWriter.println(o.getName());
            printWriter.println(o.getDate().format(formatter)+" "+
                    o.getTimeInterval().getStartingTime()+" "+o.getTimeInterval().getEndingTime());
        }
        for(RecurringEvent r: recurringEvents){
            printWriter.println(r.getName());
            String str="";
            ArrayList<Enum_Day> days = r.getRecurringDays();
            for(int j=0; j<days.size(); j++){
                str = str + days.get(j).toString();
            }
            printWriter.println(str + " " + r.getTimeInterval().getStartingTime() + " "+
                    r.getTimeInterval().getEndingTime()+" "+r.getStartingDate().format(formatter)+" "+
                    r.getendingDate().format(formatter));
        }
        printWriter.close();
    }

    /**
     * Add a one-time event.
     * @param event - a one-time event.
     * @throws IOException if error occurs when writing data to the file.
     */
    public void addOneTimeEvent(OnetimeEvent event) throws IOException {
        onetimeEvents.add(event);
        allEvents.add(event);
        Collections.sort(allEvents);
        Collections.sort(onetimeEvents);
        saveData();
    }

    /**
     * Delete a one-time event.
     * @param event - a one-time event.
     * @throws IOException if error occurs when writing data to the file.
     */
    public void deleteOneTimeEvent(OnetimeEvent event) throws IOException {
        if(onetimeEvents.contains(event))
            onetimeEvents.remove(event);
        if(allEvents.contains(event))
            allEvents.remove(event);
        saveData();
    }

    /**
     * Delete a recurring event.
     * @param r_event - a recurring event.
     * @throws IOException if error occurs when writing data to the file.
     */
    public void deleteRecurringEvent(RecurringEvent r_event) throws IOException {
        ArrayList<OnetimeEvent> tempEvents = new ArrayList<OnetimeEvent>();
        tempEvents = r_event.turnIntoOneTimeEvents();
        if(recurringEvents.contains(r_event))
            recurringEvents.remove(r_event);
        for(OnetimeEvent o: tempEvents){
            if(allEvents.contains(o))
                allEvents.remove(o);
        }
        saveData();
    }

    /**
     * To show the calendar of current month.
     */
    public void showCurrentMonth(){
        LocalDate date = LocalDate.now();
        String MonthAndYear =LocalDate.now().getMonth()+"  "+LocalDate.now().getYear();
        date = date.minusDays(LocalDate.now().getDayOfMonth() - 1); // start from the first day of the month
        int value = date.getDayOfWeek().getValue();   //Monday-->1, ..., Sunday-->7
        if (value == 7)
            value=0;                                  //Since now Sunday is in the front instead of the end
        System.out.println();
        System.out.println("         " + MonthAndYear);
        System.out.println("  Su   Mo   Tu   We   Th   Fr   Sa  ");
        for (int i = 0; i < value; i++)
            System.out.print("     ");  // find the begin position
        while (date.getMonth().getValue() == LocalDate.now().getMonthValue()) {       //just print this month
            if (date.getDayOfMonth() < 10) {
                System.out.print("  " + date.getDayOfMonth() + " ");
            }                                               //print one more space for small number
            else {
                if (date.getDayOfMonth() == LocalDate.now().getDayOfMonth())
                    System.out.print(" [" + date.getDayOfMonth());                       //[today]
                else
                    System.out.print("  " + date.getDayOfMonth());                      //other days
            }
            if (date.getDayOfMonth() == LocalDate.now().getDayOfMonth()) {               //[today]
                System.out.print("]");
            } else {
                System.out.print(" ");
            }
            date = date.plusDays(1);                     //after printed, move to next day
            if (date.getDayOfWeek().getValue() == 7) {    //Sunday is the first day of every line
                System.out.println();
            }
        }
        System.out.println("");
    }


    /**
     * To show the calendar of the month containing the specified date.
     * @param lDate - a LocalDate.
     */
    public void getMonth(LocalDate lDate){
        /**
         * To show the calendar of this month.
         */
        LocalDate date = lDate;
        int nDay = lDate.getDayOfMonth();
        String MonthAndYear =lDate.getMonth()+"  "+lDate.getYear();
        date = date.minusDays(lDate.getDayOfMonth() - 1); // start from the first day of the month
        int value = date.getDayOfWeek().getValue();   //Monday-->1, ..., Sunday-->7
        if (value == 7)
            value=0;                                //Since now Sunday is in the front instead of the end
        System.out.println();
        System.out.println("         " + MonthAndYear);
        System.out.println("  Su   Mo   Tu   We   Th   Fr   Sa  ");
        for (int i = 0; i < value; i++)
            System.out.print("     ");  // find the begin position
        while (date.getMonth().getValue() == lDate.getMonthValue()) {       //just print this month
            boolean hasEvent=false;
            if (date.getDayOfMonth() < 10) {
                System.out.print("  " + date.getDayOfMonth() + " ");
            }                                               //print one more space for small number
            else {
                /*if (date.getDayOfMonth() == lDate.getDayOfMonth())*/
                for(OnetimeEvent a: allEvents){
                    if(a.getDate().isEqual(date))
                        hasEvent=true;
                }
                if(hasEvent==true)
                    System.out.print(" {" + date.getDayOfMonth());
                else
                    System.out.print("  " + date.getDayOfMonth());                      //other dates
            }
            if(hasEvent==true)
                System.out.print("}");
            else
                System.out.print(" ");                            //other dates
            date = date.plusDays(1);                     //after printed, move to next day
            if (date.getDayOfWeek().getValue() == 7) {    //Sunday is the first day of every line
                System.out.println();
            }
        }
        System.out.println("");
    }
}
