package exception;

import model.Input;

public class EndlessLoopException extends Exception {
    public EndlessLoopException(Input input) {
        super("Encountered endless loop in input \"" + input.toString() + "\".");
    }
}
