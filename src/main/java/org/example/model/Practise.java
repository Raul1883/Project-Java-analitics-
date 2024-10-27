package org.example.model;

public class Practise extends Exercise {

    public Practise(String themeName, int maxPoints, int countOfStarted, int countOfFinished) {
        super(themeName, maxPoints, countOfStarted, countOfFinished);
    }

    @Override
    public String toString() {
        return "Practise{" +
                "name='" + super.getName() + '\'' +
                ", _maxPoints=" + super.maxPoints() +
                ", _quality=" + super.quality() +
                '}';
    }
}
