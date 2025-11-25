// Save as: 08_AssignExamFrame.java
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;

public class AssignExamFrame extends JFrame {

    private JComboBox<String> cmbExaminee, cmbExam;
    private HashMap<String, Integer> examineeMap = new HashMap<>();
    private HashMap<String, Integer> examMap = new HashMap<>();

    public AssignExamFrame() {
        setTitle("Assign Exam to Examinee");
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
        cmbExaminee = new JComboBox<>();
        cmbExam = new JComboBox<>();
        JButton btnAssign = new JButton("Assign");

        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(lblExaminee, gbc);
        gbc.gridx = 1; panel.add(cmbExaminee, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(lblExam, gbc);
        gbc.gridx = 1; panel.add(cmbExam, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(btnAssign, gbc);

        btnAssign.addActionListener(e -> assignExam());

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
                String display = id + " - " + name;
                examineeMap.put(display, id);
                cmbExaminee.addItem(display);
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
                String display = id + " - " + name;
                examMap.put(display, id);
                cmbExam.addItem(display);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading exams: " + ex.getMessage());
        }
    }

    private void assignExam() {
        String exKey = (String) cmbExaminee.getSelectedItem();
        String examKey = (String) cmbExam.getSelectedItem();

        if (exKey == null || examKey == null) {
            JOptionPane.showMessageDialog(this, "Select both examinee and exam.");
            return;
        }

        int examineeId = examineeMap.get(exKey);
        int examId = examMap.get(examKey);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO assigned_exam (examinee_id, exam_id) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, examineeId);
            ps.setInt(2, examId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Exam assigned successfully.");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Assign failed: " + ex.getMessage());
        }
    }
}


// PAGE 8