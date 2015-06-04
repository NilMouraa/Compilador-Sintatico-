/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author Aluno
 */
public class Token {

    String tipo;
    String valor;
    private int linha;

    public Token(String tipo, String valor, int linha) {
        this.tipo = tipo;
        this.valor = valor;
        this.linha = linha;
    }

//    public token(){
//    
//    }
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
//        return "<" + tipo + " " + valor + ">";
        return "<" + " " + tipo + " , " + valor + " >";
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

}
