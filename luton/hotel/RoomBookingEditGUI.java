package hotel;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RoomBookingEditGUI extends JFrame {
    private int bookingId = 0;
    private final JTextField nameField;
    private final JTextField emailField;
    private final JTextField phoneField;
    private final JSpinner checkinSpinner;
    private final JSpinner checkoutSpinner;
    private final JComboBox<String> roomTypeComboBox;
    private final JSpinner guestCountSpinner;
    private final JTextArea specialRequestsArea;

    public RoomBookingEditGUI() {
        this.bookingId = bookingId;
        setTitle("Edit Room Booking");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add title label
        JLabel titleLabel;
        titleLabel = new JLabel("Edit Room Booking");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Add title image
        ImageIcon icon = new ImageIcon("C:\\Users\\Santosh\\OneDrive\\Pictures\\pexels-pixabay-164595 (1).jpg");
        titleLabel = new JLabel("", icon, JLabel.CENTER);
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

        nameField = new JTextField();
        constraints.gridx = 1;
        constraints.gridy = 0;
        formPanel.add(nameField, constraints);

        JLabel emailLabel = new JLabel("Email:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        formPanel.add(emailLabel, constraints);

        emailField = new JTextField();
        constraints.gridx = 1;
        constraints.gridy = 1;
        formPanel.add(emailField, constraints);

        JLabel phoneLabel = new JLabel("Phone:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        formPanel.add(phoneLabel, constraints);

        phoneField = new JTextField();
        constraints.gridx = 1;
        constraints.gridy = 2;
        formPanel.add(phoneField, constraints);

        JLabel checkinLabel = new JLabel("Check-in date:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        formPanel.add(checkinLabel, constraints);

        checkinSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor checkinEditor = new JSpinner.DateEditor(checkinSpinner, "dd/MM/yyyy");
        checkinSpinner.setEditor(checkinEditor);
        constraints.gridx = 1;
        constraints.gridy = 3;
        formPanel.add(checkinSpinner, constraints);

        JLabel checkoutLabel = new JLabel("Check-out date:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        formPanel.add(checkoutLabel, constraints);

        checkoutSpinner = new JSpinner(new SpinnerDateModel());
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
        roomTypeComboBox = new JComboBox<>(roomTypes);
        constraints.gridx = 1;
        constraints.gridy = 5;
        formPanel.add(roomTypeComboBox, constraints);
        JLabel guestCountLabel = new JLabel("Number of guests:");
        constraints.gridx = 0;
        constraints.gridy = 6;
        formPanel.add(guestCountLabel, constraints);

        guestCountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        constraints.gridx = 1;
        constraints.gridy = 6;
        formPanel.add(guestCountSpinner, constraints);

        JLabel specialRequestsLabel = new JLabel("Special requests:");
        constraints.gridx = 0;
        constraints.gridy = 7;
        formPanel.add(specialRequestsLabel, constraints);

        specialRequestsArea = new JTextArea(5, 20);
        JScrollPane specialRequestsScrollPane = new JScrollPane(specialRequestsArea);
        constraints.gridx = 1;
        constraints.gridy = 7;
        formPanel.add(specialRequestsScrollPane, constraints);

        // Add the form to the frame
        add(formPanel, BorderLayout.CENTER);

        // Add buttons to the frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveBooking());
        buttonPanel.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Load the existing booking data
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM reservations WHERE id = " + bookingId);

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                emailField.setText(rs.getString("email"));
                phoneField.setText(rs.getString("phone"));

                Date checkinDate = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("checkin_date"));
                checkinSpinner.setValue(checkinDate);

                Date checkoutDate = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("checkout_date"));
                checkoutSpinner.setValue(checkoutDate);

                String roomType = rs.getString("room_type");
                switch (roomType) {
                    case "Single":
                        roomTypeComboBox.setSelectedIndex(0);
                        break;
                    case "Suite":
                        roomTypeComboBox.setSelectedIndex(1);
                        break;
                    case "Twin":
                        roomTypeComboBox.setSelectedIndex(2);
                        break;
                }

                guestCountSpinner.setValue(rs.getInt("guest_count"));

                specialRequestsArea.setText(rs.getString("special_requests"));
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

        // Display the frame
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void saveBooking() {
        // Get the form data
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        Date checkinDate = (Date) checkinSpinner.getValue();
        Date checkoutDate = (Date) checkoutSpinner.getValue();
        String roomType = (String) roomTypeComboBox.getSelectedItem();
        int guestCount = (int) guestCountSpinner.getValue();
        String specialRequests = specialRequestsArea.getText();

        // Validate the form data
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a phone number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (checkinDate == null) {
            JOptionPane.showMessageDialog(this, "Please enter a check-in date.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (checkoutDate == null) {
            JOptionPane.showMessageDialog(this, "Please enter a check-out date.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (checkoutDate.before(checkinDate)) {
            JOptionPane.showMessageDialog(this, "Check-out date must be after check-in date.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update the booking in the database
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "");
            PreparedStatement stmt = conn.prepareStatement("UPDATE reservations SET name=?, email=?, phone=?, checkin=?, checkout=?, room_type=?, guest_count=?, special_requests=? WHERE id=?");
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setDate(4, new java.sql.Date(checkinDate.getTime()));
            stmt.setDate(5, new java.sql.Date(checkoutDate.getTime()));
            stmt.setString(6, roomType);
            stmt.setInt(7, guestCount);
            stmt.setString(8, specialRequests);
            stmt.setInt(9, bookingId);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Booking updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update booking.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Show the edit booking form for booking ID 1
        new RoomBookingEditGUI();
    }
}
