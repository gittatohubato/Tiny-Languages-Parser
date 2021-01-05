package Parser;

import javafx.scene.paint.Color;
import jdk.nashorn.internal.parser.Token;
import sample.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Syntax {


    public int token;
    public String path;
    ArrayList<String> tokens= new ArrayList<String>();
    ArrayList<String> token_type= new ArrayList<String>();

    public Syntax(String path){
        this.path = path;
        this.token = 0;
        read_file(path);
    }
    void read_file(String path)
    {
        try {
            BufferedReader bufReader = new BufferedReader(new FileReader(path));
            String token = bufReader.readLine();
            while (token != null)
            {
                token_type.add(token.split(",")[1].trim());
                tokens.add(token.split(",")[0].trim());
                token = bufReader.readLine();

            }
            bufReader.close();
        }
        catch (Exception e) {}
    }


    public void match(String tkn){
        try{
            if (tkn.equals(token_type.get(token))) {
                token++;
            }else error();
        } catch(Exception e){
            error();
            }
    }
    public void unMatch(){
        token--;
    }
    public void error(){
        try {
            Main.error.setFill(Color.RED);
            Main.error.setText("Syntax Error");
            throw new Exception();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
    public Tree program()
    {

        // for(int i=0;i<token_type.size();i++)System.out.println(token_type.get(i));
        return stmt_sequence();
    }

    public Tree stmt_sequence() {
        Tree stmt_sequence = stm();
        Tree current = new Tree();
        current.root = stmt_sequence.root;
        while ( token<token_type.size() &&  token_type.get(token).equals("SEMICOLON")){
            match("SEMICOLON");
            Tree temp = stm();
            current.root.neighbour=temp.root;
            current.root = temp.root;
        }

        return stmt_sequence;
    }

    public Tree stm(){
        Tree stm = new Tree();
        switch (token_type.get(token)){
            case "IF":
                stm = if_stmt();
                break;

            case "REPEAT":
                stm = repeat();
                break;

            case "IDENTIFIER":
                match("IDENTIFIER");
                if(token_type.get(token).equals("ASSIGN"))
                {
                    unMatch();
                    stm =assign_stmt();
                }
                break;
            case "READ":
                stm = read_stmt();
                break;
            case "WRITE":
                stm = write_stmt();
                break;
            default:
                error();

        }
        return stm;
    }

    private Tree write_stmt() {
        Tree write_stmt = new Tree();
        match("WRITE");
        write_stmt.root = new Node("write", 1);
        write_stmt.root.midChild = exp().root;
        return write_stmt;
    }

    private Tree read_stmt() {
        Tree read_stmt = new Tree();
        match("READ");
        read_stmt.root = new Node("read"+"\n"+"("+tokens.get(token)+")", 1);
        match("IDENTIFIER");
        return read_stmt;
    }


    Tree assign_stmt() {
        Tree assign_stmt = new Tree();
        match("IDENTIFIER");
        assign_stmt.root = new Node("assign"+"\n"+"("+tokens.get(token-1)+")", 1);
        match("ASSIGN");
        assign_stmt.root.midChild = exp().root;
        return assign_stmt;

    }

    private Tree repeat() {
        Tree repeat_stmt = new Tree();
        match("REPEAT");
        repeat_stmt.root = new Node("repeat", 1); // creating the root of repeat tree
        repeat_stmt.root.leftChild = stmt_sequence().root;
        match("UNTIL");
        repeat_stmt.root.rightChild = exp().root;
        return repeat_stmt;
    }

    public Tree if_stmt () {
        Tree if_stmt = new Tree();
        match("IF");
        if_stmt.root = new Node("if", 1); // creating the root of if tree
        if_stmt.root.leftChild = exp().root;
        match("THEN");
        if_stmt.root.midChild = stmt_sequence().root;
        if((token<token_type.size())&&(token_type.get(token).equals("ELSE"))){
            match("ELSE");
            if_stmt.root.rightChild = stmt_sequence().root;
        }
        match("END");
        return if_stmt;
    }

    public Tree exp() {
        Tree exp = simple_exp();
        Tree temp = new Tree();

        if((token<token_type.size())&&(token_type.get(token).equals("EQUAL" )|| token_type.get(token).equals("LESSTHAN"))){
            temp.root = comparison_op().root;
            temp.root.leftChild = exp.root;
            temp.root.rightChild = simple_exp().root;
            exp = temp;
        }

        return exp;
    }

    private Tree comparison_op() {
        Tree comparison_op = new Tree();
        if((token<token_type.size())&&(token_type.get(token).equals("EQUAL"))){
            match("EQUAL");
            comparison_op.root = new Node("op"+"\n"+"("+tokens.get(token-1)+")", 0);
        }
        else if((token<token_type.size())&&(token_type.get(token).equals("LESSTHAN"))) {
            match("LESSTHAN");
            comparison_op.root = new Node("op"+"\n"+"("+tokens.get(token-1)+")", 0);
        }
        else error();
        return comparison_op;

    }
    private Tree simple_exp() {
        Tree simple_exp = new Tree();
        Tree temp = new Tree();

        simple_exp = term();
        while (((token<token_type.size())&&(token_type.get(token).equals("PLUS") || token_type.get(token).equals("MINUS")))){
            temp.root = add_op().root;
            temp.root.leftChild = simple_exp.root;
            temp.root.rightChild = term().root;
            simple_exp = temp;
        }
        return simple_exp;
    }

    private Tree add_op() {
        Tree add_op = new Tree();
        if((token<token_type.size())&&(token_type.get(token).equals("PLUS"))){
            match("PLUS");
            add_op.root = new Node("op"+"\n"+"("+tokens.get(token-1)+")", 0);
        }
        else if((token<token_type.size())&&(token_type.get(token).equals("MINUS"))) {
            match("MINUS");
            add_op.root = new Node("op"+"\n"+"("+tokens.get(token-1)+")", 0);
        }
        else error();
        return add_op;
    }

    private Tree term() {
        Tree term = factor();
        Tree temp = new Tree();

        while ((token<token_type.size())&&(token_type.get(token).equals("MULT") || token_type.get(token).equals("DIV"))){
            temp.root = mul_op().root;
            temp.root.leftChild = term.root;
            temp.root.rightChild = factor().root;
            term = temp;
        }
        return term;
    }

    private Tree factor() {
        Tree factor = new Tree();
        switch (token_type.get(token)){
            case "OPENBRACKET":
                match("OPENBRACKET");
                factor.root = exp().root;
                match("CLOSEDBRACKET");
                break;
            case "NUMBER":
                match("NUMBER");
                factor.root = new Node("const"+"\n"+"("+tokens.get(token-1)+")", 0);
                break;
            case "IDENTIFIER":
                match("IDENTIFIER");
                factor.root = new Node("id"+"\n"+"("+tokens.get(token-1)+")", 0);
                break;
            default:
                error();
        }

        return factor;
    }

    private Tree mul_op() {
        Tree mul_op = new Tree();
        if((token<token_type.size())&&(token_type.get(token).equals("MULT"))) {
            match("MULT");
            mul_op.root = new Node("op"+"\n"+"("+tokens.get(token-1)+")", 0);

        }
        else if((token<token_type.size())&&(token_type.get(token).equals("DIV"))){
            match("DIV");
            mul_op.root = new Node("op"+"\n"+"("+tokens.get(token-1)+")", 0);
        }
        else error();
        return mul_op;
    }
}