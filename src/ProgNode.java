import java.io.IOException;
import java.util.HashMap;

public class ProgNode {
	//==================//
	// Member Variables //
	//==================//

	private StmtListNode m_stmtList;


	//=========//
	// Methods //
	//=========//

	public ProgNode(StmtListNode stmtList) {
		m_stmtList = stmtList;
	}

	public void execute(HashMap<String, Double> symTab) {
		m_stmtList.execute(symTab);
	}


	//================//
	// Static Methods //
	//================//

	public static ProgNode parseProg(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		StmtListNode stmtList = StmtListNode.parseStmtList(tokenReader);

		return new ProgNode(stmtList);
	}
}

// (eof)
