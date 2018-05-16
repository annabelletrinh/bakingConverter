package converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class Controller {

	private static final String RESOURCE_PATH = "/converter/resources/";
    private LinkedHashMap<String, Double> masseVolumique;
    private LinkedHashMap<String, Double> litre;
    private LinkedHashMap<String, Double> gramme;
    private LinkedHashMap<String, Double> volume;
    private LinkedHashMap<String, Double> masse;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    TextField input;

    @FXML
    private TextField output;

    @FXML
    TextField cel;

    @FXML
    TextField inMasse;

    @FXML
    TextField inVol;

    @FXML
    TextField far;

    @FXML
    private TextField outMasse;

    @FXML
    private TextField outVol;

    @FXML
    ChoiceBox<String> inputUnit;

    @FXML
    ChoiceBox<String> outputUnit;
    
    @FXML
	ChoiceBox<String> unitInMasse;

    @FXML
    ChoiceBox<String> unitInVol;
    
    @FXML
    ChoiceBox<String> unitOutVol;

    @FXML
    ChoiceBox<String> unitOutMasse;
    
    @FXML
    ChoiceBox<String> items;
 
    void cel2Far(String newValue){
		double celcius = 0;
    	try{
    		if(StringUtils.isNotBlank(newValue)){
    			celcius = Double.parseDouble(newValue.trim().replace(",","."));
    		}
    		double value = celcius*1.8 + 32;
    		DecimalFormat df = new DecimalFormat("#.#");
    		df.setRoundingMode(RoundingMode.CEILING);
        	far.setText(df.format(value));
    	}catch(NumberFormatException e){
    		far.setText("NULL");
    	}
    }


    void far2Cel(String newValue) {
    	
    	double fare = 0.0;
       	try{
    		if(StringUtils.isNotBlank(newValue)){
        		fare = Double.parseDouble(newValue.trim().replace(",","."));
        	}
           	double value = ( fare - 32 )/1.8;
    		DecimalFormat df = new DecimalFormat("#.#");
    		df.setRoundingMode(RoundingMode.CEILING);
        	cel.setText(df.format(value));
       	}catch(NumberFormatException e){
       		cel.setText("NULL");
       	}

    	
    }

    void convMass(String newValue) {
   		double qt = 0.0;
   		try{
    		if(StringUtils.isNotBlank(newValue)){
       			qt = Double.parseDouble(newValue.trim().replace(",","."));
        	}
       		double output = qt*(masse.get(unitInMasse.getValue())/masse.get(unitOutMasse.getValue()));
    		DecimalFormat df = new DecimalFormat("#.###");
    		df.setRoundingMode(RoundingMode.CEILING);
       		outMasse.setText(df.format(output));
   		}catch(NumberFormatException e){
   			outMasse.setText("NULL");
   		}
    }
   
    void convVol(String newValue){
		double qt = 0.0;
		try{
	  		if(StringUtils.isNotBlank(newValue)){
       			qt = Double.parseDouble(newValue.trim().replace(",","."));
        	}    		
	  		double output = qt*(volume.get(unitInVol.getValue())/volume.get(unitOutVol.getValue()));
    		DecimalFormat df = new DecimalFormat("#.###");
    		df.setRoundingMode(RoundingMode.CEILING);
        	outVol.setText(df.format(output));
		}catch(NumberFormatException e){
			outVol.setText("NULL");
		}
    }
    
    void convMetricSystem(String newValue, boolean isInput, String dimension) {
		double qt = 0.0;
		double unitInput = 0.0;
		double unitOutput = 0.0;
		switch(dimension.toLowerCase()) {
		case "volume":
			if(isInput){
				unitInput = volume.get(newValue);
				unitOutput = volume.get(unitOutVol.getValue());
			}else{
				unitInput = volume.get(unitInVol.getValue());
				unitOutput = volume.get(newValue);
			}
			try{
				qt = Double.parseDouble(inVol.getText().trim().replace(",","."));
	    		double output = qt*(unitInput/unitOutput);
	    		outVol.setText(Double.toString(output));
			}catch(NumberFormatException e){
				outVol.setText("NULL");
			}   
			break;
		case "masse":
			if(isInput){
				unitInput = masse.get(newValue);
				unitOutput = masse.get(unitOutMasse.getValue());
			}else{
				unitInput = masse.get(unitInMasse.getValue());
				unitOutput = masse.get(newValue);
			}
    		try{

    			qt = Double.parseDouble(inMasse.getText().trim().replace(",","."));
        		double output = qt*(unitInput/unitOutput);
        		outMasse.setText(Double.toString(output));
    		}catch(NumberFormatException e){
    			outMasse.setText("NULL");
    		}    	
			break;
		default:
			break;
		}
		
	
    }


    
    
    void convertVol2Weight(String newValue, boolean isInput, String ingOrUnit){
    	String iUnit = "";
    	String oUnit = "";
       	String pickedItem = newValue;
       	
    	switch (ingOrUnit.toLowerCase()){
    	case "ingredient":
    		pickedItem = newValue;
    		oUnit = outputUnit.getValue();
    		iUnit = inputUnit.getValue();
    		break;
    	case "unit":
        	if(isInput){
        		iUnit = newValue;
        		oUnit = outputUnit.getValue();
        	}else{
        		iUnit = inputUnit.getValue();
        		oUnit = newValue;
        	}
           	pickedItem = items.getValue();
           	break;
        default:
    		oUnit = outputUnit.getValue();
    		iUnit = inputUnit.getValue();
           	pickedItem = items.getValue();
        	break;
    	}

       	double value= 0.0;
       	double inQt = 0.0;
       	try{
	  		if(StringUtils.isNotBlank(input.getText())){
       			inQt = Double.parseDouble(input.getText().trim().replace(",","."));
        	}
           	if(iUnit.contains("L") && oUnit.contains("L") ){
           		value = inQt * litre.get(iUnit) / litre.get(oUnit);
           	}else if (iUnit.contains("g") && oUnit.contains("g")){
           		value = inQt * gramme.get(iUnit) / gramme.get(oUnit);
            }else if (iUnit.contains("g")){
           		value = inQt * gramme.get(iUnit) / (litre.get(oUnit ) * masseVolumique.get(pickedItem));
           	}else{
           		value = inQt * litre.get(iUnit) * masseVolumique.get(pickedItem) / gramme.get(oUnit);
           	}
       		output.setText(Double.toString(value));
        	}catch(NumberFormatException e){
        		output.setText("NULL");
        	}
    }
   
    void convertVol2Weight(String newValue){
		String oUnit = outputUnit.getValue();
		String iUnit = inputUnit.getValue();
       	String pickedItem = items.getValue();
       	double value= 0.0;
       	double inQt = 0.0;
       	try{
	  		if(StringUtils.isNotBlank(newValue)){
       			inQt = Double.parseDouble(newValue.trim().replace(",","."));
        	}
	  		if(iUnit.contains("L") && oUnit.contains("L") ){
           		value = inQt * litre.get(iUnit) / litre.get(oUnit);
           	}else if (iUnit.contains("g") && oUnit.contains("g")){
           		value = inQt * gramme.get(iUnit) / gramme.get(oUnit);
            }else if (iUnit.contains("g")){
           		value = inQt * gramme.get(iUnit) / (litre.get(oUnit ) * masseVolumique.get(pickedItem));
           	}else{
           		value = inQt * litre.get(iUnit) * masseVolumique.get(pickedItem) / gramme.get(oUnit);
           	}
       		output.setText(Double.toString(value));
        	}catch(NumberFormatException e){
        		output.setText("NULL");
        	}
    }

    private void loadVol2WeightData() throws IOException, URISyntaxException{
		masseVolumique = getMapFromCSV("massevolumique.txt");
		items.getItems().addAll(masseVolumique.keySet().stream().sorted().collect(Collectors.toList()));
    	items.getSelectionModel().selectFirst();

		litre = getMapFromCSV("litre.txt");
		gramme = getMapFromCSV("gramme.txt");
    	inputUnit.getItems().addAll(litre.keySet());
    	inputUnit.getItems().addAll(gramme.keySet());
    	outputUnit.getItems().addAll(litre.keySet());
    	outputUnit.getItems().addAll(gramme.keySet());
    	inputUnit.getSelectionModel().selectFirst();
    	outputUnit.getSelectionModel().select(3);
    
    }
    
    private void loadMetConvData() throws IOException, URISyntaxException{
		volume = getMapFromCSV("volume.txt");
		masse = getMapFromCSV("masse.txt");
		unitInMasse.getItems().addAll(masse.keySet());
		unitOutMasse.getItems().addAll(masse.keySet());
		unitInVol.getItems().addAll(volume.keySet());
		unitOutVol.getItems().addAll(volume.keySet());
		unitInMasse.getSelectionModel().selectFirst();
		unitInVol.getSelectionModel().selectFirst();
		unitOutMasse.getSelectionModel().selectFirst();
		unitOutVol.getSelectionModel().selectFirst();

    }
    
    @FXML
    void initialize() throws IOException, URISyntaxException {
        assert items != null : "fx:id=\"item\" was not injected: check your FXML file 'MainView.fxml'.";
        assert input != null : "fx:id=\"input\" was not injected: check your FXML file 'MainView.fxml'.";
        assert output != null : "fx:id=\"output\" was not injected: check your FXML file 'MainView.fxml'.";
        assert inputUnit != null : "fx:id=\"inputUnit\" was not injected: check your FXML file 'MainView.fxml'.";
        assert outputUnit != null : "fx:id=\"outputUnit\" was not injected: check your FXML file 'MainView.fxml'.";
    	loadVol2WeightData();
    	loadMetConvData();


    }
    
	public static LinkedHashMap<String, Double> getMapFromCSV(final String filePath) throws IOException, URISyntaxException{

    	InputStream in = Controller.class.getResourceAsStream(RESOURCE_PATH+filePath);
    	
    	LinkedHashMap<String, Double> resultMap = new LinkedHashMap<String,Double>();
    	
    	BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    	String line;
    	while( (line = reader.readLine()) != null ){
    		resultMap.put(line.split(",")[0], Double.parseDouble(line.split(",")[1]));
    	}
    	reader.close();
    	
        return resultMap;
    }
    

}
