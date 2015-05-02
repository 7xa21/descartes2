import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;


public class idListTailNode {
	
	//================//
	//Member Variables//
	//================//
	
	
	private String m_id;
	private idListTailNode m_idListTail;
	
	
	//=======//
	//Methods//
	//=======//
	
	public idListTailNode (String id, idListTailNode idListTail){
		m_id = id;
		m_idListTail = idListTail;
	}
	
	public idListTailNode (){
		m_id = null;
		m_idListTail = null;
	}
	
	
	public void execute(HashMap<String, Double> symTab) {
		
		Scanner input = new Scanner(System.in);
		double num = input.nextDouble();
		
		// Assign the user's value to the next ID.
			symTab.put(m_id, num);
	}
	
	public void read(ProgState progState){
		
		if (m_id != null){
			Scanner input = new Scanner(System.in);
			double num = input.nextDouble();
			
			// Assign the user's value to the ID.
				progState.symTab().put(m_id, num);
				
				m_idListTail.read(progState);
		}
	}
	
	public void print(ProgState progState){
		if(m_id != null){
			System.out.println(progState.symTab().get(m_id));
			
			m_idListTail.print(progState);
		}
		
	}
	
	
	public static boolean detectIdListTail(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		// Keep a stack of tokens read. If we DON'T find a
		// comma, we unread() all the tokens back
		// to the TokenReader.
		Stack<TokenDescriptor> toReplace = new Stack<TokenDescriptor>();
		boolean detected = false;
		TokenDescriptor token;

		// Eat up spaces.
		do {
			token = tokenReader.getToken();
			toReplace.push(token);
		} while (token.getCode() == TokenCode.T_SPACE);

		// Look for a COMMA token.
		if (token.getCode() == TokenCode.T_COMMA) {
			// Eat up spaces after COMMA.
			do {
				token = tokenReader.getToken();
				toReplace.push(token);
			} while (token.getCode() == TokenCode.T_SPACE);

			// "token" is now the first token after the space(s).

			// Look for the ID token.
			if (token.getCode() == TokenCode.T_ID) {
				detected = true;
			}
		}

		// unread() all the tokens we just read.
		while (!toReplace.empty()) {
			tokenReader.unread(toReplace.pop());
		}

		return detected;
	}
	
	
	public static idListTailNode parseidListTail(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		TokenDescriptor token;
		String id;
		idListTailNode idListTail;
		
		// Eat up spaces.
		do {
			token = tokenReader.getToken();
		} while (token.getCode() == TokenCode.T_SPACE);

		//
		// GR 21:
		//
		//		id-list-tail : , ID id-list-tail
		//

		if (token.getCode() == TokenCode.T_COMMA) {
			// Eat up spaces after READ.
			do {
				token = tokenReader.getToken();
			} while (token.getCode() == TokenCode.T_SPACE);
	
			// "token" should now be the first non-space token after
			// the COMMA, which should be ID.
			if (token.getCode() != TokenCode.T_ID) {
				throw new DCSyntaxErrorException(tokenReader,
						"Expected identifier after ','.");
			}
			id = token.getText();
	
			// Read the id-list-tail.
			idListTailNode NextIdListTail;
			NextIdListTail = idListTailNode.parseidListTail(tokenReader);
			
			// New idListTail.
			idListTail = new idListTailNode(id, NextIdListTail);
		}

		//
		// GR 22:
		//
		//		id-list-tail :
		//

		else {
			// The idListTailNode is empty.
			tokenReader.unread(token);
			idListTail = new idListTailNode();
		}
			
		return idListTail;
	}

}
