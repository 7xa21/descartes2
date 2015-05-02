import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

public class AssignStmtNode extends StmtNode {

    //==================//
    // Member Variables //
    //==================//

    private String m_id;
    private ExprNode m_expr;


    //=========//
    // Methods //
    //=========//

    public AssignStmtNode(String id, ExprNode expr) {
        m_id = id;
        m_expr = expr;
    }

    public void execute(ProgState progState)
            throws DCRuntimeErrorException
    {
        // Assign the expression's value to the ID.
        progState.symTab().put(m_id, m_expr.getVal(progState));
    }


    //================//
    // Static Methods //
    //================//

    // (RS) ...debating whether this method is necessary...
    public static boolean detectAssignStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        // Keep a stack of tokens read. If we DON'T find an
        // assignment statement, we unread() all the tokens back
        // to the TokenReader.
        Stack<TokenDescriptor> toReplace = new Stack<TokenDescriptor>();
        boolean detected = false;
        TokenDescriptor token;

        // Eat up spaces.
        do {
            token = tokenReader.getToken();
            toReplace.push(token);
        } while (token.getCode() == TokenCode.T_SPACE);

        // Look for an ID token.
        if (token.getCode() == TokenCode.T_ID) {
            // Eat up spaces after ID.
            do {
                token = tokenReader.getToken();
                toReplace.push(token);
            } while (token.getCode() == TokenCode.T_SPACE);

            // "token" is now the first token after the space(s).

            // Look for the ":=" token.
            if (token.getCode() == TokenCode.T_BECOMES) {
                detected = true;
            }
        }

        // unread() all the tokens we just read.
        while (!toReplace.empty()) {
            tokenReader.unread(toReplace.pop());
        }

        return detected;
    }

    public static AssignStmtNode parseAssignStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        //
        // GR 18:
        //
        //		assign-stmt : ID BECOMES expr
        //

        // Get the ID.
        TokenDescriptor token;

        // Eat up spaces.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        // Get the variable name in the assignment.
        assert(token.getCode() == TokenCode.T_ID);
        String id = token.getText();

        // Eat up spaces between the variable name and the ":=".
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        // "token" should now be the first non-space token after
        // the ID, which should be BECOMES (e.g. ":=").
        assert(token.getCode() == TokenCode.T_BECOMES);

        // Read the expression.
        ExprNode expr = ExprNode.parseExpr(tokenReader);


        return new AssignStmtNode(id, expr);
    }

}
