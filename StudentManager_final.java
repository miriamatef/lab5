/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package lab5;

import java.util.List;

/**
 *
 * @author karen
 */
public interface StudentManager {
    void addStudent(Student student) throws Exception;
    List<Student> searchStudents(String query); // query can be ID or name
    boolean updateStudent(int id, String name, int age, String gender, String department, float gpa)
            throws Exception;
    boolean deleteStudent(int id) 
            throws Exception;
    List<Student> viewAllStudents();
}