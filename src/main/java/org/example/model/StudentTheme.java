package org.example.model;

public record StudentTheme(
        int currentPractise,
        int currentExercise,
        int currentActivity,
        float quality,
        Theme theme
) {
    public float getRelativeActivity(int maxActivity) {
        return (float) currentActivity / maxActivity;
    }

    public String getName() {
        return theme.getName();
    }
}
