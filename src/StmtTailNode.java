import java.io.IOException;
import java.util.HashMap;

public class StmtTailNode {

	//==================//
	// Member Variables //
	//==================//

	private StmtNode m_stmt;
	private StmtTailNode m_stmtTail;


	//=========//
	// Methods //
	//=========//

	public StmtTailNode() {
		m_stmt = null;
		m_stmtTail = null;
	}

	public StmtTailNode(StmtNode stmt, StmtTailNode stmtTail) {
		m_stmt = stmt;
		m_stmtTail = stmtTail;
	}

	public void execute(HashMap<String, Double> symTab)
			// throws DCRuntimeErrorException
	{
		if (m_stmt != null) {
			/*
			if (m_stmtTail == null) {
				throw new DCRuntimeErrorException("");
			}
			*/

			m_stmt.execute(symTab);
			m_stmtTail.execute(symTab);
		}
	}


	//================//
	// Static Methods //
	//================//

	public static StmtTailNode parseStmtTail(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		StmtTailNode stmtTail;
		TokenDescriptor token;

		// Eat leading spaces.
		do {
			token = tokenReader.getToken();
		} while (token.getCode() == TokenCode.T_SPACE);


		//
		// GR 2:
		//
		// 		stmt-list : SEMICOLON stmt stmt-tail
		//

		// Look for semicolon.
		if (token.getCode() == TokenCode.T_SEMICOLON) {
			// After semicolon, get "stmt" and "stmt-tail".
			StmtNode stmt = StmtNode.parseStmt(tokenReader);
			StmtTailNode nextStmtTail =
					StmtTailNode.parseStmtTail(tokenReader);

			stmtTail = new StmtTailNode(stmt, nextStmtTail);
		}

		//
		// GR 3:
		//
		// 		stmt-list :
		//

		// If no semicolon, the statement tail is blank.
		else {
			stmtTail = new StmtTailNode();
		}


		return stmtTail;
	}
}
