import java.io.IOException;
import java.util.HashMap;

public class StmtListNode {
	//==================//
	// Member Variables //
	//==================//

	private StmtNode m_stmt;
	private StmtTailNode m_stmtTail;


	//=========//
	// Methods //
	//=========//

	public StmtListNode(StmtNode stmt, StmtTailNode stmtTail) {
		m_stmt = stmt;
		m_stmtTail = stmtTail;
	}

	public void execute(HashMap<String, Double> symTab) {
		m_stmt.execute(symTab);
		m_stmtTail.execute(symTab);
	}


	//================//
	// Static Methods //
	//================//

	public static StmtListNode parseStmtList(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		StmtNode stmt = StmtNode.parseStmt(tokenReader);
		StmtTailNode stmtTail = StmtTailNode.parseStmtTail(tokenReader);

		return new StmtListNode(stmt, stmtTail);
	}
}

// (eof)
