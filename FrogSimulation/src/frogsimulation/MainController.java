/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frogsimulation;

import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
    private Spinner<Integer> spinnerAgeMax;
    
    @FXML
    private TextField textBoxTauxReproduction;
    
    
    @FXML
    private GridPane containerEchelle;

    /**
     * methode appelé lors que l'user clique sur le bouton "simuler"
     * @param event 
     */
    @FXML
    private void SimulateClicked(ActionEvent event) {
        double tauxSurvieOeuf = Double.valueOf(textBox_SurvieOeuf.getText());
        double tauxSurvieAdulte = Double.valueOf(textBox_SurvieAdulte.getText());
        int tauxReproduction = Integer.valueOf(textBoxTauxReproduction.getText());
        
        marais.getLeslie().setSurvieOeuf(tauxSurvieOeuf);
        marais.getLeslie().setSurvieAdulte(tauxSurvieAdulte);
        marais.getLeslie().setReproduction(tauxReproduction);
        marais.getLeslie().setAgeMax(spinnerAgeMax.getValue());
        
        int iteration;
        iteration = Integer.valueOf(textBoxSteps.getText());
        Simulate(iteration);
    }
    
    /**
     * Methode appelée quand l'user clique sur le bouton "reset"
     * @param event 
     */
    @FXML
    void ResetClicked(ActionEvent event) {
        Reset();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Reset();
        DrawScale();
    }
    
    /**
     * Cette méthode permet de dessiner l'echelle des densité de grenouille
     */
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
        UpdateIHMfields();
        containerGrid.getChildren().clear();
        Simulate(25);
    }
    
    /**
     * Cette méthode permet d'initiliser les pa
     */
    private void InitFields(){
        
        double tauxSurvieOeuf = marais.getLeslie().getSurvieOeuf();
        double tauxSurvieAdulte = marais.getLeslie().getSurvieAdulte();
        int tauxReproduction = marais.getLeslie().getTauxReproduction();     
        
        textBoxTauxReproduction.setText(String.valueOf(tauxReproduction));
        textBox_SurvieAdulte.setText(String.valueOf(tauxSurvieAdulte));
        textBox_SurvieOeuf.setText(String.valueOf(tauxSurvieOeuf));
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, marais.getLeslie().getAgeMax(), marais.getLeslie().getAgeMax());
        spinnerAgeMax.setValueFactory(valueFactory);     
    }
    

    /**
     * Cette méthode permet de génnérer le contenu de la grille représentant le Marais
     */
    private void UpdateGridDisplay(){
        
        //on vide la grille
        containerGrid.getChildren().clear();
        
        float densityAt;
        
        //on parcout toute les parcelles
        for(int y = 0; y<sizeMarais; y++) {
            for (int x = 0; x<sizeMarais; x++) {
                //on récupert la somme des grenouille dans un parcelle donnée
                densityAt = marais.getMatriceAt(x, y).getSomme();
                RectangleHover rect = new RectangleHover(x, y, sizeCase, densityAt);
                //si un rectangle (case representant une parcelle de la grille) est cliqué on donne la somme de grenouille dans cette case
                rect.setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent t) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Case");
                        String message = "Nombre de grenouille: " + Float.toString(rect.GetSumGrenouille());
                        alert.setContentText(message);
                        alert.showAndWait();
                    }
                });
                rect.setFill(GenerateColorFromDensity(densityAt));
                rect.setStroke(Color.GRAY);
                rect.setStrokeWidth(1);
                containerGrid.getChildren().add(rect);
            }
        }    
    }
    
    /**
     * Cette méthode update les champs textuels IHM calculés
     */
    private void UpdateIHMfields(){
        labelSomme.setText(Float.toString(sommeGlobale));
        labelAnneeSimulee.setText(Integer.toString(anneesSimulees));
    }
    
    /**
     * Cette méthode permet de générer une couleur à partir d'une densité (de grenouille par parcelle).
     * @param density densité de grenouille à mapper en couleur
     * @return couleur à afficher pour la desnité donnée
     */
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
            //Parfois, la densité d'une parcelle donnée dépasse à peine 50 000 grenouilles
            // Plus précisement, la densité dépasse 50000 de quelques milièmes de grenouille seulement : 0.004 grenouille en plus)
            mappedColor = new Color(1, 0, 0, 1); //dans ce cas là on map la couleur en rouge
        }
        
        return mappedColor;
    }
    
    /**
     * Cette méthode permet de mapper une valeur dont le minimum sera toujours 0.
     * Cette méthode permet en autre de mapper une valeur sur une echelle initiale de 0 à 50000 (total de grenouilles d'une parcelle) sur une nouvelle échelle de 0 à 1 (pour la colorisation)
     * @param valueToMap valeur que l'on souhaite mapper/transformer dans une autre échelle
     * @param maxValueIn valeur maximum de l'échelle de la valeur que l'on souhaite mapper
     * @param maxValueOut valeur maximum de l'échelle souhaitée
     * @return la valeur mappée sur l'échelle souhaitée.
     */
    private float MapToRange(float valueToMap, float maxValueIn, float maxValueOut){       
        float ratio = maxValueIn / maxValueOut;    
        float mappedValue = valueToMap / ratio;
        return mappedValue;
    }
    
    /**
     * Cette méthode permet de simulée l'évolution de la population de grenouilles sur tout les parcelles
     * L'entier itération passé en paramètre correspond au nombre d'année que lon souhaite simuler
     * @param iteration nombre d'année à simuler
     */
    public void Simulate(int iteration){
        for(int a = 0; a<iteration; a++){
            
            //évolution d'une année du Marais
            marais.evolve();
            
            //update des attributs
            sommeGlobale = marais.getSomme();
            annee++;
            anneesSimulees++;
            
            //Mis à jour de l'IHM
            UpdateGridDisplay();
            UpdateIHMfields();
            
            //affichage console du nouveau nombre total de groenouille dans le marais
            System.out.println(sommeGlobale);
        } 
    }
    
}
