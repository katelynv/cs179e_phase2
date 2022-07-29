import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class SymbolTable {

    private HashMap<String, ClassSymbol> table;
    private List<String> classNames;

    public SymbolTable() {
        table = new HashMap<String, ClassSymbol>();
        this.classNames = new ArrayList<String>();
    }

    public void addClass(String s) {
        ClassSymbol temp = new ClassSymbol(s);
        table.put(s, temp);
        classNames.add(s);
    }

    public ClassSymbol getClass(String s) {
        return table.get(s);
    }

    public List<String> getClassList() {
        return classNames;
    }
}