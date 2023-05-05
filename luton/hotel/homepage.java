package hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;

public class homepage extends JFrame implements ActionListener {

    JButton loginButton, registerButton, viewRoomButton, viewBarButton;
    JLabel hotelName, hotelDesc, hotelImg, contactInfo, dateTimeLabel;
    JPanel panel;

    public homepage() {

        // set up the JFrame
        setTitle("Hotel Booking System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create a JPanel to hold the components
        panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        // create the hotel name label
        hotelName = new JLabel("Hotel Luton");
        hotelName.setBounds(50, 30, 100, 30);
        panel.add(hotelName);

        // create the hotel description label
        hotelDesc = new JLabel("<html>Welcome to Hotel Luton! We are located in the heart of the Luton and offer comfortable rooms and great amenities.</html>");
        hotelDesc.setBounds(50, 70, 500, 60);
        panel.add(hotelDesc);

        // create the hotel image label
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\Santosh\\OneDrive\\Desktop\\hotel\\Waldorf-Astoria-Amsterdam-768x569.jpg");
        hotelImg = new JLabel(imageIcon);
        hotelImg.setBounds(400, 200, 800, 600);
        panel.add(hotelImg);

        // create the login button
        loginButton = new JButton("Login");
        loginButton.setBounds(50, 150, 100, 30);
        loginButton.addActionListener(this);
        panel.add(loginButton);

        // create the register button
        registerButton = new JButton("Register");
        registerButton.setBounds(50, 200, 100, 30);
        registerButton.addActionListener(this);
        panel.add(registerButton);

        // create the contact information label
        contactInfo = new JLabel("<html>Contact us: <br/> Phone: 123-456-7890 <br/> Email: info@hotelluton.com <br/> Address: 123 Main Street, Luton, UK </html>");
        contactInfo.setBounds(50, 850, 500, 60);
        panel.add(contactInfo);

        // create the date and time label
        dateTimeLabel = new JLabel();
        dateTimeLabel.setBounds(400, 30, 200, 30);
        panel.add(dateTimeLabel);

        // update the date and time label every second using a Timer
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                dateTimeLabel.setText(dateTimeString);
            }
        });
        timer.start();

        // display the JFrame
        setVisible(true);
        // set the background color of the JFrame
        panel.setBackground(new Color(173, 216, 230)); // light blue color
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            // open the login page
            LoginChecker loginPage = new LoginChecker();
            loginPage.setVisible(true);
            dispose(); // destroy the homepage JFrame
        } else if (e.getSource() == registerButton) {
            // open the registration page
            RegistrationGUI registrationPage = new RegistrationGUI();
            registrationPage.setVisible(true);
            dispose(); // destroy the homepage JFrame
        } else if (e.getSource() == viewRoomButton) {
            // handle view room button click
        } else if (e.getSource() == viewBarButton) {
            // handle view bar button click
        }
    }

    public static void main(String[] args) {
        new homepage();
    }

}
