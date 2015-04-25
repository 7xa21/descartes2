import java.io.IOException;
import java.util.HashMap;

public class BoolFactorTailNode {

	//==================//
	// Member Variables //
	//==================//

	private BoolFactorNode m_boolFactor;
	private BoolFactorTailNode m_boolFactorTail;


	//=========//
	// Methods //
	//=========//

	public BoolFactorTailNode(BoolFactorNode boolFactor,
							  BoolFactorTailNode boolFactorTail)
	{
		m_boolFactor = boolFactor;
		m_boolFactorTail = boolFactorTail;
	}

	public BoolFactorTailNode() {
		m_boolFactor = null;
		m_boolFactorTail = null;
	}

	public boolean isEmpty() {
		return m_boolFactor == null;
	}

	public double getVal(HashMap<String, Double> symTab) {
		double factorVal = m_boolFactor.getVal(symTab);

		if (!m_boolFactorTail.isEmpty()) {
			double tailVal = m_boolFactorTail.getVal(symTab);
			if (factorVal != 0.0 && tailVal != 0.0) {
				factorVal = 1.0;
			} else {
				factorVal = 0.0;
			}
		}

		return factorVal;
	}


	//================//
	// Static Methods //
	//================//

	public static BoolFactorTailNode parseBoolFactorTail(
			TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		TokenDescriptor token;

		// Eat any leading spaces.
		do {
			token = tokenReader.getToken();
		} while (token.getCode() == TokenCode.T_SPACE);

		//
		// GR 27:
		//
		//		bool-factor-tail : AND bool-factor bool-factor-tail
		//

		// Look for "AND" keyword.
		BoolFactorTailNode factorTail;
		if (token.getCode() == TokenCode.T_AND) {
			// Now look for the bool-factor and bool-factor-tail.
			BoolFactorNode factor =
					BoolFactorNode.parseBoolFactor(tokenReader);
			BoolFactorTailNode nextFactorTail =
					BoolFactorTailNode.parseBoolFactorTail(tokenReader);

			factorTail = new BoolFactorTailNode(factor, nextFactorTail);
		}

		//
		// GR 28:
		//
		//		bool-factor-tail :
		//

		// If the token was not "AND" then this bool-factor-tail
		// is empty (and we need to unread() the token back to the
		// reader).
		else {
			tokenReader.unread(token);
			factorTail = new BoolFactorTailNode();
		}


		return factorTail;
	}
}
