package org.example.dataBase;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.example.model.Student;
import org.example.model.StudentTheme;
import org.example.model.Theme;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс для работы с базой данных с использованием ORM-библиотеки ORMLite.
 * Этот класс предоставляет методы для подключения к базе данных, сохранения данных студентов и тем,
 * а также извлечения данных из базы.
 * Включает методы для сохранения данных в базе и извлечения их обратно в объектную модель.
 */
public class Database implements AutoCloseable {

    /**
     * URL подключения к базе данных SQLite.
     * Путь должен быть указан корректно для вашей системы.
     */
    private static final String URL = "jdbc:sqlite:C:\\Users\\usuov\\IdeaProjects\\projectJavaAnalitics\\src\\main\\java\\org\\example\\dataBase\\mainDb.db";

    private ConnectionSource connectionSource;

    private Dao<StudentData, String> studentsDao;
    private Dao<StudentThemeData, String> studentThemesDao;

    /**
     * Устанавливает соединение с базой данных и инициализирует DAO объекты для студентов и тем.
     *
     * @throws SQLException В случае ошибки соединения с базой данных
     */
    public void connect() throws SQLException {
        connectionSource = new JdbcConnectionSource(URL);
        studentsDao = createDAO(StudentData.class);
        studentThemesDao = createDAO(StudentThemeData.class);
    }

    /**
     * Обобщенный утилитный метод для создания DAO и таблицы, если она не существует.
     *
     * @param clazz Класс, который соответствует таблице в базе данных
     * @param <T>   Класс, соответствующий таблице
     * @return Dao соответствующего типа
     * @throws SQLException В случае ошибки при создании DAO или таблицы
     */
    private <T> Dao<T, String> createDAO(Class<T> clazz) throws SQLException {
        Dao<T, String> dao = DaoManager.createDao(connectionSource, clazz);
        TableUtils.createTableIfNotExists(connectionSource, clazz);
        return dao;
    }

    /**
     * Утилитный метод для записи данных в таблицу.
     * Для каждого объекта в переданном списке вызывается метод `dao.create()`, чтобы добавить его в базу данных.
     *
     * @param dao       DAO для соответствующей таблицы
     * @param dataArray Список данных, которые нужно записать в таблицу
     * @param <T>       Тип данных
     */
    private <T> void writeDao(Dao<T, String> dao, List<T> dataArray) {
        for (T data : dataArray) {
            try {
                dao.create(data);
            } catch (SQLException e) {
                System.err.println(MessageFormat.format(
                        "Ошибка добавления в таблицу {0}\nЗначение: {1}", dao.getTableName(), data));
            }
        }
    }

    /**
     * Сохраняет данные о студентах и их темах в базу данных.
     * Для каждого студента создаются соответствующие записи в таблицах студентов и тем.
     *
     * @param students Список студентов, полученных из парсера данных
     */
    public void saveData(List<Student> students) {
        // Преобразуем студентов в StudentData для записи в таблицу студентов
        List<StudentData> studentDataList = students.stream()
                .map(StudentData::new)
                .toList();
        writeDao(studentsDao, studentDataList);

        // Преобразуем StudentTheme в StudentThemeData и сохраняем их
        List<StudentThemeData> studentThemeData = new LinkedList<>();
        for (StudentData studentData : studentDataList) {
            studentThemeData.addAll(studentData
                    .getStudent()
                    .getThemes().stream()
                    .map(x -> new StudentThemeData(studentData, x))
                    .toList());
        }

        writeDao(studentThemesDao, studentThemeData);
    }

    /**
     * Извлекает данные о студентах и их темах из базы данных.
     * Этот метод сначала загружает все записи о студентах, затем для каждого студента извлекаются связанные темы
     * и создаются объекты `Student` и `StudentTheme`.
     *
     * @return Список объектов `Student`, включая их темы
     * @throws SQLException В случае ошибки при запросе данных из базы данных
     */
    public List<Student> getData() throws SQLException {
        List<Student> students = new LinkedList<>();

        // Извлекаем данные студентов
        for (StudentData studentData : studentsDao.queryForAll()) {
            // Извлекаем темы для каждого студента по его id
            List<StudentThemeData> data = studentThemesDao.queryForEq("student_id", studentData);

            List<StudentTheme> studentThemes = new LinkedList<>();
            // Для каждой записи о теме создаем объект StudentTheme
            data.forEach(d -> {
                Theme theme = new Theme(
                        d.getName(),
                        d.getMaxPractise(),
                        d.getMaxExercise(),
                        d.getMaxActivity(),
                        d.getQuality());
                studentThemes.add(new StudentTheme(
                        theme,
                        d.getCurrentPractise(),
                        d.getCurrentExercise(),
                        d.getCurrentActivity()));
            });

            // Создаем объект студента и связываем его с темами
            Student student = new Student(studentData.getName(), studentData.getGroup());
            student.setStudentThemes(studentThemes);
            students.add(student);  // Добавляем студента в список
        }

        return students;
    }

    public void close() throws Exception {
        connectionSource.close();  // Закрытие соединения с базой данных
    }
}
