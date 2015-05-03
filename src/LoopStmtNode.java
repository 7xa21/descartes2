import java.io.IOException;
import java.util.Stack;

public class LoopStmtNode extends StmtNode {

	//==================//
	// Member Variables //
	//==================//

	private String m_id;
	private StmtListNode m_stmtList;

	//=========//
	// Methods //
	//=========//

	public LoopStmtNode(String id, StmtListNode stmtList) {
		m_id = id;
		m_stmtList = stmtList;
	}

	//================//
	// Static Methods //
	//================//

	public static boolean detectLoopStmt(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		TokenDescriptor token;
		Stack<TokenDescriptor> toReplace = new Stack<TokenDescriptor>();
		boolean detected = false;

		// Eat up leading spaces.
		do {
			token = tokenReader.getToken();
			toReplace.push(token);
		} while (token.getCode() == TokenCode.T_SPACE);

		// Look for "LOOP" keyword.
		if (token.getCode() == TokenCode.T_LOOP) {
			detected = true;
		}

		// Unread all the tokens we just read so the parse method
		// can read them.
		while (!toReplace.isEmpty()) {
			tokenReader.unread(toReplace.pop());
		}


		return detected;
	}

	public static LoopStmtNode parseLoopStmt(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		TokenDescriptor token;
		String id;
		StmtListNode stmtList;

		//
		// GR 14.
		//
		//		loop-stmt : LOOP ID COLON stmt-list REPEAT
		//

		// Eat up leading spaces.
		do {
			token = tokenReader.getToken();
		} while (token.getCode() == TokenCode.T_SPACE);

		// Look for "LOOP" keyword.
		assert(token.getCode() == TokenCode.T_LOOP);

		// Eat up spaces between "LOOP" and the loop's ID.
		do {
			token = tokenReader.getToken();
		} while (token.getCode() == TokenCode.T_LOOP);

		// "token" should now be the loop ID string.
		assert(token.getCode() == TokenCode.T_ID);
		id = token.getText();

		// Eat up spaces between the ID string and the colon.
		do {
			token = tokenReader.getToken();
		} while (token.getCode() == TokenCode.T_SPACE);

		// Look for the colon after the ID.
		assert(token.getCode() == TokenCode.T_COLON);

		// Read the statement list.
		stmtList = StmtListNode.parseStmtList(tokenReader);

		// Eat up spaces between the statement list and the REPEAT
		do {
			token = tokenReader.getToken();
		} while (token.getCode() == TokenCode.T_SPACE);

		// Look for REPEAT keyword.
		assert(token.getCode() == TokenCode.T_REPEAT);


		return new LoopStmtNode(id, stmtList);
	}

}
