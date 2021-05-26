package exception;

import model.Term;

public class NoFreeVariableException extends Exception{
    public NoFreeVariableException(Term term){
        super("No alpha conversion possible, all allowed variables [a-z] are used in \"" + term.toString() + "\".");
    }
}
