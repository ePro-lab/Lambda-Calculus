package app;

import antlr.LambdaLexer;
import antlr.LambdaParser;
import app.gui.Gui;
import converter.AntlrToInput;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;
import model.Input;

import operation.BetaReduction;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Main {

    public static void main(String[] args){
        Gui.startGui(args);
    }

    public static Accordion compute(String inputString){
        Accordion out = new Accordion();
        //ANTLR
        if(inputString.length() == 0)
            System.out.println("\u001B[34m" + "No Input " + "\u001B[0m");
        else{
            try {
                //to 'catch' parser errors as a result of wrong input - technically it's not catched, just a 'break', since it can't be catched here
                ByteArrayOutputStream bas = new ByteArrayOutputStream();
                PrintStream err = new PrintStream(bas);
                PrintStream old = System.err;
                System.setErr(err);

                LambdaParser lambdaParser = getParser(inputString);
                //build ParseTree from start symbol 'input'
                ParseTree antlrAST = lambdaParser.input();
                //create visitor for converting the ParseTree into Input/Term object
                AntlrToInput inputVisitor = new AntlrToInput();
                Input input = inputVisitor.visit(antlrAST);

                //resetting System.err and print error
                System.setErr(old);
                if(bas.toString().contains("expecting")) {
                    System.err.println(bas);
                    out.getPanes().add(new TitledPane(input.toString(), new Text("input does not match EBNF:\n" + bas)));
                }
                else {
                    System.out.println(input);
                    BetaReduction.betaReduction(input, out);
                }
            } catch (RecognitionException e) {
                e.printStackTrace();
            }
        }
        return out;
    }

    private static LambdaParser getParser(String inputString){
        LambdaParser parser;

        CharStream input = CharStreams.fromString(inputString);
        LambdaLexer lexer = new LambdaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        parser = new LambdaParser(tokens);

        return parser;
    }
}
