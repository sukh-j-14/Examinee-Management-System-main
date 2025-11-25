// Save as: 07_ViewExamsFrame.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewExamsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ViewExamsFrame() {
        setTitle("View Exams");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        loadExams();
    }

    private void initUI() {
        model = new DefaultTableModel(
                new Object[]{"ID", "Exam Name", "Date", "Duration", "Total Marks"}, 0
        );

        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        JButton btnDelete = new JButton("Delete Selected");

        JPanel bottom = new JPanel();
        bottom.add(btnDelete);

        add(scroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        btnDelete.addActionListener(e -> deleteExam());
    }

    private void loadExams() {
        model.setRowCount(0);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM exam";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("exam_name"),
                        rs.getDate("exam_date"),
                        rs.getInt("duration"),
                        rs.getInt("total_marks")
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading exams: " + ex.getMessage());
        }
    }

    private void deleteExam() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an exam to delete.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete Exam ID " + id + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM exam WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Exam deleted.");
            loadExams();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Delete failed: " + ex.getMessage());
        }
    }
}

// PAGE 7