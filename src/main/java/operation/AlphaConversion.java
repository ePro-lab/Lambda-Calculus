package operation;

import exception.NoFreeVariableException;
import model.*;

import java.util.ArrayList;

public class AlphaConversion {
    //used when context is full term
    public static Variable convert(Term term, Variable variable, ArrayList<Variable> boundVariables) throws NoFreeVariableException {
        //copy used Variables of boundVariables to variables
        ArrayList<Variable> variables = new ArrayList<>(boundVariables);
        findUsedVariables(term, variable, variables, true);

        //used as character available/used | 0 = unused
        int[] characters = new int[123];
        char newChar = ' ';

        //++ means 'remove' / the character is already used
        for(Variable v : variables)
            characters[v.getVariable()]++;

        //search for first free character to convert to
        boolean foundFree = false;
        for(int i=97; i<123 && !foundFree; i++){
            if(characters[i] == 0) {
                newChar = (char) i;
                foundFree = true;
            }
        }
        //no free character found
        if (!foundFree){
            throw new NoFreeVariableException(term);
        }

        Variable convert = new Variable(newChar);
        convert(term, variable, convert);
        return new Variable(newChar);
    }

    private static void convert(Term term, Variable variable, Variable convert){
        for(int i =0; i<term.getContentSize(); i++){
            if(term.getContentIndex(i) instanceof MultiBound){
                for(int j=0; j<((MultiBound) term.getContentIndex(i)).getVariablesSize(); j++){
                    if(((MultiBound) term.getContentIndex(i)).getVariablesIndex(j).compare(variable))
                        ((MultiBound) term.getContentIndex(i)).setVariablesIndex(j, convert);
                }
            }
            if(term.getContentIndex(i) instanceof SingleBound){
                if(((SingleBound) term.getContentIndex(i)).getVariable().compare(variable))
                    ((SingleBound) term.getContentIndex(i)).setVariable(convert);
            }
            if(term.getContentIndex(i) instanceof Variable){
                if(((Variable) term.getContentIndex(i)).compare(variable)){
                    term.setContentIndex(i,convert);
                }
            }
            if(term.getContentIndex(i) instanceof Term && !((Term) term.getContentIndex(i)).isBound(variable))
                convert((Term) term.getContentIndex(i), variable, convert);
        }
    }

    private static void findUsedVariables(Term term, Variable variable, ArrayList<Variable> variables, boolean first){
        for(int i=0; i<term.getContentSize(); i++) {
            if (term.getContentIndex(i) instanceof Term)
                findUsedVariables((Term) term.getContentIndex(i), variable, variables, false);
            else if(!term.isBound(variable) || first){                  //in 'first' term it must check all variables, since first term bound will be changed in alpha
                if (term.getContentIndex(i) instanceof SingleBound)     //if 'first' is false there will be no alpha if the variable is already bound
                    variables.add(((SingleBound) term.getContentIndex(i)).getVariable());
                else if(term.getContentIndex(i) instanceof MultiBound)
                    variables.addAll(((MultiBound) term.getContentIndex(i)).getVariables());
                else if(term.getContentIndex(i) instanceof Variable)
                    variables.add((Variable) term.getContentIndex(i));
            }
        }
    }
}
