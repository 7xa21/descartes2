import java.io.IOException;
import java.util.Stack;


/**
 * An if statement is a flow-control modifier offering alternate
 * statement lists that are executed based on a boolean condition.
 *
 * <hr/>
 * <pre>
 *         ...
 *     4.  stmt : if-stmt
 *         ...
 *     11. if-stmt : IF expr THEN stmt-list else-part
 *         ...
 * </pre>
 */
public class IfStmtNode extends StmtNode {

    //==================//
    // Member Variables //
    //==================//

    private ExprNode m_expr;
    private StmtListNode m_stmtList;
    private ElsePartNode m_else;


    //=========//
    // Methods //
    //=========//

    /**
     * Constructs a new if statement given the provided
     * expression, statement list and else-part
     *
     * @param exp The expression whose evaluation will serve as
     *            the boolean condition for this if statement
     * @param stmtList The statement list to execute when the if
     *                 statement's condition is non-zero
     * @param elsePart The else-part clause attached to this if
     *                 statement
     */
    public IfStmtNode(ExprNode exp,
                      StmtListNode stmtList,
                      ElsePartNode elsePart)
    {
        m_expr = exp;
        m_stmtList = stmtList;
        m_else = elsePart;
    }

    /**
     * Executes this if statement. The expression is evaluated;
     * if non-zero, the statement list is executed, otherwise the
     * else part is executed.
     *
     * @param progState The current program state
     */
    public void execute(ProgState progState)
            throws DCRuntimeErrorException
    {
        if (m_expr.getVal(progState) != 0.0) {
            m_stmtList.execute(progState);
        } else {
            m_else.execute(progState);
        }
    }


    //================//
    // Static Methods //
    //================//

    /**
     * Returns 'true' if an if statement appears next in the input
     * source code.
     *
     * This is used by StmtNode.parseStmt() which must detect any
     * of seven possible statements (including blank/empty
     * statements).
     *
     * The tokenReader is not advanced by this method; it only
     * detects whether an if statement appears next. If 'true' is
     * returned by this method, IfStmtNode.parseIfStmt() should be
     * called immediately after to parse the if statement.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return 'true' if an if statement appears next in the input
     *         source code
     */
    public static boolean detectIfStmt(TokenReader tokenReader)
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

        // Look for an "IF" keyword.
        if (token.getCode() == TokenCode.T_IF) {
            detected = true;
        }

        // unread() all the tokens we just read.
        while (!toReplace.empty()) {
            tokenReader.unread(toReplace.pop());
        }


        return detected;
    }

    /**
     * Reads source code tokens from tokenReader and parses them
     * into, and returns, an if statement node.
     *
     * An if statement begins with the "IF" keyword, is followed
     * by an expression, then proceeds with a THEN keyword, a
     * statement list and an else-part.
     *
     * All if statements have else parts; if no ELSE keyword
     * appears in the source code, the else part is simply empty.
     * The "FI" keyword is thus part of the else-part.
     *
     * @param tokenReader The TokenReader from which source code
     *                    tokens will be read
     *
     * @return The constructed IfStmtNode that was parsed from the
     *         source code
     */
    public static IfStmtNode parseIfStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException

    {
        TokenDescriptor token;


        // Eat leading spaces.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        //
        // GR 11.
        //
        //      if-stmt : IF expr Then stmt-list else-part
        //

        // Ensures "IF" terminal is the current token.
        assert(token.getCode() == TokenCode.T_IF);

        // Sets exp to the next expr node after "IF".
        ExprNode exp = ExprNode.parseExpr(tokenReader);

        // Eat up spaces after the expr and before "THEN".
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        // Ensure a "THEN" keyword follows the conditional
        // expression.
        assert(token.getCode() == TokenCode.T_THEN);

        // Sets the stmtList to the next stmtList after "THEN".
        StmtListNode stmtList = StmtListNode.parseStmtList(tokenReader);

        // Sets the elsePart to the next elsePart after the
        // stmt-list.
        ElsePartNode elsePart = ElsePartNode.parseElse(tokenReader);


        return new IfStmtNode(exp, stmtList, elsePart);
    }

}
