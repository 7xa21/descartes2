//
// Encapsulates a token as per the assignment requirements.
// Stores the token's name (or verbatim text in the case of
// string literals or identifiers) as well as it's code.
//
// An instance of this is returned by getToken().
//
public class TokenDescriptor {

	private String m_text;
	private TokenCode m_code;

	public TokenDescriptor(String text, TokenCode code) {
		m_text = text;
		m_code = code;
	}

	// Prints the token/code pair on the console.
	public void print() {
		System.out.println(m_text + "\t(ID: " + m_code + ")");
	}

	public String getText() {
		return m_text;
	}

	public TokenCode getCode() {
		return m_code;
	}
}
