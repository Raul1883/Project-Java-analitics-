package org.example.model;

import java.util.ArrayList;
import java.util.Objects;

public class Student {
    private final String name;
    private final float averageQuality;
    private final ArrayList<StudentTheme> themes;


    private final ArrayList<Float> qualityDynamics = new ArrayList<>();
    private final ArrayList<Float> activitiesDynamics = new ArrayList<>();
    private final ArrayList<Float> exercisesDynamics = new ArrayList<>();
    private final ArrayList<Float> practisesDynamics = new ArrayList<>();

    private ArrayList<Float> relativeActivitiesDynamics;

    public Student(String name, ArrayList<StudentTheme> themes) {
        this.name = name;
        this.themes = themes;

        double sumQuality = 0;
        int counter = 0;

        for (StudentTheme t : themes) {
            activitiesDynamics.add(getRelativeDynamicsValue(t));
            exercisesDynamics.add(getRelativeDynamicsValue(t));
            practisesDynamics.add(getRelativeDynamicsValue(t));


            if (t.quality() != -1) {
                qualityDynamics.add(t.quality());
                sumQuality += t.quality();
                counter++;
            }
        }
        averageQuality = (float) sumQuality / counter;


    }

    private static float getRelativeDynamicsValue(StudentTheme theme) {
        return (float) theme.currentPractise() / theme.theme().getMaxPractise();
    }


    public String getName() {
        return name;
    }

    public float getAverageQuality() {
        return averageQuality;
    }

    public ArrayList<Float> getQualityDynamics() {
        return qualityDynamics;
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
}
