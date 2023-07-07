/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<String> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {
    	String actorString = this.boxAttore.getValue();
    	if(actorString == null) {
    		this.txtResult.setText("Inserire un attore nella box");
    		return;
    	}
    	String actorIDs = actorString.substring(actorString.indexOf("(")+1, actorString.lastIndexOf(")"));
    	Integer actorId = Integer.parseInt(actorIDs);
    	List<Actor>simili = new ArrayList<>(model.attoriSimili(actorId));
    	String s = "Attori simili : \n";
    	for(Actor x : simili) {
    		s += x.getLastName()+" "+x.getFirstName()+" ("+x.getId()+") \n";
    	}
    	this.txtResult.setText(s);
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String genre = this.boxGenere.getValue();
    	if(genre == null) {
    		this.txtResult.setText("Inserire un genere nella box");
    		return;
    	}
    	String s = model.creaGrafo(genre);
    	this.txtResult.setText(s);
    	
    	List<Actor>vertici = new ArrayList<>(model.getGrafo().vertexSet());
    	Collections.sort(vertici);
    	for(Actor a : vertici) {
    		String actorString =  a.getLastName()+" "+a.getFirstName()+" ("+a.getId()+")";
    		this.boxAttore.getItems().add(actorString);
    	}
    }

    @FXML
    void doSimulazione(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	for(String g : model.getAllGeneri()) {
    		this.boxGenere.getItems().add(g);
    	}
    }
}
