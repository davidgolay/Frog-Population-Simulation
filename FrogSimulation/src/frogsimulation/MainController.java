/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frogsimulation;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.Map;

/**
 *
 * @author David
 */
public class MainController implements Initializable {
    
    private int sizeMarais = 30;
    
    private int sizeCase = 20;
    
    private Map marais;
    
    private float sommeGlobale;
    
    private int annee;
    
    private int anneesSimulees;
    
    @FXML
    private Pane containerGrid;
    
    @FXML
    private Label labelSomme;
    
    @FXML
    private Label labelAnneeSimulee;
    
    @FXML
    private TextField textBoxSteps;
    
    @FXML
    private TextField textBox_SurvieOeuf;

    @FXML
    private TextField textBox_SurvieAdulte;
    
    @FXML
    private TextField textBoxTauxReproduction;
    
    
    @FXML
    private GridPane containerEchelle;

    
    @FXML
    private void SimulateClicked(ActionEvent event) {
        System.out.println("Simulation!");

        double tauxSurvieOeuf = Double.valueOf(textBox_SurvieOeuf.getText());
        double tauxSurvieAdulte = Double.valueOf(textBox_SurvieAdulte.getText());
        int tauxReproduction = Integer.valueOf(textBoxTauxReproduction.getText());
        
        marais.getLeslie().setSurvieOeuf(tauxSurvieOeuf);
        marais.getLeslie().setSurvieAdulte(tauxSurvieAdulte);
        marais.getLeslie().setReproduction(tauxReproduction);
        
        int iteration;
        iteration = Integer.valueOf(textBoxSteps.getText());
        Simulate(iteration);
    }
    
    
    @FXML
    void ResetClicked(ActionEvent event) {
        Reset();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        marais = new Map();
        sommeGlobale = 0;
        anneesSimulees = 0;
        annee = -25;
        marais.getMatriceAt(2, 3).setVal(3, 50);
        InitFields();
        DrawScale();
        Simulate(25);
    }
    
    private void DrawScale(){
        float densityAt;
        float StepWanted = 10;
        float SizeCaseEchelle = 70;
      
        for(int x = 0; x<StepWanted; x++) {
                VBox v = new VBox();
                
                //v.setVgrow(containerEchelle, Priority.ALWAYS);
                densityAt = MapToRange(x, StepWanted, 50000);
                Label labelValue = new Label(String.valueOf(densityAt));
                Rectangle r = new Rectangle(0, 0, SizeCaseEchelle, SizeCaseEchelle/2);
                r.setFill(GenerateColorFromDensity(densityAt));
                r.setStroke(Color.ALICEBLUE);
                r.setStrokeWidth(1);
                v.getChildren().addAll(labelValue, r);

                containerEchelle.addColumn(x, v);        
        }
    }
    
    private void Reset(){
        
        marais = new Map();
        sommeGlobale = 0;
        anneesSimulees = 0;
        annee = -25;
        marais.getMatriceAt(2, 3).setVal(3, 50);
        InitFields();
        containerGrid.getChildren().clear();
        Simulate(25);
    }
    
    private void InitFields(){
        
        double tauxSurvieOeuf = marais.getLeslie().getSurvieOeuf();
        double tauxSurvieAdulte = marais.getLeslie().getSurvieAdulte();
        int tauxReproduction = marais.getLeslie().getTauxReproduction();     
        
        textBoxTauxReproduction.setText(String.valueOf(tauxReproduction));
        textBox_SurvieAdulte.setText(String.valueOf(tauxSurvieAdulte));
        textBox_SurvieOeuf.setText(String.valueOf(tauxSurvieOeuf));
        
        UpdateIHMfields();
    }
    

    
    private void UpdateGridDisplay(){
        
        float densityAt;
        
        for(int y = 0; y<sizeMarais; y++) {
            for (int x = 0; x<sizeMarais; x++) {
                Rectangle r = new Rectangle(x*sizeCase, y*sizeCase, sizeCase, sizeCase);
                densityAt = marais.getMatriceAt(x, y).getSomme();
                r.setFill(GenerateColorFromDensity(densityAt));
                r.setStroke(Color.GRAY);
                r.setStrokeWidth(1);
                containerGrid.getChildren().add(r);
            }
        }    
    }
    
    private void UpdateIHMfields(){
        labelSomme.setText(Float.toString(sommeGlobale));
        labelAnneeSimulee.setText(Integer.toString(anneesSimulees));
    }
    
    private Color GenerateColorFromDensity(float density){ 
        
        Color mappedColor;
        float normalizedDensity = MapToRange(density, 50000, 1);
        
        float R = 1;
        float G = 1 - normalizedDensity;
        float B = 0;
        float A = normalizedDensity;
        
        //Si il y a moins de 50 000 grenouilles sur la parcelle
        if(normalizedDensity <= 1){
            if(normalizedDensity >= 0)
            {
                //si il y a entre 0 et 50 000 grenouilles            
                mappedColor = new Color(R, G, B, A); // coloration classique
            }
            else
            {
                // si il y a moins de 0 grenouille par parcelle
                mappedColor = Color.BLUE; // la case devient bleu
            }          
        } 
        else 
        {
            System.out.println("PLUS DE 50 000 Grenouilles: " + normalizedDensity);
            float overDensity = normalizedDensity - 1;
            mappedColor = new Color(1, 0, 0, 1);
            //mappedColor = Color.BLUE; // la case devient bleu
        }
        
        return mappedColor;
    }
    
    private float MapToRange(float valueToMap, float maxValueIn, float maxValueOut){       
        float ratio = maxValueIn / maxValueOut;    
        float mappedValue = valueToMap / ratio;
        return mappedValue;
    }
    
    public void Simulate(int iteration){
        for(int a = 0; a<iteration; a++){
            marais.evolve();
            sommeGlobale = marais.getSomme();
            annee++;
            anneesSimulees++;
            UpdateGridDisplay();
            UpdateIHMfields();
            System.out.println(sommeGlobale);
        } 
    }
    
}
