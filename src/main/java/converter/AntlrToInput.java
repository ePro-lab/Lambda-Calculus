package converter;

import antlr.LambdaBaseVisitor;
import antlr.LambdaParser;
import model.Input;
import model.Variable;

import java.util.ArrayList;

public class AntlrToInput extends LambdaBaseVisitor<Input> {
    ArrayList<String> errors;

    @Override
    public Input visitInput(LambdaParser.InputContext ctx) {
        Input input = new Input();
        errors = new ArrayList<>();
        AntlrToTerm termVisitor = new AntlrToTerm(errors);          //a helper visitor for transforming each subtree into a term object
        for(int i=0; i<ctx.getChildCount(); i++){
            if(i == ctx.getChildCount() -1) {
                //last child of the start symbol input is EOF
            }else
            if(ctx.getChild(i).getChildCount() == 0)                //no child nodes = variable
                input.addVariable(new Variable(ctx.getChild(i).getText().toCharArray()[0]));
            else
                input.addTerm(termVisitor.visit(ctx.getChild(i)));  //recursively visit the child term
        }

        return input;
    }

    public ArrayList<String> getErrors(){
        return errors;
    }
}
