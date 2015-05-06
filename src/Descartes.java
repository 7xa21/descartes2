import java.io.*;
import java.util.TreeSet;


/**
 * The Descartes object parses and executes programs written in
 * the Descartes 2 programming language.
 */
public class Descartes {

    //==================//
    // Member Variables //
    //==================//

    // Contains the notorious "getToken()" method.
    private TokenReader m_tokenReader;


    // =========//
    // Methods //
    // =========//

    /**
     * Construct a new Descartes interpreter that reads source
     * code tokens from the specified TokenReader.
     *
     * @param tokenReader The TokenReader instance that source
     *        code tokens will be read from
     */
    public Descartes(TokenReader tokenReader) {
        m_tokenReader = tokenReader;
    }

    /**
     * Reads, parses and executes the program.
     */
    private void run() throws IOException {
        ProgState progState = new ProgState();

        try {
            // Parse the source code file: build the parse tree.
            ProgNode progNode = ProgNode.parseProg(m_tokenReader);

            // Execute the program: walk the parse tree.
            progNode.execute(progState);
            System.out.println("===================");
            System.out.println("Execution complete.");

            // Dump the symbol table.
            progState.dumpSymTab();
        } catch (DCSyntaxErrorException e) {
            System.out.println(e.getMessage());
        } catch (DCRuntimeErrorException e) {
            System.out.println(e.getMessage());
            System.out.println("================================");
            System.out.println("Execution completed with errors.");
            progState.dumpSymTab();
            System.exit(-1);
        }
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Program entry.
     *
     * Sets up to read the program source code, parse it and run
     * it.
     *
     * @param args The first (and only) argument is the name of a
     *             Descartes source code file.
     */
    public static void main(String[] args) {
        try {
            // Get the source file name and create an InputStream
            // to read from.
            if (args.length != 1) {
                System.err.println("Usage:\n\tjava Descartes source_file");
                System.exit(-1);
                return;
            }
            String sourceFileName = args[0];
            InputStream inStream = new FileInputStream(sourceFileName);

            // Create a TokenReader that will read source code
            // tokens from the InputStream.
            TokenReader tokenReader = new TokenReader(
                    sourceFileName, inStream);

            // Construct the interpreter and run the program.
            Descartes interpreter = new Descartes(tokenReader);
            interpreter.run();
        } catch (FileNotFoundException e) {
            System.err.println("Source file not found: \"" + args[0] + "\"");
            System.exit(-1);
        } catch (Exception e) {
            System.err.println("An unexpected exception occurred:");
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
