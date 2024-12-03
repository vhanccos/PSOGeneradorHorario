/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horariosinterface;

import javax.swing.SwingUtilities;

/**
 *
 * @author arles
 */
public class HorariosInterface {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
          SwingUtilities.invokeLater(() -> {
            CrudInterface app = new CrudInterface("C:\\pity\\redes\\cursos.csv");
            app.setVisible(true);
        });
        
    }
    
}
