/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab5;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
//helps manage the data shown in the table
import java.awt.*;
//used for layouts and spacing
import java.awt.event.ActionEvent;
//for button click
import java.util.List;
//for students list

/**
 *
 * @author Miriam
 */

public class SearchStudentPanel extends JPanel {
    private final StudentManager studentManager;
    private final JTextField searchField = new JTextField(20); //box where the user types the student name or id
    private final JButton searchButton = new JButton("Search"); //search button
    private final DefaultTableModel tableModel; //holds data shown inside JTable
    private final JTable table; //displays results in rows and columns

    public SearchStudentPanel(StudentManager manager) {
        this.studentManager = manager;
        setLayout(new BorderLayout(8,8));
        //divides panel into regions (North, Center, South, etc.), with 8-pixel gaps.
        add(buildSearchPanel(), BorderLayout.NORTH);
        //adds the search bard to the top part

        //Creating a table model with column names: ID,Name,Age, ....
        tableModel = new DefaultTableModel(new Object[]{"ID","Name","Age","Gender","Department","GPA"}, 0) {
            @Override 
            public boolean isCellEditable(int row, int column) { 
                return false; }
            //the user can't edit cells directly
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
        //Wraps the table inside a JScrollPane (so it scrolls if many results appear) and adds it to the center of the layout.
        initActions(); //call to make the button work
    }

    private JPanel buildSearchPanel() { //build the top search bar
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //adds a label explaining what to type
        p.add(new JLabel("Search by ID or Name:"));
        p.add(searchField);
        p.add(searchButton);
        return p;
    }

    private void initActions() {
        searchButton.addActionListener((ActionEvent e) -> doSearch());
        searchField.addActionListener(e -> doSearch());
        //when the user click the button or when he presses Enter inside text box, it runs doSearch()
    }

    private void doSearch() {
        String query = searchField.getText().trim(); //read what the user typed
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter an ID or a name to search.", "Validation", JOptionPane.WARNING_MESSAGE);
            return; //if it's empty shows a message and stop
        }

        List<Student> results = studentManager.searchStudents(query);
        populateTable(results);
        if (results == null || results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No matching students found.", "Search", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void populateTable(List<Student> students) {
        tableModel.setRowCount(0); //clear old results
        if (students == null) return;
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                s.getID(),
                s.getName(),
                s.getAge(),
                s.getGender(),
                s.getDepartment(),
                s.getGpa()
            });
        } //loops through each student in the result list
        //it adds new row with that student's data
    }
}
