import syntaxtree.*;
import visitor.*;

public class SecondPassVisitor extends GJDepthFirst<String, SymbolTable> {
	private SymbolTable symbol_table;
	private ClassSymbol current_class;
	private FunctionSymbol current_function;
	private boolean anyErrors = false;

	public boolean errors(){
		return anyErrors;
	}

	public SecondPassVisitor(SymbolTable stable) {
		this.symbol_table = stable;
	}

	public String visit(Goal n, SymbolTable stable){
		n.f0.accept(this, stable);
		n.f1.accept(this, stable);
		return null;
	}

	public String visit(MainClass n, SymbolTable stable) {
		current_class = symbol_table.getClass(VisitorFunctions.className(n));
		current_function = current_class.getFunction("main");
		n.f15.accept(this, stable);
		return null;
	}

	public String visit(ClassDeclaration n, SymbolTable stable){
		current_class = symbol_table.getClass(VisitorFunctions.className(n));
		current_function = null;
		n.f4.accept(this, stable);
		return null;
	}

	public String visit(MethodDeclaration n, SymbolTable stable){
		
		current_function = current_class.getFunction(VisitorFunctions.methodName(n));
		String m_type = VisitorFunctions.getType(n.f1);
		String returnType = n.f10.accept(this, stable);
		if(m_type != null || m_type != "" || returnType != null || returnType != "" ){
			returnType = getIDType(returnType);
			if(m_type != returnType){
				anyErrors = true;
				return null;
			}
		}
		n.f8.accept(this, stable);
		return null;
	}

	public String visit(Statement n, SymbolTable stable) {
		n.f0.choice.accept(this, stable);
		return null;
	}

	public String visit(AssignmentStatement n, SymbolTable stable) {
		String id = VisitorFunctions.getId(n.f0);
		String id2 = n.f2.accept(this, stable);
		if(id != null && id2 != null){
			String id_type = getIDType(id);
			String id_type2 = getIDType(id2);
			if(id_type != id_type2 || id_type == null || id_type2 == null){
				anyErrors = true;
			}
		}
		
		return null;
	}

	public String visit(PrintStatement n, SymbolTable stable) {
		String statementType = getIDType(n.f2.accept(this, stable));
		if(statementType != "int"){
			anyErrors = true;
		}
		return null;
	}

	public String visit(Expression n, SymbolTable stable){
		return n.f0.choice.accept(this, stable);
	}

	public String getIDType(String x){
		if(symbol_table.getClass(x) != null){
			return x;	
		}
		else{
			if(x == "int" || x == "boolean"){
				return x;
			}
			String name = "";
			if(current_function.getLocalType(x) != null){
				name = current_function.getLocalType(x);
			}
			else if(current_function.getParameterType(x) != null){
				name = current_function.getParameterType(x);
			}
			else if(current_class.getVariable(x) != null){
				name = current_class.getVariable(x);
			}
			return name;
		}
	}

	public String visit(MessageSend n, SymbolTable stable){
		
		String c_name = getIDType(n.f0.accept(this, stable));
		String m_name = VisitorFunctions.getId(n.f2);
		ClassSymbol c_Class = symbol_table.getClass(c_name);
		if(c_Class != null){
				FunctionSymbol c_Method = c_Class.getFunction(m_name);
			if(c_Method == null){
				anyErrors = true;
				return "";
			}
			else{
				if(n.f4.node != null){
					int callSize = VisitorFunctions.getListSize(n.f4.node);
					int methodSize = c_Method.parameterSize();
					if(methodSize != callSize){
						anyErrors = true;
					}
				}
				String m_type = c_Method.getFunctionType();
				return m_type;
			}
		}
		else{
			anyErrors = true;
			return "";
		}
		
		
	}

	public String visit(PrimaryExpression n, SymbolTable stable){
		String c_name = n.f0.choice.accept(this, stable);
		return c_name; 
	}

	public String visit(ArrayLookup n, SymbolTable stable){
		String id1 = getIDType(n.f0.accept(this, stable));
		String id2 = getIDType(n.f2.accept(this, stable));
		if(id1 == "int []" && id2 == "int"){
			return id2;
		}
		else{
			anyErrors = true;
			return "";
		}
	}

	public String visit(AllocationExpression n, SymbolTable stable){
		String c_name = VisitorFunctions.getId(n.f1);
		return c_name;
	}

	public String visit(IntegerLiteral n, SymbolTable stable){
		String c_name = "int";
		return c_name;
	}

	public String visit(FalseLiteral n, SymbolTable stable){
		String c_name = "boolean";
		return c_name;
	}

	public String visit(TrueLiteral n, SymbolTable stable){
		String c_name = "boolean";
		return c_name;
	}

	public String visit(ThisExpression n, SymbolTable stable){
		return current_class.getClassName();
	}

	public String visit(BracketExpression n, SymbolTable stable){
		return n.f1.accept(this, stable);
	}

	public String visit(PlusExpression n, SymbolTable stable){
		String first = getIDType(n.f0.accept(this, stable));
		String second = getIDType(n.f2.accept(this, stable));
		if(first == second){
			return first;
		}
		else{
			return null;
		}
	}

	public String visit(MinusExpression n, SymbolTable stable){
		String first = getIDType(n.f0.accept(this, stable));
		String second = getIDType(n.f2.accept(this, stable));
		if(first == second){
			return first;
		}
		else{
			return null;
		}
	}

	public String visit(TimesExpression n, SymbolTable stable){
		String first = getIDType(n.f0.accept(this, stable));
		String second = getIDType(n.f2.accept(this, stable));
		if(first == second){
			return first;
		}
		else{
			return null;
		} 
	}

	public String visit(Identifier n, SymbolTable stable){
		return VisitorFunctions.getId(n);
	}



}