package hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class HotelBookingEditPage extends JFrame implements ActionListener {
    // GUI components
    private JTextField nameField, emailField, phoneField, roomTypeField, guestCountField;
    private JSpinner checkinSpinner, checkoutSpinner;
    private JButton saveButton, backButton;
    private JLabel nameLabel, emailLabel, phoneLabel, checkinLabel, checkoutLabel, roomTypeLabel, guestCountLabel;
    // Database connection variables
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    // Constructor
    public HotelBookingEditPage() {
        // Set window properties
        setTitle("Hotel Booking Edit Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);

        // Create labels and fields
        nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);
        emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField(20);
        checkinLabel = new JLabel("Check-in Date:");
        checkinSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor checkinEditor = new JSpinner.DateEditor(checkinSpinner, "dd/MM/yyyy");
        checkinSpinner.setEditor(checkinEditor);
        checkoutLabel = new JLabel("Check-out Date:");
        checkoutSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor checkoutEditor = new JSpinner.DateEditor(checkoutSpinner, "dd/MM/yyyy");
        checkoutSpinner.setEditor(checkoutEditor);
        roomTypeLabel = new JLabel("Room Type:");
        roomTypeField = new JTextField(20);
        guestCountLabel = new JLabel("Guest Count:");
        guestCountField = new JTextField(20);

        // Create buttons
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        backButton = new JButton("Back");
        backButton.addActionListener(this);

        // Add components to the content pane
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(8, 2));
        contentPane.add(nameLabel);
        contentPane.add(nameField);
        contentPane.add(emailLabel);
        contentPane.add(emailField);
        contentPane.add(phoneLabel);
        contentPane.add(phoneField);
        contentPane.add(checkinLabel);
        contentPane.add(checkinSpinner);
        contentPane.add(checkoutLabel);
        contentPane.add(checkoutSpinner);
        contentPane.add(roomTypeLabel);
        contentPane.add(roomTypeField);
        contentPane.add(guestCountLabel);
        contentPane.add(guestCountField);
        contentPane.add(saveButton);
        contentPane.add(backButton);

        // Connect to the database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "");
            statement = connection.createStatement();
            System.out.println("Connected to database");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ActionListener method
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            // Call a function to save the edited booking
            displayEditedBooking();
        } else if (e.getSource() == backButton) {
            // Go back to the previous page
            dispose();
        }
    }
    // Function to display the edited booking
    public void displayEditedBooking() {
        try {
            // Prepare SQL query to update the reservation table with the edited data
            String updateQuery = "UPDATE reservation SET guest_name=?, email=?, phone=?, room_type=?, check_in_date=?, check_out_date=?, guest_count=? WHERE id=?";
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, nameField.getText());
            preparedStatement.setString(2, emailField.getText());
            preparedStatement.setString(3, phoneField.getText());
            preparedStatement.setString(4, roomTypeField.getText());
            preparedStatement.setDate(5, new java.sql.Date(((java.util.Date)checkinSpinner.getValue()).getTime()));
            preparedStatement.setDate(6, new java.sql.Date(((java.util.Date)checkoutSpinner.getValue()).getTime()));
            preparedStatement.setInt(7, Integer.parseInt(guestCountField.getText()));

            // Execute the query
            int rowsAffected = preparedStatement.executeUpdate();

            // Check if the update was successful
            if (rowsAffected > 0) {
                // Display a message dialog to notify that the booking has been updated
                JOptionPane.showMessageDialog(this, "Booking has been updated.", "Booking Updated", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update booking.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        HotelBookingEditPage page = new HotelBookingEditPage();
        page.setVisible(true);
    }
}