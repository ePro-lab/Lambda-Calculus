package operation;

import exception.EndlessLoopException;
import exception.NoFreeVariableException;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.*;

import java.util.ArrayList;

public class BetaReduction {
    private static int count = 0;

    public static void resetCount(){
        count = 0;
    }

    public static LambdaExpression reduce(Term term, LambdaExpression insert, Input input, Accordion out, String preString, String postString) throws Exception {
        if (++count >= 1000)
            throw new EndlessLoopException(input); //thrown to stop endless loop, caught in Gui

        //found MultiBound
        if (term.getContentIndex(0) instanceof MultiBound) {
            MultiBound multiBound = ((MultiBound) term.getContentIndex(0));
            Variable v = multiBound.getVariablesIndex(0);  //save lambda bound variable

            //find and replace matches if context exists
            if (!multiBound.containsMultipleBound(v)) {
                if (term.getContentIndex(1) instanceof Term && insert instanceof Variable && term.isBound((Variable) insert)) {
                    //ALPHA   in case of inner term at index 1 | else beta operation will be executed before alpha operation
                    Term oldTerm = term.copyTerm();
                    Variable newVariable = AlphaConversion.convert(term, (Variable) insert, multiBound.getVariables());
                    System.out.println("alpha result " + term);
                    if (out != null) {
                        writeAlpha(insert, newVariable, oldTerm, term, out, false);
                        System.out.println(" 0 beta reduction:\ninsert " + insert + " into " + term);
                        if(preString.equals("") && postString.equals(""))
                            writeBeta(input, term, insert, out);
                        else
                            writeInnerBeta(input, term, insert, out, null, preString, postString);
                    }
                }
                boolean changedTermAtIndexOne = false;
                for (int j = 1; j < term.getContentSize(); j++) {                             //start search after MultiBound
                    if (term.getContentIndex(j) instanceof Variable) {
                        if (insert instanceof Variable && !(v.compare((Variable) insert)) && multiBound.containsAnotherBound((Variable) insert)) {
                            //insert type is Variable and is not same as lambda bound variable
                            //ALPHA                                                 //alpha conversion necessary
                            Term oldTerm = term.copyTerm();
                            Variable newVariable = AlphaConversion.convert(term, (Variable) insert, multiBound.getVariables());
                            System.out.println("alpha result " + term);
                            if (out != null) {
                                writeAlpha(insert, newVariable, oldTerm, term, out, false);
                                System.out.println(" 1 beta reduction:\ninsert " + insert + " into " + term);
                                if(preString.equals("") && postString.equals(""))
                                    writeBeta(input, term, insert, out);
                                else
                                    writeInnerBeta(input, term, insert, out, null, preString, postString);
                            }
                        }
                        if (((Variable) term.getContentIndex(j)).compare(v)) {           //if variable matches lambda bound variable replace
                            term.setContentIndex(j, insert);
                        }
                        if (term.getContentIndex(1) instanceof Term && !((Term) term.getContentIndex(1)).isBound(v) && !changedTermAtIndexOne) {
                            ((Term) term.getContentIndex(1)).replaceVariables(v, insert);
                            changedTermAtIndexOne = true;
                        }
                    } else
                        //if Term in Term check context
                        if (term.getContentIndex(j) instanceof Term && !((Term) term.getContentIndex(j)).isBound(v))
                            replaceVariablesInTermInTerm(input, (Term) term.getContentIndex(j), v, insert, out, preString, postString);
                }
            }
            //remove lambda variable
            multiBound.removeVariablesIndex(0);
            //will change MultiBound to SingleBound
            if (multiBound.getVariablesSize() == 1) {
                term.setContentIndex(0, multiBound.convertToSingleBound());
            }
        } else
            //found SingleBound
            if (term.getContentIndex(0) instanceof SingleBound) {
                SingleBound singleBound = ((SingleBound) term.getContentIndex(0));
                Variable v = singleBound.getVariable();  //save lambda bound variable

                //find and replace matches
                ArrayList<Term> terms = new ArrayList<>();
                boolean foundMultipleMatchingBounds = false;                //if true, the lambda bound variable and the insert will be deleted due to no context
                for (int j = 1; j < term.getContentSize() && !foundMultipleMatchingBounds; j++) {                             //start first SingleBound
                    if (term.getContentIndex(j) instanceof Variable) {
                        if (insert instanceof Variable) {
                            for (int k = 1; k < term.getContentSize() && !foundMultipleMatchingBounds; k++) {
                                if (term.getContentIndex(k) instanceof SingleBound)
                                    if (((SingleBound) term.getContentIndex(k)).getVariable().compare(v))       //compare with other SingleBound variables
                                        foundMultipleMatchingBounds = true;
                                    else if (((Variable) insert).compare((((SingleBound) term.getContentIndex(k)).getVariable()))) {        //insert type is Variable and is same as lambda bound variable
                                        //ALPHA                                                 //alpha conversion necessary
                                        System.out.println("alpha");
                                        ArrayList<Variable> e = new ArrayList<>();
                                        for (int z = 0; z < term.getContentSize(); z++) {
                                            if (term.getContentIndex(z) instanceof SingleBound)
                                                e.add(((SingleBound) term.getContentIndex(z)).getVariable());
                                        }
                                        Term oldTerm = term.copyTerm();
                                        Variable newVariable = AlphaConversion.convert(term, (Variable) insert, e);
                                        System.out.println("alpha result " + term);

                                        //in testcase out is null
                                        if (out != null) {
                                            writeAlpha(insert, newVariable, oldTerm, term, out, false);
                                            System.out.println(" 2 beta reduction:\ninsert " + insert + " into " + term);
                                            if(preString.equals("") && postString.equals(""))
                                                writeBeta(input, term, insert, out);
                                            else
                                                writeInnerBeta(input, term, insert, out, null, preString, postString);
                                        }
                                    }
                            }
                        }
                        if (!foundMultipleMatchingBounds && ((Variable) term.getContentIndex(j)).compare(v)) {           //if variable matches lambda bound variable replace
                            if (insert instanceof Term && term.getContentSize() == 2 && ((Term) insert).getContentSize() == 2)       //solves '(λp.p)(λp.p)'
                                if (term.compare((Term) insert))
                                    return term;
                            if (insert instanceof Term) {
                                Term tmp = ((Term) insert).copyTerm();
                                term.setContentIndex(j, tmp);
                            } else
                                term.setContentIndex(j, insert);
                        }
                    }

                    //if Term in Term check context
                    if (term.getContentSize() > 1 && term.getContentIndex(j) instanceof Term && !((Term) term.getContentIndex(j)).isBound(v)) {
                        if (insert instanceof Variable && ((Term) term.getContentIndex(j)).isBound((Variable) insert) && ((Term) term.getContentIndex(j)).containsVariable((Variable) insert) && ((Term) term.getContentIndex(j)).containsVariable(v)) {
                            ArrayList<Variable> boundVariables = new ArrayList<>();
                            for (int i = 0; i < ((Term) term.getContentIndex(j)).getContentSize(); i++) {
                                if (((Term) term.getContentIndex(j)).getContentIndex(i) instanceof SingleBound)
                                    boundVariables.add(((SingleBound) ((Term) term.getContentIndex(j)).getContentIndex(i)).getVariable());
                            }
                            Term oldTerm = ((Term) term.getContentIndex(j)).copyTerm();
                            Variable newVariable = AlphaConversion.convert((Term) term.getContentIndex(j), (Variable) insert, boundVariables);

                            //in testcase out is null
                            if (out != null) {
                                //TitledPane pane = out.getPanes().get(out.getPanes().size() - 1);
                                System.out.println("alpha conversion:\nchange bound " + insert + " to " + newVariable + " in inner term " + oldTerm);
                                System.out.println(input);
                                //takes inner term term.getContentIndex(j) instead of term for comparison
                                writeAlpha(insert, newVariable, oldTerm, (Term) term.getContentIndex(j), out, true);
                                System.out.println(" 3 beta reduction:\ninsert " + insert + " into " + term);
                                if(preString.equals("") && postString.equals(""))
                                    writeBeta(input, term, insert, out);
                                else
                                    writeInnerBeta(input, term, insert, out, null, preString, postString);
                            }
                        }
                        if (insert instanceof Term) {
                            if (!((Term) term.getContentIndex(j)).compare((Term) insert))
                                terms.add((Term) term.getContentIndex(j));
                        } else
                            terms.add((Term) term.getContentIndex(j));
                    }
                }
                for (Term t : terms)
                    replaceVariablesInTermInTerm(input, t, v, insert, out, preString, postString);
                term.removeContentIndex(0);
            }

        if (term.getContentSize() == 1)         //will remove parentheses if term
            if (term.getContentIndex(0) instanceof Term)
                term.replaceContent(((Term) term.getContentIndex(0)).getContent());
        return term;
    }

    public static Input betaReduction(Input input, Accordion out, String preString, String postString) {
        try {
            LambdaExpression previous = input.getInputListIndex(0);       //the one to replace a bound-variable in
            LambdaExpression current;                                       //replacement

            int lastTermIndex = 0;
            if (!(input.getInputListIndex(0) instanceof Variable)) {
                for (int i = 1; i < input.getInputListSize() && !(input.getInputListIndex(0) instanceof Variable); i++) {
                    if (++count >= 300)
                        throw new EndlessLoopException(input); //thrown to stop endless loop, caught in Gui -> will stop e.g. (Lx.xx)(Lx.xxx)
                    current = input.getInputListIndex(i);
                    if (previous != null) {
                        //in testcase out is null
                        if (out != null)
                            if(preString.equals("") && postString.equals(""))
                                writeBeta(input, previous, current, out);
                            else
                            if(previous instanceof Term)
                                writeInnerBeta(input, (Term) previous, current, out, null, preString, postString);
                    }
                    if (previous instanceof Term && !(((Term) previous).getContentIndex(0) instanceof Variable)) {  //check if previous is type Term and does not start with a Variable
                        if (((Term) previous).getContentIndex(0) instanceof Term) {   //if first LambdaExpression of term is a Term, go inside
                            checkInnerTerm(input, (Term) previous, lastTermIndex, 0, out, preString, postString);
                            if (input.getInputListSize() == 2 && input.getInputListIndex(0) instanceof Term && ((Term) input.getInputListIndex(0)).containsNoVariableAtIndexZero()) {
                                i = 0;
                                previous = input.getInputListIndex(0);
                            }
                        } else {
                            input.setInputListIndex(lastTermIndex, BetaReduction.reduce((Term) previous, current, input, out, preString, postString));
                            input.deleteListIndex(i);
                            //'current' has been deleted, so index i will not be 'next' LambdaExpression
                            i = 0;
                            //check if returned 'term' is size 1, if so replace with content of term
                            if (((Term) input.getInputListIndex(lastTermIndex)).getContentSize() == 1)
                                input.setInputListIndex(lastTermIndex, ((Term) input.getInputListIndex(lastTermIndex)).getContentIndex(0));
                            else if (input.containsOnlyTerms() && input.getInputListIndex(0) instanceof Term && ((Term) input.getInputListIndex(0)).getContentIndex(0) instanceof Term) {
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
                                            ((((Term) input.getInputListIndex(0)).getContentSize() > 1) && (((Term) input.getInputListIndex(0)).getContentIndex(0) instanceof Term) && ((Term) input.getInputListIndex(0)).getContentIndex(1) instanceof Variable))) {
                                input.replaceInputList(((Term) previous).getContent());
                                previous = input.getInputListIndex(0);
                                i = 0;
                            }
                        }
                        System.out.println(input);
                    } else {
                        //found Variable at index 0 of the Term
                        lastTermIndex = i;
                        previous = current;
                        if (previous instanceof Term) {
                            checkInnerTerm(input, (Term) previous, lastTermIndex, 0, out, preString, postString);
                        }
                    }
                }

                //check inner terms
                for (int i = 0; i < input.getInputListSize(); i++)
                    if (input.getInputListIndex(i) instanceof Term)
                        if (((Term) input.getInputListIndex(i)).containsTerm())
                            checkInnerTerm(input, (Term) input.getInputListIndex(i), i, 0, out, preString, postString);

                //check for more beta
                for (int i = 0; i < input.getInputListSize(); i++) {
                    if (input.getInputListIndex(i) instanceof Term && ((Term) input.getInputListIndex(i)).containsTermAtIndexZero()) {
                        checkInnerTerm(input, (Term) input.getInputListIndex(i), i, 0, out, preString, postString);
                        i = 0;
                    }
                }
            }

            //check if first term contains a term after bound -> more beta
            boolean runInner = false;
            ArrayList<LambdaExpression> lambdaExpressionsForTestInput = new ArrayList<>();  //used for tests
            if(input.getInputListIndex(0) instanceof Term )
                if(((Term) input.getInputListIndex(0)).containsBound() && ((Term) input.getInputListIndex(0)).containsTermAfterBound()) {
                    ArrayList<LambdaExpression> lambdaExpressions = new ArrayList<>();
                    Term outerTerm = (Term) input.removeListIndex(0);
                    Term termAfterBound = outerTerm.getTermAfterBound();
                    lambdaExpressions.add(termAfterBound);
                    boolean found = false;  //when found index of term after bound; copy rest
                    StringBuilder preStringBuilder = new StringBuilder(preString);
                    preStringBuilder.append("(");
                    for(int i = 0; i<outerTerm.getContentSize(); i++){
                        if(!found) {
                            if (outerTerm.getContentIndex(i) instanceof Term && ((Term) outerTerm.getContentIndex(i)).compare(termAfterBound))
                                found = true;
                            else {
                                preStringBuilder.append(outerTerm.getContentIndex(i));
                                lambdaExpressionsForTestInput.add(outerTerm.getContentIndex(i));
                            }
                        }else {
                            if (outerTerm.getContentIndex(i) instanceof Variable)
                                lambdaExpressions.add(new Variable(((Variable) outerTerm.getContentIndex(i)).getVariable()));
                            else if (outerTerm.getContentIndex(i) instanceof Term)
                                lambdaExpressions.add(((Term) outerTerm.getContentIndex(i)).copyTerm());
                        }
                    }
                    preString = preStringBuilder.toString();
                    postString = postString + ")";
                    Input newInput = new Input(lambdaExpressions);
                    input = betaReduction(newInput, out, preString, postString);
                    Term tmp = new Term(lambdaExpressionsForTestInput);
                    for(int i=0; i<input.getInputListSize(); i++){
                        if(input.getInputListIndex(i) instanceof Term)
                            tmp.addTerm((Term) input.getInputListIndex(i));
                        else if(input.getInputListIndex(i) instanceof Variable)
                            tmp.addVariable((Variable) input.getInputListIndex(i));
                    }
                    ArrayList<LambdaExpression> replacementList = new ArrayList<>();
                    replacementList.add(tmp);
                    input.replaceInputList(replacementList);
                    runInner = true;
                }

            if (out != null && !runInner) {
                if (out.getPanes().size() > 0) {
                    if (!out.getPanes().get(out.getPanes().size() - 1).getText().equals(preString + input + postString))
                        out.getPanes().add(new TitledPane(input.toString(), new Text("end")));
                    else
                        out.getPanes().get(out.getPanes().size() - 1).setContent(new Text("end"));
                } else
                    out.getPanes().add(new TitledPane(input.toString(), new Text("end")));
            }
            System.out.println(input);
            return input;
        } catch (NoFreeVariableException e) {         //caused by alpha conversion
            if (out != null)
                out.getPanes().get(out.getPanes().size() - 1).setContent(new Text(e.getMessage()));
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) { //caused by illegal character input
            e.printStackTrace();
            System.out.println("illegal input");
        } catch (EndlessLoopException e1) {
            if (out != null) {
                if(out.getPanes().size() > 1) {
                    TextFlow tmpTF = new TextFlow();
                    tmpTF.getChildren().addAll(out.getPanes().get(0).getContent(), new Text(System.lineSeparator()), new Text(System.lineSeparator()), new Text("Encountered endless loop."));
                    out.getPanes().get(0).setContent(tmpTF);
                }else
                    out.getPanes().get(0).setContent(new Text("Encountered endless loop."));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }

    public static void checkInnerTerm(Input input, Term term, int lastTermIndex, int dimension, Accordion out, String preString, String postString) {
        TitledPane pane;
        //in testcase out is null
        if (out != null && term.getContentIndex(0) instanceof Term) {
            if (out.getPanes().size() > 0) {
                pane = out.getPanes().get(out.getPanes().size() - 1);
                if (pane.getText().equals(input.toString()))
                    writeInnerBeta(input, (Term) term.getContentIndex(0), term.getContentIndex(1), out, pane, preString, postString);
                else
                    writeInnerBeta(input, (Term) term.getContentIndex(0), term.getContentIndex(1), out, null, preString, postString);
            } else if (term.containsBound())
                writeInnerBeta(input, (Term) term.getContentIndex(0), term.getContentIndex(1), out, null, preString, postString);
        }
        System.out.println("inner term:" + term.getContentIndex(0) + " and " + term.getContentIndex(1));
        try {
            if (term.getContentIndex(0) instanceof Term && ((Term) term.getContentIndex(0)).containsBound()) {
                if (!(((Term) term.getContentIndex(0)).getContentIndex(0) instanceof Term)) {
                    BetaReduction.reduce((Term) term.getContentIndex(0), term.getContentIndex(1), input, out, preString, postString);
                    term.removeContentIndex(1);
                    if (((Term) term.getContentIndex(0)).getContentSize() == 1)
                        term.setContentIndex(0, ((Term) term.getContentIndex(0)).getContentIndex(0));
                    if (term.getContentSize() == 1) {                             //will remove parentheses
                        if (term.getContentIndex(0) instanceof Term)
                            term.replaceContent(((Term) term.getContentIndex(0)).getContent());
                        else if (dimension == 0)
                            input.setInputListIndex(lastTermIndex, term.getContentIndex(0));
                    }
                    System.out.println("inner result:" + input.getInputListIndex(lastTermIndex));
                } else
                    //if first LE of (2)Term in (1)Term is a (3)Term check inner Term of (2)
                    checkInnerTerm(input, (Term) term.getContentIndex(0), 0, dimension + 1, out, preString, postString);
            } else if (term.getContentIndex(0) instanceof Variable || (term.getContentIndex(0) instanceof Term && ((Term) term.getContentIndex(0)).getContentIndex(0) instanceof Variable)) {
                for (int i = 1; i < term.getContentSize(); i++) {
                    if (term.getContentIndex(i) instanceof Term) {
                        checkInnerTerm(input, (Term) term.getContentIndex(i), i, dimension + 1, out, preString, postString);
                        term.clean();
                        if (term.containsOnlyVariableTerms())
                            if (term.getContentSize() == 2) {
                                ArrayList<LambdaExpression> tmp = new ArrayList<>();
                                for (int j = 0; j < input.getInputListSize(); j++) {
                                    if (j != lastTermIndex)
                                        tmp.add(input.getInputListIndex(j));
                                    else
                                        for (int k = 0; k < term.getContentSize(); k++) {
                                            tmp.add(term.getContentIndex(k));
                                        }
                                }
                                input.replaceInputList(tmp);
                            } else
                                term.clean();
                        else if (term.getContentSize() > 1 && term.containsOnlyVariableTerms()) {
                            ArrayList<LambdaExpression> tmp = new ArrayList<>();
                            for (i = 0; i < lastTermIndex; i++)
                                tmp.add(input.getInputListIndex(i));
                            tmp.addAll(term.getContent());
                            input.replaceInputList(tmp);
                        }

                    }
                }
            } else if (term.getContentIndex(0) instanceof Bound && term.getContentIndex(1) instanceof Term && ((Term) term.getContentIndex(1)).containsTermAtIndexZero())
                checkInnerTerm(input, (Term) term.getContentIndex(1), lastTermIndex, dimension + 1, out, preString, postString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void replaceVariablesInTermInTerm(Input input, Term term, Variable variable, LambdaExpression insert, Accordion out, String preString, String postString) throws NoFreeVariableException {
        boolean alphaDOne = false;
        for (int i = 0; i < term.getContentSize(); i++) {
            if (term.getContentIndex(i) instanceof Term) {
                if (!((Term) term.getContentIndex(i)).isBound(variable))
                    ((Term) term.getContentIndex(i)).replaceVariables(variable, insert);
            } else if (term.getContentIndex(i) instanceof Variable && insert instanceof Variable) {
                if (term.isBound(variable) || (term.isBound((Variable) insert) && term.containsVariable(variable)) || term.containsVariable((Variable) insert)) {
                    ArrayList<Variable> boundVariables = new ArrayList<>();
                    boundVariables.add(variable);
                    boundVariables.add((Variable) insert);
                    if (term.getContentIndex(0) instanceof MultiBound)
                        boundVariables.addAll(((MultiBound) term.getContentIndex(0)).getVariables());
                    else
                        for (int j = 0; j < term.getContentSize(); j++)
                            if (term.getContentIndex(j) instanceof SingleBound)
                                boundVariables.add(((SingleBound) term.getContentIndex(j)).getVariable());
                    if(term.isBound(variable) && term.containsVariable(variable)) {
                        Term oldTerm = term.copyTerm();
                        Variable newVariable = AlphaConversion.convert(term, variable, boundVariables);
                        if(!oldTerm.compare(term))
                            alphaDOne = true;
                        if (out != null) {
                            writeAlpha(insert, newVariable, oldTerm, term, out, true);
                            if(preString.equals("") && postString.equals(""))
                                writeBeta(input, input.getInputListIndex(0), insert, out);
                            else
                            if(input.getInputListIndex(0) instanceof Term)
                                writeInnerBeta(input, (Term) input.getInputListIndex(0), insert, out, null, preString, postString);

                        }
                    }
                    else if(term.isBound((Variable) insert) && term.containsVariable(variable)) {
                        Term oldTerm = term.copyTerm();
                        Variable newVariable = AlphaConversion.convert(term, (Variable) insert, boundVariables);
                        if(!oldTerm.compare(term))
                            alphaDOne = true;
                        if (out != null) {
                            writeAlpha(insert, newVariable, oldTerm, term, out, true);
                            if(preString.equals("") && postString.equals(""))
                                writeBeta(input, input.getInputListIndex(0), insert, out);
                            else
                            if(input.getInputListIndex(0) instanceof Term)
                                writeInnerBeta(input, (Term) input.getInputListIndex(0), insert, out, null, preString, postString);
                        }
                    }
                }
                if(!alphaDOne)
                    term.replaceVariables(variable, insert);
            } else if (term.getContentIndex(i) instanceof Variable && insert instanceof Term) {
                if (((Variable) term.getContentIndex(i)).compare(variable))
                    term.setContentIndex(i, insert);
            }

        }
    }

    private static void writeBeta(Input input, LambdaExpression previous, LambdaExpression current, Accordion out) {
        TextFlow textFlow = new TextFlow();
        textFlow.getChildren().addAll(new Text("beta reduction:"), new Text(System.lineSeparator()));
        String text = input.toString();

        ArrayList<Text> textElements = new ArrayList<>();
        Text previousText = new Text();
        if (previous instanceof Term)
            if (!((Term) previous).containsTerm()) {
                previousText.setText(text.substring(0, previous.toString().length()));
                previousText.setFill(Color.GREEN);
                text = text.substring(previous.toString().length());
            } else { //previous is Term and contains Terms, check for context green/black output
                Text tmp = new Text("(");
                tmp.setFill(Color.GREEN);
                textElements.add(tmp);
                text = text.substring(tmp.getText().length());
                for (int i = 0; i < ((Term) previous).getContentSize(); i++) {
                    LambdaExpression element = ((Term) previous).getContentIndex(i);
                    if (!(element instanceof Term) || !((Term) element).containsBound()) {
                        tmp = new Text(element.toString());
                        tmp.setFill(Color.GREEN);
                        textElements.add(tmp);
                        text = text.substring(tmp.getText().length());
                    } else {
                        Variable var = new Variable(' ');
                        LambdaExpression le = ((Term) previous).getContentIndex(0);
                        if (le instanceof SingleBound)
                            var = ((SingleBound) le).getVariable();
                        else if (le instanceof MultiBound)
                            var = ((MultiBound) le).getVariables().get(0);
                        if(((Term) element).isBound(var) || !((Term) element).containsVariable(var)){ //is bound output black
                            tmp = new Text(element.toString());
                            tmp.setFill(Color.BLACK);
                            textElements.add(tmp);
                            text = text.substring(tmp.getText().length());
                        }else { //is not bound output green
                            tmp = new Text(element.toString());
                            tmp.setFill(Color.GREEN);
                            textElements.add(tmp);
                            text = text.substring(tmp.getText().length());
                        }
                    }
                }
                tmp = new Text(")");
                tmp.setFill(Color.GREEN);
                textElements.add(tmp);
                text = text.substring(tmp.getText().length());
            }

        Text currentText;
        if (text.length() > 0) {
            currentText = new Text(text.substring(0, current.toString().length()));
            text = text.substring(current.toString().length());
        } else
            currentText = new Text(text);
        currentText.setFill(Color.RED);

        Text post = new Text(text);
        Text replaceText1 = new Text();
        if (previous instanceof Term) {
            if (((Term) previous).containsBound()) {
                LambdaExpression le = ((Term) previous).getContentIndex(0);
                if (le instanceof SingleBound)
                    replaceText1 = new Text("replace all " + ((SingleBound) le).getVariable() + " in ");
                else if (le instanceof MultiBound)
                    replaceText1 = new Text("replace all " + ((MultiBound) le).getVariables().get(0) + " in ");
            }
        }
        Text replaceText2 = new Text(previousText.getText());
        replaceText2.setFill(Color.GREEN);
        Text replaceText3 = new Text(" with ");
        Text replaceText4 = new Text(currentText.getText());
        replaceText4.setFill(Color.RED);

        if(textElements.isEmpty())
            if (post.getText().equals(""))  //if content of post is "" it will result in empty line, so don't add post
                textFlow.getChildren().addAll(previousText, currentText,
                        new Text(System.lineSeparator()), replaceText1, replaceText2, replaceText3, replaceText4);
            else
                textFlow.getChildren().addAll(previousText, currentText, post,
                        new Text(System.lineSeparator()), replaceText1, replaceText2, replaceText3, replaceText4);
        else {
            ArrayList<Text> copy = new ArrayList<>(); //needed since duplicate children are not allowed in TextFlow
            for (Text previousTextElement : textElements) {
                textFlow.getChildren().add(previousTextElement);
                Text tmpCopy = new Text(previousTextElement.getText());
                tmpCopy.setFill(previousTextElement.getFill());
                copy.add(tmpCopy);
            }
            if (post.getText().equals(""))   //if content of post is "" it will result in empty line, so don't add post
                textFlow.getChildren().addAll(currentText, new Text(System.lineSeparator()), replaceText1);
            else
                textFlow.getChildren().addAll(currentText, post, new Text(System.lineSeparator()), replaceText1);
            for (Text previousTextElementCopy : copy)
                textFlow.getChildren().add(previousTextElementCopy);
            textFlow.getChildren().addAll(replaceText3, replaceText4);
        }


        if (out.getPanes().size() == 0)
            out.getPanes().add(new TitledPane(input.toString(), textFlow));
        else if (!out.getPanes().get(out.getPanes().size() - 1).getText().equals(input.toString()))
            out.getPanes().add(new TitledPane(input.toString(), textFlow));
        else
            out.getPanes().get(out.getPanes().size() - 1).setContent(new Text("beta reduction:\ninsert " + current + " into " + previous));
    }

    private static void writeInnerBeta(Input input, Term innerTerm, LambdaExpression insert, Accordion out, TitledPane pane, String preString, String postString) {
        TextFlow textFlow = new TextFlow();
        textFlow.getChildren().addAll(new Text("beta reduction in inner Term:"), new Text(System.lineSeparator()));

        String text;
        if(preString.equals(""))
            if (postString.equals(""))
                text = input.toString();
            else
                text = input.toString() + postString;
        else
            text = preString + input.toString() + postString;

        Text pre = new Text(text.substring(0, text.indexOf(innerTerm.toString())));

        int i = text.indexOf(innerTerm.toString());
        Text previousText = new Text(text.substring(i, innerTerm.toString().length() + i));
        previousText.setFill(Color.GREEN);
        text = text.substring(previousText.getText().length() + pre.getText().length());

        Text currentText = new Text(text.substring(0, insert.toString().length()));
        currentText.setFill(Color.RED);
        text = text.substring(currentText.getText().length());

        Text post = new Text(text);

        Text replaceText1 = new Text();
        LambdaExpression le = innerTerm.getContentIndex(0);
        if (le instanceof SingleBound)
            replaceText1 = new Text("replace all " + ((SingleBound) le).getVariable() + " in ");
        if (le instanceof MultiBound)
            replaceText1 = new Text("replace all " + ((MultiBound) le).getVariables().get(0) + " in ");

        Text replaceText2 = new Text(previousText.getText());
        replaceText2.setFill(Color.GREEN);
        Text replaceText3 = new Text(" with ");
        Text replaceText4 = new Text(currentText.getText());
        replaceText4.setFill(Color.RED);

        textFlow.getChildren().addAll(pre, previousText, currentText, post, new Text(System.lineSeparator()),
                replaceText1, replaceText2, replaceText3, replaceText4);

        if (pane == null)
            if(preString.equals(""))
                if (postString.equals(""))
                    out.getPanes().add(new TitledPane(input.toString(), textFlow));
                else
                    out.getPanes().add(new TitledPane(input + postString, textFlow));
            else
                out.getPanes().add(new TitledPane(preString + input + postString, textFlow));
        else
            pane.setContent(textFlow);
    }

    private static void writeAlpha(LambdaExpression insert, Variable newVariable, Term oldTerm, Term term, Accordion out, boolean inner) {
        TitledPane pane = out.getPanes().get(out.getPanes().size() - 1);

        TextFlow textFlow = new TextFlow();
        if (!inner)
            textFlow.getChildren().addAll(new Text("alpha conversion:"), new Text(System.lineSeparator()));
        else
            textFlow.getChildren().addAll(new Text("alpha conversion in inner Term:"), new Text(System.lineSeparator()));

        String oldTermString = oldTerm.toString();
        String termString = term.toString();

        int lastDif = 0;
        for (int i = 0; i < oldTermString.length(); i++) {
            if (oldTermString.charAt(i) != termString.charAt(i)) {
                Text dif = new Text(oldTermString.charAt(i) + "");
                dif.setFill(Color.RED);
                if(lastDif == i)
                    textFlow.getChildren().addAll(dif);
                else
                    textFlow.getChildren().addAll(new Text(oldTermString.substring(lastDif, i)), dif);
                lastDif = i + 1;
            }
        }
        if (lastDif < oldTermString.length())
            textFlow.getChildren().add(new Text(oldTermString.substring(lastDif)));

        Text insertText = new Text(insert.toString());
        insertText.setFill(Color.RED);
        Text newVariableText = new Text(newVariable.toString());
        newVariableText.setFill(Color.GREEN);

        textFlow.getChildren().addAll(new Text(System.lineSeparator()), new Text("change bound "), insertText, new Text(" to "), newVariableText);

        pane.setContent(textFlow);
    }
}
