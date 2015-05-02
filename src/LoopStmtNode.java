import java.io.IOException;
import java.util.Stack;

public class LoopStmtNode extends StmtNode {

    //==================//
    // Member Variables //
    //==================//

    private String m_id;
    private StmtListNode m_stmtList;


    //=========//
    // Methods //
    //=========//

    public LoopStmtNode(String id, StmtListNode stmtList) {
        m_id = id;
        m_stmtList = stmtList;
    }

    public void execute(ProgState progState)
            throws DCRuntimeErrorException
    {
        // Push this loop's ID to the stack.
        progState.loopIDStack().push(m_id);

        // Repeatedly execute the loop's statement list until this
        // loop's ID is no longer at the top of the stack.
        do {
            m_stmtList.execute(progState);
        } while (!progState.loopIDStack().isEmpty() &&
                  progState.loopIDStack().peek().equals(m_id));
    }

    //================//
    // Static Methods //
    //================//

    public static boolean detectLoopStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        TokenDescriptor token;
        Stack<TokenDescriptor> toReplace = new Stack<TokenDescriptor>();
        boolean detected = false;

        // Eat up leading spaces.
        do {
            token = tokenReader.getToken();
            toReplace.push(token);
        } while (token.getCode() == TokenCode.T_SPACE);

        // Look for "LOOP" keyword.
        if (token.getCode() == TokenCode.T_LOOP) {
            detected = true;
        }

        // Unread all the tokens we just read so the parse method
        // can read them.
        while (!toReplace.isEmpty()) {
            tokenReader.unread(toReplace.pop());
        }


        return detected;
    }

    public static LoopStmtNode parseLoopStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        TokenDescriptor token;
        String id;
        StmtListNode stmtList;

        //
        // GR 14.
        //
        //		loop-stmt : LOOP ID COLON stmt-list REPEAT
        //

        // Eat up leading spaces.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        // Look for "LOOP" keyword.
        assert(token.getCode() == TokenCode.T_LOOP);

        // Eat up spaces between "LOOP" and the loop's ID.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        // "token" should now be the loop ID string.
        if (token.getCode() != TokenCode.T_ID) {
            throw new DCSyntaxErrorException(tokenReader,
                    "Expected loop identifier.");
        }
        id = token.getText();

        // Eat up spaces between the ID string and the colon.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        // Look for the colon after the ID.
        if (token.getCode() != TokenCode.T_COLON) {
            throw new DCSyntaxErrorException(tokenReader,
                    "Expected ':' after loop identifier.");
        }

        // Read the statement list.
        stmtList = StmtListNode.parseStmtList(tokenReader);

        // Eat up spaces between the statement list and the REPEAT
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        // Look for REPEAT keyword.
        if (token.getCode() != TokenCode.T_REPEAT) {
            throw new DCSyntaxErrorException(tokenReader,
                    "Expected 'REPEAT' after loop statement list.");
        }


        return new LoopStmtNode(id, stmtList);
    }

}
