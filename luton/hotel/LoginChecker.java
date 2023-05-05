package hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginChecker extends JFrame implements ActionListener {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel titleLabel, emailLabel, passLabel, imageLabel;
    private JButton loginButton, signUpButton;

    public LoginChecker() {
        // set the window properties
        setTitle("Login Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(255, 255, 204)); // set background color

        // create the subtitle label
        JLabel subtitleLabel = new JLabel("Please enter your email and password to login", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(subtitleLabel, BorderLayout.SOUTH);

        // create the email input fields
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        emailLabel = new JLabel("Email: ");
        inputPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        emailField = new JTextField(20);
        emailField.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // add border
        inputPanel.add(emailField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        passLabel = new JLabel("Password: ");
        inputPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordField = new JPasswordField(20);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // add border
        inputPanel.add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(new JLabel(" "), gbc); // add space for aesthetics

        // create the login button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        inputPanel.add(loginButton, gbc); // add login button

        // create the sign up button
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        signUpButton = new JButton("Sign up");
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // open the registration page
                dispose(); // close the login page
                new RegistrationGUI(); // open the registration page
            }
        });
        inputPanel.add(signUpButton, gbc); // add sign up button

        add(inputPanel, BorderLayout.CENTER);

        // create the title label
        titleLabel = new JLabel("Welcome to Hotel Luton", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Load the image
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\Santosh\\Downloads\\Untitled design (1).png");

        // Create a label to display the image
        imageLabel = new JLabel(imageIcon);
        add(imageLabel, BorderLayout.EAST);

        // Show the window
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        String url = "jdbc:mysql://localhost:3306/user";
        String user = "root";
        String password = "";
        String userEmail = emailField.getText();
        String userPassword = new String(passwordField.getPassword());

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?");
            statement.setString(1, userEmail);
            statement.setString(2, userPassword);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // email and password match a record in the login table
                JOptionPane.showMessageDialog(null, "Login successful!"); // display pop-up message
                HotelRoomViewer roomViewer = new HotelRoomViewer();
                roomViewer.setVisible(true);
                dispose(); // close the login frame
            } else {
                // email and password do not match a record in the login table
                JOptionPane.showMessageDialog(null, "Invalid email or password!");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error connecting to database: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginChecker());
    }
}
