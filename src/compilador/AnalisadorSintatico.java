/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;



import compilador.Erros.MinhaException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Nil
 */
public class AnalisadorSintatico {

    LinkedHashMap<Integer, ArrayList<Token>> tokens;
    ArrayList<Token> auxComandos = new ArrayList();
    ArvoreBinaria<Token> arvore01 = new ArvoreBinaria(new Token("", ""));
    Stack<String> pilha = new Stack<>();

    public AnalisadorSintatico(LinkedHashMap<Integer, ArrayList<Token>> resulLexico) {
        this.tokens = resulLexico;

    }

    void Analisar() {
        boolean Eprograma = false;
//        Token t = new Token("endprog", "fim");
//        for (int i = 1; 0 < tokens.size(); i++) {
//            for (int j = 0; j < tokens.get(i).size(); j++) {
//                
//               //aux.add(tokens.get(i).get(j).tipo);
//                System.out.println(tokens.get(i).get(j).tipo);
//                
//            }
//        }
//        for (int i = 0; i < aux.size(); i++){
//            System.out.println(aux.get(i));
//        }
        for (Map.Entry<Integer, ArrayList<Token>> entrySet : tokens.entrySet()) {
            Integer key = entrySet.getKey();
            ArrayList<Token> value = entrySet.getValue();
            for (Token aux : value) {
                if (aux.getTipo().equals("endprog")) {
                    Eprograma = true;
                    comandos(auxComandos);

                } else {
                    auxComandos.add(aux);
                }
            }

        }
        if(!Eprograma){
            MinhaException e0 = new MinhaException("Erro: Não é um Programa!");
        }

    }

    void comandos(ArrayList<Token> comandos) {
        for (Token comando : comandos) {
            
            if(comando.tipo.equals("cond")){
               //verifica o fim cond e manda pra condicao() 
            }
            else if(comando.tipo.equals("whileloop")){
                //verifica o fim whileloop e manda pra condicao() 
            }
            else if(comando.tipo.equals("forloop")){
                //verifica o fim forloop e manda pra condicao() 
            }
            else if(comando.tipo.equals("id")){
                // manda pra condicao() o que esta depois do token equal
            }
            else if(comando.tipo.equals("vetor")){
                //manda pra condicao() o que esta depois do token vector
            }
            else if(comando.tipo.equals("func")){
                //manda os parametros para param, comandos para comandos  até encontrar fimFunçao
            }
        }
    }

    void condicao() {

    }

    void expressao() {

    }

    void expressaoprec() {

    }

    void termo() {

    }

    void vetor() {

    }

    void decimal() {

    }

    void params() {

    }

    void declvetor() {

    }

}
