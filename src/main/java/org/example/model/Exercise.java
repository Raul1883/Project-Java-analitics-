package org.example.model;


/**
 * Класс описывает одно упражнение
 */
public class Exercise implements Task {
    private final String name;
    private final int _maxPoints;
    private float _quality;
    private int countOfStarted;

    public Exercise(String themeName, int maxPoints) {
        _maxPoints = maxPoints;
        name = themeName;
    }

    /**
     * @param themeName       название упражнения
     * @param maxPoints       максимальные баллы, за упражнение
     * @param countOfStarted  количество людей, приступивших к упражнению
     * @param countOfFinished количество людей, сдавших упражнение
     */

    public Exercise(String themeName, int maxPoints, int countOfStarted, int countOfFinished) {
        _maxPoints = maxPoints;
        this.countOfStarted = countOfStarted;
        _quality = (float) countOfStarted / countOfFinished;
        name = themeName;
    }


    public String themeName() {
        return "";
    }


    public int points() {
        return _maxPoints;
    }


    public float quality() {
        return _quality;
    }


    public float engagement(int peoplesCount) {
        return (float) countOfStarted / peoplesCount;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "name='" + name + '\'' +
                ", _maxPoints=" + _maxPoints +
                ", _quality=" + _quality +
                '}';
    }
}
