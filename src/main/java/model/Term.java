package model;

import java.util.ArrayList;

public class Term extends LambdaExpression{
    private ArrayList<LambdaExpression> content;

    public Term(){
        content = new ArrayList<>();
    }

    public Term(ArrayList<LambdaExpression> content){ this.content = content; }

    public void addTerm(Term term){
        content.add(term);
    }

    public void addVariable(Variable variable){
        content.add(variable);
    }

    public void addBound(Bound bound){
        content.add(bound);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        for(LambdaExpression le : content)
            stringBuilder.append(le.toString());
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
        if(replacement instanceof Term){                        //necessary to avoid StackOverflowError
            Term tmp = ((Term) replacement).copyTerm();
            ArrayList<LambdaExpression> a = new ArrayList<>(tmp.getContent());
            content.set(i,new Term(a));
        }
        else
            content.set(i,replacement);
    }

    public void removeContentIndex(int i){
        content.remove(i);
    }

    public ArrayList<LambdaExpression> getContent() {
        return content;
    }

    public void replaceContent(ArrayList<LambdaExpression> newList) {
        content = newList;
    }

    public boolean compare(Term other){
        ArrayList<LambdaExpression> otherContent = other.getContent();
        if(content.size() != otherContent.size())
            return false;
        for (int i=0; i<content.size(); i++) {
            if (content.get(i) instanceof SingleBound && otherContent.get(i) instanceof SingleBound) {
                if (!((SingleBound) content.get(i)).getVariable().compare(((SingleBound) otherContent.get(i)).getVariable()))
                    return false;
            }
            else
            if (content.get(i) instanceof MultiBound && otherContent.get(i) instanceof MultiBound){
                ArrayList<Variable> firstMultiBound = ((MultiBound) content.get(i)).getVariables();
                ArrayList<Variable> otherMultiBound = ((MultiBound) otherContent.get(i)).getVariables();
                if(firstMultiBound.size() != otherMultiBound.size())
                    return false;
                for(int j=0; j<firstMultiBound.size(); j++){
                    if(!(firstMultiBound.get(j).compare(otherMultiBound.get(j))))
                        return false;
                }
            }
            else
            if(content.get(i) instanceof Variable && otherContent.get(i) instanceof Variable) {
                if(!(((Variable) content.get(i)).compare((Variable) otherContent.get(i))))
                    return false;
            }
        }
        return true;
    }

    public boolean isBound(Variable variable){
        boolean run = true;     //only if MultiBound
        for(int i=0; i<content.size() && run; i++){
            if(content.get(i) instanceof SingleBound) {
                if (((SingleBound) content.get(i)).getVariable().compare(variable))
                    return true;
            }
            else{
                if(content.get(i) instanceof MultiBound){
                    run = false;
                    for(int j=0; j<((MultiBound) content.get(i)).getVariablesSize(); j++)
                        if(((MultiBound) content.get(i)).getVariablesIndex(j).compare(variable))
                            return true;
                }
            }
        }
        return false;
    }

    public boolean containsBound(){
        for (LambdaExpression lambdaExpression : content)
            if (lambdaExpression instanceof SingleBound || lambdaExpression instanceof MultiBound)
                return true;
            else if (lambdaExpression instanceof Term)
                return ((Term) lambdaExpression).containsBound();
        return false;
    }

    public boolean containsVariable(Variable variable){
        for (LambdaExpression lambdaExpression : content) {
            if (lambdaExpression instanceof Variable)
                if (((Variable) lambdaExpression).compare(variable))
                    return true;
        }
        return false;
    }


    public boolean containsTerm(){
        for (LambdaExpression lambdaExpression : content)
            if (lambdaExpression instanceof Term)
                return true;
        return false;
    }

    public boolean containsOnlyVariableTerms(){
        for(LambdaExpression lambdaExpression : content)
            if(lambdaExpression instanceof Variable || (lambdaExpression instanceof Term && !(((Term) lambdaExpression).getContentIndex(0) instanceof Variable)))
                return false;
        return true;
    }

    public boolean containsOnlyOneVariable(){
        return content.size() == 1 && content.get(0) instanceof Variable;
    }

    //((z)(z)) -> (zz)
    public void clean(){
        for(int i=0; i<content.size(); i++)
            if(content.get(i) instanceof Term)
                if(((Term) content.get(i)).containsOnlyOneVariable())
                    content.set(i, ((Term) content.get(i)).getContentIndex(0));
                else
                    ((Term) content.get(i)).clean();

    }

    public boolean containsNoVariableAtIndexZero(){
        if(content.get(0) instanceof Variable)
            return false;
        if(content.get(0) instanceof Term)
            return ((Term) content.get(0)).containsNoVariableAtIndexZero();
        return true;
    }

    public boolean containsTermAtIndexZero(){
        return content.get(0) instanceof Term;
    }

    public void replaceVariables(Variable variable, LambdaExpression lambdaExpression){
        for(int i=0; i<content.size(); i++)
            if(content.get(i) instanceof Variable && ((Variable) content.get(i)).compare(variable))
                content.set(i, lambdaExpression);
    }

    public Term copyTerm(){
        ArrayList<LambdaExpression> tmp = new ArrayList<>();
        for(LambdaExpression lambdaExpression : content)
            if(lambdaExpression instanceof Term)
                tmp.add(((Term) lambdaExpression).copyTerm());
            else
            if(lambdaExpression instanceof MultiBound)
                tmp.add(((MultiBound) lambdaExpression).copyMultiBound());
            else
            if(lambdaExpression instanceof SingleBound)
                tmp.add(new SingleBound(((SingleBound) lambdaExpression).getVariable()));
            else
            if(lambdaExpression instanceof Variable)
                tmp.add(new Variable(((Variable) lambdaExpression).getVariable()));
        return new Term(tmp);
    }

}
