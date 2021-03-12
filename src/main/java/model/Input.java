package model;

import java.util.ArrayList;

public class Input{
    private final ArrayList<LambdaExpression> inputList;

    public Input() {
        inputList = new ArrayList<>();
    }

    public void addTerm(Term term){

        System.out.println("add term to input");
        inputList.add(term);
    }

    public void addVariable(Variable variable){

        System.out.println("add variable to input");
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

    public void setInputListIndex(int i, LambdaExpression lambdaExpression){
        inputList.set(i,lambdaExpression);
    }

    public void removeInputListIndex(int i){
        inputList.remove(i);
    }
}
