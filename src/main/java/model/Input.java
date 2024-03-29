package model;

import java.util.ArrayList;

public class Input{
    private ArrayList<LambdaExpression> inputList;

    public Input() {
        inputList = new ArrayList<>();
    }

    public Input(ArrayList<LambdaExpression> lambdaExpressions){
        this.inputList = lambdaExpressions;
    }

    public void addTerm(Term term){
        inputList.add(term);
    }

    public void addVariable(Variable variable){
        inputList.add(variable);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(LambdaExpression le : inputList)
            stringBuilder.append(le.toString());
        return stringBuilder.toString();
    }

    public int getInputListSize(){
        return inputList.size();
    }

    public LambdaExpression getInputListIndex(int i){
        return inputList.get(i);
    }

    public LambdaExpression removeListIndex(int i){
        return inputList.remove(i);
    }

    public void setInputListIndex(int i, LambdaExpression lambdaExpression){
        inputList.set(i,lambdaExpression);
    }

    public void deleteListIndex(int i){
        inputList.remove(i);
    }

    public void replaceInputList(ArrayList<LambdaExpression> newList) {
        inputList = newList;
    }

    public boolean containsOnlyTerms(){
        for (LambdaExpression lambdaExpression : inputList)
            if (!(lambdaExpression instanceof Term))
                return false;
        return true;
    }

    //((λx.xz)(x(λx.xz)))
    //should only be used if containsOnlyTerms returns true
    public void removeParentheses(){
        if(inputList.size() == 1 && inputList.get(0) instanceof Term)
            inputList = ((Term) inputList.get(0)).getContent();
        else {
            ArrayList<LambdaExpression> tmp = new ArrayList<>();
            for (LambdaExpression t : inputList) {
                if (t instanceof Term && ((Term) t).getContentSize() == 1 && ((Term) t).getContentIndex(0) instanceof Term) {
                    tmp.addAll(((Term) t).getContent());
                } else
                    tmp.add(t);
            }
            inputList = tmp;
        }
    }
}
