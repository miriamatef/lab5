/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package lab5;

/**
 *
 * @author karen
 */
import java.io.Serializable;
//da interface by2ol en ay object mn el class da can be written and read mn file

public class Student implements Serializable {
    private int id;
    private String name;
    private int age;
    private String gender;
    private String department;
    private float gpa;
    
//constructor
    public Student(int id, String name, int age, String gender, String department, double gpa) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.department = department;
        this.gpa = (float) gpa;
    }

    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public float getGpa() { return gpa; }
    public void setGpa(float gpa) { this.gpa = gpa; }

    @Override
    public String toString() {
        return id + "," + name + "," + age + "," + gender + "," + department + "," + gpa;
    }
}
