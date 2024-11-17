package org.example.model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Класс, описывающий сущность студента, прошедшего курс
 */
public class Student {
    private final String name;
    private final String group;
    private final ArrayList<StudentTheme> themes;


    // изменение величин с течением времени
    private final ArrayList<Float> activitiesDynamics = new ArrayList<>();
    private final ArrayList<Float> exercisesDynamics = new ArrayList<>();
    private final ArrayList<Float> practisesDynamics = new ArrayList<>();

    private ArrayList<Float> relativeActivitiesDynamics;

    /**
     * @param name   ФИО студента
     * @param themes пройденные темы
     * @param group  группа
     */
    public Student(String name, String group, ArrayList<StudentTheme> themes) {
        this.name = name;
        this.group = group;
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

    public ArrayList<Float> getRelativeActivitiesDynamics(int maxActivity) {
        if (Objects.isNull(relativeActivitiesDynamics)) {
            relativeActivitiesDynamics = new ArrayList<>();
            for (StudentTheme t : themes)
                relativeActivitiesDynamics.add(t.getRelativeActivity(maxActivity));
        }
        return relativeActivitiesDynamics;
    }

    public ArrayList<StudentTheme> getThemes() {
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
                getPractisesDynamics() + "\n}";
    }
}
