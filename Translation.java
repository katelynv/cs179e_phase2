import java.util.*;
import syntaxtree.*;
import visitor.*;

public class Translation extends GJNoArguDepthFirst<String> {

    private PrintOutput print_function;
    private SymbolTable symbol_table;
    private ClassSymbol current_class;
    private FunctionSymbol current_function;
    private boolean alloc_function;

    public Translation (SymbolTable table_var)
    {
        this.symbol_table=table_var;
        this.alloc_function = false;
        this.print_function= new PrintOutput();
        List<String> tempList = table_var.getClassList();
        for (int i=1; i< tempList.size(); i++)
        {
            print_function.classes(tempList.get(i),table_var.getClass(tempList.get(i)).getFunctionNames());
        }
        print_function.enter();
    }

    public String visit(MainClass x) {
        print_function.main();
        x.f14.accept(this);
        x.f15.accept(this);
        print_function.ret("");
        print_function.enter();
        return null;
    }

    public String visit(Goal x) {
        x.f0.accept(this);
        x.f1.accept(this);
        if (alloc_function) {
            print_function.newArray();
        }
        return null;
    }

    public String visit(ClassDeclaration x) {
        current_class = symbol_table.getClass(x.f1.accept(this));
        x.f3.accept(this);
        x.f4.accept(this);
        return null;
    }

    public String visit(ClassExtendsDeclaration x) {
        current_class = symbol_table.getClass(x.f1.accept(this));
        x.f5.accept(this);
        x.f6.accept(this);
        return null;
    }

    public String visit(MethodDeclaration x) {
        String class_name = current_class.getClassName();
        String function_name = x.f2.accept(this);
        String parameters = x.f4.accept(this);
        current_function = current_class.getFunction(function_name);
        print_function.methodDeclare(class_name, function_name, parameters);
        x.f8.accept(this);
        String var1 = x.f10.accept(this);
        if (current_class.checkRecordTable(var1)) {
            var1 = print_function.getRecord(current_class.getRecordOffset(var1));
        }
        print_function.ret(var1);
        print_function.enter();
        return null;
    }

    public String visit(Identifier x) {
        return x.f0.toString();
    }

    public String visit(ExpressionRest x) {
        String exp = x.f1.accept(this);
        exp = recordVariableCheck(exp);
        return exp;
    }

    public String visit(ExpressionList x) {
        String exp = x.f0.accept(this);
       exp = exp + x.f1.accept(this);
        return exp;
    }

    public String visit(ArrayLookup x) {
        String arr_name = x.f0.accept(this);
        String alloc = x.f2.accept(this);
        arr_name = recordVariableCheck(arr_name);
        return print_function.lookUp(arr_name, alloc);
    }

    public String visit(Statement x) {
        x.f0.accept(this);
        return null;
    }

    public String visit(PrintStatement x) {
        print_function.output((x.f2.accept(this)));
        return null;
    }

    public String visit(IfStatement x) {
        String state_var = x.f2.accept(this);
        state_var = recordVariableCheck(state_var);
        String if_state_var = state_var;
        String label_var = print_function.startIf(if_state_var);
        x.f4.accept(this);
        print_function.endIf(label_var);
        print_function.startIfElse(label_var);
        x.f6.accept(this);
        print_function.endIfElse(label_var);
        return null;
    }

    public String visit(WhileStatement x) {
        String current_Label = print_function.startWhile();
        String var_label = x.f2.accept(this);
        print_function.continueWhile(current_Label, var_label);
        x.f4.accept(this);
        print_function.endWhile(current_Label);
        return null;
    }

    public String visit(AssignmentStatement x) {
        String first = x.f0.accept(this);
        String second = x.f2.accept(this);
        if (second.contains("call :")) {
            second = print_function.callFunction(second);
            first = recordCheck(first);
        } else {
            first = recordCheck(first);
        }
        second = recordCheck(second);
        print_function.assign(first, second);
        return null;
    }

    public String visit(ArrayAssignmentStatement x) {
        String first;String var1; String equal_var; 
        first= recordCheck(x.f0.accept(this));
        var1 = print_function.arrayPrint(first, x.f2.accept(this));
        equal_var = x.f5.accept(this);
        var1 = "[" + var1 + "+4]";
        print_function.assign(var1, equal_var);
        return null;
    }

    public String visit(NodeListOptional x) {
        String _ret = "";
        for (Enumeration<Node> en = x.elements(); en.hasMoreElements();) {
            _ret = _ret + " " + en.nextElement().accept(this);
        }
        return _ret;
    }

    public String visit(MessageSend x) {
        String class_var; 
        String class_name;
        String return_val; 
        String function_name;
        String param_var; 
        ClassSymbol class_symbol;

        class_var = x.f0.accept(this);
        class_name= print_function.getClassName(class_var);
        if (class_name == null) {
            class_name = getIDType(class_var);
        }
        
        class_symbol= symbol_table.getClass(class_name);
        if (class_symbol == null) {
            class_symbol= current_class;
        }
         
        function_name = x.f2.accept(this);
        param_var= x.f4.accept(this);
        class_var = recordCheck(class_var);
        
        return_val= print_function.functionCall(class_var, class_symbol.getvtable_offset(function_name), param_var);
        return return_val;
    }

    public String visit(TrueLiteral x) {
        return "0";
    }

    public String visit(FalseLiteral x) {
        return "1";
    }

    public String visit(IntegerLiteral x) {
        String info = x.f0.toString();
        return info;
    }

    public String visit(Expression x) {
        return x.f0.choice.accept(this);
    }

    public String visit(PrimaryExpression x) {
        return x.f0.choice.accept(this);
    }

    public String visit(PlusExpression x) {
        String first = x.f0.accept(this);
        String second = x.f2.accept(this);
        first = recordVariableCheck(first);
        second = recordVariableCheck(second);
        return print_function.add(first, second);
    }

    public String visit(MinusExpression x) {
        String first = x.f0.accept(this);
        String second = x.f2.accept(this);
        first = recordVariableCheck(first);
        second = recordVariableCheck(second);
        return print_function.subtract(first, second);
    }

    public String visit(TimesExpression x) {
        String first = x.f0.accept(this);
        String second = x.f2.accept(this);
        first = recordVariableCheck(first);
        second = recordVariableCheck(second);
        return print_function.multiply(first, second);
    }

    public String visit(AllocationExpression x) {
        return print_function.allocation(x.f1.accept(this), symbol_table.getClass(x.f1.accept(this)).recordTableSize());
    }

    public String visit(ThisExpression x) {
        return x.f0.toString();
    }

    public String visit(AndExpression x) {
        String a = x.f0.accept(this);
        String b = print_function.startSS(a);
        String var1 = x.f2.accept(this);
        print_function.continueSS(b);
        print_function.noWork(var1);
        print_function.endSS(b);
        return var1;
    }

    public String visit(NotExpression x) {
        return print_function.subtract("1", x.f1.accept(this));
    }

    public String visit(CompareExpression x) {
        String first = x.f0.accept(this);
        String second = x.f2.accept(this);
        first = recordVariableCheck(first);
        second = recordVariableCheck(second);
        return print_function.lts(first, second);
    }

    public String visit(BracketExpression x) {
        String expr = x.f1.accept(this);

        return expr;
    }

    public String visit(ArrayAllocationExpression x) {
        this.alloc_function = true;
        String expr = "call :AllocArray(" + x.f3.accept(this) + ")";
        return expr;
    }

    public String visit(FormalParameter x) {
        String state = x.f1.accept(this);
        state = recordVariableCheck(state);
        return state;
    }

    public String visit(FormalParameterRest x) {
        String state = x.f1.accept(this);
        state = recordVariableCheck(state);
        return state;
    }

    public String visit(FormalParameterList x) {
        String parameters = x.f0.accept(this);
        parameters = parameters + x.f1.accept(this);
        return parameters;
    }

    public String getIDType(String x) {
        if (symbol_table.getClass(x) != null) {
            return x;
        } else if (x == "this") {
            return current_class.getClassName();
        } else {
            if (x == "int" || x == "boolean") {
                return x;
            }
            String temp = "";
            if (current_function.getLocalType(x) != null) {
                temp = current_function.getLocalType(x);
            } else if (current_function.getParameterType(x) != null) {
                temp = current_function.getParameterType(x);
            } else if (current_class.getVariable(x) != null) {
                temp = current_class.getVariable(x);
            }
            return temp;
        }
    }
    public String recordCheck(String x){
		if(current_class != null){
			if(current_class.checkRecordTable(x)){
				x = "[this+" + current_class.getRecordOffset(x) + "]";
			}
		}
		return x;
	}

	public String recordVariableCheck(String x){
		if(current_class != null){
			if(current_class.checkRecordTable(x)){
				x = print_function.getRecord(current_class.getRecordOffset(x));
			}
		}
		
		return x;
	}
}
