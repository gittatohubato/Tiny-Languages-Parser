package Parser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Locale;

enum states{
    START,
    SPACE,
    COMMENT1,
    COMMENT2,
    OPERATION,
    SEPARATOR,
    EQUALITY1,
    EQUALITY2,
    COMPARISON,
    ASSIGN1,
    ASSIGN2,
    IDENTIFIER,
    NUM1, NUM2, NUM3, IN_DOT, IN_E, SIGN1, SIGN2,
    ERROR,
    DONE,
    ENTER;
}
//int x = -5 float y = +10

public class Scannerr {

    public static void scanner (String input_path,String output_path) throws IOException {
        String INPUT_PATH =input_path;
        String OUTPUT_PATH =output_path;
        String[] reservedWords = {"WRITE", "READ", "IF", "ELSE", "RETURN", "BEGIN", "END", "MAIN", "STRING", "INT",
                "REAL", "UNTIL", "REPEAT", "THEN", "write", "read", "if", "else", "return", "begin", "end", "main", "string",
                "int", "real", "until", "repeat", "then"};
        String token = "", tokenType = "";
        File input = new File(INPUT_PATH);
        new FileWriter(OUTPUT_PATH);
        Files.write(Paths.get(INPUT_PATH), " ".getBytes(), StandardOpenOption.APPEND);
        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            int c = br.read();
            boolean flag = false;
            char character = (char) c;
            token += character;
            states state = states.START;
            while (c != -1) {
                // process by char.
                switch(state){
                    case START:
                        if(Character.isDigit(character))  state = states.NUM1;
                        else if(character == '\r' || character == '\n') state = states.ENTER;
                        else if(Character.isAlphabetic(character))  state = states.IDENTIFIER;
                        else if((character == '+' || character == '-') && !flag )   state = states.SIGN1;
                        else if(character == '*' || character == '/' || ((character == '+' || character == '-') && flag))state = states.OPERATION;
                        else if(character == '<' || character == '>')   state = states.COMPARISON;
                        else if(character == '!' || character == '=')  state = states.EQUALITY1;
                        else if(character == '{')   state = states.COMMENT1;
                        else if(character == ':') state = states.ASSIGN1;
                        else if(character == ',' || character == ';' || character == '(' || character == ')')
                            state = states.SEPARATOR;
                        else if(character == ' ')   state = states.SPACE;
                        else if(character == '\t')  state = states.SPACE;
                        else state = states.ERROR;
                        break;

                    case ENTER:
                        if(character == '\n')
                        {
                            //c = br.read(); //\n
                            character = (char) c;
                            //token += character; // \r\n
                            c = br.read();  //m
                            character = (char) c;
                            state = states.DONE;
                            token += character; //\r\nm
                            tokenType = "ENTER";
                            flag = false;
                        }
                        else if(character == '\r')
                        {
                            c = br.read(); //\n
                            character = (char) c;
                            token += character; // \r\n
                            c = br.read();  //m
                            character = (char) c;
                            state = states.DONE;
                            token += character; //\r\nm
                            tokenType = "ENTER";
                            flag = false;
                        }
                        break;

                    case SPACE:
                        c = br.read();
                        character = (char) c;
                        if(character == ' ') state = states.SPACE;
                        else {
                            state = states.DONE;
                            token += character;
                            tokenType = "SPACE";}
                        break;

                    case COMMENT1:
                        c = br.read();
                        character = (char) c;
                        if(character == '}') state = states.COMMENT2;
                        break;

                    case COMMENT2:
                        c = br.read();
                        character = (char) c;
                        state = states.DONE;
                        token += character;
                        tokenType = "COMMENT";
                        break;

                    case SEPARATOR:
                        c = br.read();
                        character = (char) c;
                        state = states.DONE;
                        token += character;
                        tokenType = "SEPARATOR";
                        flag = false;
                        break;

                    case ASSIGN1:
                        c = br.read();
                        character = (char) c;
                        if(character == '=')    state = states.ASSIGN2;
                        else state = states.ERROR;
                        token += character;
                        flag = false;
                        break;

                    case ASSIGN2:
                        c = br.read();
                        character = (char) c;
                        state = states.DONE;
                        token += character;
                        tokenType = "ASSIGN";
                        flag = false;
                        break;

                    case OPERATION:
                        c = br.read();
                        character = (char) c;
                        state = states.DONE;
                        token += character;
                        tokenType = "OPERATION";
                        flag = false;
                        break;

                    case EQUALITY1:
                        c = br.read();
                        character = (char) c;
                        if(character == '=') state = states.EQUALITY2;
                        else state = states.DONE;
                        token += character;
                        tokenType = "MULTI CHARACTER OPERATOR";
                        flag = false;
                        break;

                    case EQUALITY2:
                        c = br.read();
                        character = (char) c;
                        state = states.DONE;
                        token += character;
                        tokenType = "MULTI CHARACTER OPERATOR";
                        flag = false;
                        break;

                    case COMPARISON:
                        c = br.read();
                        character = (char) c;
                        if(character == '=') state = states.EQUALITY2;
                        else state = states.DONE;
                        token += character;
                        tokenType = "COMPARISON";
                        flag = false;
                        break;

                    case IDENTIFIER:
                        c = br.read();
                        character = (char) c;
                        if(Character.isAlphabetic(character) || Character.isDigit(character))   state = states.IDENTIFIER;
                        else state = states.DONE;
                        token += character;
                        tokenType = "IDENTIFIER";
                        flag = true;
                        break;

                    case SIGN1:
                        c = br.read();
                        character = (char) c;
                        if(Character.isDigit(character)) state = states.NUM1;
                        else if (character == ' ') state = states.SIGN1;
                        else if (Character.isAlphabetic(character)) {state = states.DONE; tokenType = "OPERATION";}
                        else state = states.ERROR;
                        token += character;
                        token = token.trim();
                        break;

                    case NUM1:
                        c = br.read();
                        character = (char) c;
                        if(Character.isDigit(character))    state = states.NUM1;
                        else if(character == '.')   state = states.IN_DOT;
                        else if(Character.toLowerCase(character) == 'e')   state = states.IN_E;
                        else if(Character.isAlphabetic(character)||character==':') state = states.ERROR;
                        else state = states.DONE;
                        token += character;
                        tokenType = "NUMBER";
                        flag = true;
                        break;

                    case IN_DOT:
                        c = br.read();
                        character = (char) c;
                        if(Character.isDigit(character))    state = states.NUM2;
                        else state = states.ERROR;
                        token += character;
                        break;

                    case NUM2:
                        c = br.read();
                        character = (char) c;
                        if(Character.toLowerCase(character) == 'e') state = states.IN_E;
                        else if(Character.isDigit(character))   state = states.NUM2;
                        else state = states.DONE;
                        token += character;
                        tokenType = "NUMBER";
                        break;

                    case IN_E:
                        c = br.read();
                        character = (char) c;
                        if(Character.isDigit(character))    state = states.NUM3;
                        else if (character == '+' || character == '-')  state = states.SIGN2;
                        else state = states.ERROR;
                        token += character;
                        break;

                    case SIGN2:
                        c = br.read();
                        character = (char) c;
                        if(Character.isDigit(character))    state = states.NUM3;
                        else state = states.ERROR;
                        token += character;
                        break;

                    case NUM3:
                        c = br.read();
                        character = (char) c;
                        if(Character.isDigit(character))    state = states.NUM3;
                        else    state = states.DONE;
                        token += character;
                        tokenType = "NUMBER";
                        break;

                    case ERROR:
                        Files.write(Paths.get(OUTPUT_PATH), "INVALID TOKEN\n".getBytes(), StandardOpenOption.APPEND);
                        token = "";
                        c = -1;
                        flag = false;
                        break;

                    case DONE:
                        if(tokenType.equals("IDENTIFIER")){
                            if(Arrays.asList(reservedWords).contains(token.substring(0,token.length()-1)))
                                tokenType = (token.substring(0,token.length()-1)).toUpperCase();
                        }
                        else if(tokenType.equals("SEPARATOR"))
                        {
                            switch(token.substring(0,token.length()-1))
                            {
                                case ";":
                                    tokenType = "SEMICOLON";
                                    break;
                                case ",":
                                    tokenType = "COMMA";
                                    break;
                                case ")":
                                    tokenType = "CLOSEDBRACKET";
                                    break;
                                case "(":
                                    tokenType = "OPENBRACKET";
                                    break;
                                default:
                                    tokenType = "SEPARATOR";
                                    break;
                            }
                        }
                        else if(tokenType.equals("OPERATION"))
                        {
                            switch(token.substring(0,token.length()-1))
                            {
                                case "+":
                                    tokenType = "PLUS";
                                    break;
                                case "-":
                                    tokenType = "MINUS";
                                    break;
                                case "*":
                                    tokenType = "MULT";
                                    break;
                                case "/":
                                    tokenType = "DIV";
                                    break;
                                default:
                                    tokenType = "OPERATOR";
                                    break;
                            }
                        }
                        else if(tokenType.equals("MULTI CHARACTER OPERATOR"))
                        {
                            switch(token.substring(0,token.length()-1))
                            {
                                case "=":
                                    tokenType = "EQUAL";
                                    break;
                                case "==":
                                    tokenType = "EQUAL";
                                    break;
                                case "!=":
                                    tokenType = "NOTEQUAL";
                                    break;
                                default:
                                    tokenType = "OPERATOR";
                                    break;
                            }
                        }
                        else if(tokenType.equals("COMPARISON"))
                        {
                            switch(token.substring(0,token.length()-1))
                            {
                                case ">":
                                    tokenType = "GREATERTHAN";
                                    break;
                                case "<":
                                    tokenType = "LESSTHAN";
                                    break;
                                case ">=":
                                    tokenType = "GREATERTHANOREQUAL";
                                    break;
                                case "<=":
                                    tokenType = "LESSTHANOREQUAL";
                                    break;
                                default:
                                    tokenType = "COMPARISON";
                                    break;
                            }
                        }


                        if(!(tokenType.equals("COMMENT") || tokenType.equals("SPACE") || tokenType.equals("ENTER")))
                            Files.write(Paths.get(OUTPUT_PATH), (token.substring(0,token.length()-1)+" ,"+tokenType+"\n").getBytes(), StandardOpenOption.APPEND);
                        token = token.substring(token.length()-1).trim();
                        state = states.START;

                        break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
