public class DCSyntaxErrorException extends Exception {
	public DCSyntaxErrorException(String message) {
		super("Syntax error: " + message);
	}
}
