import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeSet;


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

    /**
     * Reads a value from the console and stores it in the
     * identified variable in the symbol table.
     *
     * @param id The name of the variable to store the read value
     *           in
     */
    public void readVar(String id) {
        // Read the value from the user.
        System.out.print("Enter value for " + id + ": ");
        Scanner input = new Scanner(System.in);
        double num = input.nextDouble();

        // Assign the user's value to the ID.
        m_symTab.put(id, num);
    }

    /**
     * Prints the value of a variable in the symbol table on the
     * console.
     *
     * @param id The name of the variable to print the value of
     */
    public void printVar(String id)
            throws DCRuntimeErrorException
    {
        // Make sure the variable exists in the symbol table.
        if (!m_symTab.containsKey(id)) {
            throw new DCRuntimeErrorException(
                    "Unrecognized variable name: " + id
            );
        }

        // Print the value on the console.
        System.out.println("Value of " + id + ": " + m_symTab.get(id));
    }

    /**
     * Dumps the symbol table to the console so its contents may
     * be examined.
     */
    public void dumpSymTab() {
        System.out.println("Symbol Table:");

        // Alphabetize the symbol names.
        TreeSet<String> keys = new TreeSet<String>();
        keys.addAll(m_symTab.keySet());
        for (String key : keys) {
            System.out.println(key + " = " + m_symTab.get(key));
        }

    }

}
