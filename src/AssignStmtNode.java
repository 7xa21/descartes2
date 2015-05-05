import java.io.IOException;
import java.util.Stack;


/**
 * An assignment statement updates a variable with the value of an
 * expression, creating the variable if necessary.
 *
 * <hr/>
 * <pre>
 *         ...
 *     7.  stmt : assign-stmt
 *         ...
 *     18. assign-stmt : ID BECOMES expr
 *         ...
 * </pre>
 */
public class AssignStmtNode extends StmtNode {

    //==================//
    // Member Variables //
    //==================//

    private String m_id;
    private ExprNode m_expr;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs an assignment expression node.
     *
     * Assignment expressions consist of a variable's ID and an
     * expression to evaluate and assign the value of to the
     * variable.
     *
     * @param id The ID of the variable being assigned
     * @param expr The expression to evaluate and assign the value
     *             of
     */
    public AssignStmtNode(String id, ExprNode expr) {
        m_id = id;
        m_expr = expr;
    }

    /**
     * Evaluates the expression and assigns the result to the
     * variable with the ID provided in the constructor.
     *
     * @param progState The current program state (symbol table,
     *                  etc.)
     */
    public void execute(ProgState progState)
            throws DCRuntimeErrorException
    {
        // Assign the expression's value to the ID.
        progState.symTab().put(m_id, m_expr.getVal(progState));
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Returns 'true' if an assignment statement appears next in
     * the input source code.
     *
     * This is used by StmtNode.parseStmt() which must detect any
     * of seven possible statements (including blank/empty
     * statements).
     *
     * The tokenReader is not advanced by this method; it only
     * detects whether an assignment statement appears next. If
     * 'true' is returned by this method,
     * AssignStmtNode.parseAssignStmt() should be called
     * immediately after to parse the assignment statement.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return 'true' if an assignment statement appears next in
     *         the input source code
     */
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

    /**
     * Reads source code tokens from tokenReader and parses them
     * into, and returns, an assignment statement.
     *
     * Assignment statements consist of a variable ID, the BECOMES
     * operator (e.g. ":=") and an expression. When this method is
     * called an assignment statement is imminently expected.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed AssignStmtNode that was parsed from
     *         the source code
     */
    public static AssignStmtNode parseAssignStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        //
        // GR 18.
        //
        //      assign-stmt : ID BECOMES expr
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
