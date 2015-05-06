/**
 * Encapsulates a token that's read from a Descartes source code
 * file.
 *
 * An instance of this is returned by getToken().
 */
public class TokenDescriptor {

    //==================//
    // Member Variables //
    //==================//

    private String m_text;
    private TokenCode m_code;


    /**
     * Constructs a new TokenDescriptor with the specified token
     * text and code.
     *
     * @param text The literal text that constitutes the token
     * @param code One of the token values from the enum TokenCode
     */
    public TokenDescriptor(String text, TokenCode code) {
        m_text = text;
        m_code = code;
    }

    /**
     * Prints the token/code pair on the console.
     */
    public void print() {
        System.out.println(m_text + "\t(ID: " + m_code + ")");
    }

    /**
     * Returns the verbatim token text as read from the source
     * code.
     */
    public String getText() {
        return m_text;
    }

    /**
     * Returns the token code; this is one of the values from the
     * TokenCode enum.
     */
    public TokenCode getCode() {
        return m_code;
    }

}
