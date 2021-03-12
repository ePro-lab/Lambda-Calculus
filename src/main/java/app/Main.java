package app;

import antlr.LambdaLexer;
import antlr.LambdaParser;
import converter.AntlrToInput;
import model.Input;

import model.LambdaExpression;
import model.Term;
import operation.BetaReduction;
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
            String inputString = "(Lxzu.xxuzu)t(Lp.p)f";

            LambdaParser lambdaParser = getParser(inputString);

            //build ParseTree from start symbol 'input'
            ParseTree antlrAST = lambdaParser.input();
            //create visitor for converting the ParseTree into Input/Term object
            AntlrToInput inputVisitor = new AntlrToInput();
            Input input = inputVisitor.visit(antlrAST);

            if(inputVisitor.getErrors().isEmpty()){
                System.out.println("output:");
                System.out.println(input.toString());
            }else{
                for(String err : inputVisitor.getErrors())
                    System.out.println(err);
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
