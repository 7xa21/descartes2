public class DCRuntimeErrorException extends Exception {
	public DCRuntimeErrorException(String message) {
		super("A runtime error occurred: " + message);
	}
}
