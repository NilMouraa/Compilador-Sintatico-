package compilador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnalisadorLexico {

    LinkedHashMap<Integer, ArrayList<Token>> tokens;
    HashMap<String, String> lexemas;

    public AnalisadorLexico(String path) throws IOException {
        tokens = new LinkedHashMap<>();
        lexemas = new HashMap<>();

        //Token objToken = new Token();
//############### LEXEMAS DA LINGUAGEM #################
//Aritmeticos
        lexemas.put("+", "sum");
        lexemas.put("-", "sub");
        lexemas.put("*", "mult");
        lexemas.put("x", "mult");
        lexemas.put("/", "div");
        lexemas.put(":", "div");
        lexemas.put(".", ".");
        lexemas.put(",", ".");
//Comparativos
        lexemas.put(">", "gt");
        lexemas.put(">=", "gte");
        lexemas.put("=>", "gte");
        lexemas.put("<", "lt");
        lexemas.put("=<", "lte");
        lexemas.put("<=", "lte");
        lexemas.put("==", "eq");
        lexemas.put("!=", "neq");
//Gerais
        lexemas.put("=", "atrib");
        lexemas.put("int", "int");
        lexemas.put("float", "float");
        lexemas.put("str", "str");
        lexemas.put("var", "id");
//Palavras-chave
//Condicionais
        lexemas.put("se", "cond");
        lexemas.put("então", "initcond");
        lexemas.put("senão", "altcond");
        lexemas.put("fim-se", "endcond");
        lexemas.put("e", "and");
//Loops
        lexemas.put("para", "forloop");
        lexemas.put("de", "rng1forloop");
        lexemas.put("até", "rng2forloop");
        lexemas.put("faça", "initforloop");
        lexemas.put("fim-para", "endforloop");
        lexemas.put("enquanto", "whileloop");
        lexemas.put("fim-função", "endfunction");
        lexemas.put("fim-enquanto", "endwhileloop");
        lexemas.put("fim", "endprog");

//Outros
        lexemas.put("[", "[");
        lexemas.put("]", "]");
        lexemas.put(")", ")");
        lexemas.put("(", "(");
        lexemas.put(".", "ponto");
        lexemas.put(",", "virgula");
        lexemas.put("endline", "endline");
//##############################################################################
    }

    LinkedHashMap<Integer, ArrayList<Token>> Analisar() throws IOException {
        boolean coment = false;

        int line = 1;
        int contComent = 0;
        BufferedReader reader = null;
        String linha = "";
//        Pattern pCondicao = Pattern.compile("(-|*)");
//        
//        if(pCondicao.matcher("oi").matches())
//        {
//            
//        }

        try {
            reader = carrega("prog.txt" /*pegar argumento args*/);
        } catch (IOException ex) {
            Logger.getLogger(AnalisadorLexico.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        String texto = "";
        boolean dentroString = false;
        boolean dentroFunc = false;
        boolean entrei = false;
        while (reader.ready()) {

            int contParenAbre = 0;
            int contParenFecha = 0;
            String textoConcat = "";
            int contAspas = 0;

            ArrayList<Token> tokenLine = new ArrayList<>();
            linha = reader.readLine();

            for (int i = 0; i < linha.length(); i++) {

                //System.out.println(c);
                char c = linha.charAt(i);
                String c1, c0;
                int h = 0;

                if (i != (linha.length() - 1)) {
                    c1 = linha.charAt(i + 1) + "";
                } else {
                    c1 = "";
                }

                if (i > 0) {
                    c0 = linha.charAt(i - 1) + "";
                } else {
                    c0 = "";
                }

                if (c == '#') {
                    contComent++;
                    coment = true;
                } else if (contComent == 2) {
                    coment = false;
                }
                if (coment == false) {
                    if (c == '=' && linha.charAt(i - 1) != '=' && linha.charAt(i - 1) != '<' && linha.charAt(i - 1) != '>' && linha.charAt(i - 1) != '!' && linha.charAt(i + 1) != '=' && linha.charAt(i + 1) != '>' && linha.charAt(i + 1) != '<') {
                        String retorno = lexemas.get("=");
                        tokenLine.add(new Token(retorno, "=",line));
                    } else if (c == '+') {
                        String retorno = lexemas.get("+");
                        tokenLine.add(new Token(retorno, "+",line));
                    } else if (c == '-' && (linha.charAt(i - 1) != 'm' && linha.charAt(i - 2) != 'i' && linha.charAt(i - 3) != 'f') && (linha.charAt(i - 4) != ' ' || i != 4)) {
                        String retorno = lexemas.get("-");
                        tokenLine.add(new Token(retorno, "-",line));
                    } else if (c == '*') {
                        String retorno = lexemas.get("*");
                        tokenLine.add(new Token(retorno, "*",line));
                    } else if (c == '[') {
                        String retorno = lexemas.get("[");
                        tokenLine.add(new Token(retorno, "[",line));
                    } else if (c == ']') {
                        String retorno = lexemas.get("]");
                        tokenLine.add(new Token(retorno, "]",line));
                    } else if (c == '(') {
                        String retorno = lexemas.get("(");
                        tokenLine.add(new Token(retorno, "(",line));

                        if (dentroFunc == true) {
                            contParenAbre++;
                        }
                    } else if (c == ')') {
                        String retorno = lexemas.get(")");
                        tokenLine.add(new Token(retorno, ")",line));

                        if (dentroFunc == true) {
                            contParenFecha++;
                        }

                        if (contParenAbre == contParenAbre) {
                            dentroFunc = false;
                        }
                    } else if ((c0 + c + c1).equals(" x ")) {
                        String retorno = lexemas.get("x");
                        tokenLine.add(new Token(retorno, "x",line));

                    } else if (c == 'x') {
                        if ((i + 1) < linha.length() && (i - 1) >= 0) {
                            if (Character.isDigit(linha.charAt(i + 1)) && Character.isDigit(linha.charAt(i - 1))) {
                                String retorno = lexemas.get("x");
                                tokenLine.add(new Token(retorno, "x",line));
                            }
                        }

                    } else if (c == '/') {
                        String retorno = lexemas.get("/");
                        tokenLine.add(new Token(retorno, "/",line));
                    } else if (c == '.' && Character.isLetter(linha.charAt(i + 1)) && Character.isLetter(linha.charAt(i - 1))) {
                        String retorno = lexemas.get(".");
                        tokenLine.add(new Token(retorno, ".",line));
                    } else if ((c == ',' && (!Character.isDigit(linha.charAt(i + 1)))) || (c == ',')) {
                        String retorno = lexemas.get(",");
                        tokenLine.add(new Token(retorno, ",",line));
                    } else if (c == ':') {
                        String retorno = lexemas.get(":");
                        tokenLine.add(new Token(retorno, ":",line));
                    } else if (c == '<' && linha.charAt(i - 1) != '=' && linha.charAt(i + 1) != '=') {
                        String retorno = lexemas.get("<");
                        tokenLine.add(new Token(retorno, "<",line));
                    } else if (c == '>' && linha.charAt(i - 1) != '=' && linha.charAt(i + 1) != '=') {
                        String retorno = lexemas.get(">");
                        tokenLine.add(new Token(retorno, ">",line));
                    } else if ((c + c1).equals("<=")) {
                        String retorno = lexemas.get("<=");
                        tokenLine.add(new Token(retorno, "<=",line));
                    } else if ((c + c1).equals("=<")) {
                        String retorno = lexemas.get("=<");
                        tokenLine.add(new Token(retorno, "=<",line));
                    } else if ((c + c1).equals(">=")) {
                        String retorno = lexemas.get(">=");
                        tokenLine.add(new Token(retorno, ">=",line));
                    } else if ((c + c1).equals("=>")) {
                        String retorno = lexemas.get("=>");
                        tokenLine.add(new Token(retorno, "=>",line));
                    } else if (((c + c1).equals("=="))) {
                        String retorno = lexemas.get("==");
                        tokenLine.add(new Token(retorno, "==",line));
                    } else if (((c + c1).equals("!="))) {
                        String retorno = lexemas.get("!=");
                        tokenLine.add(new Token(retorno, "!=",line));
                    } //                    else if((linha.length()>(i+5))&&((""+linha.charAt(i)+linha.charAt(i+1)+linha.charAt(i+2)+linha.charAt(i+3)+linha.charAt(i+4)).equals("tela."))){
                    //                         String retorno = lexemas.get("var");
                    //                         tokenLine.add(new Token(retorno,"tela"));
                    //                     }
                    //                    else if(c==('"')){
                    //                        contAspas++;
                    //                        
                    //                        
                    //                        String texto="";
                    //                        for(int j = i+1; j < linha.length(); j++) {
                    //                            if(linha.charAt(j)!='"'){
                    //                                texto = texto + linha.charAt(j);
                    //                                dentroString=true;
                    //                                i=j;
                    //                            }
                    //                            
                    //                            else if(contAspas==2){
                    //                                    dentroString=false;
                    //                                    i=j;
                    //                                    contAspas=0;
                    //                                    break;
                    //                                    
                    //                                }
                    //                        }
                    ////                        System.out.println(texto);
                    //                         String retorno = lexemas.get("str");
                    //                        tokenLine.add(new Token(retorno , texto));
                    //                        
                    //                    }
                    else if (c == '"' || dentroString) {
                        int k = 0;
                        dentroString = true;

                        if (c == '"') {
                            k = 1;
                        } else {
                            k = 0;
                        }
                        for (int j = i + k; j < linha.length(); j++) {
                            if (linha.charAt(j) != '"') {
                                texto = texto + linha.charAt(j);
                                i = j;

                            } else if (linha.charAt(j) == '"') {
                                dentroString = false;
                                i = j;
                                break;

                            }
                        }

                        if (!dentroString) {
                            String retorno = lexemas.get("str");
                            tokenLine.add(new Token(retorno, texto, line));
                            texto = "";
                        }

                    } //                    && linha.charAt(i-1)!='['
                    else if (Character.isDigit(linha.charAt(i)) && contAspas == 0) {
                        String var = "";
                        int contVirg = 0;
                        int add = 0;

                        for (int j = i; j < linha.length(); j++) {
                            if (Character.isDigit(linha.charAt(j))) {
                                var = var + linha.charAt(j);
                                add = j;

                            } else if (linha.charAt(j) == ',' && Character.isDigit(linha.charAt(j + 1)) && (!dentroFunc || (contParenAbre > contParenFecha && contParenAbre > 1))) {
                                contVirg++;
                                if (contVirg == 1) {
                                    var = var + linha.charAt(j);
                                }
                                // else break;

                            } else if (linha.charAt(j) == '.') {
                                var = var + linha.charAt(j);

                            } else {
                                break;
                            }

                        }
                        i = add;
                        char resp = 'i';
                        for (int k = 0; k < var.length(); k++) {
                            if (var.charAt(k) == '.' || var.charAt(k) == ',') {
                                resp = 'f';
                                break;
                            }

                        }
                        if (resp == 'f') {
                            String retorno = lexemas.get("float");
                            tokenLine.add(new Token(retorno, var, line));
                        } else if (resp == 'i') {
                            String retorno = lexemas.get("int");
                            tokenLine.add(new Token(retorno, var, line));
                        }
                    } else if ((Character.isLetter(c) || linha.charAt(i) == '_') && contAspas == 0) {
                        String variavel = "";
                        int value = 0, value2 = 0;
                        boolean func = false;
                        for (int j = i; j < linha.length(); j++) {

                            if (Character.isLetter(linha.charAt(j))) {
                                variavel = variavel + linha.charAt(j);
                                value = j;
                            } else if (Character.isDigit(linha.charAt(j))) {
                                variavel = variavel + linha.charAt(j);
                                value = j;
                            } else if (linha.charAt(j) == '-') {
                                variavel = variavel + linha.charAt(j);
                            } else if (linha.charAt(j) == '_') {
                                variavel = variavel + linha.charAt(j);
                                value = j;

                            } else {
                                //value = j;
                                value2 = j;
                                break;

                            }

                        }

                        i = value;
                        //System.out.print(variavel+" ");

                        if (!lexemas.containsKey(variavel)) {
                            String retorno = lexemas.get("var");

                            if (linha.charAt(value2) == '(') {
                                tokenLine.add(new Token("func", variavel, line));
                                
                                dentroFunc = true;
                            } else if (linha.charAt(value2) == ' ') {
                                for (int j = value2; j < linha.length(); j++) {
                                    if (linha.charAt(j) == ' ') {
                                        func = true;
                                    } else if (linha.charAt(j) == '(') {
                                        func = true;
                                        break;
                                    } else {
                                        func = false;
                                        break;

                                    }

                                }
                                if (func == true) {
                                    tokenLine.add(new Token("func", retorno, line));
                                    dentroFunc = true;
                                    
                                } else {
                                    tokenLine.add(new Token(retorno, variavel, line));
                                    dentroFunc = false;
                                }
                            } else {
                                tokenLine.add(new Token(retorno, variavel, line));

                            }
                        } else {
                            if ("e".equals(variavel) && " ".equals(c0) && " ".equals(c1)) {
                                String retorno = lexemas.get(variavel);
                                tokenLine.add(new Token(retorno, variavel, line));
                                dentroFunc = false;
                                } 
                                //else if ("e".equals(variavel)) {
//                                String retorno = lexemas.get("var");
//                                tokenLine.add(new Token(variavel, retorno));
//                                dentroFunc = false;
//                            } 
                            else{
                                String retorno = lexemas.get(variavel);
                                tokenLine.add(new Token(retorno, variavel, line));
                                
                                dentroFunc = false;
                            }
                        }

                    } else if (linha.charAt(i) != ' ') {
                        tokenLine.add(new Token(linha.charAt(i) + "", linha.charAt(i) + "", line));
                    }

                }

            }
            if(tokenLine.size()!=0)
            tokenLine.add(new Token("endline","", line));
            tokens.put(line, tokenLine);
            line++;
        }
        imprimeResul();
        return tokens;
    }

    private void imprimeResul() {
        for (Map.Entry<Integer, ArrayList<Token>> entry : tokens.entrySet()) {
            Integer integer = entry.getKey();
            ArrayList<Token> arrayList = entry.getValue();
            System.out.print(integer);
            for (Token object : arrayList) {
                System.out.print(" " + object.toString());

            }
            System.out.println("");

        }
    }

    private AnalisadorLexico() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private BufferedReader carrega(String path) throws IOException {
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(path), "Cp1252"));
        return reader;
    }

    private void salva(String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(path + ".lex"), "Cp1252"));
        //Serializa o linkedhashmap de tokens e salva
    }

}
