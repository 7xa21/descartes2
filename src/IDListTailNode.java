import java.io.IOException;
import java.util.Scanner;


public class IDListTailNode {

    //==================//
    // Member Variables //
    //==================//

    private String m_id;
    private IDListTailNode m_idListTail;


    //=========//
    // Methods //
    //=========//

    public IDListTailNode(String id, IDListTailNode idListTail) {
        m_id = id;
        m_idListTail = idListTail;
    }

    public void read(ProgState progState) {
        // If m_id is null, it means this IDListTailNode is empty
        // and thus its own, subsequent m_idListTail is empty.
        if (m_id != null) {
            // Read a value from the user.
            Scanner input = new Scanner(System.in);
            double num = input.nextDouble();

            // Assign the user's value to the ID.
            progState.symTab().put(m_id, num);

            // Read this id-list-tail's subsequent id-list-tail.
            m_idListTail.read(progState);
        }
    }

    public void print(ProgState progState) {
        // If m_id is null, it means this IDListTailNode is empty
        // and thus its own, subsequent m_idListTail is empty.
        if (m_id != null) {
            // Print the value on the console.
            System.out.println(progState.symTab().get(m_id));

            // Print this id-list-tail's subsequent id-list-tail.
            m_idListTail.print(progState);
        }
    }


    //================//
    // Static Methods //
    //================//

    public static IDListTailNode parseIDListTail(TokenReader tokenReader)
            throws IOException, DCSyntaxErrorException
    {
        TokenDescriptor token;
        String id;
        IDListTailNode idListTail;

        // Eat up spaces.
        do {
            token = tokenReader.getToken();
        } while (token.getCode() == TokenCode.T_SPACE);

        //
        // GR 21.
        //
        //      id-list-tail : , ID id-list-tail
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
            IDListTailNode nextIDListTail;
            nextIDListTail = IDListTailNode.parseIDListTail(tokenReader);

            // Build the IDListTailNode we just parsed.
            idListTail = new IDListTailNode(id, nextIDListTail);
        }

        //
        // GR 22.
        //
        //      id-list-tail :
        //

        else {
            // The IDListTailNode is empty.
            tokenReader.unread(token);
            idListTail = new IDListTailNode(null, null);
        }

        return idListTail;
    }

}
