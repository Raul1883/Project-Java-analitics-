package org.example;


import org.example.dataBase.Database;
import org.example.model.Student;
import org.example.parser.Parser;


import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {

        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        String path = "src\\main\\java\\org\\example\\srcfiles\\basicprogramming_2.csv";

        save(path);
        get();


    }

    public static void save(String path) {
        ArrayList<Student> students = Parser.readStudents(path);
        try (Database db = new Database()) {
            db.connect();
            db.saveData(students);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void get() {
        List<Student> students;

        try (Database db = new Database()) {
            db.connect();
            students = db.getData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (Student s : students)
            System.out.println(s);
    }
}