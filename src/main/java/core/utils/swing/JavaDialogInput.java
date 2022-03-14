package core.utils.swing;

import core.utils.mail.MailUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static core.utils.timeout.TimeoutUtils.sleep;

public class JavaDialogInput {

//    //FOR DEBUGGING PURPOSES
//    public static void main(String[] args) {
//        int maxWaitTimeInSeconds = 33;
//        String message = String.format("Please input one time password value\n\nIn order to get OTP value you can use Email:\n*login = %s\n*password = %s", MailUtils.user, MailUtils.password);
//        String message2 = "Please enter some value";
//        String userValue = askUserToInputValue(message, maxWaitTimeInSeconds);
//        System.out.println("userValue = " + userValue);
//    }

    public static String askUserToInputValue(String dialogMessage, int maxWaitTimeInSeconds) {
        final String[] result = {null};
        long endTime = System.currentTimeMillis() + maxWaitTimeInSeconds * 1000;
        String timerFormat = "<html>This message will self-destruct in %s seconds</html>"; //JTextArea

        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JTextArea textArea = new JTextArea();
        JTextField jTextField = new JTextField();
        JButton button = new JButton( "OK");
        JLabel timerLabel = new JLabel(String.format(timerFormat, maxWaitTimeInSeconds));

        textArea.setFont(new Font("DigifaceWide Regular", Font.PLAIN, 20));
        textArea.setText(dialogMessage + "\n");
        textArea.setEditable(false);

        jTextField.setHorizontalAlignment(JTextField.CENTER);
        jTextField.setFont(new Font("DigifaceWide Regular", Font.PLAIN, 20));
        jTextField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    button.doClick();
                }
            }
        });

        button.addActionListener( e2->{
            result[0] = jTextField.getText();
            System.out.println("value = " + result[0]);
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        } );

        timerLabel.setFont(new Font("DigifaceWide Regular", Font.PLAIN, 20));
        Timer timer = new Timer( 500, new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                long timeLeftInSeconds = (endTime - System.currentTimeMillis()) / 1000;
                timerLabel.setText(String.format(timerFormat, timeLeftInSeconds));
            }
        } );
        timer.setRepeats( true );
        timer.start();


        panel.setLayout((new BoxLayout(panel, BoxLayout.Y_AXIS)));
        panel.add(textArea);
        panel.add(jTextField);
        panel.add(new JLabel(" ")); //empty line
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(button);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(timerLabel, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible( true );
        frame.setAlwaysOnTop(true);
        frame.toFront();
        frame.requestFocus();
        frame.repaint();


        while (frame.isShowing() && System.currentTimeMillis() < endTime){
            sleep(1);
        }
        if(frame.isShowing()) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
        timer.stop();

        return result[0];
    }
}
