import java.util.HashMap;
import java.util.Set;

public class ClassSymbol {
    private Symbol name;
    private HashMap<Symbol, String> variables; 
    private HashMap<String, FunctionSymbol> functions; 

    public ClassSymbol (String s) {
        name = Symbol.symbol(s);
        variables = new HashMap<Symbol, String>();
        functions = new HashMap<String, FunctionSymbol>();
    }

    public String getClassName() {
        return this.name.toString();
    }

    public void addFunction(String name, String type) {
        this.functions.put(name, new FunctionSymbol(name, type));
    }

    public void addVariables(String name, String type) {
        this.variables.put(Symbol.symbol(name), type);
    }

    public FunctionSymbol getFunction(String s) {
        return this.functions.get(s);
    }

    public String getVariable(String s) {
        return this.variables.get(Symbol.symbol(s));
    }

    public int functionSize() {
        return functions.size();
    }

    public Set<String> getFunctionNames() {
        return functions.keySet();
    }
}