/**
 * Created by Luke on 4/30/2015.
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;
public class ElsePartNode {

    //==================//
    // Member Variable //
    //==================//

    private StmtListNode m_stmtList;

    //=========//
    // Methods //
    //=========//

    public ElsePartNode(){

    }

    public ElsePartNode(StmtListNode stmtList){
        m_stmtList = stmtList;
    }

    public void execute(ProgState progState)
            throws DCRuntimeErrorException
    {
        if(m_stmtList != null){
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
        StmtListNode stmtList;
        Stack<TokenDescriptor> toReplace = new Stack<TokenDescriptor>();

        // Eat up spaces.
        do {
            token = tokenReader.getToken();
            toReplace.push(token);
        } while (token.getCode() == TokenCode.T_SPACE);

        if (token.getCode() == TokenCode.T_ELSE) {
            stmtList = StmtListNode.parseStmtList(tokenReader);
            elsePart = new ElsePartNode(stmtList);
        }
        else {
            // If the token wasn't an ELSE keyword, put it back.
            while (!toReplace.isEmpty()) {
                tokenReader.unread(toReplace.pop());
            }
            elsePart = new ElsePartNode();
        }
        // Eat up spaces.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        assert(token.getCode() == TokenCode.T_FI);
        return elsePart;
    }

}
