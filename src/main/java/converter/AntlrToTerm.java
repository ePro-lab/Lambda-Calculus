package converter;

import antlr.LambdaBaseVisitor;
import antlr.LambdaParser;
import model.MultiBound;
import model.SingleBound;
import model.Term;
import model.Variable;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class AntlrToTerm extends LambdaBaseVisitor<Term> {
    private final ArrayList<String> errors;

    public AntlrToTerm(ArrayList<String> errors){
        this.errors = errors;
    }

    @Override
    public Term visitMultiBound(LambdaParser.MultiBoundContext ctx) {
        Term term = new Term();
        MultiBound multiBound = new MultiBound();

        Token errorToken = ctx.MULTIBOUND().getSymbol();
        int errorLine = errorToken.getLine();
        int errorColumn = errorToken.getCharPositionInLine() + 1;

        //add multiBound object to term
        String bound = ctx.MULTIBOUND().getText();
        int[] charCount = new int[123];
        for(char c : bound.toCharArray()){
            if(c>=97 && c<=122 && charCount[c] == 0) {                 //if the char is lowercase alphabetic between a and z and distinct
                charCount[c]++;
                multiBound.addVariable(new Variable(c));
            }else
                if(!(c == 'λ' | c == 'L' | c == '.' | c == ' '))       //ignore
                    errors.add("FormatException at (" + errorLine + "," + errorColumn + ")");           //only on 'λxx.'
        }
        term.addBound(multiBound);

        //add (VARIABLE|term)+ objects to term
        char charAt;
        for(int i=2; i< ctx.getChildCount(); i++){
            charAt = ctx.getChild(i).getText().toCharArray()[0];
            if(charAt != ')') {                                         //| ')' is last char item in a term from antlr
                if (ctx.getChild(i).getChildCount() == 0)               //no child nodes = variable
                    term.addVariable(new Variable(charAt));             //recursively visit the child term
                else {
                    term.addTerm(visit(ctx.getChild(i)));
                }
            }
        }

        return term;
    }

    @Override
    public Term visitSingleBound(LambdaParser.SingleBoundContext ctx) {
        Term term = new Term();

        Token errorToken;
        int errorLine;
        int errorColumn;

        //add singleBound object to term
        List<TerminalNode> singleBounds = ctx.SINGLEBOUND();
        int[] charCount = new int[123];
       for(TerminalNode node : singleBounds){
           char charAt = node.getText().toCharArray()[1];
           errorToken = node.getSymbol();
           errorLine = errorToken.getLine();
           errorColumn = errorToken.getCharPositionInLine() + 1;
           if(charCount[charAt] == 0) {
               charCount[charAt]++;
               term.addBound(new SingleBound(new Variable(charAt)));
           }else{
               errors.add("FormatException at (" + errorLine + "," + errorColumn + ")");        //only on 'λx.λx.'
           }
       }

        //add (VARIABLE|term)+ objects to term
        for(int i=singleBounds.size()+1; i< ctx.getChildCount(); i++){  //starts after all singleBounds
            char charAt = ctx.getChild(i).getText().toCharArray()[0];
            if(charAt != ')') {                                         //')' is last char item in a term from antlr
                if (ctx.getChild(i).getChildCount() == 0)               //no children = variable
                    term.addVariable(new Variable(charAt));
                else
                    term.addTerm(visit(ctx.getChild(i)));
            }
        }

       return term;
    }

    @Override
    public Term visitTermOnly(LambdaParser.TermOnlyContext ctx) {
        Term term = new Term();

        //add (term)+ objects to term
        List<LambdaParser.TermContext> terms = ctx.term();
        for(LambdaParser.TermContext t : terms)
            term.addTerm(visit(t));                                     //recursively visit the child term

        return term;
    }

    @Override
    public Term visitTermVariable(LambdaParser.TermVariableContext ctx) {
        Term term = new Term();

        //add (term)+ (variable)+ objects to term
        for(int i=1; i< ctx.getChildCount(); i++) {
            char charAt = ctx.getChild(i).getText().toCharArray()[0];
            if (charAt != ')')                                          //')' is last char item in a term from antlr
                if (ctx.getChild(i).getChildCount() == 0)               //no children = variable
                    term.addVariable(new Variable(ctx.getChild(i).getText().toCharArray()[0]));
                 else
                    term.addTerm(visit(ctx.getChild(i)));                //recursively visit the child term
        }

        return term;
    }

    @Override
    public Term visitVariables(LambdaParser.VariablesContext ctx) {
        Term term = new Term();

        //add variable (variable)+ objects to term
        List<TerminalNode> variables = ctx.VARIABLE();
        for(TerminalNode node : variables)
            term.addVariable(new Variable(node.getText().toCharArray()[0]));

        return term;
    }
}
