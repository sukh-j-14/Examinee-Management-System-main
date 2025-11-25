// Save as: 05_EditExamineeFrame.java
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EditExamineeFrame extends JFrame {

    private int examineeId;
    private JTextField txtName, txtAge, txtEmail, txtCourse;
    private JComboBox<String> cmbGender;

    public EditExamineeFrame(int id) {
        this.examineeId = id;
        setTitle("Edit Examinee - ID: " + id);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        loadExaminee();
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

        JButton btnSave = new JButton("Update");

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

        btnSave.addActionListener(e -> updateExaminee());

        add(panel);
    }

    private void loadExaminee() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT name, age, gender, email, course FROM examinee WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, examineeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtName.setText(rs.getString("name"));
                txtAge.setText(String.valueOf(rs.getInt("age")));
                cmbGender.setSelectedItem(rs.getString("gender"));
                txtEmail.setText(rs.getString("email"));
                txtCourse.setText(rs.getString("course"));
            } else {
                JOptionPane.showMessageDialog(this, "Examinee not found!");
                dispose();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading: " + ex.getMessage());
        }
    }

    private void updateExaminee() {
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
            String sql = "UPDATE examinee SET name=?, age=?, gender=?, email=?, course=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, Integer.parseInt(ageStr));
            ps.setString(3, gender);
            ps.setString(4, email);
            ps.setString(5, course);
            ps.setInt(6, examineeId);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Examinee updated successfully.");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Update failed: " + ex.getMessage());
        }
    }
}


// PAGE 5