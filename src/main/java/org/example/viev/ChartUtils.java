package org.example.viev;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;

import java.util.List;

public class ChartUtils {
    private static XYChart.Series<String, Number> getLineSeries(String name, List<DataPair> data) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(name);

        for (DataPair pair : data) {
            series.getData().add(new XYChart.Data<>(pair.x(), pair.y()));
        }

        return series;
    }

    public static LineChart<String, Number> createLineChart(String name, List<DataPair> data) {

        LineChart<String, Number> lineChart = new LineChart<>(new CategoryAxis(), new NumberAxis());
        lineChart.getData().add(getLineSeries(name, data));

        return lineChart;
    }

    public static PieChart createPieChart(String name, List<DataPair> data) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (DataPair pair : data) {
            pieChartData.add(new PieChart.Data(pair.x(), pair.y().doubleValue()));
        }

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle(name);

        return pieChart;
    }
}



