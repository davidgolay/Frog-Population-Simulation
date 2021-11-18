/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author td796033
 */
public class Leslie {
    
    /**
     * tableau correspondant a la matrice de Leslie
     */
    private double[][] les;
    
    /**
     * taux de survie d'un oeuf entre l'an 0 et l'an 1
     */
    private double survieOeuf;
    
    /**
     * taux de survie des grenouille de 1 an ou plus
     */
    private double survieAdulte;
    
    /**
     * taux de reproduction des grenouilles de plus de 2 ans
     */
    private int tauxReproduction;
    
    /**
     * age maximum d'une grenouille
     */
    private int ageMax;
    
    /**
     * Constructeur de la matrice de Leslie
     * @param survieOeuf taux de survie d'un oeuf entre l'an 0 et l'an 1
     * @param survieAdulte taux de survie des grenouille de 1 an ou plus
     * @param taux taux de reproduction des grenouilles de plus de 2 ans
     * @param ageMax age maximum d'une grenouille
     */
    public Leslie(double survieOeuf, double survieAdulte, int taux, int ageMax){
        this.survieOeuf = survieOeuf;
        this.survieAdulte = survieAdulte;
        this.tauxReproduction = taux;
        this.ageMax = ageMax;
        this.les = new double[][]
            {{0,0,0,700,700,700,700},
            {0.01,0,0,0,0,0,0},
            {0,0.7,0,0,0,0,0},
            {0,0,0.7,0,0,0,0},
            {0,0,0,0.7,0,0,0},
            {0,0,0,0,0.7,0,0},
            {0,0,0,0,0,0.7,0}};
        
        setSurvieOeuf(survieOeuf);
        setSurvieAdulte(survieAdulte);
        setReproduction(taux);
        setAgeMax(ageMax);
    }
    
    /**
     * fait grandire, mourir et naitre les grenouille sur une parcelle
     * @param m parcelle qui doit grandir
     * @return la nouvelle parcelle une fois évoluée
     */
     public Matrice produit(Matrice m){
        Matrice res = new Matrice(m.getColonne(),m.getLigne());
        float somme;
        for (int i = 0; i < 7; i++){
            somme = 0;
            for (int j = 0; j < 7; j++) {
                somme += les[i][j]*m.getVal(j);
            }
            res.setVal(i, somme);
        }
        res.normalize();
        return res;
    }
     
     /**
      * change le taux de survie d'un oeuf entre l'an 0 et l'an 1
      * @param s nouveau taux de survie
      */
    public final void setSurvieOeuf(double s){
        this.les[1][0] = s;
        this.survieOeuf = s;
    }
    
    /**
     * change le taux de survie des grenouille de 1 an ou plus
     * @param s nauveau taux de survie
     */
    public final void setSurvieAdulte(double s){
        for (int i = 2; i < this.ageMax; i++){
            this.les[i][i-1] = s;
        }
        this.survieAdulte = s;
    }
    
    /**
     * change le taux de reproduction des grenouilles de plus de 2 ans
     * @param taux nombre d'oeuf que fera chaques grenouilles d'une parcelle
     */
    public final void setReproduction(int taux){
        for (int i = 3; i < 7; i++){
            this.les[0][i] = taux;
        }
        this.tauxReproduction = taux;
    }
    
    /**
     * change l'age maximum d'une grenouille
     * @param age nouvel age maximum des grenouilles
     */
    public final void setAgeMax(int age){
        if (age > 6) {
            age = 6;
        }
        if (age < 1) {
            age = 1;
        }
        for (int i = age+1; i < 6; i++){
            this.les[i][i-1] = 0;
        }
        this.ageMax = age;
    }

    public double getSurvieOeuf() {
        return survieOeuf;
    }

    public double getSurvieAdulte() {
        return survieAdulte;
    }

    public int getTauxReproduction() {
        return tauxReproduction;
    }

    public int getAgeMax() {
        return ageMax;
    }
    
    
}
