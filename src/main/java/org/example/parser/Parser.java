package org.example.parser;

import com.opencsv.CSVReader;
import org.example.model.*;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.util.Objects;

public class Parser {

    public static ArrayList<Theme> readThemes(String path) {
        try (CSVReader reader = new CSVReader(new FileReader(path, StandardCharsets.UTF_8), ';')) {
            ArrayList<Theme> themes = new ArrayList<>();

            String[] themeNames = reader.readNext();
            String[] headings = reader.readNext();
            String[] fullPoints = reader.readNext();

            ArrayList<int[]> themesRanges = getThemesIndex(themeNames);
            for (int[] indexPair : themesRanges) {
                ArrayList<Task> themeTasks = new ArrayList<>();

                String themeName = themeNames[indexPair[0]];
                for (int i = indexPair[0]; i < indexPair[1]; i++) {
                    Task themeTask = getTask(themeName, headings[i], fullPoints[i]);
                    if (!Objects.isNull(themeTask))
                        themeTasks.add(themeTask);

                }
                themes.add(new Theme(themeName, themeTasks));
                themeTasks.clear();
            }
            return themes;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения из файла");
        }
    }


    public static ArrayList<Student> readStudents(String path) {
        try (CSVReader reader = new CSVReader(new FileReader(path, StandardCharsets.UTF_8), ';')) {

            ArrayList<Student> students = new ArrayList<>();
            String[] nextLine;

            String[] themeNames = reader.readNext();
            String[] headings = reader.readNext();
            String[] fullPoints = reader.readNext();

            ArrayList<int[]> themesRanges = getThemesIndex(themeNames);
            while ((nextLine = reader.readNext()) != null) {
                students.add(createStudent(nextLine, themeNames, headings, fullPoints, themesRanges));
            }

            return students;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения из файла");
        }
    }

    private static Student createStudent(String[] line, String[] themeNames,
                                         String[] headings, String[] fullPoints,
                                         ArrayList<int[]> themesRanges) {


        return new Student(line[0], line[1],
                getStudentThemes(line, themeNames, headings, fullPoints, themesRanges));
    }

    private static ArrayList<StudentTheme> getStudentThemes(String[] line, String[] themeNames,
                                                            String[] headings, String[] fullPoints,
                                                            ArrayList<int[]> themesRanges) {
        ArrayList<StudentTheme> studentThemes = new ArrayList<>();
        for (int[] indexPair : themesRanges) {
            ArrayList<Task> themeTasks = new ArrayList<>();
            ArrayList<Task> studentTasks = new ArrayList<>();

            String themeName = themeNames[indexPair[0]];
            for (int i = indexPair[0]; i < indexPair[1]; i++) {
                Task themeTask = getTask(themeName, headings[i], fullPoints[i]);
                Task studentTask = getTask(themeName, headings[i], line[i]);

                if (!Objects.isNull(themeTask))
                    themeTasks.add(themeTask);
                if (!Objects.isNull(studentTask))
                    studentTasks.add(studentTask);
            }
            Theme currentTheme = new Theme(themeName, themeTasks);
            if (currentTheme.getMaxExercise() > 0 || currentTheme.getMaxPractise() > 0)
                studentThemes.add(new StudentTheme(studentTasks, currentTheme));
            themeTasks.clear();
            studentTasks.clear();
        }
        return studentThemes;
    }

    private static Task getTask(String themeName, String heading, String points) {
        if (heading.matches("Упр:.*")) {
            return new Exercise(themeName, Integer.parseInt(points));
        }

        if (heading.matches("ДЗ:.*")) {
            return new Practise(themeName, Integer.parseInt(points));
        }

        if (heading.matches("Акт.*")) {
            return new Activity(themeName, Integer.parseInt(points));
        }

        return null;
    }

    // Стартовый индекс задан 2, так как специфика документов.
    private static ArrayList<int[]> getThemesIndex(String[] themeNames) {
        int previousIndex = 2;
        ArrayList<int[]> indexes = new ArrayList<>();
        for (int i = previousIndex + 1; i < themeNames.length; i++) {
            if (!themeNames[i].isBlank()) {
                indexes.add(new int[]{previousIndex, i});
                previousIndex = i;
            }
        }
        return indexes;
    }


}
