
package lab5;

import javax.swing.*; //this imports all Swing classes from javax.swing package

import java.awt.*; //AWT (Abstract Window Toolkit) handles layout, colors, and positioning — Swing is built on top of AWT.
/*java.awt.* imports:Color, Font,
Layout managers like GridLayout, BorderLayout, GridBagLayout
Insets, etc.*/

import java.awt.event.ActionEvent; //It represents something that happens — like a button click.

/**
 *
 * @author Miriam
 */

public class AddStudentPanel extends JPanel {
    
    private final StudentManager studentManager; //A reference to your backend logic (the class that actually adds/saves students).
    //Each JTextField in the following lines is a box where the user can type text.
    //The number (like 15 or 6) means the field’s preferred width
    private final JTextField idField = new JTextField(15);
    private final JTextField nameField = new JTextField(15);
    private final JTextField ageField = new JTextField(6);
    private final JComboBox<String> genderCombo = new JComboBox<>(new String[] {"Male", "Female"}); //JComboBox<String> creates a dropdown menu.
    private final JTextField deptField = new JTextField(12);
    private final JTextField gpaField = new JTextField(6);
    private final JButton addButton = new JButton("Add Student"); //creates a clickable button with that label

    public AddStudentPanel(StudentManager manager) {
        this.studentManager = manager;
        setLayout(new BorderLayout()); //The panel uses a BorderLayout — a layout that divides space into: NORTH, SOUTH, EAST, WEST, and CENTER.
        add(buildForm(), BorderLayout.CENTER); //Adds the main input form (labels and text fields) in the center.
        add(buildButtonPanel(), BorderLayout.SOUTH); //Adds the button bar at the bottom.
        initActions(); //connects the button with its click action.
    }

    private JPanel buildForm() { //this builds the form layout
        JPanel p = new JPanel(new GridBagLayout()); 
        //creates a panel with a flexible grid layout
        GridBagConstraints c = new GridBagConstraints(); 
        //how each label field is positioned, tells where to place each component
        c.insets = new Insets(6,6,6,6); //adds spaces around each component
        c.anchor = GridBagConstraints.WEST; //align everything to the left

        int y = 0;
        //Adds a label in column 0, and its matching input in column 1,then moves to the next row (y++).
        c.gridx = 0; c.gridy = y; p.add(new JLabel("Student ID:"), c);
        c.gridx = 1; p.add(idField, c); y++;
        //x=0: first column->label
        //x=1: second colomn->text field
        //y++ ->move to the next row

        c.gridx = 0; c.gridy = y; p.add(new JLabel("Full Name:"), c);
        c.gridx = 1; p.add(nameField, c); y++;

        c.gridx = 0; c.gridy = y; p.add(new JLabel("Age:"), c);
        c.gridx = 1; p.add(ageField, c); y++;

        c.gridx = 0; c.gridy = y; p.add(new JLabel("Gender:"), c);
        c.gridx = 1; p.add(genderCombo, c); y++;

        c.gridx = 0; c.gridy = y; p.add(new JLabel("Department:"), c);
        c.gridx = 1; p.add(deptField, c); y++;

        c.gridx = 0; c.gridy = y; p.add(new JLabel("GPA:"), c);
        c.gridx = 1; p.add(gpaField, c); y++;

        return p;
    }

    private JPanel buildButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        //places components next to each other, aligned to the right
        p.add(addButton);
        //adds "Add student" button to it
        return p;
    }

    private void initActions() {
        addButton.addActionListener((ActionEvent e) -> onAdd());
        //When the addButton is clicked, call the method onAdd()
    }

    //  Generates a random unique ID that doesn’t already exist
private int generateUniqueRandomId() {
    int id;
    do {
        id = (int) (Math.random() * 9000) + 1000; // random number between 1000–9999
    } while (isIdUsed(id));
    return id;
}

// Checks if a specific ID already exists in the student list
private boolean isIdUsed(int id) {
    // we’ll search through the studentManager’s existing students
    for (Student s : studentManager.searchStudents("")) { // empty string returns all students
        if (s.getId() == id) {
            return true;
        }
    }
    return false;
}
    private void onAdd() {
        //reading what the user has typed 
        String idText = idField.getText().trim();
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String gender = (String) genderCombo.getSelectedItem();
        String dept = deptField.getText().trim();
        String gpaText = gpaField.getText().trim();

        //Validation:
        if (name.isEmpty() || ageText.isEmpty() || dept.isEmpty() || gpaText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //generate or read ID
        int id;
    if (idText.isEmpty()) {
        id = generateUniqueRandomId(); // generate a random unique one if left blank
    } else {
        try {
            id = Integer.parseInt(idText);
            if (id < 0) throw new NumberFormatException();
            // check if the entered ID already exists
            if (isIdUsed(id)) {
                JOptionPane.showMessageDialog(this, "This ID already exists! Please choose another.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID must be a non-negative integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age < 0 || age > 150) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a valid integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double gpa;
        try {
            gpa = Double.parseDouble(gpaText);
            if (gpa < 0.0 || gpa > 4.0) {
                JOptionPane.showMessageDialog(this, "GPA must be between 0.0 and 4.0.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "GPA must be a number (e.g., 3.5).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student s = new Student(id, name, age, gender, dept, gpa);

        try {
            studentManager.addStudent(s);
            JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (Exception ex) {
            // Backend should throw exceptions for duplicate id or IO errors
            JOptionPane.showMessageDialog(this, "Failed to add student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() { 
//After successfully adding a student, this resets the fields to empty.
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        genderCombo.setSelectedIndex(0);
        deptField.setText("");
        gpaField.setText("");
    }
}


