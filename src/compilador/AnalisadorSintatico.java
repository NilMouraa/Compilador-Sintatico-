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
                if (aux.tipo.equals("endprog")) {

                    Eprograma = true;
                    comandos(auxComandos);

                } else if (!aux.tipo.equals("endline") && Eprograma) {
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
//| secondicao então comandos senão comandos fim-se
//| enenquanto condicao faça comandos fim-enquanto
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
                    salvarErros("Erro: Estrutura Condicional 'if' Mal Formada! (Faltou 'então')", cmd.get(i-1).getLinha());
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
                    salvarErros("Erro: Estrutura Condicional 'if' Mal Formada! (Faltou fechamento "+processos.peek().getValor()+")", processos.peek().getLinha());
                }
                processos.clear();
                comandos(auxComandos);
                auxComandos.clear();
                
            } 
//| enenquanto condicao faça comandos fim-enquanto
            else if (cmd.get(i).tipo.equals("whileloop")) {
                //verifica o fim whileloop e manda pra condicao() 
                processos.clear();
                processos.push(cmd.get(i));
                i++;
                  
                while(i < cmd.size() && !(cmd.get(i).tipo.equals("initforloop")) && !(cmd.get(i).tipo.equals("endline"))){
                   aux.add(cmd.get(i));
                   i++;
                }
                if(cmd.get(i).tipo.equals("endline")){
                    salvarErros("Erro: Estrutura de Repetição 'While' Mal Formada! (Faltou 'Faça')", cmd.get(i-1).getLinha());
                }
                
                condicao(aux);
                aux.clear();
                
                i++;
                
                while (i < cmd.size()) {
                    if (cmd.get(i).tipo.equals("whileloop")) {
                        processos.push(cmd.get(i));
                    } else if (cmd.get(i).tipo.equals("endwhileloop")) {
                        processos.pop();
                        if (processos.isEmpty()) {
                            if ((i + 1) < cmd.size() && !cmd.get(i+1).tipo.equals("endline")) {
                                salvarErros("Erro: Bloco Mal Formado! (Após Comando Deve Haver Quebra de Linha", cmd.get(i).getLinha());

                            }
                            break;
                        }
                    }
                    auxComandos.add(cmd.get(i));
                    i++;
                }
                if (!processos.isEmpty()) {
                    salvarErros("Erro: Estrutura de Repetição 'While' Mal Formada! (Faltou fechamento "+processos.peek().getValor()+")", processos.peek().getLinha());
                }
                
                comandos(cmd);
                cmd.clear();
                
            } 
            //| para id de num até num faça comandos fim-para
            else if (cmd.get(i).tipo.equals("forloop")) {
                //verifica o fim forloop e manda pra condicao() 
                processos.clear();
                processos.push(cmd.get(i));
                i++;
                
                if(i < cmd.size() || !(cmd.get(i).tipo.equals("id"))){
                    salvarErros("Erro: Estrutura de Repetição 'For' Mal Formada! (Faltou 'id')", cmd.get(i-1).getLinha());
                }
                else{
                    i++;
                    if((i) >= cmd.size() || !cmd.get(i).tipo.equals("rng1forloop")){
                        salvarErros("Erro: Estrutura de Repetição 'For' Mal Formada! (Faltou 'de')", cmd.get(i-1).getLinha());
                    }
                    else{
                        i++;
                        if ((i) >= cmd.size() || !(cmd.get(i).tipo.equals("int"))) {
                            salvarErros("Erro: Estrutura de Repetição 'For' Mal Formada! (Faltou inteiro após 'de')", cmd.get(i-1).getLinha());
                        }
                        else{
                            i++;
                            if ((i) >= cmd.size() || !(cmd.get(i).tipo.equals("rng2forloop"))) {
                                salvarErros("Erro: Estrutura de Repetição 'For' Mal Formada! (Faltou 'até')", cmd.get(i-1).getLinha());
                            }
                            else{
                                i++;
                                if ((i) >= cmd.size() || !(cmd.get(i).tipo.equals("int"))) {
                                    salvarErros("Erro: Estrutura de Repetição 'For' Mal Formada! (Faltou inteiro após 'até')", cmd.get(i-1).getLinha());
                                }
                                else{
                                    i++;
                                    if ((i) >= cmd.size() || !(cmd.get(i).tipo.equals("initforloop"))) {
                                        salvarErros("Erro: Estrutura de Repetição 'For' Mal Formada! (Faltou 'faça')", cmd.get(i-1).getLinha());
                                    }
                                    else{
                                        i++;
                                        
                                        while(i < cmd.size()){
                                            if(cmd.get(i).tipo.equals("forloop")){
                                                processos.push(cmd.get(i));
                                            }
                                            else if(cmd.get(i).tipo.equals("endforloop")){
                                                processos.pop();
                                                
                                                if(processos.isEmpty()){
                                                    if((i+1) < cmd.size() && !(cmd.get(i+1).tipo.equals("endline"))){
                                                        salvarErros("Erro: Bloco Mal Formado! (Após Comando Deve Haver Quebra de Linha)", cmd.get(i).getLinha());
                                                    }
                                                    break;
                                                }
                                            }
                                            aux.add(cmd.get(i));
                                            i++;   
                                        }
                                        if(!processos.isEmpty()){
                                            salvarErros("Erro: Estrutura de Repetição Mal Formada! (Faltou fechamento "+processos.peek().getValor()+")", processos.peek().getLinha());
                                        }
                                        processos.clear();
                                        comandos(aux);
                                        aux.clear();
                                    }
                                }
                            }
                        }
                    }
                }
            } 
            //| id = condicao
            else if (cmd.get(i).tipo.equals("id")) {
                // manda pra condicao() o que esta depois do token equal
                i++;
                //Verifica se é vetor
                if ((i) < cmd.size() && cmd.get(i).tipo.equals("[")) {
                    i++;
                    while (i < cmd.size() && !(cmd.get(i).tipo.equals("endline")) && !(cmd.get(i).tipo.equals("]"))) {
                        aux.add(cmd.get(i));
                        i++;
                    }
                    if(i >= cmd.size() || cmd.get(i).tipo.equals("endline")){
                        salvarErros("Erro: Estrutura Vetorial Mal Formada! (Faltou fechamento com ']')", cmd.get(i-1).getLinha());
                    }
                    condicao(aux);
                    aux.clear();
                    
                    i++;
                    
                    if (i < cmd.size() && cmd.get(i).tipo.equals("[")) {
                        i++;
                        
                        while ((i < cmd.size()) && !(cmd.get(i).tipo.equals("endline")) && !(cmd.get(i).tipo.equals("]"))) {
                            aux.add(cmd.get(i));
                            i++;
                        }
                        
                        if (i >= cmd.size() || cmd.get(i).tipo.equals("endline")) {
                            salvarErros("Erro: Estrutura Matricial Mal Formada! (Faltou fechamento com ']')", cmd.get(i-1).getLinha());
                        }
                        condicao(aux);
                        aux.clear();
                        i++;
                    }
                }
                if (i >= cmd.size() || !(cmd.get(i).tipo.equals("atrib"))) {
                    if (cmd.get(i).tipo.equals("endline")) {
                        salvarErros("Erro: Estrutura Mal Formada! (Faltou Operador de atribuição '=')", cmd.get(i-1).getLinha());
                    }
                    else{
                        salvarErros("Erro: Token ' " + cmd.get(i).getValor() + " ' inválido! ", cmd.get(i).getLinha());
                    }
                }
                else{
                    i++;
                    while (i < cmd.size() && !(cmd.get(i).tipo.equals("endline"))) {
                        aux.add(cmd.get(i));
                        i++;
                    }
                    condicao(aux);
                    aux.clear();
                }
                
            } 
            
            //| funcao id(params) comandos fim-funcao
            else if (cmd.get(i).tipo.equals("func")) {
                
            }
            //| vetor = condicao
            else if (cmd.get(i).tipo.equals("vetor")) {
                //manda pra condicao() o que esta depois do token vector
                i++;
                if (i < cmd.size() && cmd.get(i).tipo.equals("id")) {
                    i++;
                    if (i < cmd.size() && cmd.get(i).tipo.equals("[")) {
                        if (i >= cmd.size() || !(cmd.get(i).tipo.equals("int"))) {
                            salvarErros("Erro: Estrutura Vetorial Mal Formada! (Esperava [<int>])", cmd.get(i - 1).getLinha());
                        } else {
                            i++;
                            if (i >= cmd.size() || !(cmd.get(i).tipo.equals("]"))) {
                                salvarErros("Erro: Estrutura Vetorial Mal Formada! (Faltou fechamento com ']')", cmd.get(i - 1).getLinha());
                            } else {
                                i++;
                                if (i < cmd.size() && cmd.get(i).tipo.equals("[")) {
                                    i++;
                                    if (i >= cmd.size() || !(cmd.get(i).tipo.equals("int"))) {
                                        salvarErros("Erro: Estrutura Matricial Mal Formada! (Esperava [<int>][<int>])", cmd.get(i - 1).getLinha());
                                    } else {
                                        i++;
                                        if (i >= cmd.size() || !(cmd.get(i).tipo.equals("]"))) {
                                            salvarErros("Erro: Estrutura Matricial Mal Formada! (Faltou fechamento com ']')", cmd.get(i - 1).getLinha());
                                        } else {
                                            i++;
                                            if (i >= cmd.size() || !(cmd.get(i).tipo.equals("endline"))) {
                                                salvarErros("Erro: Estrutura Mal Formada! (Após Comando Deve Haver Quebra de Linha)", cmd.get(i).getLinha());
                                            }
                                        }
                                    }
                                } else if (i >= cmd.size() || !(cmd.get(i).tipo.equals("endline"))) {
                                    salvarErros("Erro: Estrutura Mal Formada! (Após Comando Deve Haver Quebra de Linha)", cmd.get(i).getLinha());
                                }

                            }
                        }
                    }
                    else {
                        salvarErros("Erro: Estrutura Vetorial Mal Formada! (Esperava [<int>])", cmd.get(i - 1).getLinha());
                    }
                }
                else{
                    salvarErros("Erro: Estrutura Mal Formada! (Esperava 'id' do Vetor)", cmd.get(i-1).getLinha());
                }
            } 
            else {
                if(!(cmd.get(i).tipo.equals("endline"))){
                    salvarErros("Erro: Token ' " + cmd.get(i).getValor() + " ' inválido! ", cmd.get(i).getLinha());
                }
            }
            
        }
    }

    void condicao(ArrayList<Token> cond) {

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
