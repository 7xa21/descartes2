import java.io.IOException;
import java.util.HashMap;

public class TermNode {

	//==================//
	// Member Variables //
	//==================//

	private FactorNode m_factor;
	private FactorTailNode m_factorTail;


	//=========//
	// Methods //
	//=========//

	public TermNode(FactorNode factor, FactorTailNode factorTail) {
		m_factor = factor;
		m_factorTail = factorTail;
	}

	public double getVal(HashMap<String, Double> symTab) {
		double factorVal = m_factor.getVal(symTab);

		if (!m_factorTail.isEmpty()) {
			double factorTailVal;
		}

		return factorVal;
	}

	//================//
	// Static Methods //
	//================//

	public static TermNode parseTerm(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		//
		// GR 41:
		//
		//		term : factor factor-tail
		//

		// Read the factor and the factor-tail.
		FactorNode factor = FactorNode.parseFactor(tokenReader);
		FactorTailNode factorTail =
				FactorTailNode.parseFactorTail(tokenReader);


		return new TermNode(factor, factorTail);
	}
}
