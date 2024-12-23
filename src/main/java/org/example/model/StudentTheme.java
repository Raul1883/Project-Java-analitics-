package org.example.model;



import java.util.ArrayList;

/**
 * Рекорд, описывающий тему, которую прошел конкретный студент
 */

public final class StudentTheme {
    private final Theme theme;
    private int currentPractise;
    private int currentExercise;
    private int currentActivity;

    /**
     * @param theme тема, которую прошел студент
     */
    public StudentTheme(
            ArrayList<Task> tasks,
            Theme theme
    ) {
        for (Task t : tasks) {
            if (t.getClass() == Activity.class)
                currentActivity += t.points();
            else if (t.getClass() == Practise.class)
                currentPractise += t.points();
            else if (t.getClass() == Exercise.class)
                currentExercise += t.points();
        }


        this.theme = theme;
    }

    public StudentTheme(Theme theme, int currentPractise, int currentExercise, int currentActivity) {
        this.theme = theme;
        this.currentPractise = currentPractise;
        this.currentExercise = currentExercise;
        this.currentActivity = currentActivity;
    }

    public float getRelativeActivity(int maxActivity) {
        return (float) currentActivity / maxActivity;
    }

    public String getName() {
        return theme.getName();
    }

    public int currentPractise() {
        return currentPractise;
    }

    public int currentExercise() {
        return currentExercise;
    }

    public int currentActivity() {
        return currentActivity;
    }


    public Theme theme() {
        return theme;
    }

    @Override
    public String toString() {
        return "StudentTheme[" +
                "currentPractise=" + currentPractise + ", " +
                "currentExercise=" + currentExercise + ", " +
                "currentActivity=" + currentActivity + ", " +
                "theme=" + theme + ']';
    }
}
