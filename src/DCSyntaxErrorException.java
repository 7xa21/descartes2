/**
 * Syntax error exceptions occur while parsing a program's source
 * code.
 */
public class DCSyntaxErrorException extends Exception {

    /**
     * Construct a syntax error exception using file name and line
     * number information collected from the given TokenReader,
     * and with the given message explaining the reason for the
     * exception.
     *
     * @param tokenReader The TokenReader instance that was being
     *                    used when the exception occurred
     * @param message A String that explains the reason for the
     *                exception to be thrown
     */
    public DCSyntaxErrorException(TokenReader tokenReader, String message) {
        super(tokenReader.fileName() + ": line " + tokenReader.lineNum() +
                ": " + message);
    }

}
