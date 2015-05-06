/**
 * Runtime error exceptions occur when a program's parse tree is
 * executed, and are errors that were not detected during parsing.
 *
 * These errors may include division by zero, or trying to
 * dereference a variable that doesn't exist in the symbol tree.
 *
 * Since Java already has a RuntimeErrorException, we prefixed our
 * exceptions with "DC". Although Java's RuntimeException is an
 * unchecked exception, ours is derived from Exception and is thus
 * a checked exception.
 */
public class DCRuntimeErrorException extends Exception {

    /**
     * Construct a runtime exception with the given message to
     * explain the reason for the exception.
     *
     * @param message A String that explains the reason for the
     *                exception to be thrown.
     */
    public DCRuntimeErrorException(String message) {
        super("A runtime error occurred: " + message);
    }

}
