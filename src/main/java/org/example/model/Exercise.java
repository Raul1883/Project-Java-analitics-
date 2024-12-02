package org.example.model;


/**
 * Класс описывает одно упражнение
 */
public class Exercise implements Task {
    private final String name;
    private final int _maxPoints;
    private float _quality;

    private final int countOfStarted;
    private final int countOfFinished;

    public Exercise(String themeName, int maxPoints) {
        countOfStarted = 0;
        countOfFinished = 0;

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
        this.countOfFinished = countOfFinished;
        _quality = (float) countOfFinished / countOfStarted;

        name = themeName;
    }


    public String themeName() {
        return name;
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


    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", _maxPoints=" + _maxPoints +
                ", _quality=" + _quality +
                ", start=" + countOfStarted +
                ", end=" + countOfFinished +
                '}';
    }
}
