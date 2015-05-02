import java.util.HashMap;
import java.util.Stack;

public class ProgState {

    //==================//
    // Member Variables //
    //==================//

    private Stack<String> m_loopIDStack;
    private HashMap<String, Double> m_symTab;


    //=========//
    // Methods //
    //=========//

    public ProgState() {
        m_loopIDStack = new Stack<String>();
        m_symTab = new HashMap<String, Double>();
    }

    public Stack<String> loopIDStack() {
        return m_loopIDStack;
    }

    public HashMap<String, Double> symTab() {
        return m_symTab;
    }

}
