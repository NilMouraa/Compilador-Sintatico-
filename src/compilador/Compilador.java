/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nil Martins
 */
public class Compilador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            LinkedHashMap<Integer, ArrayList<Token>> resulLexico;
            
            AnalisadorLexico a = new AnalisadorLexico("");
            resulLexico = a.Analisar();
            
            AnalisadorSintatico b = new AnalisadorSintatico(resulLexico);
            b.Analisar();
            
        } catch (IOException ex) {
            Logger.getLogger(Compilador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
