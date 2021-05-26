package model;

public class SingleBound extends Bound{
    private Variable variable;

    public SingleBound(Variable variable){
        System.out.println("add variable to singleBound");
        this.variable = variable;
    }

    @Override
    public String toString() {
        return "λ"+variable+".";
    }

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable){
        this.variable = variable;
    }
}
