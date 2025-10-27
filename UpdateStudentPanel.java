package lab5;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UpdateStudentPanel extends JPanel {
    private JTextField idField, nameField, ageField, gpaField, departmentField;
    private JComboBox<String> genderBox;
    private JButton loadButton, updateButton;
    private StudentManager manager;

    public UpdateStudentPanel(StudentManager manager) {
        this.manager = manager;
        setLayout(new GridLayout(8, 2, 10, 10));

        add(new JLabel("Student ID:"));
        idField = new JTextField();
        add(idField);

        loadButton = new JButton("Load Student");
        add(new JLabel()); // empty cell
        add(loadButton);

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Age:"));
        ageField = new JTextField();
        add(ageField);

        add(new JLabel("Gender:"));
        genderBox = new JComboBox<>(new String[]{"Male", "Female"});
        add(genderBox);

        add(new JLabel("Department:"));
        departmentField = new JTextField();
        add(departmentField);

        add(new JLabel("GPA:"));
        gpaField = new JTextField();
        add(gpaField);

        updateButton = new JButton("Update Student");
        add(new JLabel()); // empty cell
        add(updateButton);

        // Button actions
        loadButton.addActionListener(e -> loadStudent());
        updateButton.addActionListener(e -> updateStudent());
    }

    private void loadStudent() {
        String idText = idField.getText().trim();
        try {
            int id = Integer.parseInt(idText);
            Student s = manager.searchStudents(idText).stream().findFirst().orElse(null);
            if (s == null) {
                JOptionPane.showMessageDialog(this, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            nameField.setText(s.getName());
            ageField.setText(String.valueOf(s.getAge()));
            genderBox.setSelectedItem(s.getGender());
            departmentField.setText(s.getDepartment());
            gpaField.setText(String.valueOf(s.getGpa()));

            JOptionPane.showMessageDialog(this, "Student loaded successfully!", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudent() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String gender = (String) genderBox.getSelectedItem();
            String dept = departmentField.getText().trim();
            float gpa = Float.parseFloat(gpaField.getText().trim());

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to update this student?", "Confirm Update", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            boolean success = manager.updateStudent(id, name, age, gender, dept, gpa);
            if (success) {
                JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Update failed. Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Called by ViewStudentsPanel when a row is clicked
    public void loadStudentById(int id) {
        Student s = manager.searchStudents(String.valueOf(id)).stream().findFirst().orElse(null);
        if (s != null) {
            fillForm(s.getId(), s.getName(), s.getAge(), s.getGender(), s.getDepartment(), s.getGpa());
        }
    }

    public void fillForm(int id, String name, int age, String gender, String dept, float gpa) {
        idField.setText(String.valueOf(id));
        nameField.setText(name);
        ageField.setText(String.valueOf(age));
        genderBox.setSelectedItem(gender);
        departmentField.setText(dept);
        gpaField.setText(String.valueOf(gpa));
    }
}
