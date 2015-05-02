import java.io.IOException;


public class ProgNode {
    //==================//
    // Member Variables //
    //==================//

    private StmtListNode m_stmtList;


    //=========//
    // Methods //
    //=========//

    public ProgNode(StmtListNode stmtList) {
        m_stmtList = stmtList;
    }

    public void execute(ProgState progState)
            throws DCRuntimeErrorException
    {
        // Execute child stmt-list.
        m_stmtList.execute(progState);
    }


    //================//
    // Static Methods //
    //================//

    public static ProgNode parseProg(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        //
        // GR 0:
        //
        //		prog : stmt-list PERIOD
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

// (eof)
