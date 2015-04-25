import java.io.IOException;
import java.util.HashMap;

public class BoolTermTailNode {

	//==================//
	// Member Variables //
	//==================//

	private BoolTermNode m_boolTerm;
	private BoolTermTailNode m_boolTermTail;


	//=========//
	// Methods //
	//=========//

	public BoolTermTailNode(BoolTermNode boolTerm,
							BoolTermTailNode boolTermTail)
	{
		m_boolTerm = boolTerm;
		m_boolTermTail = boolTermTail;
	}

	public BoolTermTailNode() {
		m_boolTerm = null;
		m_boolTermTail = null;
	}

	public boolean isEmpty() {
		return m_boolTerm == null;
	}

	public double getVal(HashMap<String, Double> symTab) {
		double termVal = m_boolTerm.getVal(symTab);

		if (!m_boolTermTail.isEmpty()) {
			double tailVal = m_boolTermTail.getVal(symTab);
			if (termVal != 0.0 || tailVal != 0.0) {
				termVal = 1.0;
			} else {
				termVal = 0.0;
			}
		}

		return termVal;
	}


	//================//
	// Static Methods //
	//================//

	public static BoolTermTailNode parseBoolTermTail(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		TokenDescriptor token;

		// Eat any leading spaces.
		do {
			token = tokenReader.getToken();
		} while (token.getCode() == TokenCode.T_SPACE);

		//
		// GR 24:
		//
		//		bool-term-tail : OR bool-term bool-term-tail
		//

		// Look for "OR" keyword.
		BoolTermTailNode termTail;
		if (token.getCode() == TokenCode.T_OR) {
			// Now look for another bool-term and bool-term-tail.
			BoolTermNode term = BoolTermNode.parseBoolTerm(tokenReader);
			BoolTermTailNode nextTermTail =
					BoolTermTailNode.parseBoolTermTail(tokenReader);

			termTail = new BoolTermTailNode(term, nextTermTail);
		}

		//
		// GR 25:
		//
		//		bool-term-tail :
		//

		// If the token was not "OR" then this bool-term-tail is
		// empty (and we need to unread() the token back to the
		// reader).
		else {
			tokenReader.unread(token);
			termTail = new BoolTermTailNode();
		}


		return termTail;
	}
}
