import java.util.*;

public class ClassSymbol {
    private Symbol name;
    private HashMap<Symbol, String> variables; 
    private HashMap<String, FunctionSymbol> functions; 
    private LinkedHashMap<String, Integer> vtable;
    private Integer vtable_offset;
    private LinkedHashMap<String, Integer> recordtable;
    private Integer record_offset;

    public ClassSymbol (String s) {
        name = Symbol.symbol(s);
        variables = new HashMap<Symbol, String>();
        functions = new HashMap<String, FunctionSymbol>();
        this.vtable = new LinkedHashMap<String, Integer>();
        this.recordtable = new LinkedHashMap<String, Integer>();
        vtable_offset = 0;
        record_offset = 4;
    }

    public Integer getvtable_offset(String s) {
        Integer temp = 0;
        if (vtable.containsKey(s)) {
            temp = vtable.get(s);
        }
        return temp;
    }

    public HashMap<Symbol, String> getVariables() {
        return this.variables;
    }

    public void setVariables(HashMap<Symbol, String> h) {
        this.variables = h;
    }

    public LinkedHashMap<String, Integer> getRecordTable() {
        return this.recordtable;
    }

    public int recordTableSize() {
        return recordtable.size();
    }

    public void setRecordTable(LinkedHashMap<String, Integer> h) {
        this.recordtable = h;
    }

    public LinkedHashMap<String, Integer> getVTable() {
        return vtable;
    }

    public boolean checkRecordTable(String s) {
        if (recordtable.containsKey(s)) {
            return true;
        }
        return false;
    }

    public Integer getRecordOffset(String s) {
        return recordtable.get(s);
    }

    public String getClassName() {
        return this.name.toString();
    }

    public void addFunction(String name, String type) {
        this.functions.put(name, new FunctionSymbol(name, type));
        vtable.put(name,vtable_offset);
        vtable_offset = vtable_offset + 4;
    }

    public void addVariables(String name, String type) {
        this.variables.put(Symbol.symbol(name), type);
        recordtable.put(name,record_offset);
        record_offset = record_offset + 4;
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

    public int variableSize() {
        return variables.size();
    }

    public Set<String> getFunctionNames() {
        return vtable.keySet();
    }
}