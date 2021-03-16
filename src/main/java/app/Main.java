package app;

import antlr.LambdaLexer;
import antlr.LambdaParser;
import converter.AntlrToInput;
import model.Input;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {
    public static void main(String[] args){
        //ANTLR
        if(args.length != 0)
            System.err.println("No input string");
        else{
            try {
                String inputString = "(Lxzu.xxuzu)t(Lp.p)f";

                LambdaParser lambdaParser = getParser(inputString);

                //build ParseTree from start symbol 'input'
                ParseTree antlrAST = lambdaParser.input();
                //create visitor for converting the ParseTree into Input/Term object
                AntlrToInput inputVisitor = new AntlrToInput();
                Input input = inputVisitor.visit(antlrAST);

                System.out.println("output:");
                System.out.println(input.toString());

                System.out.println("output:");
                System.out.println(input.toString());
            }catch (ArrayIndexOutOfBoundsException e) { //caused by illegal character input
                System.out.println("illegal input");
            }
        }
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
