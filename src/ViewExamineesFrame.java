// Save as: 04_ViewExamineesFrame.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class ViewExamineesFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ViewExamineesFrame() {
        setTitle("View Examinees");
        setSize(750, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        loadData();
    }

    private void initUI() {
        model = new DefaultTableModel(
                new Object[]{"ID", "Name", "Age", "Gender", "Email", "Course"}, 0
        );

        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        JButton btnDelete = new JButton("Delete");
        JButton btnEdit = new JButton("Edit");

        JPanel bottom = new JPanel();
        bottom.add(btnEdit);
        bottom.add(btnDelete);

        add(scroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        btnDelete.addActionListener(e -> deleteRow());
        btnEdit.addActionListener(e -> editRow());
    }

    private void loadData() {
        model.setRowCount(0);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM examinee";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("email"),
                        rs.getString("course")
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading: " + ex.getMessage());
        }
    }

    private void deleteRow() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this, "Delete Examinee ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM examinee WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Deleted successfully.");
            loadData();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Delete error: " + ex.getMessage());
        }
    }

    private void editRow() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to edit.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        EditExamineeFrame editFrame = new EditExamineeFrame(id);
        editFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                loadData(); // refresh after edit window closes
            }
        });
        editFrame.setVisible(true);
    }
}

// PAGE 4