package Parser;

import sample.Main;

import java.io.IOException;
import java.lang.reflect.Array;

public class ParserMain {

    private Tree program;

    public ParserMain(String INPUT_PATH) {

        String OUTPUT_PATH = "Scanner Output.txt";

        try {
            Scannerr.scanner (INPUT_PATH,OUTPUT_PATH);
        } catch (IOException e) {
            Main.error.setText("File not Found");
        }
        Syntax syn = new Syntax(OUTPUT_PATH);
        program = syn.program();
        //Tree.printPreorder(program.root);
    }

    public Tree syntaxTree()    {return program;}
}
