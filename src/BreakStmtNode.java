import java.io.IOException;
import java.util.Stack;


public class BreakStmtNode extends StmtNode {

    //==================//
    // Member Variables //
    //==================//

    private IDOptionNode m_idOption;


    //=========//
    // Methods //
    //=========//

    public BreakStmtNode(IDOptionNode idOption) {
        m_idOption = idOption;
    }

    public void execute(ProgState progState) {
        m_idOption.popLoopID(progState);
    }


    //================//
    // Static Methods //
    //================//

    public static boolean detectBreakStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        Stack<TokenDescriptor> toReplace = new Stack<TokenDescriptor>();
        TokenDescriptor token;
        boolean detected = false;

        // Eat leading spaces.
        do {
            token = tokenReader.getToken();
            toReplace.push(token);
        } while (token.getCode() == TokenCode.T_SPACE);

        // Check for BREAK keyword.
        if (token.getCode() == TokenCode.T_BREAK) {
            detected = true;
        }

        // Put back all the tokens we just read.
        while (!toReplace.isEmpty()) {
            tokenReader.unread(toReplace.pop());
        }


        return detected;
    }

    public static BreakStmtNode parseBreakStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        TokenDescriptor token;

        // Eat leading spaces.
        do {
            token = tokenReader.getToken();
        } while(token.getCode() == TokenCode.T_SPACE);

        //
        // GR 15.
        //
        //      break-stmt : BREAK id-option
        //

        // Ensure token is BREAK keyword.
        assert(token.getCode() == TokenCode.T_BREAK);

        // Get the subsequent id-option.
        IDOptionNode idOption = IDOptionNode.parseIDOption(tokenReader);

        return new BreakStmtNode(idOption);
    }
}
