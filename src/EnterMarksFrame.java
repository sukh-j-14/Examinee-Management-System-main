// Save as: 09_EnterMarksFrame.java
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;

public class EnterMarksFrame extends JFrame {

    private JComboBox<String> cmbExaminee, cmbExam;
    private JTextField txtMarks;
    private HashMap<String, Integer> examineeMap = new HashMap<>();
    private HashMap<String, Integer> examMap = new HashMap<>();

    public EnterMarksFrame() {
        setTitle("Enter Marks");
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        loadExaminees();
        loadExams();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblExaminee = new JLabel("Examinee:");
        JLabel lblExam = new JLabel("Exam:");
        JLabel lblMarks = new JLabel("Marks:");

        cmbExaminee = new JComboBox<>();
        cmbExam = new JComboBox<>();
        txtMarks = new JTextField(5);

        JButton btnSave = new JButton("Save");

        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(lblExaminee, gbc);
        gbc.gridx = 1; panel.add(cmbExaminee, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(lblExam, gbc);
        gbc.gridx = 1; panel.add(cmbExam, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(lblMarks, gbc);
        gbc.gridx = 1; panel.add(txtMarks, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(btnSave, gbc);

        btnSave.addActionListener(e -> saveMarks());

        add(panel);
    }

    private void loadExaminees() {
        examineeMap.clear();
        cmbExaminee.removeAllItems();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id, name FROM examinee";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String key = id + " - " + name;
                examineeMap.put(key, id);
                cmbExaminee.addItem(key);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading examinees: " + ex.getMessage());
        }
    }

    private void loadExams() {
        examMap.clear();
        cmbExam.removeAllItems();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id, exam_name FROM exam";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("exam_name");
                String key = id + " - " + name;
                examMap.put(key, id);
                cmbExam.addItem(key);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading exams: " + ex.getMessage());
        }
    }

    private void saveMarks() {
        String exKey = (String) cmbExaminee.getSelectedItem();
        String examKey = (String) cmbExam.getSelectedItem();
        String marksStr = txtMarks.getText().trim();

        if (exKey == null || examKey == null || marksStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all details.");
            return;
        }

        int examineeId = examineeMap.get(exKey);
        int examId = examMap.get(examKey);
        int marks;

        try {
            marks = Integer.parseInt(marksStr);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Marks must be a number.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO result (examinee_id, exam_id, marks) VALUES (?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, examineeId);
            ps.setInt(2, examId);
            ps.setInt(3, marks);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Marks saved.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving marks: " + ex.getMessage());
        }
    }
}

// PAGE 9