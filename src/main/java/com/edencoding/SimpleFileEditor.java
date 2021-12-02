package com.edencoding;

/**
 *
 * @author mayanton
 */

import com.jfreechart.trendline.thomas.CombinedView.LoggerManager;
import com.jfreechart.trendline.thomas.CombinedView.MultiChartView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleFileEditor extends Application {

    private static Logger log = LoggerFactory.getLogger(MultiChartView.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/SimpleFileEditor.fxml"));
        Parent root = loader.load();
        //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("img/EdenCodingIcon.png")));
        primaryStage.setTitle("Multi Charts View Editor");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        LoggerManager.initializeLoggingContext();
        log.info("Init Start");
        launch(args);
    }
}
