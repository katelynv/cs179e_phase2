import java.util.HashMap;

public class SymbolTable {

    private HashMap<String, ClassSymbol> table;

    public SymbolTable() {
        table = new HashMap<String, ClassSymbol>();
    }

    public void addClass(String s) {
        ClassSymbol temp = new ClassSymbol(s);
        table.put(s, temp);
    }

    public ClassSymbol getClass(String s) {
        return table.get(s);
    }
}