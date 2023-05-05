package hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class HotelRoomViewer extends JFrame implements ActionListener {
    private JLabel roomTypeLabel, priceLabel, facilitiesLabel, imageLabel; // add imageLabel
    private JComboBox<String> roomTypeComboBox;
    private JTextField priceField;
    private JCheckBox tvCheckBox, wifiCheckBox, acCheckBox, telephoneCheckBox;
    private JButton confirmButton;
    private Connection con;

    public HotelRoomViewer() {
        super("Hotel Room Viewer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // disable frame resizing
        setLayout(new BorderLayout()); // Use BorderLayout instead of GridLayout

        // Initialize GUI components
        roomTypeLabel = new JLabel("Room Type:");
        priceLabel = new JLabel("Price:");
        facilitiesLabel = new JLabel("Facilities:");
        roomTypeComboBox = new JComboBox<String>(new String[]{"Single", "Twin", "Suite"});
        roomTypeComboBox.addActionListener(this);

        // Set default prices for room types
        int defaultPrice = 0;
        switch (roomTypeComboBox.getSelectedItem().toString()) {
            case "Single":
                defaultPrice = 1500;
                break;
            case "Twin":
                defaultPrice = 2500;
                break;
            case "Suite":
                defaultPrice = 300;
                break;
        }
        priceField = new JTextField(Integer.toString(defaultPrice));

        tvCheckBox = new JCheckBox("TV");
        wifiCheckBox = new JCheckBox("WiFi");
        acCheckBox = new JCheckBox("AC");
        telephoneCheckBox = new JCheckBox("Telephone");
        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(this);

        // Add GUI components to the JFrame
        JPanel centerPanel = new JPanel(new GridLayout(6, 2));
        centerPanel.add(roomTypeLabel);
        centerPanel.add(roomTypeComboBox);
        centerPanel.add(priceLabel);
        centerPanel.add(priceField);
        centerPanel.add(facilitiesLabel);
        centerPanel.add(new JLabel());
        centerPanel.add(tvCheckBox);
        centerPanel.add(wifiCheckBox);
        centerPanel.add(acCheckBox);
        centerPanel.add(telephoneCheckBox);
        centerPanel.add(confirmButton);
        add(centerPanel, BorderLayout.CENTER);

        // Add image to the north panel
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\Santosh\\OneDrive\\Desktop\\cheriton-w800h600.jpg");
        Image image = imageIcon.getImage().getScaledInstance(800, 300, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);
        imageLabel = new JLabel(imageIcon);
        JPanel northPanel = new JPanel();
        northPanel.add(imageLabel);
        add(northPanel, BorderLayout.NORTH);

        // Connect to database
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add WindowListener to close database connection when window is closed
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    con.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == confirmButton) {
            // Check if connection is established
            if (con == null) {
                JOptionPane.showMessageDialog(null, "Database connection not established.");
                return;
            }

            // Get input values
            String roomType = (String) roomTypeComboBox.getSelectedItem();
            int price = Integer.parseInt(priceField.getText());
            boolean tv = tvCheckBox.isSelected();
            boolean wifi = wifiCheckBox.isSelected();
            boolean ac = acCheckBox.isSelected();
            boolean telephone = telephoneCheckBox.isSelected();

            // Validate room type selection
            if (roomType == null || roomType.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select a room type.");
                return;
            }

            // Validate facilities selection
            if (!tv && !wifi && !ac && !telephone) {
                JOptionPane.showMessageDialog(null, "Please select at least one facility.");
                return;
            }

            // Insert values into database
            try {
                if (con != null) {
                    PreparedStatement ps = con.prepareStatement("INSERT INTO rooms (room_type, price, tv, wifi, ac, telephone) VALUES (?, ?, ?, ?, ?, ?)");
                    ps.setString(1, roomType);
                    ps.setInt(2, price);
                    ps.setBoolean(3, tv);
                    ps.setBoolean(4, wifi);
                    ps.setBoolean(5, ac);
                    ps.setBoolean(6, telephone);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Room details saved successfully.");

                    // Destroy the window
                    dispose();
                    // Open RoomBookingGUI
                    RoomBookingGUI roomBookingGUI = new RoomBookingGUI();
                    roomBookingGUI.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Database connection not established.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == roomTypeComboBox) {
            // Update price field based on selected room type
            int defaultPrice = 100;
            switch (roomTypeComboBox.getSelectedItem().toString()) {
                case "Single":
                    defaultPrice = 1500;
                    break;
                case "Twin":
                    defaultPrice = 2500;
                    break;
                case "Suite":
                    defaultPrice = 3000;
                    break;
            }
            priceField.setText(Integer.toString(defaultPrice));
        }
    }

    public static void main(String[] args) {
        HotelRoomViewer viewer = new HotelRoomViewer();
        viewer.setVisible(true);
    }
}