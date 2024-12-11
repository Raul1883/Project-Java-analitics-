package org.example.dataBase;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.example.model.StudentTheme;
import org.example.model.Theme;

/**
 * Дата класс, отображающий класс StudentTheme и Theme
 * связь с таблицей students по внешнему ключу student_id
 */

@DatabaseTable(tableName = "StudentThemes")
public class StudentThemeData {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField(foreign = true)
    private StudentData student;

    @DatabaseField
    private int maxPractise;

    @DatabaseField
    private int currentPractise;

    @DatabaseField
    private int maxExercise;

    @DatabaseField
    private int currentExercise;

    @DatabaseField
    private int maxActivity;

    @DatabaseField
    private int currentActivity;

    @DatabaseField
    private float quality;

    public StudentThemeData() {
    }

    public StudentThemeData(StudentData student, StudentTheme studentTheme) {
        Theme theme = studentTheme.theme();

        this.student = student;
        this.name = studentTheme.getName();

        this.maxPractise = theme.getMaxPractise();
        this.maxActivity = theme.getMaxActivity();
        this.maxExercise = theme.getMaxExercise();

        this.currentPractise = studentTheme.currentPractise();
        this.currentActivity = studentTheme.currentActivity();
        this.currentExercise = studentTheme.currentExercise();

        this.quality = theme.getQuality();
    }

    public String getName() {
        return name;
    }

    public StudentData getStudent() {
        return student;
    }

    public int getMaxPractise() {
        return maxPractise;
    }

    public int getCurrentPractise() {
        return currentPractise;
    }

    public int getMaxExercise() {
        return maxExercise;
    }

    public int getCurrentExercise() {
        return currentExercise;
    }

    public int getMaxActivity() {
        return maxActivity;
    }

    public int getCurrentActivity() {
        return currentActivity;
    }

    public float getQuality() {
        return quality;
    }
}
