// Save as: 02_DashboardFrame.java
import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        setTitle("Dashboard - Examinee Management System");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton addExaminee = new JButton("Add Examinee");
        JButton viewExaminees = new JButton("View Examinees");
        JButton addExam = new JButton("Add Exam");
        JButton viewExams = new JButton("View Exams");
        JButton assignExam = new JButton("Assign Exam");
        JButton enterMarks = new JButton("Enter Marks");
        JButton viewResults = new JButton("View Results");
        JButton logout = new JButton("Logout");

        panel.add(addExaminee);
        panel.add(viewExaminees);
        panel.add(addExam);
        panel.add(viewExams);
        panel.add(assignExam);
        panel.add(enterMarks);
        panel.add(viewResults);
        panel.add(logout);

        add(panel);

        addExaminee.addActionListener(e -> new AddExamineeFrame().setVisible(true));
        viewExaminees.addActionListener(e -> new ViewExamineesFrame().setVisible(true));
        addExam.addActionListener(e -> new AddExamFrame().setVisible(true));
        viewExams.addActionListener(e -> new ViewExamsFrame().setVisible(true));
        assignExam.addActionListener(e -> new AssignExamFrame().setVisible(true));
        enterMarks.addActionListener(e -> new EnterMarksFrame().setVisible(true));
        viewResults.addActionListener(e -> new ViewResultsFrame().setVisible(true));

        logout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }
}

// PAGE 2