package converter;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	private static final String RESOURCE_PATH = "/converter/resources/";
	private static final String VIEW_PATH = "/converter/view/";

	private Stage primaryStage;
	private BorderPane mainLayout;
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Baking Converter");
		this.primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream(RESOURCE_PATH+"muffin.png")));
		showMainView();
		
	}
	
	private void showMainView() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource(VIEW_PATH+"MainView.fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
		Controller controller = loader.getController();
		controller.unitInMasse.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue) -> controller.convMetricSystem(newValue,true,"masse"));;
		controller.unitOutMasse.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue) -> controller.convMetricSystem(newValue,false,"masse"));;
		controller.unitInVol.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue) -> controller.convMetricSystem(newValue,true,"volume"));;
		controller.unitOutVol.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue) -> controller.convMetricSystem(newValue,false,"volume"));;
		controller.items.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue) -> controller.convertVol2Weight(newValue, false, "ingredient"));
		controller.inputUnit.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue) -> controller.convertVol2Weight(newValue, true, "unit"));
		controller.outputUnit.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue) -> controller.convertVol2Weight(newValue, false, "unit"));
		controller.input.textProperty().addListener((v,oldValue,newValue) -> controller.convertVol2Weight(newValue));
		controller.inVol.textProperty().addListener((v,oldValue,newValue) -> controller.convVol(newValue));
		controller.inMasse.textProperty().addListener((v,oldValue,newValue) -> controller.convMass(newValue));
		final StringProperty textCel = new SimpleStringProperty(controller.cel.getText());
		textCel.addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> obs, String oldValue, String newValue) {
		        controller.cel.setText(newValue);
		    }
		});
		
		controller.cel.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> obs, String oldValue, String newValue) {
		        if (! newValue.equals(textCel.get())) { 
		    		double celcius = 0;
		        	try{
		        		if(StringUtils.isNotBlank(newValue)){
		        			celcius = Double.parseDouble(newValue.trim().replace(",","."));
		        		}
		        		double value = celcius*1.8 + 32;
		        		DecimalFormat df = new DecimalFormat("#.#");
		        		df.setRoundingMode(RoundingMode.CEILING);
		        		controller.far.setText(df.format(value));
		        	}catch(NumberFormatException e){
		        		controller.far.setText("NULL");
		        	}   
		        	textCel.set(newValue);
		        }
		    }
		});
		final StringProperty textFar = new SimpleStringProperty(controller.far.getText());
		textFar.addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> obs, String oldValue, String newValue) {
		        controller.far.setText(newValue);
		    }
		});
		
		controller.far.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> obs, String oldValue, String newValue) {
		        if (! newValue.equals(textFar.get())) { 
		        	double fare = 0.0;
		           	try{
		        		if(StringUtils.isNotBlank(newValue)){
		            		fare = Double.parseDouble(newValue.trim().replace(",","."));
		            	}
		               	double value = ( fare - 32 )/1.8;
		        		DecimalFormat df = new DecimalFormat("#.#");
		        		df.setRoundingMode(RoundingMode.CEILING);
		            	controller.cel.setText(df.format(value));
		           	}catch(NumberFormatException e){
		           		controller.cel.setText("NULL");
		           	}
		        	textFar.set(newValue);
		        }
		    }
		});

	}

	public static void main(String[] args) {
		launch(args);
	}
}
