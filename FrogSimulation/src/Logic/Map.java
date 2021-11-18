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
public class Map {
    
    /**
     * tableau a deux dimensions de parcelles de grenouilles
     */
    private Matrice[][] map;
    
    /**
     * Matrice de Leslie pour le terrain
     */
    private Leslie les;
    
    /**
     * largeur du terrain en nombre de parcelles
     */
    private final int LENGTH = 30;
    
    /**
     * Probabilité qu'une grenouille se déplace sur une parcelle voisine chaque annees
     */
    private final float PROBA_DEPLACEMENT = 0.05f;
    
    /**
     * Constructeur de la map et initialisation de sa matrice de Leslie
     */
    public Map(){
        this.map = new Matrice[this.LENGTH][this.LENGTH];
        for (int y = 0; y < this.LENGTH; y++){
            for (int x = 0; x < this.LENGTH; x++){
                map[y][x] = new Matrice(x,y);
            }
        }
        this.les = new Leslie(0.01,0.7,700,6);
    }
    
    /**
     * recupere la parcelle aux coordonnées x, y
     * @param x numero de colonne de la parcelle
     * @param y numero de ligne de la parcelle
     * @return parcelle aux coordonnées x, y
     */
    public Matrice getMatriceAt(int x, int y){
        return map[y][x];
    }
    
    /**
     * Donne le nombre de parcelles voisines a une parcelle donnée
     * @param m parcelle initiale
     * @return nombre de parcelles voisine
     */
    public int getNbVoisins(Matrice m){
        int res = 4;
        int x = m.getColonne();
        int y = m.getLigne();
        if (x==0 || x==this.LENGTH-1) {
            res = res-1;
        }
        if (y==0 || y==this.LENGTH-1) {
            res = res-1;
        }
        return res;
    }
    
    /**
     * Donne la liste de tout les voisins d'une parcelle donnée
     * @param m parcelle initiale
     * @return tableau de parcelles qui sont voisines
     */
    public Matrice[] getVoisins(Matrice m){
        Matrice[] res = new Matrice[getNbVoisins(m)];
        int nb = 0;
        int x = m.getColonne();
        int y = m.getLigne();
        if (x>0){
            res[nb] = getMatriceAt(x-1,y);
            nb++;
        }
        if (x<this.LENGTH-1){
            res[nb] = getMatriceAt(x+1,y);
            nb++;
        }
        if (y>0){
            res[nb] = getMatriceAt(x,y-1);
            nb++;
        }
        if (y<this.LENGTH-1){
            res[nb] = getMatriceAt(x,y+1);
            nb++;
        }
        return res;
    }
    
    /**
     * Fait evoluer le terrain d'un an, avec toutes les grenouilles
     */
    public void evolve(){
        float nbVoisins = 0;
        for (int y = 0; y < this.LENGTH; y++){
            for (int x = 0; x < this.LENGTH; x++){
                nbVoisins = getNbVoisins(getMatriceAt(x,y));
                for(int i = 3; i < 7; i++){
                    for (Matrice m : getVoisins(getMatriceAt(x,y))){
                        m.getMatrice()[i] += getMatriceAt(x,y).getVal(i) *(this.PROBA_DEPLACEMENT/nbVoisins);
                        getMatriceAt(x,y).setVal(i,getMatriceAt(x,y).getVal((i))*(1-this.PROBA_DEPLACEMENT/nbVoisins));
                    }
                }
            }
        }
        for (int y = 0; y < this.LENGTH; y++){
            for (int x = 0; x < this.LENGTH; x++){
                getMatriceAt(x,y).setMatrice(les.produit(getMatriceAt(x,y)).getMatrice());
            }
        }
    }
    
    /**
     * Donne la somme de toutes les grenouilles sur le terrain
     * @return total de toutes les grenouilles
     */
    public float getSomme(){
        float res = 0;
        for (int y = 0; y < this.LENGTH; y++){
            for (int x = 0; x < this.LENGTH; x++){
                res += getMatriceAt(x,y).getSomme();
            }
        }
        return res;
    }
    
    /**
      * change le taux de survie d'un oeuf entre l'an 0 et l'an 1
      * @param s nouveau taux de survie
      */
    public void setSurvieOeuf(double s){
        this.les.setSurvieOeuf(s);
    }
    
    /**
     * change le taux de survie des grenouille de 1 an ou plus
     * @param s nauveau taux de survie
     */
    public void setSurvieAdulte(double s){
        this.les.setSurvieAdulte(s);
    }
    
    /**
     * change le taux de reproduction des grenouilles de plus de 2 ans
     * @param taux nombre d'oeuf que fera chaques grenouilles d'une parcelle
     */
    public void setReproduction(int taux){
        this.les.setReproduction(taux);
    }
    
    /**
     * change l'age maximum d'une grenouille
     * @param age nouvel age maximum des grenouilles
     */
    public void setAgeMax(int age){
        this.les.setAgeMax(age);
    }
    
    public double getSurvieOeuf(){
        return this.les.getSurvieOeuf();
    }
    
    public double getSurvieAdulte(){
        return this.les.getSurvieAdulte();
    }
    
    public int getTauxReproduction(){
        return this.les.getTauxReproduction();
    }

    public Leslie getLeslie() {
        return les;
    }
    
    
}
