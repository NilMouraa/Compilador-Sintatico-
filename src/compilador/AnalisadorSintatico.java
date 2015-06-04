/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import compilador.Erros.MinhaException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Nil
 */
public class AnalisadorSintatico {

    int count = 0;
    Token coringa = null;
    ArrayList<MinhaException> errosSintaticos = new ArrayList<>();
    LinkedHashMap<Integer, ArrayList<Token>> tokens;
    ArrayList<Token> auxComandos = new ArrayList();
    ArvoreBinaria<Token> arvore01 = new ArvoreBinaria();
    MinhaException exc;
    LinkedHashSet<String> msg = new LinkedHashSet<>();
    private Object Colletions;

    public AnalisadorSintatico(LinkedHashMap<Integer, ArrayList<Token>> resulLexico) {
        this.tokens = resulLexico;

    }
    
    public void salvarErros(String erroSintatico, int line){
        exc = new MinhaException();
        exc.setErro(erroSintatico);
        exc.setLinha(line);
        
        errosSintaticos.add(exc);
    }

    void Analisar() {
        boolean Eprograma   = false;
        boolean ExisteErro  = false;

        voltar:
        for (Map.Entry<Integer, ArrayList<Token>> entrySet : tokens.entrySet()) {
            Integer key = entrySet.getKey();
            ArrayList<Token> value = entrySet.getValue();
            for (Token aux : value) {
                if (aux.getTipo().equals("endprog")) {

                    Eprograma = true;
                    comandos(auxComandos);

                } else if (!aux.getTipo().equals("endline") && Eprograma) {
                    salvarErros("Erro: Token(s) após Token 'Fim'",key);
                    break voltar;
                } else {
                    auxComandos.add(aux);
                }
            }

        }
        if (!Eprograma) {
            salvarErros("Erro: Não é Programa! (Faltou 'fim')",0);
        }

        Collections.sort(errosSintaticos);
        for (MinhaException erros : errosSintaticos) {
            msg.add(erros.getErro() + erros.getLinha());
            
        }

        if (errosSintaticos.size() > 0) {
            ExisteErro = true;
        }
        System.out.println("#######################################################################################");
        if (ExisteErro) {
            
            System.out.println("Erros sintáticos: ");
            for (MinhaException errosSintatico : errosSintaticos) {
                System.err.println(errosSintatico.getErro()+"- linha "+errosSintatico.linha);   
            }
        } else if (!ExisteErro) {
            System.out.println("Sem erros Sintáticos!");
            System.out.println("#######################################################################################");
            //gerar arvrore
        }
        
    }
    
//comandos -> se condicao então comandos fim-se
//| se condicao então comandos senão comandos fim-se
//| enquanto condicao faça comandos fim-enquanto
//| para id de num até num faça comandos fim-para
//| id = condicao
//| vetor = condicao
//| funcao id(params) comandos fim-funcao
//| declvetor
        
    void comandos(ArrayList<Token> cmd) {
       
        boolean Ecomando        = false;
        boolean achouComando    = false;
        boolean ESenao          = false;
        boolean achouCondicao   = false;
        
        ArrayList<Token> auxCondicao    = new ArrayList<>();
        ArrayList<Token> auxComandos    = new ArrayList<>();
        ArrayList<Token> aux            = new ArrayList();
        Stack<Token> processos         = new Stack<>();
        
        

        for (int i = 0; i < cmd.size(); i++) {
            
            if (cmd.get(i).tipo.equals("cond")) {
                processos.clear();
                processos.push(cmd.get(i));
                
                i++;
                while(i<cmd.size() && !(cmd.get(i).tipo.equals("initcond")) && !(cmd.get(i).tipo.equals("endline"))){
                    auxCondicao.add(cmd.get(i));
                    i++;
                }
                if(cmd.get(i).tipo.equals("endline")){
                    salvarErros("Erro: Estrutura Condicional Mal Formada! (Faltou 'então')", cmd.get(i-1).getLinha());
                }
                condicao(auxCondicao);
                auxCondicao.clear();
                i++;
                
                while(i < cmd.size()){
                    if(cmd.get(i).tipo.equals("cond")){
                        processos.push(cmd.get(i));
                    }   
                    else if(cmd.get(i).tipo.equals("endcond")){
                        processos.pop();
                        if(processos.isEmpty()){
                            if((i+1) < cmd.size() && !(cmd.get(i+1).tipo.equals("endline"))){
                                salvarErros("Erro: Bloco Mal Formado! (Após Comando Deve Haver Quebra de Linha)", cmd.get(i).getLinha());
                            }
                            break;
                        }
                    }
                    else if(cmd.get(i).tipo.equals("altcond")){
                        if(processos.size() == 1){
                            comandos(auxComandos);
                            auxComandos.clear();
                            i++;
                        }
                    }
                    auxComandos.add(cmd.get(i));
                    i++;
                }
                if(!processos.isEmpty()){
                    salvarErros("Erro: Estrutura Condicional Mal Formada! (Faltou fechamento "+processos.peek().getValor()+")", processos.peek().getLinha());
                }
                processos.clear();
                comandos(auxComandos);
                auxComandos.clear();

            } else if (cmd.get(i).tipo.equals("whileloop")) {
                //verifica o fim whileloop e manda pra condicao() 
            } else if (cmd.get(i).tipo.equals("forloop")) {
                //verifica o fim forloop e manda pra condicao() 
            } else if (cmd.get(i).tipo.equals("id")) {
                // manda pra condicao() o que esta depois do token equal
            } else if (cmd.get(i).tipo.equals("vetor")) {
                //manda pra condicao() o que esta depois do token vector
            } else if (cmd.get(i).tipo.equals("func")) {
                //manda os parametros para param, comandos para comandos  até encontrar fimFunçao
            }
        }
    }

    void condicao(ArrayList<Token> condicao) {

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
