package hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class RoomBookingGUI extends JFrame {
    public RoomBookingGUI() {
        setTitle("Room Booking Hotel Luton");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add title image
        ImageIcon icon = new ImageIcon("C:\\Users\\Santosh\\Downloads\\7N8A1143.jpg");
        JLabel titleLabel = new JLabel("", icon, JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Create the form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        // Add fields to the form
        JLabel nameLabel = new JLabel("Name:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        formPanel.add(nameLabel, constraints);

        JTextField nameField = new JTextField();
        constraints.gridx = 1;
        constraints.gridy = 0;
        formPanel.add(nameField, constraints);

        JLabel emailLabel = new JLabel("Email:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        formPanel.add(emailLabel, constraints);

        JTextField emailField = new JTextField();
        constraints.gridx = 1;
        constraints.gridy = 1;
        formPanel.add(emailField, constraints);

        JLabel phoneLabel = new JLabel("Phone:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        formPanel.add(phoneLabel, constraints);

        JTextField phoneField = new JTextField();
        constraints.gridx = 1;
        constraints.gridy = 2;
        formPanel.add(phoneField, constraints);

        JLabel checkinLabel = new JLabel("Check-in date:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        formPanel.add(checkinLabel, constraints);

        JSpinner checkinSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor checkinEditor = new JSpinner.DateEditor(checkinSpinner, "dd/MM/yyyy");
        checkinSpinner.setEditor(checkinEditor);
        constraints.gridx = 1;
        constraints.gridy = 3;
        formPanel.add(checkinSpinner, constraints);

        JLabel checkoutLabel = new JLabel("Check-out date:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        formPanel.add(checkoutLabel, constraints);

        JSpinner checkoutSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor checkoutEditor = new JSpinner.DateEditor(checkoutSpinner, "dd/MM/yyyy");
        checkoutSpinner.setEditor(checkoutEditor);
        constraints.gridx = 1;
        constraints.gridy = 4;
        formPanel.add(checkoutSpinner, constraints);

        JLabel roomTypeLabel = new JLabel("Room type:");
        constraints.gridx = 0;
        constraints.gridy = 5;
        formPanel.add(roomTypeLabel, constraints);

        String[] roomTypes = {"Single", "Suite", "Twin"};
        JComboBox<String> roomTypeComboBox = new JComboBox<>(roomTypes);
        constraints.gridx = 1;
        constraints.gridy = 5;
        formPanel.add(roomTypeComboBox, constraints);

        JLabel guestCountLabel = new JLabel("Number of guests:");
        constraints.gridx = 0;
        constraints.gridy = 6;
        formPanel.add(guestCountLabel, constraints);

        JSpinner guestCountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        constraints.gridx = 1;
        constraints.gridy = 6;
        formPanel.add(guestCountSpinner, constraints);

        JLabel specialRequestsLabel = new JLabel("Special requests:");
        constraints.gridx = 0;
        constraints.gridy = 7;
        formPanel.add(specialRequestsLabel, constraints);

        JTextArea specialRequestsArea = new JTextArea(4, 20);
        JScrollPane specialRequestsScrollPane = new JScrollPane(specialRequestsArea);
        constraints.gridx = 1;
        constraints.gridy = 7;
        formPanel.add(specialRequestsScrollPane, constraints);

        // Add confirm button
        JButton confirmButton = new JButton("Confirm");
        constraints.gridx = 0;
        constraints.gridy = 8;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        formPanel.add(confirmButton, constraints);

        // Add cancel button
        JButton cancelButton = new JButton("Cancel");
        constraints.gridx = 2;
        constraints.gridy = 8;
        formPanel.add(cancelButton, constraints);

        cancelButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this booking?", "Cancel Booking", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                dispose(); // destroy the window
                JOptionPane.showMessageDialog(this, "Booking canceled.", "Booking Canceled", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        // Add edit button
        JButton editButton = new JButton("Edit");
        constraints.gridx = 1;
        constraints.gridy = 8;
        formPanel.add(editButton, constraints);
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create an instance of the RoomBookingEditGUI window
                RoomBookingEditGUI editWindow = new RoomBookingEditGUI();
                editWindow.setSize(800, 900); // Set the size to 800x600 pixels
                // Make the window visible
                editWindow.setVisible(true);
                // Destroy the current window
                dispose();
            }
        });
        // Add view details button
        JButton viewDetailsButton = new JButton("View Details");
        constraints.gridx = 3;
        constraints.gridwidth = 8;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        formPanel.add(viewDetailsButton, constraints);
        // ActionListener for view details button
        viewDetailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the values from the form fields
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                Date checkinDate = (Date) checkinSpinner.getValue();
                Date checkoutDate = (Date) checkoutSpinner.getValue();
                String roomType = (String) roomTypeComboBox.getSelectedItem();
                int guestCount = (int) guestCountSpinner.getValue();
                String specialRequests = specialRequestsArea.getText();

                // Calculate the total based on the room type and number of guests
                double roomRate = 0.0;
                switch (roomType) {
                    case "Single":
                        roomRate = 1500.0;
                        break;
                    case "Double":
                        roomRate = 2500.0;
                        break;
                    case "Suite":
                        roomRate = 3000.0;
                        break;
                }
                double total = roomRate * guestCount;

                // Create a string to display the booking details
                String details = "Name: " + name + "\n"
                        + "Email: " + email + "\n"
                        + "Phone: " + phone + "\n"
                        + "Check-in date: " + checkinDate.toString() + "\n"
                        + "Check-out date: " + checkoutDate.toString() + "\n"
                        + "Room type: " + roomType + "\n"
                        + "Number of guests: " + guestCount + "\n"
                        + "Special requests: " + specialRequests + "\n"
                        + "Total: $" + String.format("%.2f", total);

                // Show the details in a JOptionPane
                JOptionPane.showMessageDialog(RoomBookingGUI.this, details, "Booking Details", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // ActionListener for confirm button
        confirmButton.addActionListener(e -> {
            // Get the input values
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            Date checkin = (Date) checkinSpinner.getValue();
            Date checkout = (Date) checkoutSpinner.getValue();
            String roomType = (String) roomTypeComboBox.getSelectedItem();
            int guestCount = (int) guestCountSpinner.getValue();
            String specialRequests = specialRequestsArea.getText();

            // Validate input values
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Incomplete Form", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!email.matches("\\S+@\\S+\\.\\S+")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
                return;
            }
            if (!phone.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid 10-digit phone number.");
                return;
            }
            // Get the current date
            Date presentDate = new Date();

            // Check if check-in date is valid (not in the past)
            if (checkin.before(presentDate)) {
                JOptionPane.showMessageDialog(this, "Check-in date must be present or a future date.", "Invalid Date", JOptionPane.WARNING_MESSAGE);
                return;
            }

           // Check if check-out date is valid (after check-in date)
            if (checkout.before(new Date(checkin.getTime() + 86400000))) {
                JOptionPane.showMessageDialog(this, "Check-out date must be at least one day after check-in date.", "Invalid Date", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Calculate the number of nights
            long diff = checkout.getTime() - checkin.getTime();
            int nights = (int) (diff / (24 * 60 * 60 * 1000));

            // Calculate the total price
            int basePrice;
            switch (roomType) {
                case "Single":
                    basePrice = 50;
                    break;
                case "Suite":
                    basePrice = 100;
                    break;
                case "Twin":
                    basePrice = 75;
                    break;
                default:
                    basePrice = 0;
            }
            int totalPrice = basePrice * nights * guestCount;
            // Confirm the booking with the user
            String message = String.format("Please confirm your booking:\n\nName: %s\nEmail: %s\nPhone: %s\nCheck-in: %s\nCheck-out: %s\nRoom type: %s\nGuests: %d\nSpecial requests: %s\n\nTotal price: %d GBP", name, email, phone, checkin, checkout, roomType, guestCount, specialRequests, totalPrice);
            int result = JOptionPane.showConfirmDialog(this, message, "Confirm Booking", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                // Save the booking to the database
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "")) {
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO reservations (name, email, phone, checkin, checkout, room_type, guest_count, special_requests, total) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    stmt.setString(1, name);
                    stmt.setString(2, email);
                    stmt.setString(3, phone);
                    stmt.setDate(4, new java.sql.Date(checkin.getTime()));
                    stmt.setDate(5, new java.sql.Date(checkout.getTime()));
                    stmt.setString(6, roomType);
                    stmt.setInt(7, guestCount);
                    stmt.setString(8, specialRequests);
                    stmt.setInt(9, totalPrice);
                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Booking confirmed.", "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
                        dispose(); // destroy the window
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to save booking to database.", "Database Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to connect to database.", "Database Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        // Add the form to the window
        add(formPanel, BorderLayout.CENTER);

        // Display the window
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        RoomBookingGUI gui = new RoomBookingGUI();
    }
}
