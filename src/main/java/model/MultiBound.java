package model;

import java.util.ArrayList;

public class MultiBound extends Bound{
    private final ArrayList<Variable> variables;

    public MultiBound(){
        variables = new ArrayList<>();
    }

    public void addVariable(Variable variable){
        System.out.println("add variable to multiBound");
        variables.add(variable);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("λ");
        for(LambdaExpression le : variables) {
            stringBuilder.append(le.toString());
        }
        stringBuilder.append(".");
        return stringBuilder.toString();
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public int getVariablesSize(){
        return variables.size();
    }

    public Variable getVariablesIndex(int index){
        return variables.get(index);
    }

    public void setVariablesIndex(int index, Variable variable){
        variables.set(index,variable);
    }

    public void removeVariablesIndex(int index){
        variables.remove(index);
    }
}
