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

    public SingleBound convertToSingleBound() throws Exception {
        if(variables.size() > 1)
            throw new Exception("Cannot be cast to SingleBound. More than 1 Variable."); //should not happen
        return new SingleBound(variables.get(0));
    }

    public MultiBound copyMultiBound(){
        MultiBound tmp = new MultiBound();
        for (Variable variable : variables) tmp.addVariable(new Variable(variable.getVariable()));
        return tmp;
    }

    public boolean containsMultipleBound(Variable variable){
        int count = 0;
        for (Variable value : variables) {
            if (value.compare(variable))
                count++;
            if (count == 2)
                return true;
        }
        return false;
    }

    public boolean containsAnotherBound(Variable variable){
        for(int i=1; i<variables.size(); i++) {
            if (variables.get(i).compare(variable))
                return true;
        }
        return false;
    }
}
