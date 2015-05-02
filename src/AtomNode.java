import java.io.IOException;
import java.util.HashMap;

public class AtomNode {

	//==================//
	// Member Variables //
	//==================//

	private String m_id;
	private double m_constVal;


	//=========//
	// Methods //
	//=========//

	public AtomNode(String id) {
		m_id = id;
	}

	public AtomNode(double constVal) {
		m_constVal = constVal;
	}

	public double getVal(ProgState progState) {
		if (m_id == null)
			return m_constVal;
		else
			return progState.symTab().get(m_id);
	}


	//================//
	// Static Methods //
	//================//

	public static AtomNode parseAtom(TokenReader tokenReader)
		throws IOException, DCSyntaxErrorException
	{
		TokenDescriptor token;

		// Eat up any leading spaces.
		do {
			token = tokenReader.getToken();
		} while(token.getCode() == TokenCode.T_SPACE);

		AtomNode atom;

		if (token.getCode() == TokenCode.T_ID) {
			atom = new AtomNode(token.getText());
		} else if (token.getCode() == TokenCode.T_CONST) {
			atom = new AtomNode(Double.parseDouble(token.getText()));
		} else {
			throw new DCSyntaxErrorException("Expected atom");
		}

		return atom;
	}

}