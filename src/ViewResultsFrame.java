// Save as: 10_ViewResultsFrame.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewResultsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ViewResultsFrame() {
        setTitle("View Results");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        loadResults();
    }

    private void initUI() {
        model = new DefaultTableModel(
                new Object[]{"Result ID", "Examinee", "Exam", "Marks", "Status"}, 0
        );
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        add(scroll, BorderLayout.CENTER);
    }

    private void loadResults() {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql =
                    "SELECT r.id, e.name AS examinee_name, ex.exam_name, r.marks, ex.total_marks " +
                    "FROM result r " +
                    "JOIN examinee e ON r.examinee_id = e.id " +
                    "JOIN exam ex ON r.exam_id = ex.id";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int marks = rs.getInt("marks");
                int total = rs.getInt("total_marks");
                String status = (marks >= total * 0.4) ? "PASS" : "FAIL"; // 40% pass criteria
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("examinee_name"),
                        rs.getString("exam_name"),
                        marks + "/" + total,
                        status
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading results: " + ex.getMessage());
        }
    }
}

// PAGE 10