package org.example.viev;

import org.example.model.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class DataProcessor {
    private final List<Student> students;

    private final List<DataPair> fullDataFlat;
    private final List<DataPair> activityDataFlat;
    private final List<DataPair> exerciseDataFlat;
    private final List<DataPair> practiseDataFlat;
    private final List<DataPair> qualityData;

    private final List<DataPair> goodStudentData;
    private final List<DataPair> normalStudentData;
    private final List<DataPair> badStudentData;
    private final List<DataPair> categoryRelationsData;

    private Integer _average;


    public DataProcessor(List<Student> students) {
        this.students = students;

        fullDataFlat = getDataForType(Task.class);
        activityDataFlat = getDataForType(Activity.class);
        exerciseDataFlat = getDataForType(Exercise.class);
        practiseDataFlat = getDataForType(Practise.class);
        qualityData = getQuality();

        goodStudentData = getDataForCategory(Category.GOOD);
        normalStudentData = getDataForCategory(Category.NORMAL);
        badStudentData = getDataForCategory(Category.BAD);
        categoryRelationsData = getCategoriesRelation();
    }

    private List<DataPair> getCategoriesRelation() {
        HashMap<Category, Integer> pointsMap = new LinkedHashMap<>();
        pointsMap.put(Category.BAD, 0);
        pointsMap.put(Category.NORMAL, 0);
        pointsMap.put(Category.GOOD, 0);


        int average = getAverage();

        for (Student student : students) {
            int summaryPoints = student.getSummaryPoints();
            pointsMap.computeIfPresent(getCategoryByPoints(summaryPoints, average), (k, v) -> v + 1);
        }

        return pointsMap.entrySet().stream().map(x -> new DataPair(x.getKey().toString(), x.getValue())).toList();
    }

    private List<DataPair> getQuality() {
        HashMap<String, Number> pointsMap = new LinkedHashMap<>();
        students.get(0).getThemes().stream().map(StudentTheme::theme).forEach(theme -> pointsMap.put(theme.getName(), theme.getQuality()));

        return DataPair.fromMap(pointsMap);
    }

    private List<DataPair> getDataForType(Class<?> clazz) {
        HashMap<String, Integer> pointsMap = new LinkedHashMap<>();
        students.get(0).getThemes().stream().map(StudentTheme::theme).forEach(x -> pointsMap.put(x.getName(), 0));

        students.forEach(student -> student.getThemes().forEach(studentTheme -> pointsMap.computeIfPresent(studentTheme.getName(), (k, v) -> v + getTaskPoints(clazz, studentTheme))));


        return pointsMap.entrySet().stream().map(x -> new DataPair(x.getKey(), x.getValue())).toList();
    }

    private List<DataPair> getDataForCategory(Category category) {
        HashMap<String, Integer> pointsMap = new LinkedHashMap<>();
        students.get(0).getThemes().stream().map(StudentTheme::theme).forEach(x -> pointsMap.put(x.getName(), 0));

        int average = getAverage();
        students.forEach(student -> {
            int summaryPoints = student.getSummaryPoints();


            if (getCategoryByPoints(summaryPoints, average) == category) {
                student.getThemes().forEach(studentTheme -> pointsMap.computeIfPresent(studentTheme.getName(), (k, v) -> v + getTaskPoints(Task.class, studentTheme)));
            }
        });

        return pointsMap.entrySet().stream().map(x -> new DataPair(x.getKey(), x.getValue())).toList();
    }

    private int getAverage() {
        if (!Objects.isNull(_average))
            return _average;
        _average = (int) Math.round(students.stream().mapToInt(Student::getSummaryPoints).average().orElse(0));
        return _average;
    }

    private Category getCategoryByPoints(int points, int average) {
        if (points > average * 1.25) return Category.GOOD;
        if (points > average * 0.6) return Category.NORMAL;
        return Category.BAD;
    }


    private int getTaskPoints(Class<?> clazz, StudentTheme theme) {
        try {
            if (clazz.equals(Practise.class)) return theme.currentPractise();

            if (clazz.equals(Activity.class)) return theme.currentActivity();

            if (clazz.equals(Exercise.class)) return theme.currentExercise();

            if (clazz.equals(Task.class))
                return theme.currentExercise() + theme.currentActivity() + theme.currentPractise();

        } catch (ArithmeticException e) {
            return 0;
        }
        throw new RuntimeException("Unknown class for interface Tack");
    }

    public List<DataPair> getFullDataFlat() {
        return fullDataFlat;
    }

    public List<DataPair> getActivityDataFlat() {
        return activityDataFlat;
    }

    public List<DataPair> getExerciseDataFlat() {
        return exerciseDataFlat;
    }

    public List<DataPair> getPractiseDataFlat() {
        return practiseDataFlat;
    }

    public List<DataPair> getGoodStudentData() {
        return goodStudentData;
    }

    public List<DataPair> getNormalStudentData() {
        return normalStudentData;
    }

    public List<DataPair> getBadStudentData() {
        return badStudentData;
    }

    public List<DataPair> getCategoryRelationsData() {
        return categoryRelationsData;
    }

    public List<DataPair> getQualityData() {
        return qualityData;
    }


    private enum Category {
        GOOD, NORMAL, BAD
    }
}
