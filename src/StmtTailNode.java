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
		TokenDescriptor token;
		do {
			token = tokenReader.getToken();
		} while (token.getCode() == TokenCode.T_SPACE);

		StmtTailNode stmtTail = null;
		if (token.getCode() == TokenCode.T_SEMICOLON) {
			StmtNode stmt = StmtNode.parseStmt(tokenReader);
			StmtTailNode nextStmtTail =
					StmtTailNode.parseStmtTail(tokenReader);

			stmtTail = new StmtTailNode(stmt, nextStmtTail);
		} else {
			stmtTail = new StmtTailNode();
		}

		return stmtTail;
	}
}
