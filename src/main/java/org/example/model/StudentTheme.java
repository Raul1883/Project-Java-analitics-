package org.example.model;

/**
 * Рекорд, описывающий тему, которую прошел конкретный студент
 * @param currentPractise полученные баллы за практики
 * @param currentExercise полученные баллы за упражнения
 * @param currentActivity полученные баллы за активности
 * @param quality "качество" прохождения темы. Подробнее в интерфейсе Task
 * @param theme тема, которую прошел студент
 */
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
