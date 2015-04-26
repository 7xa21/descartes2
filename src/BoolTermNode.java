import java.io.IOException;
import java.util.HashMap;

public class BoolTermNode {


	//==================//
	// Member Variables //
	//==================//

	private BoolFactorNode m_boolFactor;
	private BoolFactorTailNode m_boolFactorTail;


	//=========//
	// Methods //
	//=========//

	public BoolTermNode(BoolFactorNode boolFactor,
						BoolFactorTailNode boolFactorTail)
	{
		m_boolFactor = boolFactor;
		m_boolFactorTail = boolFactorTail;
	}

	public double getVal(HashMap<String, Double> symTab) {
		// Get the value of the child bool-factor.
		double factorVal = m_boolFactor.getVal(symTab);

		// If the value of the child factor-tail is non-zero, then
		// this is a conditional/boolean bool-term.
		if (!m_boolFactorTail.isEmpty()) {
			// Get the value of the child bool-factor-tail.
			double tailVal = m_boolFactorTail.getVal(symTab);

			// If either the bool-factor or bool-factor-tail
			// values are non-zero, return 1; otherwise return 0.
			if (factorVal != 0.0 || tailVal != 0.0) {
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

	public static BoolTermNode parseBoolTerm(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		//
		// GR 26:
		//
		//		bool-term : bool-factor bool-factor-tail
		//

		// Read the bool-factor and the bool-factor tail.
		BoolFactorNode factor = BoolFactorNode.parseBoolFactor(tokenReader);
		BoolFactorTailNode factorTail =
				BoolFactorTailNode.parseBoolFactorTail(tokenReader);


		return new BoolTermNode(factor, factorTail);
	}

}
