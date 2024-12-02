package org.example;

import org.example.model.Task;
import org.example.model.Theme;
import org.example.parser.Parser;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {

        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        String path = "src\\main\\java\\org\\example\\srcfiles\\basicprogramming_2.csv";


        for (Theme s : Parser.readThemes(path)) {
            System.out.println(s);
        }
        System.out.println("\n");
        for (Task t : Parser.tasks) {
            if (t.quality() > 1)
                System.out.println("!!!" + t);
            else{
                System.out.println(t);
            }
        }
        System.out.println("end");

    }
}