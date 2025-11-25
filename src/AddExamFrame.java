// Save as: 06_AddExamFrame.java
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddExamFrame extends JFrame {

    private JTextField txtName, txtDate, txtDuration, txtTotalMarks;

    public AddExamFrame() {
        setTitle("Add Exam");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblName = new JLabel("Exam Name:");
        JLabel lblDate = new JLabel("Exam Date (YYYY-MM-DD):");
        JLabel lblDuration = new JLabel("Duration (minutes):");
        JLabel lblTotal = new JLabel("Total Marks:");

        txtName = new JTextField(15);
        txtDate = new JTextField(10);
        txtDuration = new JTextField(5);
        txtTotalMarks = new JTextField(5);

        JButton btnSave = new JButton("Save");

        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(lblName, gbc);
        gbc.gridx = 1; panel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(lblDate, gbc);
        gbc.gridx = 1; panel.add(txtDate, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(lblDuration, gbc);
        gbc.gridx = 1; panel.add(txtDuration, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(lblTotal, gbc);
        gbc.gridx = 1; panel.add(txtTotalMarks, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(btnSave, gbc);

        btnSave.addActionListener(e -> saveExam());

        add(panel);
    }

    private void saveExam() {
        String name = txtName.getText().trim();
        String date = txtDate.getText().trim();
        String durationStr = txtDuration.getText().trim();
        String totalStr = txtTotalMarks.getText().trim();

        if (name.isEmpty() || date.isEmpty() || durationStr.isEmpty() || totalStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO exam (exam_name, exam_date, duration, total_marks) VALUES (?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, date);   // expects YYYY-MM-DD (MySQL will parse)
            ps.setInt(3, Integer.parseInt(durationStr));
            ps.setInt(4, Integer.parseInt(totalStr));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Exam added successfully.");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding exam: " + ex.getMessage());
        }
    }
}

// PAGE 6