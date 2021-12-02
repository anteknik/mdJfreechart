package com.jfreechart;

/**
 *
 * @author mayanton
 */
import com.jfreechart.trendline.thomas.CombinedView.LoggerManager;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JchartEditor extends Application {

    private static Logger log = LoggerFactory.getLogger(JchartEditor.class);

    private static PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("A", 0.8);
        dataset.setValue("B", 9.4);
        dataset.setValue("C", 0.1);
        dataset.setValue("D", 89.5);
        dataset.setValue("E", 0.2);
        dataset.setValue("F", 0.0);
        return dataset;
    }

    private static PieDataset createDatasetTwo() {
        DefaultPieDataset datasettwo = new DefaultPieDataset();
        datasettwo.setValue("A", 0.2);
        datasettwo.setValue("B", 19.4);
        datasettwo.setValue("C", 0.1);
        datasettwo.setValue("D", 79.5);
        datasettwo.setValue("E", 0.2);
        datasettwo.setValue("F", 0.0);
        return datasettwo;
    }

    private static JFreeChart createChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                "", dataset, false, true, false);
        chart.setBackgroundPaint(Color.LIGHT_GRAY);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setOutlineVisible(false);
        plot.setSectionPaint("A", Color.RED);
        plot.setSectionPaint("B", Color.BLUE);
        plot.setSectionPaint("C", Color.GREEN);
        plot.setSectionPaint("D", Color.YELLOW);
        plot.setSectionPaint("E", Color.CYAN);
        plot.setLabelFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        // Custom labels https://stackoverflow.com/a/17507061/230513
        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
                "{0}: {2}", new DecimalFormat("0"), new DecimalFormat("0.0%"));
        plot.setLabelGenerator(gen);
        return chart;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/JchartEditor.fxml"));
        Parent root = loader.load();

        PieDataset dataset = createDataset();
         PieDataset datasettwo = createDatasetTwo();
        JFreeChart chart = createChart(dataset);

        TabPane tabPane = new TabPane(
                new Tab("Tab 1", new ChartViewer(createChart(dataset))),
                new Tab("Tab 2", new ChartViewer(createChart(datasettwo))),
                new Tab("Tab 3", new ChartViewer(createChart(dataset)))
        );

        ChartViewer viewer = new ChartViewer(chart);
        //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("img/EdenCodingIcon.png")));
        primaryStage.setTitle("Multi Charts View Editor");
        //primaryStage.setScene(new Scene(root));
        //primaryStage.setScene(new Scene(viewer));
        primaryStage.setScene(new Scene(tabPane));
        primaryStage.show();
    }

    public static void main(String[] args) {
        LoggerManager.initializeLoggingContext();
        log.info("Init Start");
        launch(args);
    }
}
