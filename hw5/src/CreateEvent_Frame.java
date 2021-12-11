import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * To show a frame for user to input event-name, start-time, and end-time to create a schedule.
 */
public class CreateEvent_Frame extends JFrame{

    private DataModel dataModel;
    private ArrayList<Integer> datas;
    private ArrayList<OnetimeEvent> allEvents;
    private LocalDate date;

    /**
     * To construct a CreateEvent Frame.
     * @param dataModel
     * @param date
     * @throws IOException
     */
    public CreateEvent_Frame(DataModel dataModel, LocalDate date) throws IOException {
        this.dataModel = dataModel;
        datas = dataModel.getDatas();
        allEvents = dataModel.getAllEvents();
        this.date = date;
        JTextField field_Event = new JTextField(58);
        field_Event.setText("Input event here.");

        JTextField field_Date = new JTextField(8);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        field_Date.setText(date.format(formatter));
        field_Date.setEditable(false);


        JTextField field_StartTime = new JTextField(8);
        field_StartTime.setText("HH:MM");
        JLabel lblTo = new JLabel("To");
        JTextField field_EndTime = new JTextField(8);
        field_EndTime.setText("HH:MM");
        JButton btnSave =new JButton("SAVE");
        setLayout( new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        JPanel panelUp = new JPanel();
        JPanel panelDown = new JPanel();
        panelDown.setLayout(new FlowLayout(FlowLayout.CENTER, 62, 20));

        panelUp.add(field_Event);
        add(panelUp);

        panelDown.add(field_Date);
        panelDown.add(field_StartTime);
        panelDown.add(lblTo);
        panelDown.add(field_EndTime);
        panelDown.add(btnSave);
        add(panelDown);
        pack();


        field_StartTime.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                field_StartTime.setText("");
            }
        });

        field_EndTime.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                field_EndTime.setText("");
            }
        });

        field_Event.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                field_Event.setText("");
            }
        });


        btnSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String c_Name = field_Event.getText();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate c_Date = LocalDate.parse(field_Date.getText(), formatter);
                LocalTime c_StartingTime = LocalTime.parse(field_StartTime.getText());
                LocalTime c_EndingTime = LocalTime.parse(field_EndTime.getText());
                OnetimeEvent newEvent = new OnetimeEvent(c_Name, c_Date, new TimeInterval(c_StartingTime, c_EndingTime));
                try {
                    dataModel.addOneEvent(newEvent);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                    setVisible(false);
            }
        });
    }

}
