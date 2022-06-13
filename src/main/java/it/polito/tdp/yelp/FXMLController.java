/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
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

    @FXML // fx:id="btnLocaleMigliore"
    private Button btnLocaleMigliore; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML // fx:id="cmbAnno"
    private ComboBox<Integer> cmbAnno; // Value injected by FXMLLoader

    @FXML // fx:id="cmbLocale"
    private ComboBox<Business> cmbLocale; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	this.txtResult.clear();
    	Business partenza = this.cmbLocale.getValue();
    	
    	if(partenza == null) {
    		this.txtResult.setText("Errore: devi selezionare un locale di partenza!");
    		return;
    	}
    	
    	double x = 0;
    	try {
        	Double.parseDouble(this.txtX.getText());  
    	}
    	catch(NumberFormatException e) {
    		e.printStackTrace();
    		this.txtResult.setText("Errore: inserire un valore compreso tra 0 e 1!");
    		return;
    	}
    	
    	// TODO controlla che esista locale migliore
    	if(this.model.getLocaleMigliore() == null) {
        	this.txtResult.setText("Errore: devi prima trovare il locale migliore!");
    	}
    	this.model.calcolaPercorso(partenza, x);
    	this.txtResult.setText(this.model.getPercorso().toString());
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	// ripulisco l'area di testo del risultato
    	this.txtResult.clear();
    	
    	// salvo città e anno
    	String citta = this.cmbCitta.getValue();
    	Integer anno = this.cmbAnno.getValue();
    	
    	// verifico correttezza dati inseriti
    	if(citta == null) {
    		this.txtResult.setText("Errore: devi selezionare una città!");
    		return;
    	}
    	if(anno == null) {
    		this.txtResult.setText("Errore: devi selezionare un anno!");
    		return;
    	}
    	
    	// creo il grafo
    	String msg = this.model.creaGrafo(citta, anno);
    	
    	// stampo il risultato
    	this.txtResult.setText(msg);
    	
    	// riempio la tendina dei locali
    	this.cmbLocale.getItems().clear();
    	this.cmbLocale.getItems().addAll(this.model.getVertici());
    }

    @FXML
    void doLocaleMigliore(ActionEvent event) {
    	// ripulisco l'area di testo del risultato
    	this.txtResult.clear();
    	
    	try {	// alternativa a try catch: creare metodo bool grafoCreato() nella classe Model
        	// trovo il locale migliore
    		this.model.localeMigliore();
        	Business b = this.model.getLocaleMigliore();
        	
        	// stampo il risultato
        	this.txtResult.setText(String.format("LOCALE MIGLIORE: %s\n", b));
    	}
    	catch(NullPointerException e) {
    		// stampo errore
    		this.txtResult.setText("Errore: Devi prima creare il grafo!");
    		return;
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnLocaleMigliore != null : "fx:id=\"btnLocaleMigliore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbLocale != null : "fx:id=\"cmbLocale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	// riempio la tendina delle città
    	this.cmbCitta.getItems().addAll(this.model.getAllCitta());
    	
    	// riempio la tendina degli anni
    	for(int anno=2005;anno<=2013;anno++) {
        	this.cmbAnno.getItems().add(anno);
    	}
    	
    }
}
