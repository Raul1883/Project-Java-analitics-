package org.example.model;

/**
 * Сущность описывающая практику. Наследуется от упражнения, ввиду их сходства.
 * Разделение на два класса из-за разности природы этих объектов, а также, возможно,
 * дополнительное функциональности обработки практик.
 */
public class Practise extends Exercise {

    public Practise(String themeName, int maxPoints) {
        super(themeName, maxPoints);

    }

    public Practise(String themeName, int maxPoints, int countOfStarted, int countOfFinished) {
        super(themeName, maxPoints, countOfStarted, countOfFinished);

    }
}
