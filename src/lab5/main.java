package lab5;
import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Create backend manager
                StudentManager manager = new StudentManagerImpl();

                //  Optional: preload some students
                try {
                    manager.addStudent(new Student(1, "Alice Johnson", 20, "Female", "CS", 3.5f));
                    manager.addStudent(new Student(2, "Bob Smith", 22, "Male", "IT", 3.2f));
                    manager.addStudent(new Student(3, "Carol White", 19, "Female", "Math", 3.9f));
                } catch (Exception e) {
                    System.out.println("⚠ Some sample students might already exist: " + e.getMessage());
                }

                // Show login first
                LoginFrame login = new LoginFrame(manager);
                login.setVisible(true);

            } catch (Exception ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}