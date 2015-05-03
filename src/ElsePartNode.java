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

    public void execute(HashMap<String, Double> symTab) {
        if(m_stmtList != null){
            m_stmtList.execute(symTab);
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

        // Eat up spaces.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        if(token.getCode()== TokenCode.T_ELSE) {
            stmtList = StmtListNode.parseStmtList(tokenReader);

            // Eat up spaces.
            do {
                token = tokenReader.getToken();
            } while (token.getCode() == TokenCode.T_SPACE);

            elsePart = new ElsePartNode(stmtList);
        }
        else{
            elsePart = new ElsePartNode();
        }
        // Eat up spaces.

        assert(token.getCode() == TokenCode.T_FI);
        return elsePart;
    }

}
