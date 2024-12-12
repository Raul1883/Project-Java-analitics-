package org.example.model;


import java.util.List;
import java.util.Objects;


/**
 * Класс, описывающий сущность студента, прошедшего курс
 */

public class Student {
    private final String name;
    private final String group;
    private List<StudentTheme> themes;
    private Integer summaryPoints;

    /**
     * @param name  ФИО студента
     * @param group группа
     */
    public Student(String name, String group) {
        this.name = name;
        this.group = group;
    }


    public void setStudentThemes(List<StudentTheme> themes) {
        this.themes = themes;
        summaryPoints = themes.stream()
                .mapToInt(x ->
                        x.currentExercise() + x.currentActivity() + x.currentPractise()
                )
                .sum();
    }


    public String getName() {
        return name;
    }


    public String getGroup() {
        return group;
    }

    public List<StudentTheme> getThemes() {
        if (Objects.isNull(themes))
            throw new IllegalStateException("Full statistics was not initialized");

        return themes;
    }

    public int getSummaryPoints() {
        if (Objects.isNull(summaryPoints))
            throw new IllegalStateException("Full statistics was not initialized");

        return summaryPoints;
    }

    @Override
    public String toString() {
        return name + "{\nex:" +
                themes.toString();
    }


}
