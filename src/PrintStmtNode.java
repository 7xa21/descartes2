import java.io.IOException;
import java.util.Stack;


public class PrintStmtNode extends StmtNode{


    //================//
    //Member Variables//
    //================//

    private String m_id;
    private IDListTailNode m_idListTail;


    //========//
    //Methods//
    //=======//

    public PrintStmtNode (String id, IDListTailNode idListTail){
        m_id = id;
        m_idListTail = idListTail;
    }

    public void execute(ProgState progState){
        System.out.println(progState.symTab().get(m_id));
        m_idListTail.print(progState);
    }


    //================//
    // Static Methods //
    //================//

    public static boolean detectPrintStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        // Keep a stack of tokens read. If we DON'T find a
        // PRINT, we unread() all the tokens back to the
        // TokenReader.
        Stack<TokenDescriptor> toReplace = new Stack<TokenDescriptor>();
        boolean detected = false;
        TokenDescriptor token;

        // Eat up spaces.
        do {
            token = tokenReader.getToken();
            toReplace.push(token);
        } while (token.getCode() == TokenCode.T_SPACE);

        // Look for a PRINT token.
        if (token.getCode() == TokenCode.T_PRINT) {
            detected = true;
        }

        // unread() all the tokens we just read.
        while (!toReplace.empty()) {
            tokenReader.unread(toReplace.pop());
        }

        return detected;
    }

    public static PrintStmtNode parsePrintStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        //
        // GR 19:
        //
        //		print-stmt : PRINT ID id-list-tail
        //
        //

        TokenDescriptor token;
        String id;

        // Eat up spaces.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        assert(token.getCode() == TokenCode.T_PRINT);

        // Eat up spaces after PRINT.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        // "token" should now be the first non-space token after
        // PRINT, which should be ID.
        if (token.getCode() != TokenCode.T_ID) {
            throw new DCSyntaxErrorException(tokenReader,
                    "Expected identifier after 'PRINT'.");
        }
        id = token.getText();

        // Read the id-list-tail.
        IDListTailNode idListTail =
                IDListTailNode.parseIDListTail(tokenReader);


        return new PrintStmtNode(id, idListTail);
    }

}
