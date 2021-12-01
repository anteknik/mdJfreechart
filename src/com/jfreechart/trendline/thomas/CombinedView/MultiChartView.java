/**
 * 
 */
package com.jfreechart.trendline.thomas.CombinedView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import yahoofinance.histquotes.Interval;
//import yahoofinance.histquotes.Interval;

/**
 * @author Ghanshyam Vaghasiya
 *
 */
public class MultiChartView extends Application{

	/**
	 * @param args
	 */
	
	private static Stage topPrimaryStage; 
	private static Logger log = LoggerFactory.getLogger(MultiChartView.class);
	private static ArrayList stockName = new ArrayList();


	public static void main(String args[]){
		LoggerManager.initializeLoggingContext();
		log.info("Init Start");
		launch(args);

	}
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		 	getContractList();
	        
	        topPrimaryStage = primaryStage;
	        FlowPane root = new FlowPane();
	        root.setAlignment(Pos.CENTER);
	        Scene scene = getMainScene();
	        
			primaryStage.setScene(scene);
			primaryStage.setTitle("MultiChart View");
			primaryStage.show();
			
	}
	
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	private Scene getMainScene() {
	    Label symbolNameLabel = new Label("Symbol: ");
		ComboBox symbolcombo_box = new ComboBox(FXCollections.observableArrayList(stockName));	
		
		symbolcombo_box.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			if(stockName.contains(newValue)){
				symbolcombo_box.setValue(newValue);
			}		

			}
		});
		
		new AutoCompleteComboBox.AutoCompleteComboBoxListener<>(symbolcombo_box);
		
		Button btn1=new Button("Draw Chart");
		btn1.setStyle("-fx-background-color: lightblue;; -fx-text-fill:darkblue; ");
		btn1.setMaxWidth(100);
		btn1.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {

				try {
					                               System.out.println("click get value : "+(String)symbolcombo_box.getValue());
					new OHLCMultiSymbolTrendLineChart((String)symbolcombo_box.getValue(),Calendar.YEAR, -10,Interval.DAILY);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});

	
		FlowPane root = new FlowPane(symbolNameLabel,symbolcombo_box,btn1);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 400, 100);
		return scene;
	}
	
	@SuppressWarnings("unchecked")
	public static void getContractList() throws IOException{
		File file = new File("conf\\nasdaq_screener_1630576232472.csv");
		FileReader fr = new FileReader(file); 
		BufferedReader br = new BufferedReader(fr);
		String line ;
		while((line = br.readLine())!=null ){
				String tokens[] = line.split(",");
				String symbolName = tokens[0].replace(" ", "");
				stockName.add(symbolName);
		} 
		
	}

}
