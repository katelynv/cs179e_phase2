
import java.util.*;

public class SymbolTable {

    private HashMap<String, ClassSymbol> table;
    private List<String> class_names;

    public SymbolTable() {
        table = new HashMap<String, ClassSymbol>();
    }

    public void addClass(String s) {
        ClassSymbol temp = new ClassSymbol(s);
        table.put(s, temp);
        class_names.add(s);
    }

    public ClassSymbol getClass(String s) {
        return table.get(s);
    }

    public List<String> getClassList() {
        return class_names;
    }
}