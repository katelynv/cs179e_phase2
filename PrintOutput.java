import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class PrintOutput {
	private int depth;
	private int variable;
	private int label;
	private int null_label;
	private int if_else;
	private int while_count;
	private HashMap<String, String> variable_info;

	public String getClassName(String s){
		return variable_info.get(s);
	}

	public PrintOutput(){
		this.depth = 0;
		this.label = 0;
		this.variable = 0;
		this.null_label = 0;
		this.if_else = 0;
		this.while_count = 0;
		this.variable_info = new HashMap<String, String>();
	}

	public String createLabel(){
		label++;
		return "Label"+label;
	}

	public void main(){
		print("func Main()");
		this.depth++;
	}

	public void enter(){
		print("");
	}

	public void classes(String cName, Set<String> list){
		print("const vmt_" + cName);
		this.depth++;
		for(String s : list){
			print(":"+cName+"."+ s);
		}
		this.depth--;
		enter();
	}

	public void output(String s){
		print("PrintIntS(" + s + ")");
	}

	public String getRecord(Integer x){
		String var = createVariable();
		print(var + " = [this+" + x + "]");
		return var;
	}

	public String createVariable(){
		String var = "t." + variable;
		variable++;
		return var;
	}

	public String add(String x, String y){
		String var = createVariable();
		print(var + " = Add(" + x + " " + y + ")");
		return var;
	}

	public String subtract(String x, String y){
		String var = createVariable();
		print(var + " = Sub(" + x + " " + y + ")");
		return var;
	}

	public String multiply(String x, String y){
		String var = createVariable();
		print(var + " = MulS(" + x + " " + y + ")");
		return var;
	}

	public String equal(String x, String y){
		String var = createVariable();
		print(var + " = Eq(" + x + " " + y + ")");
		return var;
	}

	public String lessThan(String x, String y){
		String var = createVariable();
		print(var + " = Lt(" + x + " " + y + ")");
		return var;
	}

	public String startWhile(){
		String cWhile = "while"+while_count;
		while_count++;
		print(cWhile + "_top:");
		return cWhile;
	}

	public void continueWhile(String whileLabel, String varLabel){
		print("if0 " + varLabel +  " goto :" + whileLabel + "_end");
		depth++;
	}

	public void endWhile(String someLabel){
		print("goto :" + someLabel + "_top");
		depth--;
		print(someLabel + "_end:");
	}

	public String startSS(String varLabel){
		String s = "ss" + label;
		label++;
		print("if0 " + varLabel + " goto :" + s + "_else");
		depth++;
		return s;
	}

	public void continueSS(String l){
		label++;
		print("goto :" +l + "_end");
		depth--;
  	print(l+"_else:");
		depth++;
	}

	public void noWork(String l){
		print(l + " = 0");
	}

	public void endSS(String someLabel){
		depth--;
		print(someLabel + "_end:");
	}

	public String lts(String x, String y){
		String var = createVariable();
		print(var + " = LtS(" + x + " " + y + ")");
		return var;
	}

	public String arrayPrint(String name, String loc){
		String str = name;
		if(name.contains("this+")){
			String v = createVariable();
			print(v + " = " + name);
			name = v;
		}
		print("s = [" + name + "]");
		print("ok = LtS(" + loc + " s)");
		String l1 = createLabel();
		print("if ok goto :" + l1);
		print("Error(\"array index out of bounds\")");
		print(l1 + ": ok = LtS(-1 " + loc + ")");
		String l2 = createLabel();
		print("if ok goto :" + l2);
		print("Error(\"array index out of bounds\")");
		print(l2 + ": o =  MulS(" + loc + " 4)");
		if(str.contains("this+")){
			String v = createVariable();
			print(v + " = " + str);
			str = v;
		}
		String d = createVariable();
		print(d + " = Add("+str+" o)");
		return d;
	}

	public String lookUp(String name, String loc){
		String str = name;
		if(name.contains("this+")){
			String v = createVariable();
			print(v + " = " + name);
			name = v;
		}
		print("s = [" + name + "]");
		print("ok = Lt(" + loc + " s)");
		String l1 = createLabel();
		print("if ok goto :" + l1);
		print("Error(\"array index out of bounds\")");
		print(l1 + ": o =  MulS(" + loc + " 4)");
		if(str.contains("this+")){
			String v = createVariable();
			print(v + " = " + str);
			str = v;
		}
		print("d = Add("+str+" o)");
		String r = createVariable();
		print(r + " = [d+4]");
		return r;
	}

	public String callFunction(String s){
		String var = createVariable();
		print(var + " = " + s);
		return var;
	}

	public void newArray(){
		print("func AllocArray(size)");
		this.depth++;
		print("bytes = MulS(size 4)");
		print("bytes = Add(bytes 4)");
		print("v = HeapAllocZ(bytes)");
		print("[v] = size");
		print("ret v");
		this.depth--;
	}

	public void ret(String s){
		if(s == null){
			s = "";
		}
		print("ret " + s);
		this.depth--;
	}

	public void assign(String var, String equals){
		print(var + " = " + equals);
	}

	public void methodDeclare(String cName, String mName, String params){
		variable = 0;
		if(params == null){
			params = "";
		}
		else{
			params = " " + params;
		}
		params = "this" + params;
		print("func " + cName + "." + mName +  "(" + params  + ")");
		this.depth++;
	}

	public String allocation(String cname, int size){
		size = size * 4 + 4;
		String var = createVariable();
		variable_info.put(var, cname);
		print(var + " = HeapAllocZ(" + size + ")");
		print("[" + var + "] = :vmt_" + cname);
		return var;
	}

	public String functionCall(String cVar, int offset, String param){
		if(cVar.contains("this+")){
			String v = createVariable();
			print(v + " = " + cVar);
			cVar = v;
		}
		print("if " + cVar + " goto :null" + null_label);
		this.depth++;
		print("Error(\"null pointer\")");
		this.depth--;
		print("null" + null_label + ":");
		null_label++;
		String var = createVariable();
		print(var + " = [" + cVar + "]");
		print(var + " = [" + var + "+" + offset + "]");
		String newVar = createVariable();
		if(param == null){
			param = "";
		}
		else{
			param = " " + param;
		}
		print(newVar +  " = call " + var + "(" + cVar + param + ")");
		return newVar;
	}

	public String startIf(String s){
		String if0 = "if0";
		String ifLabel = "if" + if_else;
		if_else++;
		print(if0 + " " + s + " goto :" + ifLabel + "_else");
		this.depth++;
		return ifLabel;
	}

	public void startIfElse(String ifLabel){
		print(ifLabel +"_else:");
		this.depth++;
	}

	public void endIfElse(String ifLabel){
		this.depth--;
		print(ifLabel+"_end:");
	}

	public void endIf(String ifLabel){

		print("goto :" + ifLabel+"_end");
		this.depth--;
	}

	private void print(String s){
		if(depth > 0){
			String space = String.format("%"+ depth +"s", " ");
			System.out.print(space);
		}
			System.out.println(s);
	}

	
}
