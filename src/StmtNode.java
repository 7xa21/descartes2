import java.io.IOException;


public class StmtNode {

    //=========//
    // Methods //
    //=========//

    /**
     * Constructs an empty, blank statement node (GR 10).
     */
    public StmtNode() {
    }

    /**
     * Executes this statement node.
     *
     * This method is overridden by the other types of statement
     * nodes (IfStmtNode, LoopStmtNode, BreakStmtNode,
     * AssignStmtNode, ReadStmtNode, PrintStmtNode).
     *
     * @param progState The current ProgramState instance.
     *
     * @throws DCRuntimeErrorException
     */
    public void execute(ProgState progState)
            throws DCRuntimeErrorException
    {
        //
        // This is only used for blank statements (GR 10). Thus it
        // doesn't do anything.
        //
        // Classes that extend this one override it to implement
        // their own execute() functionality.
        //
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Parses source code read from tokenReader into a stmt.
     *
     * This detects any of the various types of statements (if,
     * loop, break, assign, read, print) and returns an instance
     * of the proper node class that corresponds to it.
     *
     * @param tokenReader The TokenReader object from which source
     *                    code will be read.
     *
     * @return An instance of whichever StmtNode derivative is
     * 		   parsed from the source code.
     */
    public static StmtNode parseStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        StmtNode node;


        //
        // GR 4.
        //
        //      stmt : if-stmt
        //
        if (IfStmtNode.detectIfStmt(tokenReader)) {
            node = IfStmtNode.parseIfStmt(tokenReader);
        }

        //
        // GR 5.
        //
        //      stmt : loop-stmt
        //
        else if (LoopStmtNode.detectLoopStmt(tokenReader)) {
            node = LoopStmtNode.parseLoopStmt(tokenReader);
        }

        //
        // GR 6.
        //
        //      stmt : break-stmt
        //
        else if (BreakStmtNode.detectBreakStmt(tokenReader)) {
            node = BreakStmtNode.parseBreakStmt(tokenReader);
        }

        //
        // GR 7.
        //
        //      stmt : assign-stmt
        //
        else if (AssignStmtNode.detectAssignStmt(tokenReader)) {
            node = AssignStmtNode.parseAssignStmt(tokenReader);
        }

        //
        // GR 8.
        //
        //      stmt : read-stmt
        //
        else if (ReadStmtNode.detectReadStmt(tokenReader)) {
            node = ReadStmtNode.parseReadStmt(tokenReader);
        }

        //
        // GR 9.
        //
        //      stmt : print-stmt
        //
        else if (PrintStmtNode.detectPrintStmt(tokenReader)) {
            node = PrintStmtNode.parsePrintStmt(tokenReader);
        }

        //
        // GR 10.
        //
        //      stmt :
        //

        // If none of the above statement types were found, the
        // statement is empty.
        else {
            node = new StmtNode();
        }


        return node;
    }
}
