package operation;

import exception.NoFreeVariableException;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.*;

import java.util.ArrayList;

public class BetaReduction {
    public static LambdaExpression reduce(Term term, LambdaExpression insert, Input input, Accordion out) throws Exception {

        //found MultiBound
        if(term.getContentIndex(0) instanceof MultiBound){
            MultiBound multiBound = ((MultiBound) term.getContentIndex(0));
            Variable v = multiBound.getVariablesIndex(0);  //save lambda bound variable

            //find and replace matches if context exists
            if(!multiBound.containsMultipleBound(v))
                for(int j=1; j<term.getContentSize(); j++){                             //start search after MultiBound
                    if (term.getContentIndex(j) instanceof Variable) {
                        if (insert instanceof Variable && !(v.compare((Variable) insert)) && multiBound.containsAnotherBound((Variable) insert)) {
                            //insert type is Variable and is not same as lambda bound variable
                            //ALPHA                                                 //alpha conversion necessary
                            Term oldTerm = term.copyTerm();
                            Variable tmp = AlphaConversion.convert(term, (Variable) insert, multiBound.getVariables());
                            System.out.println("alpha result " + term);
                            if(out != null) {
                                TitledPane pane = out.getPanes().get(out.getPanes().size() - 1);
                                pane.setContent(new Text("alpha conversion:\n" + "change bound " + insert + " to " + tmp + " in " + oldTerm));
                                System.out.println(" 1 beta reduction:\ninsert " + insert + " into " + term);
                                out.getPanes().add(new TitledPane(input.toString(), new Text("beta reduction:\ninsert " + insert + " into " + term)));
                            }
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
            //will change MultiBound to SingleBound
            if(multiBound.getVariablesSize() == 1) {
                term.setContentIndex(0, multiBound.convertToSingleBound());
            }
        }else
            //found SingleBound
            if(term.getContentIndex(0) instanceof SingleBound) {
                SingleBound singleBound = ((SingleBound) term.getContentIndex(0));
                Variable v = singleBound.getVariable();  //save lambda bound variable

                //find and replace matches
                ArrayList<Term> terms = new ArrayList<>();
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
                                        Term oldTerm = term.copyTerm();
                                        Variable newVariable = AlphaConversion.convert(term, (Variable) insert, e);
                                        System.out.println("alpha result " + term);

                                        //in testcase out is null
                                        if(out != null) {
                                            TitledPane pane = out.getPanes().get(out.getPanes().size() - 1);
                                            pane.setContent(new Text("alpha conversion:\nchange bound " + insert + " to " + newVariable + " in " + oldTerm));
                                            System.out.println(" 2 beta reduction:\ninsert " + insert + " into " + term);
                                            out.getPanes().add(new TitledPane(input.toString(), new Text("beta reduction:\ninsert " + insert + " into " + term)));
                                        }
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
                        if(insert instanceof Variable && ((Term) term.getContentIndex(j)).isBound((Variable) insert) && ((Term) term.getContentIndex(j)).containsVariable((Variable) insert) && ((Term) term.getContentIndex(j)).containsVariable(v)) {
                            ArrayList<Variable> boundVariables = new ArrayList<>();
                            for(int i=0; i<((Term) term.getContentIndex(j)).getContentSize(); i++){
                                if(((Term) term.getContentIndex(j)).getContentIndex(i) instanceof SingleBound)
                                    boundVariables.add(((SingleBound) ((Term) term.getContentIndex(j)).getContentIndex(i)).getVariable());
                            }
                            Term oldTerm = ((Term) term.getContentIndex(j)).copyTerm();
                            Variable newVariable = AlphaConversion.convert((Term) term.getContentIndex(j), (Variable) insert, boundVariables);

                            //in testcase out is null
                            if(out != null) {
                                TitledPane pane = out.getPanes().get(out.getPanes().size() - 1);
                                System.out.println("alpha conversion:\nchange bound " + insert + " to " + newVariable + " in inner term " + oldTerm);
                                System.out.println(input);
                                pane.setContent(new Text("alpha conversion:\nchange bound " + insert + " to " + newVariable + " in inner term " + oldTerm));
                                System.out.println(" 3 beta reduction:\ninsert " + insert + " into " + term);
                                out.getPanes().add(new TitledPane(input.toString(), new Text("beta reduction:\ninsert " + insert + " into " + term)));
                            }
                        }
                        if(insert instanceof Term) {
                            if (!((Term) term.getContentIndex(j)).compare((Term) insert))
                                terms.add((Term) term.getContentIndex(j));
                        }
                        else
                            terms.add((Term) term.getContentIndex(j));
                    }
                }
                for(Term t : terms)
                    replaceVariablesInTermInTerm(t, v, insert);
                term.removeContentIndex(0);
            }

        if(term.getContentSize() == 1)         //will remove parentheses if term
            if (term.getContentIndex(0) instanceof Term)
                term.replaceContent(((Term) term.getContentIndex(0)).getContent());
        return term;
    }

    public static void betaReduction(Input input, Accordion out){
        try{
            LambdaExpression previous = input.getInputListIndex(0);       //the one to replace a bound-variable in
            LambdaExpression current;                                       //replacement

            int lastTermIndex = 0;
            for (int i = 1; i < input.getInputListSize() && !(input.getInputListIndex(0) instanceof Variable); i++) {
                current = input.getInputListIndex(i);
                if (previous != null) {
                    //in testcase out is null
                    if(out != null) {
                        TextFlow textFlow = new TextFlow();
                        textFlow.getChildren().addAll(new Text("beta reduction:"), new Text(System.lineSeparator()));
                        String text = input.toString();

                        Text previousText = new Text(text.substring(0, previous.toString().length()));
                        previousText.setFill(Color.GREEN);
                        text = text.substring(previous.toString().length());

                        Text currentText = new Text(text.substring(0, current.toString().length()));
                        currentText.setFill(Color.RED);
                        text = text.substring(current.toString().length());

                        Text post = new Text(text);
                        Text replaceText1 = null;
                        if(previous instanceof Term){
                            if(((Term) previous).containsBound()) {
                                LambdaExpression le = ((Term) previous).getContentIndex(0);
                                if(le instanceof SingleBound)
                                    replaceText1 = new Text("replace all " + ((SingleBound) le).getVariable() + " in ");
                                if(le instanceof MultiBound)
                                    replaceText1 = new Text("replace all " + ((MultiBound) le).getVariables().get(0) + " in ");
                            }
                        }
                        Text replaceText2 = new Text(previousText.getText());
                        replaceText2.setFill(Color.GREEN);
                        Text replaceText3 = new Text(" with ");
                        Text replaceText4 = new Text(currentText.getText());
                        replaceText4.setFill(Color.RED);

                        textFlow.getChildren().addAll(previousText, currentText, post,
                                new Text(System.lineSeparator()), replaceText1, replaceText2, replaceText3,replaceText4);

                        if(out.getPanes().size() == 0)
                            out.getPanes().add(new TitledPane(input.toString(), textFlow));
                        else
                        if(!out.getPanes().get(out.getPanes().size()-1).getText().equals(input.toString()))
                            out.getPanes().add(new TitledPane(input.toString(), textFlow));
                        else
                            out.getPanes().get(out.getPanes().size()-1).setContent(new Text("beta reduction:\ninsert " + current + " into " + previous));
                    }
                }
                if (previous instanceof Term && !(((Term) previous).getContentIndex(0) instanceof Variable)) {  //check if previous is type Term and does not start with a Variable
                    if(((Term) previous).getContentIndex(0) instanceof Term){   //if first LambdaExpression of term is a Term, go inside
                        checkInnerTerm(input, (Term) previous, lastTermIndex,0, out);
                        if(input.getInputListSize() == 2 && input.getInputListIndex(0) instanceof Term && ((Term) input.getInputListIndex(0)).containsNoVariableAtIndexZero()) {
                            i = 0;
                            previous = input.getInputListIndex(0);
                        }
                    }
                    else {
                        input.setInputListIndex(lastTermIndex, BetaReduction.reduce((Term) previous, current, input, out));
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
                    System.out.println(input);
                }else
                {
                    //found Variable at index 0 of the Term
                    lastTermIndex = i;
                    previous = current;
                    if(previous instanceof Term ) {
                        checkInnerTerm(input, (Term) previous, lastTermIndex,0, out);
                    }
                }
            }

            //check inner terms
            for(int i=0; i<input.getInputListSize(); i++)
                if(input.getInputListIndex(i) instanceof Term)
                    if(((Term) input.getInputListIndex(i)).containsTerm())
                        checkInnerTerm(input, (Term) input.getInputListIndex(i), i,0, out);

            //check for more beta
            for(int i=0; i<input.getInputListSize(); i++){
                if(input.getInputListIndex(i) instanceof Term && ((Term) input.getInputListIndex(i)).containsTermAtIndexZero()) {
                    checkInnerTerm(input, (Term) input.getInputListIndex(i), i,0, out);
                    i=0;
                }
            }

            if(out != null) {
                if (out.getPanes().size() > 0) {
                    if (!out.getPanes().get(out.getPanes().size() - 1).getText().equals(input.toString()))
                        out.getPanes().add(new TitledPane(input.toString(), new Text("end")));
                    else
                        out.getPanes().get(out.getPanes().size() - 1).setContent(new Text("end"));
                }
                else
                    out.getPanes().add(new TitledPane(input.toString(), new Text("end")));
            }
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

    public static void checkInnerTerm(Input input, Term term, int lastTermIndex, int dimension, Accordion out){
        TitledPane pane;
        //in testcase out is null
        if(out != null) {
            pane = out.getPanes().get(out.getPanes().size() - 1);
            if(pane.getText().equals(input.toString()))
                pane.setContent(new Text("beta reduction in inner Term\n" + term + "\ninsert " + term.getContentIndex(1) + " into " + term.getContentIndex(0)));
            else
                out.getPanes().add(new TitledPane(input.toString(), new Text("beta reduction in inner Term\n" + term + "\ninsert " + term.getContentIndex(1) + " into " + term.getContentIndex(0))));
        }
        System.out.println("inner term:" + term.getContentIndex(0) + " and " + term.getContentIndex(1));
        try{
            if(term.getContentIndex(0) instanceof Term  && ((Term) term.getContentIndex(0)).containsBound()) {
                if(!(((Term) term.getContentIndex(0)).getContentIndex(0) instanceof Term)){
                    BetaReduction.reduce((Term) term.getContentIndex(0), term.getContentIndex(1), input, out);
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
                    checkInnerTerm(input, (Term) term.getContentIndex(0), 0, dimension+1, out);
            }
            else
            if(term.getContentIndex(0) instanceof Variable || (term.getContentIndex(0) instanceof Term && ((Term) term.getContentIndex(0)).getContentIndex(0) instanceof Variable)){
                for(int i=1; i<term.getContentSize(); i++){
                    if(term.getContentIndex(i) instanceof Term){
                        checkInnerTerm(input,(Term) term.getContentIndex(i), i, dimension+1, out);
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
                checkInnerTerm(input, (Term) term.getContentIndex(1), lastTermIndex, dimension+1, out);
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
