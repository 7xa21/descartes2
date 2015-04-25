import java.util.HashMap;

public class FactorNode {

	//==================//
	// Member Variables //
	//==================//

	private AtomNode m_atom;
	private FactorNode m_negFactor;
	private ExprNode m_parenExpr;


	//=========//
	// Methods //
	//=========//

	public FactorNode(AtomNode atom) {
		m_atom = atom;
		m_negFactor = null;
		m_parenExpr = null;
	}

	public FactorNode(FactorNode negFactor) {
		m_atom = null;
		m_negFactor = negFactor;
		m_parenExpr = null;
	}

	public FactorNode(ExprNode parenExpr) {
		m_atom = null;
		m_negFactor = null;
		m_parenExpr = parenExpr;
	}

	public double getVal(HashMap<String, Double> symTab) {
		double value;

		if (m_atom != null) {
			assert (m_negFactor == null);
			assert (m_parenExpr == null);
			value = m_atom.getVal(symTab);
		}
		else if (m_negFactor != null) {
			assert (m_parenExpr == null);
			value = m_negFactor.getVal(symTab);
		}
		else {
			assert (m_parenExpr != null);
			value = m_parenExpr.getVal(symTab);
		}

		return value;
	}


	//================//
	// Static Methods //
	//================//

	public static FactorNode parseFactor(TokenReader tokenReader) {

		return null;
		//
		// Look for:
		//
		// * '-' character and factor
		// * atom
		// * '(' ')' set with enclosed expression
		//
	}
}
