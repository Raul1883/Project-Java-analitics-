package org.example;



import org.example.dataBase.Database;
import org.example.model.Student;
import org.example.parser.Parser;
import org.example.viev.MainApplication;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        MainApplication app = new MainApplication();
        //save(path);
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

    public static List<Student> get() {
        List<Student> students;

        try (Database db = new Database()) {
            db.connect();
            students = db.getData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return students;
    }
}