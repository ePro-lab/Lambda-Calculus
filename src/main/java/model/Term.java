package model;

import java.util.ArrayList;

public class Term extends LambdaExpression{
    private final ArrayList<LambdaExpression> content;

    public Term(){
        content = new ArrayList<>();
    }

    public void addTerm(Term term){

        System.out.println("add term to term");
        content.add(term);
    }

    public void addVariable(Variable variable){
        System.out.println("add variable to term");
        content.add(variable);
    }

    public void addBound(Bound bound){
        System.out.println("add bound to term");
        content.add(bound);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        for(LambdaExpression le : content) {
               stringBuilder.append(le.toString());
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public int getContentSize(){
        return content.size();
    }

    public LambdaExpression getContentIndex(int i){
        return content.get(i);
    }

    public void setContentIndex(int i, LambdaExpression replacement){
        content.set(i,replacement);
    }

    public void removeContentIndex(int i){
        content.remove(i);
    }
}
