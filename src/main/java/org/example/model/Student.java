package org.example.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Класс, описывающий сущность студента, прошедшего курс
 */

public class Student {
    private final String name;
    private final String group;
    private List<StudentTheme> themes;

    // изменение величин с течением времени
    private final ArrayList<Float> activitiesDynamics = new ArrayList<>();
    private final ArrayList<Float> exercisesDynamics = new ArrayList<>();
    private final ArrayList<Float> practisesDynamics = new ArrayList<>();


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

        for (StudentTheme t : themes) {
            activitiesDynamics.add(getRelativeActivityValue(t));
            exercisesDynamics.add(getRelativeExercisesValue(t));
            practisesDynamics.add(getRelativePractisesValue(t));
        }
    }


    private static float getRelativeActivityValue(StudentTheme theme) {
        return (float) theme.currentActivity() / theme.theme().getMaxActivity();
    }

    private static float getRelativeExercisesValue(StudentTheme theme) {
        return (float) theme.currentExercise() / theme.theme().getMaxExercise();
    }

    private static float getRelativePractisesValue(StudentTheme theme) {
        return (float) theme.currentPractise() / theme.theme().getMaxPractise();
    }


    public String getName() {
        return name;
    }


    public ArrayList<Float> getActivitiesDynamics() {
        return activitiesDynamics;
    }

    public ArrayList<Float> getExercisesDynamics() {
        return exercisesDynamics;
    }

    public ArrayList<Float> getPractisesDynamics() {
        return practisesDynamics;
    }


    public List<StudentTheme> getThemes() {
        return themes;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return name + "{\nex:" +
                getExercisesDynamics() + "\nact:" +
                getActivitiesDynamics() + "\npr:" +
                getPractisesDynamics() + "\n}" +
                themes.toString();
    }
}
