import java.io.*;
import java.util.HashMap;

public class Descartes {

	// ==================//
	// Member Variables //
	// ==================//

	/** Stream that source code will be read from. */
	private InputStream m_sourceStream;

	// =========//
	// Methods //
	// =========//

	/**
	 * Construct a new Descartes interpreter with sourceStream providing the program source code.
	 *
	 * @param sourceStream
	 *            An initialized InputStream from which to read the program source code
	 */
	public Descartes(InputStream sourceStream) {
		this.m_sourceStream = sourceStream;
	}

	/**
	 * Reads, parses and executes the program.
	 */
	private void run() throws IOException {
		try {
			// Initialize the token reader.
			TokenReader tokenReader = new TokenReader(m_sourceStream);

			// Build parse tree.
			ProgNode progNode = ProgNode.parseProg(tokenReader);

			// Execute parse tree.
			HashMap<String, Double> symTab = new HashMap<String, Double>();
			progNode.execute(symTab);
		} catch (DCSyntaxErrorException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
		// catch (DCRuntimeErrorException e) {
		// System.err.println(e.getMessage());
		// System.exit(-1);
		// }
	}

	// ================//
	// Static Methods //
	// ================//

	/**
	 * Program entry. Sets up to read the program source code, parse it and run it.
	 *
	 * @param args
	 *            The first (and only, optional) argument is the name of a Descartes source code file.
	 */
	public static void main(String[] args) {
		// Get the source file name, or
		// read from stdin if none provided.
		InputStream inStream = System.in;
		try {
			if (args.length > 0) {
				inStream = new FileInputStream(args[0]);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Source file not found: \"" + args[0] + "\"");
			System.exit(-1);
		}

		// Construct the interpreter and run the program.
		try {
			Descartes interpreter = new Descartes(inStream);
			interpreter.run();
		} catch (Exception e) {
			System.err.println("An unexpected exception occurred:");
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
