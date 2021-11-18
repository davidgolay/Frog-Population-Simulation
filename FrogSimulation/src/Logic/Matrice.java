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
public class Matrice {
    
    /**
     * tableau de nombre de grenouille de chaques annee
     */
    private float[] matrice;
    
    /**
     * numero de la colonne de cette parcelle sur le terrain
     */
    private int x;
    
    /**
     * numero de la ligne de cette parcelle sur le terrain
     */
    private int y;
    
    /**
     * nombre de grenouilles adultes sur la parcelle
     */
    private float somme;
    
    /**
     * Constructeur de la parcelle
     * @param x numero de la ligne de la parcelle sur le terrain
     * @param y numero de la colonne de la parcelle sur le terrain
     */
    public Matrice(int x, int y){
        matrice = new float[7];
        this.x = x;
        this.y = y;
    }
    
    /**
     * Initialisation du nombre de grenouilles pour chaque age
     * @param a0 nombre d'oeuf de grenouille
     * @param a1 nombre de grenouille de 1 an
     * @param a2 nombre de grenouille de 2 an
     * @param a3 nombre de grenouille de 3 an
     * @param a4 nombre de grenouille de 4 an
     * @param a5 nombre de grenouille de 5 an
     * @param a6 nombre de grenouille de 6 an
     */
    public void initMatrice(float a0, float a1, float a2, float a3, float a4, float a5, float a6) {
        matrice[0] = a0;
        matrice[1] = a1;
        matrice[2] = a2;
        matrice[3] = a3;
        matrice[4] = a4;
        matrice[5] = a5;
        matrice[6] = a6;
        this.getSomme();
    }
    
    /**
     * Initialisation de 0 grenouilles dans la parcelle
     */
    public void initMatrice(){
        for (int i = 0; i < matrice.length; i++){
            matrice[i] = 0;
            somme = 0;
        }
    }
    
    /**
     * Remplacement de la martice actuelle avec celle donnée
     * @param a nouvelle matrice
     */
    public void setMatrice(float[] a){
        matrice = a;
        this.getSomme();
    }
    
    /**
     * Remplacement du nombre de grenouille pour un age donné
     * @param age age des grenouille à remplacer
     * @param val nouveau nombre de grenouilles
     */
    public void setVal(int age, float val){
        matrice[age] = val;
        this.getSomme();
    }
    
    /**
     * Donne le nombre de grenouille pour un age donné
     * @param age age des grenouille
     * @return 
     */
    public float getVal(int age){
        return matrice[age];
    }
    
    /**
     * Donne le tableau de grenouille sur la parcelle
     * @return Tableau de grenouille de chaque age
     */
    public float[] getMatrice(){
        return matrice;
    }
    
    /**
     * Donne la somme des grenouilles adultes sur une parcelle (max 50000)
     * @return nombre de grenouilles adultes
     */
    public float getSomme(){
        float res = 0;
        for (int  i = 1; i < matrice.length; i++) {
            res += matrice[i];
        }
        somme = res;
        return res;
    }
    /**
     * Donne la ligne de la parcelle
     * @return ligne de la parcelle
     */
    public int getLigne(){
        return y;
    }
    
    /**
     * Donne la colonne de la parcelle
     * @return colonne de la parcelle
     */
    public int getColonne(){
        return x;
    }
    
    /**
     * Normalise le nombre de grenouilles sur une parcelle
     * Tue les grenouilles de 1 an si total >50000
     */
    public void normalize(){
        if (this.getSomme() > 50000){
            this.setVal(1,0);
            this.setVal(1,50000-this.getSomme());
        }
    }
}
