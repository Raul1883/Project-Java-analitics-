package org.example.model;


/**
 * Рекорд описывает активности за промежуток времени (обычно одна тема).
 * @param themeName название темы
 * @param points максимальные баллы, которые можно получить за активность
 */
public record Activity(String themeName, int points) implements Task {

    public float engagement(int peoplesCount) {
        return -1;
    }

    public float quality() {
        return -1;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "themeName='" + themeName + '\'' +
                ", maxPoints=" + points +
                '}';
    }
}
