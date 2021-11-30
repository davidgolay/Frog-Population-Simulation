/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frogsimulation;

import javafx.scene.shape.Rectangle;

/**
 *
 * @author mp671721
 */
public class RectangleHover extends Rectangle{
    private float sumGrenouille;
    
    public float GetSumGrenouille()
    {
        return sumGrenouille;
    }
    
    public RectangleHover(int posX, int posY, int sizeCase, float sumGrenouille)
    {
        super(posX*sizeCase, posY*sizeCase, sizeCase, sizeCase);
        this.sumGrenouille = sumGrenouille;
    }
}
