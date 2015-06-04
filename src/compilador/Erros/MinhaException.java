package compilador.Erros;

import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nil
 */
public class MinhaException implements Comparable<MinhaException>{
    
    private String erro;
    public int linha;
    
    

    public MinhaException(String erro, int linha) {
        this.erro = erro;
        this.linha = linha;
    }

    public MinhaException() {
    }
    
   
    /**
     * @return the erro
     */
    
    public String getErro() {
        return erro;
    }

    /**
     * @param erro the erro to set
     */
    public void setErro(String erro) {
        this.erro = erro;
    }

    /**
     * @return the linha
     */
    public int getLinha() {
        return linha;
    }

    /**
     * @param linha the linha to set
     */
    public void setLinha(int linha) {
        this.linha = linha;
    }
   
    
    @Override
    public int compareTo(MinhaException o) {
        if(this.linha < o.linha)
            return 1;
        else if(this.linha > o.linha)
            return 2;
        else 
            return 0;
    }
    
}
