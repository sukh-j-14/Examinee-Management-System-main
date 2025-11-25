// Save as: 01_LoginFrame.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.JTextComponent;
import java.sql.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    // Turn on debug to print key/focus events to console
    private static final boolean DEBUG_UI = true;

    public LoginFrame() {
        setTitle("Login - Examinee Management System");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initUI();

        // Ensure the username text field gets focus when the window appears
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                txtUsername.requestFocusInWindow();
            }
        });
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblUser = new JLabel("Username:");
        JLabel lblPass = new JLabel("Password:");
        // Make the fields larger so they're easier to click/type into
        txtUsername = new JTextField(25);
        txtUsername.setColumns(25);
        txtUsername.setPreferredSize(new Dimension(300, 28));

        txtPassword = new JPasswordField(25);
        txtPassword.setColumns(25);
        txtPassword.setPreferredSize(new Dimension(300, 28));
        JButton btnLogin = new JButton("Login");

        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(lblUser, gbc);
        gbc.gridx = 1; panel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(lblPass, gbc);
        gbc.gridx = 1; panel.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> loginUser());

        // Make sure fields are editable and the Login button is the default button
        // Ensure the fields are editable and focusable
        txtUsername.setEditable(true);
        txtUsername.setFocusable(true);
        txtPassword.setEditable(true);
        txtPassword.setFocusable(true);
        // Ensure clicking on fields requests focus (sometimes focus problems occur in some environments)
        txtUsername.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txtUsername.requestFocusInWindow();
            }
        });
        txtPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txtPassword.requestFocusInWindow();
            }
        });

        // Make caret and text visible and add debug listeners so we can observe
        // whether keypresses and focus events reach these components.
        txtUsername.setForeground(Color.BLACK);
        txtUsername.setCaretColor(Color.BLACK);
        txtPassword.setForeground(Color.BLACK);
        txtPassword.setCaretColor(Color.BLACK);

        if (DEBUG_UI) {
            txtUsername.addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) { System.out.println("[DEBUG] txtUsername gained focus"); }
                @Override public void focusLost(FocusEvent e) { System.out.println("[DEBUG] txtUsername lost focus"); }
            });
            txtPassword.addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) { System.out.println("[DEBUG] txtPassword gained focus"); }
                @Override public void focusLost(FocusEvent e) { System.out.println("[DEBUG] txtPassword lost focus"); }
            });

            KeyAdapter debugKeyListener = new KeyAdapter() {
                @Override public void keyTyped(KeyEvent e) {
                    System.out.println("[DEBUG] keyTyped -> char='" + e.getKeyChar() + "' (code=" + (int)e.getKeyChar() + ") on " + e.getSource().getClass().getSimpleName());
                }
                @Override public void keyPressed(KeyEvent e) {
                    System.out.println("[DEBUG] keyPressed -> code=" + e.getKeyCode() + " on " + e.getSource().getClass().getSimpleName());
                }
                @Override public void keyReleased(KeyEvent e) {
                    // show caret/content
                    JTextComponent src = (JTextComponent) e.getSource();
                    System.out.println("[DEBUG] keyReleased -> text='" + src.getText() + "' caret=" + src.getCaretPosition());
                }
            };

            txtUsername.addKeyListener(debugKeyListener);
            txtPassword.addKeyListener(debugKeyListener);
        }
        getRootPane().setDefaultButton(btnLogin);

        add(panel);
    }

    private void loginUser() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter username and password!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                new DashboardFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
        }
    }
}

// PAGE 1