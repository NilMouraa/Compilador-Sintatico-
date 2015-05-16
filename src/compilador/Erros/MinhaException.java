package compilador.Erros;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nil
 */
public class MinhaException extends Exception{
    
    public MinhaException(String msg){     
     System.err.println(msg);  
   }     
}
