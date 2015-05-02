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

	public void execute(ProgState progState)
			throws DCRuntimeErrorException
	{
		// Execute child stmt.
		m_stmt.execute(progState);

		// Execute child stmt-tail.
		m_stmtTail.execute(progState);
	}


	//================//
	// Static Methods //
	//================//

	public static StmtListNode parseStmtList(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		//
		// GR 1:
		//
		// 		stmt-list : stmt stmt-tail
		//

		// Read "stmt" and "stmt-tail".
		StmtNode stmt = StmtNode.parseStmt(tokenReader);
		StmtTailNode stmtTail = StmtTailNode.parseStmtTail(tokenReader);


		return new StmtListNode(stmt, stmtTail);
	}
}

// (eof)
