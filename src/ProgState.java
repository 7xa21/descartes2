import java.util.HashMap;
import java.util.Stack;


/**
 * Maintains a Descartes program state while it's executing.
 *
 * This includes the program's symbol table mapping variable
 * identifiers to values, a stack of currently-active loop
 * identifiers, and (if not null) a break name that causes the
 * named loop's statement list to halt execution and exit the
 * loop.
 */
public class ProgState {

    //==================//
    // Member Variables //
    //==================//

    private Stack<String> m_loopIDStack;
    private HashMap<String, Double> m_symTab;
    private String m_breakName;


    //=========//
    // Methods //
    //=========//

    /**
     * Initializes a new program state with an empty symbol table,
     * loop ID stack and null break name.
     */
    public ProgState() {
        m_loopIDStack = new Stack<String>();
        m_symTab = new HashMap<String, Double>();
        m_breakName = null;
    }

    /**
     * Accessor for the loop ID stack.
     */
    public Stack<String> loopIDStack() {
        return m_loopIDStack;
    }

    /**
     * Accessor for the symbol table.
     */
    public HashMap<String, Double> symTab() {
        return m_symTab;
    }

    /**
     * Accessor for the current break name.
     */
    public String breakName() {
        return m_breakName;
    }

    /**
     * Sets the current break name. Setting this to non-null will
     * break the named loop.
     *
     * @param bName The ID naming a currently active loop
     */
    public void setBreakName(String bName) {
        m_breakName = bName;
    }

}
