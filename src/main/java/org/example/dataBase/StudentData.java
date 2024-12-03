package org.example.dataBase;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.example.model.Student;

/**
 * Дата класс, отображающий класс Student
 * ФИО студентов уникальны
 */


@DatabaseTable(tableName = "students")
public class StudentData {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(unique = true)
    private String name;

    @DatabaseField
    private String group;

    private Student student;

    public StudentData() {
    }

    public StudentData(Student student) {
        this.name = student.getName();
        this.group = student.getGroup();
        this.student = student;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public Student getStudent() {
        return student;
    }
}
