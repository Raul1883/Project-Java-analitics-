package org.example.viev;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;

import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.dataBase.Database;
import org.example.model.Student;
import org.example.parser.Parser;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class MainApplication extends Application {
    private static final String PATH_TO_CSV = "src\\main\\java\\org\\example\\srcfiles\\basicprogramming_2.csv";
    private static final String HELLO_MESSAGE = """
            Это приложение создано для анализа успеваемости студентов, обучающихся на площадке ULearn.
            в графиках отображены суммарные баллы всех студентов, подходящих под критерии.
            Категории студентов:
                GOOD - баллов на 25% больше среднего балла;
                NORMAL - баллов не меньше 60% от среднего балла;
                VAD - баллы ниже 60 % от среднего балла.
            Сложность задач - отношение приступивших к закончившим задачу.
            """;
    private static final Map<ChartName, String> chartTypeToRuText = Map.of(
            ChartName.FULL_FLAT, "Все баллы",
            ChartName.ACTIVITY_FLAT, "Баллы за активности",
            ChartName.EXERCISE_FLAT, "Баллы за упражнения",
            ChartName.PRACTISE_FLAT, "Баллы за практики",
            ChartName.QUALITY_FLAT, "Сложность задач",
            ChartName.GOOD_STUDENT, "Отличники",
            ChartName.NORMAL_STUDENT, "Хорошисты",
            ChartName.BAD_STUDENT, "Все плохо",
            ChartName.CATEGORY_RELATIONS, "Соотношение категорий"
    );

    private DataProcessor data;

    VBox chartsContainer = new VBox();

    // плоские графики
    private final VBox flatContainer = new VBox();
    private final VBox flatChartContainer = new VBox();
    private List<LineChart<String, Number>> flatChartList;
    private LineChart<String, Number> fullDataChart;
    private LineChart<String, Number> activityDataChart;
    private LineChart<String, Number> exerciseDataChart;
    private LineChart<String, Number> practiseDataChart;
    private LineChart<String, Number> qualityDataChart;
    // графики по категориям
    private final VBox categoryContainer = new VBox();
    private final VBox categoryChartContainer = new VBox();
    private List<Chart> categoryChartList;
    private LineChart<String, Number> goodStudentChart; // The good
    private LineChart<String, Number> normalStudentChart; // The bad
    private LineChart<String, Number> badStudentChart; // and the ugly
    private PieChart categoryPieChart; // соотношение категорий


    public static void main(String[] args) {
        Application.launch();
    }


    @Override
    public void start(Stage stage) {
        data = getData();
        VBox root = new VBox();
        root.getStyleClass().add("root");

        root.getChildren().add(getMenu());
        initCharts();
        root.getChildren().add(getCharts());

        Scene scene = new Scene(root);


        String styleSheet = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        scene.getStylesheets().add(styleSheet);

        stage.setScene(scene);
        stage.show();

        Alert alert = new Alert(Alert.AlertType.NONE, HELLO_MESSAGE, ButtonType.OK);
        alert.show();
    }


    private HBox getMenu() {
        Button updateButton = getUpdateButton();

        Button showFlatCharts = new Button("Показать полную статистику");
        showFlatCharts.setOnAction(actionEvent -> {
            if (!chartsContainer.getChildren().contains(flatContainer)) {
                chartsContainer.getChildren().remove(categoryContainer);
                chartsContainer.getChildren().add(flatContainer);
            }
        });
        Button showCategoryCharts = new Button("Показать статистику по категориям");
        showCategoryCharts.setOnAction(actionEvent -> {
            if (!chartsContainer.getChildren().contains(categoryContainer)) {
                chartsContainer.getChildren().remove(flatContainer);
                chartsContainer.getChildren().add(categoryContainer);
            }
        });

        HBox menu = new HBox(updateButton, showFlatCharts, showCategoryCharts);
        menu.getStyleClass().addAll("main-menu");
        return menu;
    }

    private Button getUpdateButton() {
        Button updateButton = new Button("Обновить б/д");

        updateButton.setOnAction(actionEvent -> CompletableFuture.runAsync(() -> {
            Platform.runLater(() -> updateButton.setText("Чтение..."));

            var students = Parser.readStudents(PATH_TO_CSV);

            Platform.runLater(() -> updateButton.setText("Запись..."));
            data = new DataProcessor(students);

            try (var db = new Database()) {
                db.connect();
                db.saveData(students);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Platform.runLater(() -> updateButton.setText("Обновить б/д"));
        }));
        return updateButton;
    }


    private void initCharts() {
        fullDataChart = ChartUtils.createLineChart("Все данные", data.getFullDataFlat());
        activityDataChart = ChartUtils.createLineChart("Активности", data.getActivityDataFlat());
        exerciseDataChart = ChartUtils.createLineChart("Упражнения", data.getExerciseDataFlat());
        practiseDataChart = ChartUtils.createLineChart("Практики", data.getPractiseDataFlat());
        qualityDataChart = ChartUtils.createLineChart("Сложность задач", data.getQualityData());

        goodStudentChart = ChartUtils.createLineChart("Отличники", data.getGoodStudentData());
        normalStudentChart = ChartUtils.createLineChart("Хорошисты", data.getNormalStudentData());
        badStudentChart = ChartUtils.createLineChart("Все плохо", data.getBadStudentData());
        categoryPieChart = ChartUtils.createPieChart("Соотношение категорий", data.getCategoryRelationsData());


        flatChartList = new ArrayList<>(4);
        flatChartList.add(fullDataChart);
        flatChartList.add(activityDataChart);
        flatChartList.add(exerciseDataChart);
        flatChartList.add(practiseDataChart);
        flatChartList.add(qualityDataChart);

        categoryChartList = new ArrayList<>(4);
        categoryChartList.add(goodStudentChart);
        categoryChartList.add(normalStudentChart);
        categoryChartList.add(badStudentChart);
        categoryChartList.add(categoryPieChart);

        flatChartContainer.getChildren().add(fullDataChart);
        categoryChartContainer.getChildren().add(goodStudentChart);
    }

    private VBox getCharts() {
        HBox flatButtonsContainer = getFlatBox();

        flatContainer.getChildren().add(flatButtonsContainer);
        flatContainer.getChildren().add(flatChartContainer);

        HBox categoryButtonsContainer = getCategoryBox();
        categoryContainer.getChildren().add(categoryButtonsContainer);
        categoryContainer.getChildren().add(categoryChartContainer);
        chartsContainer.getChildren().add(flatContainer);

        flatButtonsContainer.getStyleClass().addAll("charts-container");
        categoryButtonsContainer.getStyleClass().addAll("charts-container");
        return chartsContainer;
    }

    private HBox getFlatBox() {
        Button allButton = createChartButton(ChartName.FULL_FLAT);
        Button activityButton = createChartButton(ChartName.ACTIVITY_FLAT);
        Button exerciseButton = createChartButton(ChartName.EXERCISE_FLAT);
        Button practiseButton = createChartButton(ChartName.PRACTISE_FLAT);
        Button qualityButton = createChartButton(ChartName.QUALITY_FLAT);

        return new HBox(allButton, activityButton, exerciseButton, practiseButton, qualityButton);
    }


    private HBox getCategoryBox() {
        Button goodButton = createChartButton(ChartName.GOOD_STUDENT);
        Button normalButton = createChartButton(ChartName.NORMAL_STUDENT);
        Button badButton = createChartButton(ChartName.BAD_STUDENT);
        Button relationsButton = createChartButton(ChartName.CATEGORY_RELATIONS);

        return new HBox(goodButton, normalButton, badButton, relationsButton);
    }

    private Button createChartButton(ChartName name) {
        Button button = new Button(chartTypeToRuText.get(name));
        button.setOnAction(actionEvent -> showChart(name));
        button.getStyleClass().addAll("charts-button");
        return button;
    }

    private void showChart(ChartName name) {
        switch (name) {
            case FULL_FLAT -> {
                flatChartList.forEach(x -> flatChartContainer.getChildren().remove(x));
                flatChartContainer.getChildren().add(fullDataChart);
            }
            case ACTIVITY_FLAT -> {
                flatChartList.forEach(x -> flatChartContainer.getChildren().remove(x));
                flatChartContainer.getChildren().add(activityDataChart);
            }
            case EXERCISE_FLAT -> {
                flatChartList.forEach(x -> flatChartContainer.getChildren().remove(x));
                flatChartContainer.getChildren().add(exerciseDataChart);
            }
            case PRACTISE_FLAT -> {
                flatChartList.forEach(x -> flatChartContainer.getChildren().remove(x));
                flatChartContainer.getChildren().add(practiseDataChart);
            }
            case QUALITY_FLAT -> {
                flatChartList.forEach(x -> flatChartContainer.getChildren().remove(x));
                flatChartContainer.getChildren().add(qualityDataChart);
            }
            case GOOD_STUDENT -> {
                categoryChartList.forEach(x -> categoryChartContainer.getChildren().remove(x));
                categoryChartContainer.getChildren().add(goodStudentChart);
            }
            case NORMAL_STUDENT -> {
                categoryChartList.forEach(x -> categoryChartContainer.getChildren().remove(x));
                categoryChartContainer.getChildren().add(normalStudentChart);
            }
            case BAD_STUDENT -> {
                categoryChartList.forEach(x -> categoryChartContainer.getChildren().remove(x));
                categoryChartContainer.getChildren().add(badStudentChart);
            }
            case CATEGORY_RELATIONS -> {
                categoryChartList.forEach(x -> categoryChartContainer.getChildren().remove(x));
                categoryChartContainer.getChildren().add(categoryPieChart);
            }

        }
    }


    private DataProcessor getData() {
        List<Student> students;

        try (Database db = new Database()) {
            db.connect();
            students = db.getData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new DataProcessor(students);
    }

    private enum ChartName {
        FULL_FLAT,
        ACTIVITY_FLAT,
        EXERCISE_FLAT,
        PRACTISE_FLAT,
        QUALITY_FLAT,
        GOOD_STUDENT,
        NORMAL_STUDENT,
        BAD_STUDENT,
        CATEGORY_RELATIONS
    }

}
