package operation;

import exception.NoFreeVariableException;
import model.*;

import java.util.ArrayList;

public class BetaReduction {
    public static LambdaExpression reduce(Term term, LambdaExpression insert) throws Exception {

        //found MultiBound
        if(term.getContentIndex(0) instanceof MultiBound){
            System.out.println("MULTI");
            MultiBound multiBound = ((MultiBound) term.getContentIndex(0));
            Variable v = multiBound.getVariablesIndex(0);  //save lambda bound variable
            System.out.println(v);

            //used if the first bound variable is not changed in the alpha conversion but a later one
            boolean alphaDone = false;
            //find and replace matches if context exists
            if(!multiBound.containsMultipleBound(v))
                for(int j=1; j<term.getContentSize(); j++){                             //start search after MultiBound

                    if (term.getContentIndex(j) instanceof Variable) {
                        if (insert instanceof Variable && !(v.compare((Variable) insert)) && !alphaDone && multiBound.containsAnotherBound((Variable) insert)) {
                            //insert type is Variable and is not same as lambda bound variable
                            //ALPHA                                                 //alpha conversion necessary
                            System.out.println("alpha");
                            Variable tmp = v;
                            v = AlphaConversion.convert(term, (Variable) insert, multiBound.getVariables());
                            if(v.compare(tmp))
                                alphaDone = true;
                            System.out.println("alpha result " + term);
                        }
                        if (((Variable) term.getContentIndex(j)).compare(v)) {           //if variable matches lambda bound variable replace
                            term.setContentIndex(j, insert);
                        }
                    }else
                        //if Term in Term check context
                        if(term.getContentIndex(j) instanceof Term)
                            replaceVariablesInTermInTerm((Term) term.getContentIndex(j), v, insert);
                }

            //remove lambda variable
            multiBound.removeVariablesIndex(0);
            if(multiBound.getVariablesSize() == 1) {
                System.out.println("replace");
                term.setContentIndex(0, multiBound.convertToSingleBound());
            }
        }else
            //found SingleBound
            if(term.getContentIndex(0) instanceof SingleBound) {
                System.out.println("SINGLE");
                SingleBound singleBound = ((SingleBound) term.getContentIndex(0));
                Variable v = singleBound.getVariable();  //save lambda bound variable

                //find and replace matches
                boolean foundMultipleMatchingBounds = false;                //if true, the lambda bound variable and the insert will be deleted due to no context
                for(int j=1; j<term.getContentSize() && !foundMultipleMatchingBounds; j++){                             //start first SingleBound
                    if(term.getContentIndex(j) instanceof Variable){
                        if(insert instanceof Variable) {
                            for (int k = 1; k < term.getContentSize() && !foundMultipleMatchingBounds; k++) {
                                if(term.getContentIndex(k) instanceof SingleBound)
                                    if (((SingleBound) term.getContentIndex(k)).getVariable().compare(v))       //compare with other SingleBound variables
                                        foundMultipleMatchingBounds = true;
                                    else
                                    if (((Variable) insert).compare((((SingleBound) term.getContentIndex(k)).getVariable()))) {        //insert type is Variable and is same as lambda bound variable
                                        //ALPHA                                                 //alpha conversion necessary
                                        System.out.println("alpha");
                                        ArrayList<Variable> e = new ArrayList<>();
                                        for(int z=0; z < term.getContentSize(); z++){
                                            if(term.getContentIndex(z) instanceof SingleBound)
                                                e.add(((SingleBound) term.getContentIndex(z)).getVariable());
                                        }
                                        AlphaConversion.convert(term, (Variable) insert, e);
                                        System.out.println("alpha result " + term);
                                    }
                            }
                        }

                        if(!foundMultipleMatchingBounds && ((Variable) term.getContentIndex(j)).compare(v)){           //if variable matches lambda bound variable replace
                            if(insert instanceof Term && term.getContentSize() == 2 && ((Term) insert).getContentSize() == 2)       //solves '(λp.p)(λp.p)'
                                if(term.compare((Term) insert))
                                    return term;
                            if(insert instanceof Term){
                                Term tmp = ((Term) insert).copyTerm();
                                term.setContentIndex(j, tmp);
                            }
                            else
                                term.setContentIndex(j, insert);
                        }
                    }

                    //if Term in Term check context
                    if(term.getContentSize() > 1 && term.getContentIndex(j) instanceof Term && !((Term) term.getContentIndex(j)).isBound(v)) {
                        if(insert instanceof Variable && ((Term) term.getContentIndex(j)).isBound((Variable) insert) && ((Term) term.getContentIndex(j)).containsVariable((Variable) insert)) {
                            ArrayList<Variable> boundVariables = new ArrayList<>();
                            for(int i=0; i<((Term) term.getContentIndex(j)).getContentSize(); i++){
                                if(((Term) term.getContentIndex(j)).getContentIndex(i) instanceof SingleBound)
                                    boundVariables.add(((SingleBound) ((Term) term.getContentIndex(j)).getContentIndex(i)).getVariable());
                            }
                            AlphaConversion.convert((Term) term.getContentIndex(j), (Variable) insert, boundVariables);
                        }
                        if(insert instanceof Term) {
                            if (!((Term) term.getContentIndex(j)).compare((Term) insert))
                                replaceVariablesInTermInTerm((Term) term.getContentIndex(j), v, insert);
                        }
                        else
                            replaceVariablesInTermInTerm((Term) term.getContentIndex(j), v, insert);
                    }
                }
                term.removeContentIndex(0);
            }

        if(term.getContentSize() == 1)         //will remove parentheses if term
            if (term.getContentIndex(0) instanceof Term)
                term.replaceContent(((Term) term.getContentIndex(0)).getContent());

        return term;
    }

    public static void betaReduction(Input input){
        try{
            LambdaExpression previous = input.getInputListIndex(0);       //the one to replace a bound-variable in
            LambdaExpression current;                                       //replacement

            int lastTermIndex = 0;
            for (int i = 1; i < input.getInputListSize() && !(input.getInputListIndex(0) instanceof Variable); i++) {
                current = input.getInputListIndex(i);
                if (previous != null) {
                    System.out.println("current " + current.toString());
                    System.out.println("previous " + previous);
                }
                if (previous instanceof Term && !(((Term) previous).getContentIndex(0) instanceof Variable)) {  //check if previous is type Term and does not start with a Variable
                    if(((Term) previous).getContentIndex(0) instanceof Term){   //if first LambdaExpression of term is a Term, go inside
                        checkInnerTerm(input, (Term) previous, lastTermIndex,0);
                        if(input.getInputListSize() == 2 && input.getInputListIndex(0) instanceof Term && ((Term) input.getInputListIndex(0)).containsNoVariableAtIndexZero()) {
                            i = 0;
                            previous = input.getInputListIndex(0);
                        }
                    }
                    else {
                        input.setInputListIndex(lastTermIndex, BetaReduction.reduce((Term) previous, current));
                        input.removeInputListIndex(i);
                        //'current' has been deleted, so index i will not be 'next' LambdaExpression
                        i--;
                        //check if returned 'term' is size 1, if so replace with content of term
                        if(((Term) input.getInputListIndex(lastTermIndex)).getContentSize() == 1)
                            input.setInputListIndex(lastTermIndex, ((Term) input.getInputListIndex(lastTermIndex)).getContentIndex(0));
                        else
                        if(input.containsOnlyTerms() && input.getInputListIndex(0) instanceof Term && ((Term) input.getInputListIndex(0)).getContentIndex(0) instanceof Term) {
                            input.removeParentheses();
                            previous = input.getInputListIndex(lastTermIndex);
                        }

                        /* check if 'input' is size 1
                         * AND
                         ** first LE in 'input' is type Variable
                         ** OR
                         *** first LE in 'input' is type Term
                         *** AND
                         *** term is size 1
                         ** OR
                         ** first LE in 'input' is Term and its first LE is type Variable
                         ** OR
                         *** first LE in 'input' is term and is size > 1
                         *** AND
                         *** first LE of term is type Term
                         *** And
                         *** second LE of term is type Variable
                         */
                        if (input.getInputListSize() == 1 &&
                                ((input.getInputListIndex(0) instanceof Variable || (input.getInputListIndex(0) instanceof Term && (((Term) input.getInputListIndex(0)).getContentSize() == 1))) ||
                                        ((Term) input.getInputListIndex(0)).getContentIndex(0) instanceof Variable ||
                                        ((((Term) input.getInputListIndex(0)).getContentSize() > 1) && (((Term) input.getInputListIndex(0)).getContentIndex(0) instanceof Term) && ((Term) input.getInputListIndex(0)).getContentIndex(1) instanceof Variable))){
                            input.replaceInputList(((Term) previous).getContent());
                            previous = input.getInputListIndex(0);
                            i = 0;
                        }
                    }
                    System.out.println("result " + input);
                }else
                {
                    System.out.println("Variable");
                    lastTermIndex = i;
                    previous = current;
                    if(previous instanceof Term ) {
                        checkInnerTerm(input, (Term) previous, lastTermIndex,0);
                    }
                }
            }

            //check inner terms
            for(int i=0; i<input.getInputListSize(); i++)
                if(input.getInputListIndex(i) instanceof Term)
                    if(((Term) input.getInputListIndex(i)).containsTerm())
                        checkInnerTerm(input, (Term) input.getInputListIndex(i), i,0);

            //check for more beta
            for(int i=0; i<input.getInputListSize(); i++){
                if(input.getInputListIndex(i) instanceof Term && ((Term) input.getInputListIndex(i)).containsTermAtIndexZero()) {
                    checkInnerTerm(input, (Term) input.getInputListIndex(i), i,0);
                    i=0;
                }
            }

            System.out.println("output:");
            System.out.println(input);
        }catch (
                NoFreeVariableException e){         //caused by alpha conversion
            System.err.println(e.getMessage());
        }catch (ArrayIndexOutOfBoundsException e) { //caused by illegal character input
            e.printStackTrace();
            System.out.println("illegal input");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkInnerTerm(Input input, Term term, int lastTermIndex, int dimension){
        System.out.println("input:" + input);
        System.out.println("inner term:" + term.getContentIndex(0) + " and " + term.getContentIndex(1));
        try{
            if(term.getContentIndex(0) instanceof Term  && ((Term) term.getContentIndex(0)).containsBound()) {
                if(!(((Term) term.getContentIndex(0)).getContentIndex(0) instanceof Term)){
                    BetaReduction.reduce((Term) term.getContentIndex(0), term.getContentIndex(1));
                    term.removeContentIndex(1);
                    if (((Term) term.getContentIndex(0)).getContentSize() == 1)
                        term.setContentIndex(0, ((Term) term.getContentIndex(0)).getContentIndex(0));
                    if (term.getContentSize() == 1){                             //will remove parentheses
                        if(term.getContentIndex(0) instanceof Term)
                            term.replaceContent(((Term) term.getContentIndex(0)).getContent());
                        else
                        if(dimension == 0)
                            input.setInputListIndex(lastTermIndex, term.getContentIndex(0));
                    }
                    System.out.println("inner result:" + input.getInputListIndex(lastTermIndex));
                }else
                    //if first LE of (2)Term in (1)Term is a (3)Term check inner Term of (2)
                    checkInnerTerm(input, (Term) term.getContentIndex(0), 0, dimension+1);
            }
            else
            if(term.getContentIndex(0) instanceof Variable || (term.getContentIndex(0) instanceof Term && ((Term) term.getContentIndex(0)).getContentIndex(0) instanceof Variable)){
                for(int i=1; i<term.getContentSize(); i++){
                    if(term.getContentIndex(i) instanceof Term){
                        checkInnerTerm(input,(Term) term.getContentIndex(i), i, dimension+1);
                        term.clean();
                        if(term.containsOnlyVariableTerms())
                            if(term.getContentSize() == 2){
                                ArrayList<LambdaExpression> tmp = new ArrayList<>();
                                for (int j=0; j<input.getInputListSize(); j++){
                                    if(j != lastTermIndex)
                                        tmp.add(input.getInputListIndex(j));
                                    else
                                        for(int k=0; k<term.getContentSize(); k++){
                                            tmp.add(term.getContentIndex(k));
                                        }
                                }
                                input.replaceInputList(tmp);
                            }else
                                term.clean();
                        else
                        if(term.getContentSize() > 1 && term.containsOnlyVariableTerms()){
                            ArrayList<LambdaExpression> tmp = new ArrayList<>();
                            for(i=0; i<lastTermIndex; i++)
                                tmp.add(input.getInputListIndex(i));
                            tmp.addAll(term.getContent());
                            input.replaceInputList(tmp);
                        }

                    }
                }
            }
            else
                if(term.getContentIndex(0) instanceof Bound && term.getContentIndex(1) instanceof Term && ((Term) term.getContentIndex(1)).containsTermAtIndexZero())
                    checkInnerTerm(input, (Term) term.getContentIndex(1), lastTermIndex, dimension+1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void replaceVariablesInTermInTerm(Term term,Variable variable, LambdaExpression insert) throws NoFreeVariableException {
        for(int i=0; i<term.getContentSize(); i++) {
            if(term.getContentIndex(i) instanceof Term) {
                if (!((Term) term.getContentIndex(i)).isBound(variable))
                    ((Term) term.getContentIndex(i)).replaceVariables(variable, insert);
            }
            else
            if(term.getContentIndex(i) instanceof Variable && insert instanceof Variable) {
                if (term.isBound(variable)) {
                    ArrayList<Variable> boundVariables = new ArrayList<>();
                    if (term.getContentIndex(0) instanceof MultiBound)
                        boundVariables.addAll(((MultiBound) term.getContentIndex(0)).getVariables());
                    else
                        for (int j = 0; j < term.getContentSize(); j++)
                            if (term.getContentIndex(j) instanceof SingleBound)
                                boundVariables.add(((SingleBound) term.getContentIndex(j)).getVariable());
                    AlphaConversion.convert(term, variable, boundVariables);
                }
                term.replaceVariables(variable, insert);
            }
            else
            if(term.getContentIndex(i) instanceof Variable && insert instanceof Term){
                if(((Variable) term.getContentIndex(i)).compare(variable))
                    term.setContentIndex(i,insert);
            }

        }
    }
}
