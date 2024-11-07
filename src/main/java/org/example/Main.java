package org.example;

import com.opencsv.CSVReader;
import org.example.model.Student;
import org.example.model.Theme;
import org.example.parser.Parser;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        String path = "src\\main\\java\\org\\example\\srcfiles\\basicprogramming_2.csv";
        //for(Student s:Parser.readStudents(path)){
        //    System.out.println(s);
        //}
        for(Theme s:Parser.readThemes(path)){
            System.out.println(s);
        }
        System.out.println("end");
    }
}