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

	public void execute(HashMap<String, Double> symTab) {
		symTab.put(m_id, m_expr.getVal(symTab));
	}


	//================//
	// Static Methods //
	//================//

	public static boolean detectAssignStmt(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		Stack<TokenDescriptor> toReplace = new Stack<TokenDescriptor>();
		boolean detected = false;

		TokenDescriptor token = tokenReader.getToken();
		if (token.getCode() == TokenCode.T_ID) {
			toReplace.push(token);

			token = tokenReader.getToken();
			if (token.getCode() == TokenCode.T_SPACE) {
				toReplace.push(token);
				token = tokenReader.getToken();
			}

			if (token.getCode() == TokenCode.T_BECOMES) {
				detected = true;
			}
		}

		while (!toReplace.empty()) {
			tokenReader.unread(toReplace.pop());
		}

		return detected;
	}

	public static AssignStmtNode parseAssignStmt(TokenReader tokenReader)
		throws IOException, DCSyntaxErrorException
	{
		TokenDescriptor token = tokenReader.getToken();
		assert(token.getCode() == TokenCode.T_ID);

		String id = token.getText();

		token = tokenReader.getToken();
		if (token.getCode() == TokenCode.T_SPACE) {
			token = tokenReader.getToken();
		}

		assert(token.getCode() == TokenCode.T_BECOMES);

		token = tokenReader.getToken();
		if (token.getCode() == TokenCode.T_SPACE) {
			token = tokenReader.getToken();
		}

		ExprNode expr = ExprNode.parseExpr(tokenReader);

		return new AssignStmtNode(id, expr);
	}

}
