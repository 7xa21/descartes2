public class DCSyntaxErrorException extends Exception {

	public DCSyntaxErrorException(String message) {
		super("Syntax error: " + message);
	}

	public DCSyntaxErrorException(String fileName, int lineNum,
								  String message)
	{
		super(fileName + ": line " + lineNum + ": " + message);
	}

	public DCSyntaxErrorException(TokenReader tokenReader, String message) {
		super(tokenReader.fileName() + ": line " + tokenReader.lineNum() +
				": " + message);
	}
}
