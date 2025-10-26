/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab5;

/**
 *
 * @author karen
 */
import java.io.*;
import java.util.*;

public class StudentManagerImpl implements StudentManager {

    private List<Student> students = new ArrayList<>();

    public StudentManagerImpl() {
        loadFromFile(); // load students if file exists
    }

    // Add student with validation
    @Override
    public void addStudent(Student student) throws Exception {
        if (student == null) {
            throw new Exception("Student object cannot be null");
        }

        // Validate required fields
        validateStudentData(student.getName(), student.getAge(), student.getGender(), student.getDepartment(), student.getGpa());

        // Check for duplicate ID
        for (Student s : students) {
            if (s.getId() == student.getId()) {
                throw new Exception("Student ID already exists: " + student.getId());
            }
        }

        students.add(student);
        saveToFile();
        System.out.println("Student added: " + student.getName());
    }

    //  Search student by ID or name
    @Override
    public List<Student> searchStudents(String query) {
        List<Student> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) return results;

        query = query.trim();

        for (Student s : students) {
            if (String.valueOf(s.getId()).equals(query) ||
                s.getName().equalsIgnoreCase(query)) {
                results.add(s);
            }
        }
        return results;
    }

    //  Update student details
    @Override
    public boolean updateStudent(int id, String name, int age, String gender, String department, float gpa) throws Exception {
        validateStudentData(name, age, gender, department, gpa);

        for (Student s : students) {
            if (s.getId() == id) {
                s.setName(name);
                s.setAge(age);
                s.setGender(gender);
                s.setDepartment(department);
                s.setGpa(gpa);
                saveToFile();
                System.out.println(" Student updated: " + s.getName());
                return true;
            }
        }

        throw new Exception(" Student with ID " + id + " not found.");
    }

    //  Delete student by ID (with confirmation check)
    @Override
    public boolean deleteStudent(int id) throws Exception {
        Iterator<Student> iterator = students.iterator();

        while (iterator.hasNext()) {
            Student s = iterator.next();
            if (s.getId() == id) {
                iterator.remove();
                saveToFile();
                System.out.println(" Student deleted: " + s.getName());
                return true;
            }
        }

        throw new Exception("Student with ID " + id + " not found.");
    }

    //  Validation method for data
    private void validateStudentData(String name, int age, String gender, String department, float gpa) throws Exception {
        if (name == null || name.trim().isEmpty()) throw new Exception("Name cannot be empty.");
        if (department == null || department.trim().isEmpty()) throw new Exception("Department cannot be empty.");
        if (age <= 0) throw new Exception("Age must be greater than 0.");
        if (!(gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female")))
            throw new Exception("Gender must be 'Male' or 'Female'.");
        if (gpa < 0.0 || gpa > 4.0) throw new Exception("GPA must be between 0.0 and 4.0.");
    }

    //  Save to file
    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("students.txt"))) {
            for (Student s : students) {
                writer.println(s.getId() + "," + s.getName() + "," + s.getAge() + "," +
                               s.getGender() + "," + s.getDepartment() + "," + s.getGpa());
            }
        } catch (IOException e) {
            System.err.println(" Error saving file: " + e.getMessage());
        }
    }

    //  Load from file
    private void loadFromFile() {
        File file = new File("students.txt");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    int age = Integer.parseInt(parts[2]);
                    String gender = parts[3];
                    String department = parts[4];
                    float gpa = Float.parseFloat(parts[5]);
                    students.add(new Student(id, name, age, gender, department, gpa));
                }
            }
        } catch (IOException e) {
            System.err.println("⚠ Error loading file: " + e.getMessage());
        }
    }

    //  view all students 
   @Override
    public List<Student> viewAllStudents() {
        // Return a copy of the list to protect original data
        return new ArrayList<>(students);
    }
}