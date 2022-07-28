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

    public String getClassName(String s) {
        return variable_info.get(s);
    }

    public Printer() {
        this.depth = 0;
        this.variable = 0;
        this.null_label = 0;
        this.if_else = 0;
        this.while_count = 0;
        this.variable_info = new HashMap<String, String>();
    }

    public String createLabel() {
        label++;
        return "Label"+label;
    }

    public void main() {
        print("func Main()");
        this.depth++;
    }

    public void enter() {
        print("");
    }
    
    public void classes(String className, set<String> list) {
        print("const vmt_" + className);
        this.depth++;
        for (String s : list) {
            print(":" + className + "." + s)
        }
        this.depth--;
        enter();
    }

    public void output(String s) {
        print("PrintIntS(" + s + ")");
    }

    public String getRecord(Integer i) {
        String temp = createVariable();
        print(temp + " - [this+" i + "]");
        return temp;
    }

    public String createVariable() {
        String temp = "t." + variable;
        variable++;
        return temp;
    }

    public String add(String x, String y) {
        String temp = createVariable();
        print(temp + " = Add(" + x + " " + y + ")");
        return temp;
    }

    public String subtract(String x, String y) {
        String temp = createVariable();
        print(temp + " = Sub(" + x + " " + y + ")");
        return temp;
    }

    public String multiply(String x, String y) {
        String temp = createVariable();
        print(temp + " = MulS(" + x+ " " + y + ")");
        return temp;
    }

    public String equal(String x, String y) {
        String temp = createVariable();
        print(temp + " = Eq(" + x + " " + y + ")");
        return temp;
    }

    public String lessthan(String x, String y) {
        String temp = createVariable();
        print(temp + " = Lt(" + x + " " + y + ")");
        return temp;
    }

    public String startWhile() {
        String temp = "while" + while_count;
        while_count++;
        print(temp + "_top:");
        return temp;
    }

    public void continueWhile(String whileLabel, String varLabel) {
        print("if0 " + varLabel + " goto :" + whileLabel + "_end");
        depth++;
    }

    public void endWhile(String s) {
        print("goto :" + s + "_top");
        depth--;
        print(s + "_end:");
    }

    public String startSS(String varLabel) {
        String temp = "ss" + label;
        label++;
        print("if0 " + varLabel + " goto :" + temp + "_else");
        depth++;
        return temp;
    }

    public void continueSS(String s) {
        label++;
        print("goto :" + s + "_end");
        depth--;
        print(s + "_else:");
        depth++;
    }

    public void endSS(String s) {
        depth--;
        print(s + "_end:");
    }

    public void noWork(String s) {
        print(s + " = 0");
    }

    public String lts(String x, String y) {
        String temp = createVariable();
        print(temp + " = LtS(" + x + " " + y + ")");
        return temp;
    }

    public String arrayPrint(String name, String location) {
        String temp = name;
        if (name.contains("this+")) {
            String s = createVariable();
            print(s + " = " + name);
            name = s;
        }
        print("s = [" + name + "]");
        print("ok = LtS(" + location + " s)");
        String l1 = createLabel();
        print("if ok goto :" + l1);
        print("Error(\"array index out of bounds\")");
        print(l1 + ": ok = LtS(-1 " + location + ")");
        String l2 = createLabel();
        print("if ok goto :" + 12);
        print("Error(\"array index out of bounds\")");
        print(l2 + ": o = MulS(" + location + " 4)");
        if (temp.contains("this+")) {
            String s = createVariable();
            print(s + " = " + temp);
            temp = s; 
        }
        String a = createVariable();
        print(a + " = Add(" + temp + " o)");
        return a;
    }
 
    public String lookUp(String name, String location) {
        String temp = name;
        if (name.contains("this+")) {
            String s = createVariable();
            print(s + " = " + name);
            name = s;
        }
        print("s = [" + name + "]");
        print("ok = Lt(" + location + " s");
        String l1 = createLabel();
        print("if ok goto :" + l1);
        print("Error(\"array index out of bounds\")");
        print(l1 + ": o = MulS(" + location + " 4)");
        if (temp.contains("this+")) {
            String s = createVariable();
            print(s + " = " + temp);
            temp = s;
        }
        print("d = Add(" + temp + " o)");
        String a = createVariable();
        print(a + " = [d+4]");
        return a;
    }

    public String callFunction(String s) {
        String temp = createVariable();
        print(temp + " = " + s);
        return temp;
    }

    public void newArray() {
        print("func AllocArray(size)");
        this.depth++;
        print("bytes = MulS(size 4)");
        print("bytes = Add(bytes 4)");
        print("v = HeapAllocZ(bytes)");
        print("[v] = size");
        print("ret v");
        this.depth--;
    }

    public void ret(String s) {
        if (s == null) {
            s = "";
        }
        print("ret " + s);
        this.depth--;
    }

    public void assign(String x, String y) {
        print(x + " = " + y);
    }

    public void methodDeclare(String className, String methodName, String parameters) {
        variable = 0;
        if (parameters == null) {
            parameters = "";
        } else {
            parameters = " " + parameters;
        }
        parameters = "this" + parameters;
        print("func " + className + "." + methodName + "(" + parameters + ")");
        this.depth++;
    }

    public String allocation(String className, int size) {
        size = size*4 + 4;
        String temp = createVariable();
        variable_info.put(temp, className);
        print(temp + " = HeapAllocZ(" + size + ")");
        print("[" + temp + "] = :vmt_" + className);
        return temp;
    }

    public String functionCall(String s, int offset, String p) {
        if (s.contains("this+")) {
            String temp = createVariable();
            print(temp + " = " + s);
        }
        print("if " + s + " goto :null" + null_label);
        this.depth++;
        print("Error(\"null pointer\")");
        this.depth--;
        print("null" + null_label + ":");
        null_label++;
        String v = createVariable();
        print(v + " = [" + s + "]");
        print(v + " = [" + v + "+" + offset + "]");
        String w = createVariable();
        if (p == null) {
            p = "";
        } else {
            p = " " + p;
        }
        print(w + " = call " + v + "(" + s + p + ")");
        return w;
    }

    public String startIf(String s) {
        String temp = "if0";
        String ifLabel = "if" + if_else;
        if_else++;
        print(temp + " " + s + " goto :" + ifLabel + "_else");
        this.depth++;
        return ifLabel;
    }

    public void startIfElse(String s) {
        print(s + "_else:");
        this.depth++;
    }

    public void endIfElse(String s) {
        this.depth--;
        print(s + "_end:");
    }

    public void endIf(String s) {
        print("goto :" + s + "_end");
        this.depth--;
    }

    private void print(String s) {
        if (depth > 0) {
            String temp = String.format("%" + depth + "s", " ");
            System.out.print(temp); 
        }
        System.out.println(s);
    }






}
