/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.eightminutes.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JPanel;

/**
 *
 * @author ricardopereira
 */
public class PanelOpcoes extends JPanel {
    
    private DataController controller;
    
    public PanelOpcoes(DataController controller) {
        this.controller = controller;
        
        // Teste
        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.setBackground(Color.BLUE);
        this.setPreferredSize(new Dimension(800,100));
        this.setMinimumSize(new Dimension(800,100));
        this.setMaximumSize(new Dimension(800,100));
    }
    
}
