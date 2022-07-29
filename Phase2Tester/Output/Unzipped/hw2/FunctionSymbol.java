import java.util.HashMap;

public class FunctionSymbol {
    private String function_name;
    private String function_type;
    private HashMap<Symbol, String> locals;
    private HashMap<Symbol, String> parameters;

    public FunctionSymbol(String name, String type) {
        function_name = name;
        function_type = type;
        locals = new HashMap<Symbol, String>();
        parameters = new HashMap<Symbol, String>();
    }

    public String getFunctionName() {
        return this.function_name;
    }
    
    public String getFunctionType() {
        return this.function_type;
    }

    public String getLocalType(String s) {
        return this.locals.get(Symbol.symbol(s));
    }

    public void addLocal(String name, String type) {
        this.locals.put(Symbol.symbol(name), type);
    }

    public void addParameter(String name, String type) {
        this.parameters.put(Symbol.symbol(name), type);
    }

    public String getParameterType(String s) {
        return this.parameters.get(Symbol.symbol(s));
    }

    public int parameterSize() {
        return this.parameters.size();
    }

}