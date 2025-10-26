package lab5;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewStudentsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private JButton refreshButton, searchButton, deleteButton;
    private JComboBox<String> gpaFilterBox; 
    private StudentManager manager;

    public ViewStudentsPanel(StudentManager manager, UpdateStudentPanel updatePanel) {
        this.manager = manager;
        setLayout(new BorderLayout(10, 10));

        //GPA filter options
        String[] gpaFilters = {"All Students", "GPA ≥ 3.0", "GPA < 2.0", "Sort by GPA Descending"};
        gpaFilterBox = new JComboBox<>(gpaFilters);

        //Table setup
        String[] columns = {"ID", "Name", "Age", "Gender", "Department", "GPA"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setAutoCreateRowSorter(true); // enables sorting
        add(new JScrollPane(table), BorderLayout.CENTER);

        //Top panel with search + GPA filter
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(15);
        searchButton = new JButton("Search");

        topPanel.add(new JLabel("Search by ID or Name:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(new JLabel(" | Filter:"));
        topPanel.add(gpaFilterBox);

        add(topPanel, BorderLayout.NORTH);

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshButton = new JButton("Refresh");
        deleteButton = new JButton("Delete Selected");
        bottomPanel.add(refreshButton);
        bottomPanel.add(deleteButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load initial data
        loadStudents();

        // Row click loads student into update form
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int selectedRow = table.getSelectedRow();
                int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
                updatePanel.loadStudentById(id);
            }
        });

        // Refresh button
        refreshButton.addActionListener(e -> loadStudents());

        // Search button
        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search query.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            List<Student> results = manager.searchStudents(query);
            model.setRowCount(0);
            for (Student s : results) {
                model.addRow(new Object[]{s.getId(), s.getName(), s.getAge(), s.getGender(), s.getDepartment(), s.getGpa()});
            }
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No matching students found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Delete button
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a student to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete student with ID " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    manager.deleteStudent(id);
                    loadStudents();
                    JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //GPA filter dropdown listener
        gpaFilterBox.addActionListener(e -> applyGpaFilter((String) gpaFilterBox.getSelectedItem()));
    }

    private void loadStudents() {
        model.setRowCount(0); // clear old data
        List<Student> students = manager.viewAllStudents();
        for (Student s : students) {
            model.addRow(new Object[]{
                s.getId(), s.getName(), s.getAge(), s.getGender(), s.getDepartment(), s.getGpa()
            });
        }
    }

    // GPA filter
    private void applyGpaFilter(String filter) {
        List<Student> students = manager.viewAllStudents();
        model.setRowCount(0);

        switch (filter) {
            case "GPA ≥ 3.0":
                students.removeIf(s -> s.getGpa() < 3.0);
                break;
            case "GPA < 2.0":
                students.removeIf(s -> s.getGpa() >= 2.0);
                break;
            case "Sort by GPA Descending":
                students.sort((a, b) -> Float.compare(b.getGpa(), a.getGpa()));
                break;
            default:
                // Show all students
                break;
        }

        for (Student s : students) {
            model.addRow(new Object[]{
                s.getId(), s.getName(), s.getAge(), s.getGender(), s.getDepartment(), s.getGpa()
            });
        }
    }
}