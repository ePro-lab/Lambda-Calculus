package model;

public class SingleBound extends Bound{
    private final Variable variable;

    public SingleBound(Variable variable){
        System.out.println("add variable to singleBound");
        this.variable = variable;
    }

    @Override
    public String toString() {
        return "λ"+variable+".";
    }
}
