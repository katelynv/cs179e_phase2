import java.util.*;

public class ClassSymbol {
	private Symbol name;
	private HashMap<Symbol, String> fields;
	private HashMap<String, FunctionSymbol> methods;
	private LinkedHashMap<String, Integer> vTable; 
	private Integer offset;
	private LinkedHashMap<String, Integer> record; 
	private Integer recordOffset;

	public ClassSymbol(String n) {
		name = Symbol.symbol(n);
		fields = new HashMap<Symbol, String>();
		methods = new HashMap<String, FunctionSymbol>();
		this.vTable = new LinkedHashMap<String,Integer>();
		this.record = new LinkedHashMap<String,Integer>();
		offset = 0; 
		recordOffset = 4;
	}

	public Integer getvtable_offset(String name){
		Integer val = 0; 
		if(vTable.containsKey(name)){
			val = vTable.get(name);
		}
		return val;
	}

    public HashMap<Symbol, String> getVariables(){
		return this.fields;
	}

	public void setVariables(HashMap<Symbol, String> s){
		this.fields = s;
	}

	public LinkedHashMap<String, Integer> getRecordTable(){
		return this.record;
	}
	
	public int recordTableSize(){
		return record.size();
	}
	public void setRecordTable(LinkedHashMap<String, Integer> r){
		this.record = r;
	}

	public LinkedHashMap<String, Integer> getVTable(){
		return vTable;
	}

    public boolean checkRecordTable(String name){
		if(record.containsKey(name)){
			return true;
		}
		return false;
	}

	public Integer getRecordOffset(String name){
		return record.get(name);
	}

	public String getClassName() {
		return this.name.toString();
	}

	public void addFunction(String name, String type) {
		this.methods.put(name, new FunctionSymbol(name, type));
		vTable.put(name,offset);
		offset = offset + 4; 
	}

	public void addVariables(String name, String type) {
		this.fields.put(Symbol.symbol(name), type);
		record.put(name, recordOffset);
		recordOffset = recordOffset + 4;
	}

	public FunctionSymbol getFunction(String name) {
		return this.methods.get(name);
	}

	public String getVariable(String name) {
		return this.fields.get(Symbol.symbol(name));
	}

	public int functionSize() {
		return methods.size();
	}

	public int variableSize(){
		return fields.size();
	}

	public Set<String> getFunctionNames() {
		return vTable.keySet();
	}

}