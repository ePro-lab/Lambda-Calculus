package model;

public class Variable extends LambdaExpression{
    private final char variable;

    public Variable(char variable){
        this.variable = variable;
    }

    @Override
    public String toString() {
        return ""+variable;
    }

    public char getVariable() {
        return variable;
    }

    public boolean compare(Variable other){
        return this.variable == other.getVariable();
    }
}
