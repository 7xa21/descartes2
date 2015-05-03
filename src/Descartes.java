import java.io.*;
import java.util.TreeSet;


public class Descartes {

    //==================//
    // Member Variables //
    //==================//

    /** TokenReader that source code tokens will be read from. */
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
        try {
            // Parse the source code file: build the parse tree.
            ProgNode progNode = ProgNode.parseProg(m_tokenReader);

            // Execute the program: walk the parse tree.
            ProgState progState = new ProgState();
            progNode.execute(progState);

            // Dump the symbol table. Use a TreeSet so the keys
            // are sorted.
            TreeSet<String> keys = new TreeSet<String>();
            keys.addAll(progState.symTab().keySet());
            for (String key : keys) {
                System.out.println(key + " : " + progState.symTab().get(key));
            }
        } catch (DCSyntaxErrorException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        } catch (DCRuntimeErrorException e) {
            System.err.println(e.getMessage());
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
     *        Descartes source code file.
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
