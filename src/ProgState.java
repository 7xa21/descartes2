import java.util.HashMap;
import java.util.Stack;

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

    public ProgState() {
        m_loopIDStack = new Stack<String>();
        m_symTab = new HashMap<String, Double>();
        m_breakName = null;
    }

    public Stack<String> loopIDStack() {
        return m_loopIDStack;
    }

    public HashMap<String, Double> symTab() {
        return m_symTab;
    }

    public String breakName() {
        return m_breakName;
    }

    public void setBreakName(String bName) {
        m_breakName = bName;
    }
}
