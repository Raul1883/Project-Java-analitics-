package org.example.model;

import java.util.ArrayList;

/**
 * Описание темы в сферическом вакууме
 */
public class Theme {
    private final String name;
    private int maxPractise;
    private int maxExercise;
    private int maxActivity;
    private final float quality;

    /**
     * @param name  название темы
     * @param tasks совокупность заданий, которые необходимо выполнить в этой теме
     */
    public Theme(String name, ArrayList<Task> tasks) {
        this.name = name;
        double sumQuality = 0;
        int counter = 0;

        for (Task t : tasks) {
            if (t.getClass() == Activity.class)
                maxActivity += t.points();
            else if (t.getClass() == Practise.class)
                maxPractise += t.points();
            else if (t.getClass() == Exercise.class)
                maxExercise += t.points();


            if (t.quality() != -1) {
                sumQuality += t.quality();
                counter++;
            }
        }
        quality = (float) sumQuality / counter;
    }

    public Theme(String name, int maxPractise, int maxExercise, int maxActivity, float quality) {
        this.name = name;
        this.maxPractise = maxPractise;
        this.maxExercise = maxExercise;
        this.maxActivity = maxActivity;
        this.quality = quality;
    }

    public int getMaxPractise() {
        return maxPractise;
    }

    public int getMaxExercise() {
        return maxExercise;
    }

    public int getMaxActivity() {
        return maxActivity;
    }

    public float getQuality() {
        return quality;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Theme{" +
                "name='" + name + '\'' +
                ", maxPractise=" + maxPractise +
                ", maxExercise=" + maxExercise +
                ", maxActivity=" + maxActivity +
                ", quality=" + quality +
                '}';
    }
}
