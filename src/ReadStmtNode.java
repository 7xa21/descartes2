import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;


public class ReadStmtNode extends StmtNode {
	
	//==================//
	// Member Variables //
	//==================//
	
	private String m_id;
	private IDListTailNode m_idListTail;
	
	
	//=========//
	// Methods //
	//=========//
	
	public ReadStmtNode (String id, IDListTailNode idListTail) {
		m_id = id;
		m_idListTail = idListTail;
	}
	
	public void execute(ProgState progState) {
		Scanner input = new Scanner(System.in);
		double num = input.nextDouble();
		
		// Assign the user's value to the ID.
		progState.symTab().put(m_id, num);

		m_idListTail.read(progState);
	}


	//================//
	// Static Methods //
	//================//

	public static boolean detectReadStmt(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		// Keep a stack of tokens read. If we DON'T find
		// READ, we unread() all the tokens back
		// to the TokenReader.
		Stack<TokenDescriptor> toReplace = new Stack<TokenDescriptor>();
		boolean detected = false;
		TokenDescriptor token;

		// Eat up spaces.
		do {
			token = tokenReader.getToken();
			toReplace.push(token);
		} while (token.getCode() == TokenCode.T_SPACE);

		// Look for a READ token.
		if (token.getCode() == TokenCode.T_READ) {
			detected = true;
		}

		// unread() all the tokens we just read.
		while (!toReplace.empty()) {
			tokenReader.unread(toReplace.pop());
		}


		return detected;
	}


	public static ReadStmtNode parseReadStmt(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		//
		// GR 20:
		//
		//		read-stmt : READ ID id-list-tail
		//

		TokenDescriptor token;
		String id;

		// Eat up spaces.
		do {
			token = tokenReader.getToken();
		} while (token.getCode() == TokenCode.T_SPACE);

		assert(token.getCode() == TokenCode.T_READ);

		// Eat up spaces after READ.
		do {
			token = tokenReader.getToken();
		} while (token.getCode() == TokenCode.T_SPACE);

		// "token" should now be the first non-space token after
		// the ID, which should be ID.
		if (token.getCode() != TokenCode.T_ID) {
			throw new DCSyntaxErrorException(tokenReader,
					"Expected identifier after 'READ'.");
		}
		id = token.getText();

		// Read the id-list-tail.
		IDListTailNode idListTail =
				IDListTailNode.parseIDListTail(tokenReader);


		return new ReadStmtNode(id, idListTail);
	}
}
