import java.io.IOException;


/**
 * A ProgNode is the root of a Descartes program's parse tree.
 *
 * To parse a ProgNode is to parse a Descartes program; to execute
 * a ProgNode is to execute the program.
 *
 * <hr/>
 * <pre>
 *     0. prog : stmt-list PERIOD
 *        ...
 * </pre>
 */
public class ProgNode {

    //==================//
    // Member Variables //
    //==================//

    private StmtListNode m_stmtList;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs a new program node with the given statement
     * list.
     *
     * @param stmtList The statement list constituting this
     *                 program
     */
    public ProgNode(StmtListNode stmtList) {
        m_stmtList = stmtList;
    }

    /**
     * Executes the program's statement list.
     *
     * @param progState A newly initialized program state
     */
    public void execute(ProgState progState)
            throws DCRuntimeErrorException
    {
        // Execute child stmt-list.
        m_stmtList.execute(progState);
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Reads source code tokens from tokenReader and parses them
     * into, and returns, the program's parse tree.
     *
     * A Descartes program consists of a statement list.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed ProgNode that was parsed from the
     *         source code
     */
    public static ProgNode parseProg(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        //
        // GR 0.
        //
        //      prog : stmt-list PERIOD
        //

        // Read the stmt-list.
        StmtListNode stmtList = StmtListNode.parseStmtList(tokenReader);

        // Eat any spaces before the trailing period.
        TokenDescriptor token;
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        // "token" should now be the trailing period.
        if (token.getCode() != TokenCode.T_PERIOD) {
            throw new DCSyntaxErrorException(tokenReader,
                    "Expected '.' after program statement list.");
        }


        return new ProgNode(stmtList);
    }

}
