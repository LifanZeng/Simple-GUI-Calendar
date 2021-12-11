import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * This is the class contained main().
 * @author Lifan Zeng
 * @version 1.0 5/12/2021
 */
public class SimpleCalendarTester {
    public static void main(String[] args) throws IOException {
        ArrayList<Integer> datas = new ArrayList<Integer>();
        ArrayList<OnetimeEvent> allEvents = new ArrayList<OnetimeEvent>();
        LocalDate thisDate=LocalDate.now();/////////////////////////////////////////////
        int nSart = thisDate.minusDays(thisDate.getDayOfMonth() - 1).getDayOfWeek().getValue() % 7;     // First day is Tuesday, then 2; Thursday, 7; Sunday, 0.
        int nDayAmont = thisDate.lengthOfMonth();
        for(int i=0; i<42; i++) {
            if(i < nSart)
                datas.add(0);
            else if(i < nSart + nDayAmont)
                datas.add(i-nSart+1);
            else
                datas.add(0);
        }
        datas.add(thisDate.getDayOfMonth()+nSart-1);                      // The datas.get(42) is the selected position, default date is today.
        //DataModel dataModel = new DataModel(datas);
        //---------------------------------------------
        Files.copy(Paths.get("events.txt"), Paths.get("events_modified.txt"), REPLACE_EXISTING);
        MyCalendar calendar = new MyCalendar("events_modified.txt");
        ArrayList<OnetimeEvent> onetimeEvents = calendar.getOnetimeEvents();
        ArrayList<RecurringEvent> recurringEvents = calendar.getRecurringEvents();
        allEvents = calendar.getAllEvents();
        //---------------------------------
        DataModel dataModel = new DataModel(datas, allEvents);

        MainFrame frame =new MainFrame(dataModel, LocalDate.now());
        dataModel.addChangeListener(frame);
    }
}
