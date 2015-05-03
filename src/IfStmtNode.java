import java.io.IOException;
import java.util.Stack;


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

    public IfStmtNode(ExprNode exp,
                      StmtListNode stmtList,
                      ElsePartNode els)
    {
        m_expr = exp;
        m_stmtList = stmtList;
        m_else = els;
    }

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

    public static IfStmtNode parseIfStmt(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException

    {
        TokenDescriptor token;


        // Eat leading spaces.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        //
        // GR 11:
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
