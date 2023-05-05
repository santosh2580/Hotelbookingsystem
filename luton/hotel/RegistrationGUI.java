package hotel;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegistrationGUI extends JFrame {

    // GUI Components
    private JLabel nameLabel = new JLabel("Name:");
    private JTextField nameField = new JTextField(15);
    private JLabel emailLabel = new JLabel("Email:");
    private JTextField emailField = new JTextField(20);
    private JLabel passwordLabel = new JLabel("Password:");
    private JPasswordField passwordField = new JPasswordField(20);
    private JLabel genderLabel = new JLabel("Gender:");
    private JComboBox<String> genderComboBox = new JComboBox<>(new String[] {"Male", "Female", "Other"});
    private JLabel phoneLabel = new JLabel("Phone Number:");
    private JTextField phoneField = new JTextField(20);
    private JLabel addressLabel = new JLabel("Address:");
    private JTextArea addressArea = new JTextArea(3, 20);
    private JButton registerButton = new JButton("Register");
    private JButton loginButton = new JButton("Login");

    // Database Connection Components
    private String url = "jdbc:mysql://localhost:3306/user";
    private String user = "root";
    private String pass = "";

    public RegistrationGUI() {
        // Set up the JFrame
        setTitle("Registration Page");
        setSize(400, 500);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        // Add components to JFrame
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBackground(new Color(239, 245, 252)); // Set background color
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 82, 155), 2, true), "Registration Form",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", Font.BOLD, 14), new Color(0, 82, 155))));
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(genderLabel);
        formPanel.add(genderComboBox);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);
        formPanel.add(addressLabel);
        formPanel.add(new JScrollPane(addressArea));
        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        buttonPanel.setBackground(new Color(239, 245, 252)); // Set background color
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        loginButton.setBackground(new Color(0, 82, 155)); // Set button color
        loginButton.setForeground(Color.WHITE); // Set font color
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Set font
        loginButton.setText("Login"); // Set button text
        buttonPanel.add(loginButton);
        registerButton.setBackground(new Color(0, 82, 155)); // Set button color
        registerButton.setForeground(Color.WHITE); // Set font color
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Set font
        registerButton.setText("Register"); // Set button text
        buttonPanel.add(registerButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners to buttons
        loginButton.addActionListener(e -> {
            new LoginChecker();
            dispose();
        });

        registerButton.addActionListener(e -> {
            // Get values from fields
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String gender = (String) genderComboBox.getSelectedItem();
            String phone = phoneField.getText();
            String address = addressArea.getText();

                    // Validation
                    if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please fill out all required fields.");
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
            // Insert data into database
            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (name, email, password, gender, phone, address) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, password);
                stmt.setString(4, gender);
                stmt.setString(5, phone);
                stmt.setString(6, address);
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Registration successful!");
                    // Clear fields
                    nameField.setText("");
                    emailField.setText("");
                    passwordField.setText("");
                    genderComboBox.setSelectedIndex(0);
                    phoneField.setText("");
                    addressArea.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Registration failed.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error connecting to database: " + ex.getMessage());
            }
        });

        // Make JFrame visible
        setVisible(true);
    }

    public static void main(String[] args) {
        new RegistrationGUI();
    }
}
