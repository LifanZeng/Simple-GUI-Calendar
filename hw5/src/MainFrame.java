import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class MainFrame extends JFrame implements ChangeListener {
    private DataModel dataModel;
    private ArrayList<Integer> datas;
    private GridShape[] shapes = new GridShape[49];
    private ShapeIcon[] shapeIcons = new ShapeIcon[49];
    private JLabel[] dayLabels = new JLabel[49];
    private JTextPane tPnlEvent= new JTextPane();
    private LocalDate thisDate;
    private int[] monthGridNumbers = new int[42];
    private String strMonthYear;
    private JLabel lblDay;
    private JLabel lblMonthYear;
    private int nSart;
    private int nDayAmont;
    private MyCalendar calendar;
    private ArrayList<OnetimeEvent> onetimeEvents;
    private ArrayList<RecurringEvent> recurringEvents;
    private ArrayList<OnetimeEvent> allEvents;

    /**
     * To define the main frame.
     * @param dataModel
     * @param theDate
     * @throws IOException
     */
    public MainFrame(DataModel dataModel, LocalDate theDate) throws IOException {
        this.dataModel = dataModel;
        datas = dataModel.getDatas();
        allEvents =dataModel.getAllEvents();
        thisDate = theDate;
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MM/dd");
        strMonthYear=thisDate.getDayOfWeek()+" "+thisDate.format(formatter2);

        nSart = thisDate.minusDays(thisDate.getDayOfMonth() - 1).getDayOfWeek().getValue() % 7;     // First day is Tuesday, then 2; Thursday, 7; Sunday, 0.
        nDayAmont = thisDate.lengthOfMonth();
        lblDay = new JLabel(strMonthYear, JLabel.CENTER);
       // System.out.println("this day is: "+thisDate);
        for (int i=0; i<42; i++){                                               // i is the grid order.
            shapes[i] = new GridShape(0, 0, 50, Color.black);
            if(i%7==0 || i%7==6)                                                // Saturday and Sunday are red.
                shapes[i].setColor(Color.red);
            shapes[i].setNumber(datas.get(i));                                  // set the value of the shape equal the date number.
            shapeIcons[i] = new ShapeIcon(shapes[i], 50, 50);
            dayLabels[i] = new JLabel(shapeIcons[i]);
            int gridOrderNumber = i;
            shapes[i].isCircled(false);
            if (shapes[i].getNumber()==datas.get(42)-nSart+1) {         //////////////////////////////////////////////////////////////////???????????????????????????
                shapes[i].isCircled(true);
               // System.out.println("i="+i+"; shapes[i].getNumber()="+shapes[i].getNumber());
            }

            dayLabels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mouseClicked(e);
                    int oldPosition = datas.get(42);
                    dataModel.update(42, gridOrderNumber);          // data.get(42) store the clicked position.
                    for(int i=0; i<42; i++){
                        shapes[i].isCircled(false);
                    }
                    shapes[datas.get(42)].isCircled(true); //only the selected will be circled.

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    //-------------------------------
                    String strM, strYMD, strD;
                    if(thisDate.getMonth().getValue()>=10)
                        strM = thisDate.getMonth().getValue()+"";
                    else
                        strM = "0"+thisDate.getMonth().getValue();

                    if(datas.get(42)-nSart+1>=10 && datas.get(42)-nSart+1<=thisDate.lengthOfMonth())
                        strD = datas.get(42)-nSart+1+"";
                    else if (datas.get(42)-nSart+1>=1 && datas.get(42)-nSart+1<10)
                        strD = "0" + (datas.get(42)-nSart+1);
                    else
                        strD = "01";
                    strYMD = thisDate.getYear()+"-"+strM+"-"+ strD;

                    thisDate = LocalDate.parse(strYMD, formatter);

                    //-----------------------------------------set JTextPane-----------------------------------------------------
                    LocalDate g_Date = thisDate;
                    ArrayList<OnetimeEvent> tempEvents_g = new ArrayList<OnetimeEvent>();
                    String strEvents;
                    for(OnetimeEvent a: allEvents){                                     //find out all events in this day and put them in ArrayList<> tempEvents.
                        if(a.getDate().isEqual(g_Date)){
                            tempEvents_g.add(a);
                        }
                    }
                    if(tempEvents_g.size()>=1){
                        strEvents=(g_Date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + ", "+
                                g_Date.getMonth() + " " + g_Date.getDayOfMonth() + ", "+ g_Date.getYear())+"\n";
                        for(OnetimeEvent t: tempEvents_g){
                            strEvents=strEvents+(t.getName()+": "+t.getTimeInterval().getStartingTime()+" - "+t.getTimeInterval().getEndingTime())+"\n";
                        }
                    }
                    else{
                        strEvents = (g_Date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + ", "+
                                g_Date.getMonth() + " " + g_Date.getDayOfMonth() + ", "+ g_Date.getYear())+"\n";
                        strEvents = strEvents+("No event in this day.")+"\n";
                    }
                    tPnlEvent.setText(strEvents);
//-----------------------------------------------------------------------------------------------------------

                    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MM/dd");
                    lblDay.setText(thisDate.getDayOfWeek() + " " + thisDate.format(formatter2)); ////////////////////////////////////////// should for grid, not next
                    lblMonthYear.setText(thisDate.getMonth() + " " + thisDate.getYear());

                    repaint();
             }
            });

//----------------------------------------------------------------------------------------------
//            Files.copy(Paths.get("events.txt"), Paths.get("events_modified.txt"), REPLACE_EXISTING);
            calendar = new MyCalendar("events_modified.txt");
            onetimeEvents = calendar.getOnetimeEvents();
            recurringEvents = calendar.getRecurringEvents();
            allEvents = calendar.getAllEvents();
            //--------------------------------------------------
            LocalDate g_Date = thisDate;
            ArrayList<OnetimeEvent> tempEvents_g = new ArrayList<OnetimeEvent>();
            String strEvents;
            for(OnetimeEvent a: allEvents){                                     //find out all events in this day and put them in ArrayList<> tempEvents.
                if(a.getDate().isEqual(g_Date)){
                    tempEvents_g.add(a);
                }
            }
            if(tempEvents_g.size()>=1){
                strEvents=(g_Date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + ", "+
                        g_Date.getMonth() + " " + g_Date.getDayOfMonth() + ", "+ g_Date.getYear())+"\n";
                for(OnetimeEvent t: tempEvents_g){
                    strEvents=strEvents+(t.getName()+": "+t.getTimeInterval().getStartingTime()+" - "+t.getTimeInterval().getEndingTime())+"\n";
                }
            }
            else{
                strEvents = (g_Date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + ", "+
                        g_Date.getMonth() + " " + g_Date.getDayOfMonth() + ", "+ g_Date.getYear())+"\n";
                strEvents = strEvents+("No event in this day.")+"\n";
            }
            tPnlEvent.setText(strEvents);

            //-----------------------------------------set JTextPane-----------------------------------------------------
        }
//-------------------------------------------------------------------------------------------------------------------
        String[] strWeek = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
        Weekdays_GridShape[] shapeWeekdays = new Weekdays_GridShape[7];
        ShapeIcon[] iconWeekdays = new ShapeIcon[7];
        JLabel[] lblWeekdays = new JLabel[7];
        for(int m=0; m<strWeek.length; m++){
            shapeWeekdays[m] = new Weekdays_GridShape(0, 0, 50, Color.BLACK);
            shapeWeekdays[m].setString(strWeek[m]);
            if (m==0 || m==6)
                shapeWeekdays[m].setColor(Color.red);
            iconWeekdays[m] = new ShapeIcon(shapeWeekdays[m], 50, 50);
            lblWeekdays[m] = new JLabel(iconWeekdays[m]);
        }
//----------------------------panels-------------------------------------------------------------------------------
        // MainFrame (panelButton, panelBottom)
        // panelButton (4 buttons)
        // panelBottom (panelLeft, panelEvent)
        // panelLeft (panelMonthYear, panelWeekday)
        // panelEvent (panelDay, tPnlEvent)
        JPanel panelWeekday = new JPanel();
        JPanel panelButtons = new JPanel();
        JPanel panelEvent = new JPanel();
        JPanel panelBottom = new JPanel();
        JPanel panelMonthYear = new JPanel();
        JPanel panelDay=new JPanel();
        JPanel panelLeft = new JPanel();

//-------------panelButtons-----------------------------------------------------------------------------------------------------
        JButton btnCreate = new JButton("Create");
        JButton btnPrevious = new JButton("<");
        JButton btnNext = new JButton(">");
        JButton btnExit = new JButton("Exit");
        panelButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 20));
        panelButtons.add(btnCreate);
        panelButtons.add(btnPrevious);
        panelButtons.add(btnNext);
        panelButtons.add(btnExit);

        btnExit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
             //   System.out.println("Good bye!");
//                try {                                                 //?????????????????????????????????????????????????????????????????????????????????????????????
//                    calendar.saveData();
//                } catch (IOException ioException) {
//                    ioException.printStackTrace();
//                }
                try {
                    Files.copy(Paths.get("events_modified.txt"), Paths.get("output.txt"), REPLACE_EXISTING);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                System.exit(0);
            }
        });

        btnCreate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                CreateEvent_Frame createEvent_frame = null;
                try {
                    createEvent_frame = new CreateEvent_Frame(dataModel, thisDate);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                createEvent_frame.setVisible(true);
            }
        });

        btnNext.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                datas = dataModel.getDatas();

              //  System.out.println("nStart="+nSart+"; nDayAmont="+nDayAmont+"; get42="+datas.get(42));

                if(datas.get(42)==thisDate.lengthOfMonth()+nSart-1){
                    thisDate=thisDate.plusDays(1);///////////////////////////////////////
                    nSart = thisDate.minusDays(thisDate.getDayOfMonth() - 1).getDayOfWeek().getValue() % 7;     // First day is Tuesday, then 2; Thursday, 7; Sunday, 0.
                    nDayAmont = thisDate.lengthOfMonth();
                   // System.out.println("btnNext, this day is: "+thisDate);
                   // System.out.println("already a new month");
                    //int old = datas.get(42);
                    dataModel.update(42, 1);          // data.get(42) store the clicked position.
                    for (int i = 0; i < 42; i++) {
                        shapes[i].isCircled(false);
                        if (i < nSart) {
                            shapes[i].setNumber(0);
                            dataModel.update(i, 0);
                        } else if (i < nSart + nDayAmont) {
                            shapes[i].setNumber(i - nSart + 1);
                            dataModel.update(i, i - nSart + 1);
                        } else {
                            shapes[i].setNumber(0);
                            dataModel.update(i, 0);
                        }
                    }
                    dataModel.update(42, nSart);
                    for(int i=0; i<42; i++){
                        shapes[i].isCircled(false);
                    }
                    shapes[datas.get(42)].isCircled(true); //only the selected will be circled.

                    datas = dataModel.getDatas();
//                    for (Integer i : datas) {           // numbers for 42 grids.
//                        System.out.print(i + " ;");
//                    }
//                    System.out.println();
                    for (int i = 0; i < 42; i++) {
                        shapes[i].setNumber(datas.get(i));          //view
                    }
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
                    lblDay.setText(thisDate.getDayOfWeek() + " " + thisDate.format(formatter)); ////////////////////////////////////////// should for grid, not next
                    lblMonthYear.setText(thisDate.getMonth() + " " + thisDate.getYear());
                   // tPnlEvent.setText();

                    repaint();
                }


                else
                {
                    thisDate=thisDate.plusDays(1);
                    nSart = thisDate.minusDays(thisDate.getDayOfMonth() - 1).getDayOfWeek().getValue() % 7;     // First day is Tuesday, then 2; Thursday, 7; Sunday, 0.
                    nDayAmont = thisDate.lengthOfMonth();
                   // System.out.println("btnNext, this day is: "+thisDate);
                    int old = datas.get(42);
                    dataModel.update(42, old+1);          // data.get(42) store the clicked position.
                    for(int i=0; i<42; i++){
                        shapes[i].isCircled(false);
                    }
                    shapes[datas.get(42)].isCircled(true); //only the selected will be circled.
                   // System.out.println();
                    for (int i = 0; i < 42; i++) {
                        shapes[i].setNumber(datas.get(i));          //view
                    }
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
                    lblDay.setText(thisDate.getDayOfWeek() + " " + thisDate.format(formatter)); ////////////////////////////////////////// should for grid, not next
                    lblMonthYear.setText(thisDate.getMonth() + " " + thisDate.getYear());
                    repaint();
                }

               // System.out.println("thisDate.getDayOfMonth()= "+thisDate.getDayOfMonth()+"; thisDate.lengthOfMonth()="+thisDate.lengthOfMonth());
            }
        });



        btnPrevious.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                datas = dataModel.getDatas();

//                System.out.println("nStart="+nSart+"; nDayAmont="+nDayAmont+"; get42="+datas.get(42));
//                for(Integer d: datas){
//                    System.out.print(d+" ,");
//                }
//                System.out.println("Privious");
                if(datas.get(42)==nSart){
                    thisDate=thisDate.minusDays(1);///////////////////////////////////////
                    nSart = thisDate.minusDays(thisDate.getDayOfMonth() - 1).getDayOfWeek().getValue() % 7;     // First day is Tuesday, then 2; Thursday, 7; Sunday, 0.
                    nDayAmont = thisDate.lengthOfMonth();
//                    System.out.println("btnPrevious, this day is: "+thisDate);
//                    System.out.println("already a new month");
                    //int old = datas.get(42);
                    dataModel.update(42, (thisDate.lengthOfMonth()+nSart-1));          // data.get(42) store the clicked position.
                    for (int i = 0; i < 42; i++) {
                        shapes[i].isCircled(false);
                        if (i < nSart) {
                            shapes[i].setNumber(0);
                            dataModel.update(i, 0);
                        } else if (i < nSart + nDayAmont) {
                            shapes[i].setNumber(i - nSart + 1);
                            dataModel.update(i, i - nSart + 1);
                        } else {
                            shapes[i].setNumber(0);
                            dataModel.update(i, 0);
                        }
                    }
 //                   dataModel.update(42, nSart);
                    for(int i=0; i<42; i++){
                        shapes[i].isCircled(false);
                    }
                    shapes[datas.get(42)].isCircled(true); //only the selected will be circled.

                    datas = dataModel.getDatas();
//                    for (Integer i : datas) {           // numbers for 42 grids.
//                        System.out.print(i + " ;");
//                    }
//                    System.out.println();
                    for (int i = 0; i < 42; i++) {
                        shapes[i].setNumber(datas.get(i));          //view
                    }
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
                    lblDay.setText(thisDate.getDayOfWeek() + " " + thisDate.format(formatter)); ////////////////////////////////////////// should for grid, not next
                    lblMonthYear.setText(thisDate.getMonth() + " " + thisDate.getYear());
                    repaint();
                }


                else
                {
                    thisDate=thisDate.minusDays(1);
                    nSart = thisDate.minusDays(thisDate.getDayOfMonth() - 1).getDayOfWeek().getValue() % 7;     // First day is Tuesday, then 2; Thursday, 7; Sunday, 0.
                    nDayAmont = thisDate.lengthOfMonth();
//                    System.out.println("btnPrevious, this day is: "+thisDate);
                    int old = datas.get(42);
                    dataModel.update(42, old-1);          // data.get(42) store the clicked position.
                    for(int i=0; i<42; i++){
                        shapes[i].isCircled(false);
                    }
                    shapes[datas.get(42)].isCircled(true); //only the selected will be circled.
//                    System.out.println();
                    for (int i = 0; i < 42; i++) {
                        shapes[i].setNumber(datas.get(i));          //view
                    }
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
                    lblDay.setText(thisDate.getDayOfWeek() + " " + thisDate.format(formatter)); ////////////////////////////////////////// should for grid, not next
                    lblMonthYear.setText(thisDate.getMonth() + " " + thisDate.getYear());
                    repaint();
                }

                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//                System.out.println("thisDate.getDayOfMonth()= "+thisDate.getDayOfMonth()+"; thisDate.lengthOfMonth()="+thisDate.lengthOfMonth());
            }
        });
//---------------panelEvent-------------------------
        panelEvent.setLayout(new BoxLayout(panelEvent, BoxLayout.Y_AXIS));
        lblDay.setPreferredSize(new Dimension(500, 50));
        panelDay.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelDay.add(lblDay);

        tPnlEvent.setPreferredSize(new Dimension(500, 350));
        panelEvent.add(panelDay);
        panelEvent.add(tPnlEvent);


        //--------------panelWeekday--------------------------------------------
        panelWeekday.setLayout(new GridLayout(7, 7));
        for (int j=0; j<7; j++){
            panelWeekday.add(lblWeekdays[j]);
        }
        for (int j=0; j<42; j++){
            panelWeekday.add(dayLabels[j]);
        }

//-----------------panelLeft----------------------------------------------------
        //JLabel lblMonthYear = new JLabel("March 2014", JLabel.LEFT);
        lblMonthYear = new JLabel(thisDate.getMonth()+" "+thisDate.getYear(), JLabel.LEFT);
        lblMonthYear.setPreferredSize(new Dimension(350, 50));
        //lblMonthYear.setBorder(new LineBorder(Color.cyan));
        panelMonthYear.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelMonthYear.add(lblMonthYear);
        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
        panelLeft.add(panelMonthYear);
        panelLeft.add(panelWeekday);
        //----------------------------------------------------------------------------
        panelBottom.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelBottom.add(panelLeft);
        panelBottom.add(panelEvent);


        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        add(panelButtons);
        add(panelBottom);
        pack();


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * To receive the notify form data model, and then repaint.
     * @param e - ChangeEvent.
     */
    @Override
    public void stateChanged(ChangeEvent e) {         //View of MVC
        datas = dataModel.getDatas();
        for(int i=0; i<42; i++)
            shapes[i].setNumber(datas.get(i));
        //tPnlEvent.setEnabled(false);                // The text panel cannot be written.
//-----------------------------------------set JTextPane-----------------------------------------------------
        try {
            calendar = new MyCalendar("events_modified.txt");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        onetimeEvents = calendar.getOnetimeEvents();
        recurringEvents = calendar.getRecurringEvents();
       // allEvents = calendar.getAllEvents();
        datas=dataModel.getDatas();
        allEvents=dataModel.getAllEvents();
        //--------------------------------------------------
        LocalDate g_Date = thisDate;
        ArrayList<OnetimeEvent> tempEvents_g = new ArrayList<OnetimeEvent>();
        String strEvents;
        for(OnetimeEvent a: allEvents){                                     //find out all events in this day and put them in ArrayList<> tempEvents.
            if(a.getDate().isEqual(g_Date)){
                tempEvents_g.add(a);
            }
        }
        if(tempEvents_g.size()>=1){
            strEvents=(g_Date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + ", "+
                    g_Date.getMonth() + " " + g_Date.getDayOfMonth() + ", "+ g_Date.getYear())+"\n";
            for(OnetimeEvent t: tempEvents_g){
                strEvents=strEvents+(t.getName()+": "+t.getTimeInterval().getStartingTime()+" - "+t.getTimeInterval().getEndingTime())+"\n";
            }
        }
        else{
            strEvents = (g_Date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + ", "+
                    g_Date.getMonth() + " " + g_Date.getDayOfMonth() + ", "+ g_Date.getYear())+"\n";
            strEvents = strEvents+("No event in this day.")+"\n";
        }
        tPnlEvent.setText(strEvents);
//-----------------------------------------------------------------------------------------------------------
        repaint();                                  //for GUI
    }
}
