// Save as: 03_AddExamineeFrame.java
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddExamineeFrame extends JFrame {

    private JTextField txtName, txtAge, txtEmail, txtCourse;
    private JComboBox<String> cmbGender;

    public AddExamineeFrame() {
        setTitle("Add Examinee");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblName = new JLabel("Name:");
        JLabel lblAge = new JLabel("Age:");
        JLabel lblGender = new JLabel("Gender:");
        JLabel lblEmail = new JLabel("Email:");
        JLabel lblCourse = new JLabel("Course:");

        txtName = new JTextField(15);
        txtAge = new JTextField(5);
        txtEmail = new JTextField(15);
        txtCourse = new JTextField(15);
        cmbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});

        JButton btnSave = new JButton("Save");

        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(lblName, gbc);
        gbc.gridx = 1; panel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(lblAge, gbc);
        gbc.gridx = 1; panel.add(txtAge, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(lblGender, gbc);
        gbc.gridx = 1; panel.add(cmbGender, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(lblEmail, gbc);
        gbc.gridx = 1; panel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(lblCourse, gbc);
        gbc.gridx = 1; panel.add(txtCourse, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(btnSave, gbc);

        btnSave.addActionListener(e -> saveExaminee());

        add(panel);
    }

    private void saveExaminee() {
        String name = txtName.getText().trim();
        String ageStr = txtAge.getText().trim();
        String gender = (String) cmbGender.getSelectedItem();
        String email = txtEmail.getText().trim();
        String course = txtCourse.getText().trim();

        if (name.isEmpty() || ageStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Age are required.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO examinee (name, age, gender, email, course) VALUES (?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, Integer.parseInt(ageStr));
            ps.setString(3, gender);
            ps.setString(4, email);
            ps.setString(5, course);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Examinee added successfully.");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving: " + ex.getMessage());
        }
    }
}

// PAGE 3