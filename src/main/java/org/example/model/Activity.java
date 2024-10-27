package org.example.model;

public record Activity(String themeName, int maxPoints) implements Task {

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
                ", maxPoints=" + maxPoints +
                '}';
    }
}
