package org.example.parser;

import com.opencsv.CSVReader;
import org.example.model.*;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.util.Objects;

public class Parser {

    public static ArrayList<Task> tasks = new ArrayList<>();


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
            ArrayList<Theme> themes = new ArrayList<>();
            for (int[] indexPair : themesRanges) {
                ArrayList<Task> themeTasks = new ArrayList<>();
                String themeName = themeNames[indexPair[0]];
                for (int i = indexPair[0]; i < indexPair[1]; i++) {
                    Task themeTask = getTask(themeName, headings[i], fullPoints[i]);

                    if (!Objects.isNull(themeTask))
                        themeTasks.add(themeTask);
                }
                themes.add(new Theme(themeName, themeTasks));
            }

            while ((nextLine = reader.readNext()) != null) {
                students.add(createStudent(themes, nextLine, themeNames, headings, themesRanges));
            }

            return students;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения из файла");
        }
    }

    private static Student createStudent(ArrayList<Theme> themes, String[] line, String[] themeNames,
                                         String[] headings,
                                         ArrayList<int[]> themesRanges) {

        Student student = new Student(line[0], line[1]);
        ArrayList<StudentTheme> studentTheme = getStudentThemes(themes, line, themeNames, headings, themesRanges);

        student.setStudentThemes(studentTheme);
        return student;
    }

    private static ArrayList<StudentTheme> getStudentThemes(ArrayList<Theme> themes, String[] line, String[] themeNames,
                                                            String[] headings,
                                                            ArrayList<int[]> themesRanges) {


        ArrayList<StudentTheme> studentThemes = new ArrayList<>();
        int counter = 0;
        for (int[] indexPair : themesRanges) {
            ArrayList<Task> studentTasks = new ArrayList<>();

            String themeName = themeNames[indexPair[0]];
            for (int i = indexPair[0]; i < indexPair[1]; i++) {
                Task studentTask = getTask(themeName, headings[i], line[i]);

                if (!Objects.isNull(studentTask))
                    studentTasks.add(studentTask);
            }
            Theme currentTheme = themes.get(counter);
            counter++;
            if (currentTheme.getMaxExercise() > 0 || currentTheme.getMaxPractise() > 0)
                studentThemes.add(new StudentTheme(studentTasks, currentTheme));

            studentTasks.clear();
        }
        return studentThemes;
    }

    private static Task getTask(String themeName, String heading, String points) {

        if (heading.matches("Акт.*")) {
            Activity activity = new Activity(themeName, Integer.parseInt(points));
            tasks.add(activity);
            return activity;
        }

        if (!heading.matches(".*: .*")) {
            return null;
        }

        String[] splitHeading = heading.split(": ");
        String taskType = splitHeading[0];
        String taskName = splitHeading[1];

        SlideData slideData = ULearn.getSlideData(splitHeading[1]);


        switch (taskType) {
            case "Упр":
                Exercise exercise = Objects.isNull(slideData) ?
                        new Exercise(taskName, Integer.parseInt(points)) :
                        new Exercise(taskName, Integer.parseInt(points),
                                slideData.countOfStarted(), slideData.countOfFinished());
                tasks.add(exercise);
                return exercise;
            case "ДЗ":
                Practise practise = Objects.isNull(slideData) ?
                        new Practise(taskName, Integer.parseInt(points)) :
                        new Practise(taskName, Integer.parseInt(points),
                                slideData.countOfStarted(), slideData.countOfFinished());
                tasks.add(practise);
                return practise;
            default:
                return null;
        }

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
