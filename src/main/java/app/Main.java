package app;

import antlr.LambdaLexer;
import antlr.LambdaParser;
import app.gui.Gui;
import converter.AntlrToInput;
import javafx.scene.control.Accordion;
import model.Input;

import operation.BetaReduction;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTree;

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

                LambdaParser lambdaParser = getParser(inputString);
                //build ParseTree from start symbol 'input'
                ParseTree antlrAST = lambdaParser.input();
                //create visitor for converting the ParseTree into Input/Term object
                AntlrToInput inputVisitor = new AntlrToInput();
                Input input = inputVisitor.visit(antlrAST);

                System.out.println(input);
                BetaReduction.betaReduction(input, out);

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
