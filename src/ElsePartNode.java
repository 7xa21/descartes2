import java.io.IOException;


public class ElsePartNode {

    //==================//
    // Member Variable //
    //==================//

    private StmtListNode m_stmtList;


    //=========//
    // Methods //
    //=========//

    public ElsePartNode(StmtListNode stmtList) {
        m_stmtList = stmtList;
    }

    public void execute(ProgState progState)
            throws DCRuntimeErrorException
    {
        if (m_stmtList != null) {
            m_stmtList.execute(progState);
        }
    }


    //================//
    // Static Methods //
    //================//

    public static ElsePartNode parseElse(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        ElsePartNode elsePart;
        TokenDescriptor token;

        // Eat up spaces.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        //
        // GR 12.
        //
        //      else-part : ELSE stmt-list FI
        //

        if (token.getCode() == TokenCode.T_ELSE) {
            // Read the statement list for this ELSE clause.
            StmtListNode stmtList = StmtListNode.parseStmtList(tokenReader);

            // Create the populated ElsePartNode.
            elsePart = new ElsePartNode(stmtList);
        }

        //
        // GR 13.
        //
        //      else-part : FI
        //

        else {
            // If the token wasn't an ELSE keyword, put it back.
            tokenReader.unread(token);

            // Create an empty ElsePartNode.
            elsePart = new ElsePartNode(null);
        }

        // Eat any spaces before the "FI" keyword.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        // Ensure a "FI" keyword ends this else-part.
        assert(token.getCode() == TokenCode.T_FI);


        return elsePart;
    }

}
