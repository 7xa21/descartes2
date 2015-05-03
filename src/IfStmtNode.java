/**
 * Created by Luke on 4/30/2015.
 */
import java.io.IOException;
import java.util.HashMap;
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

    public IfStmtNode(ExprNode exp, StmtListNode stmtList, ElsePartNode els){
        m_expr = exp;
        m_stmtList = stmtList;
        m_else = els;
    }

    public void execute(HashMap<String, Double> symTab) {
        if(m_expr.getVal(symTab)!=0){
            m_stmtList.execute(symTab);
        }
        else{
            m_else.execute(symTab);
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

        // Look for an ID token.
        if (token.getCode() == TokenCode.T_IF) {
            detected = true;
        }

        // unread() all the tokens we just read.
        while (!toReplace.empty()) {
            tokenReader.unread(toReplace.pop());
        }

        return detected;
    }

    public static IfStmtNode parseIf(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException

    {
        //
        // GR 11:
        //
        //		if-stmt : IF expr Then stmt-list else-part
        //
        ExprNode exp;
        StmtListNode stmtList;
        ElsePartNode elsePart;
        TokenDescriptor token;

        // Eat up spaces.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        //Ensures 'IF' terminal is the current tokens code
        assert(token.getCode() == TokenCode.T_IF);
        //sets exp to the next expr node after IF
        exp = ExprNode.parseExpr(tokenReader);

        // Eat up spaces after the expr.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        assert(token.getCode() == TokenCode.T_THEN);
        // sets the stmtList to the next stmtList after THEN
        stmtList = StmtListNode.parseStmtList(tokenReader);
        // sets the elsePart to the next elsePart after te stmtList
        elsePart = ElsePartNode.parseElse(tokenReader);


        return new IfStmtNode(exp, stmtList, elsePart);
    }

}
