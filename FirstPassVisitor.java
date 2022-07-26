import java.util.HashSet;
import java.util.Set;

import syntaxtree.*;
import visitor.*;

public class FirstPassVisitor extends DepthFirstVisitor {
    ErrorMsg error = new ErrorMsg();
    private SymbolTable symbol_table = new SymbolTable();
    private ClassSymbol current_class;
    private FunctionSymbol current_function;

    public boolean errors() {
        return error.errors;
    }

    public SymbolTable getSymbolTable() {
        return this.symbol_table;
    }

    public void visit (Goal n) {
        NodeList classNames = new NodeList(n.f0);
        if (n.f1.size() > 0) {
            for (int i = 0; i < n.f1.size(); i++) {
                classNames.addNode(((TypeDeclaration) n.f1.elementAt(i)).f0.choice);
            }
        }
        if (VisitorFunctions.checkClass(classNames) == true) {
            n.f0.accept(this);
            n.f1.accept(this);
        } else {
            error.sendError("ERROR: Class Names are the same");
        }
    }

    public void visit (MainClass n) {
        String className = VisitorFunctions.className(n);
        symbol_table.addClass(className);
        current_class = symbol_table.getClass(className);
        current_class.addFunction("main", "void");
        current_function = current_class.getFunction("main");
        n.f14.accept(this);
    }

    public void visit (ClassDeclaration n) {
        String className = VisitorFunctions.className(n);
        symbol_table.addClass(className);
        current_class = symbol_table.getClass(className);
        current_function = null;
        if (VisitorFunctions.checkId(n.f3)) {
            n.f3.accept(this);
        } else {
            error.sendError("ERROR: Id names are the same");
        }
        
        if (VisitorFunctions.checkMethod(n.f4)) {
            n.f4.accept(this);
        } else {
            error.sendError("ERROR: Method names are the same");
        }
    }

    public void visit(ClassExtendsDeclaration n) {
        String className = VisitorFunctions.className(n);
        symbol_table.addClass(className);
        current_class = symbol_table.getClass(className);
        String temp_class = VisitorFunctions.getId(n.f3);
        ClassSymbol temp_classSymbol = symbol_table.getClass(temp_class);
        Set<String> temp_methods = temp_classSymbol.getFunctionNames();
        if (VisitorFunctions.checkSetContains(temp_methods, n.f6)) {
            if (VisitorFunctions.checkId(n.f5)) {
                n.f5.accept(this);
            } else {
                error.sendError("ERROR: Id names are the same");
            }

            if (VisitorFunctions.checkMethod(n.f6)) {
                n.f6.accept(this);
            } else {
                error.sendError("ERROR: Method names are the same");
            }
        } else {
            error.sendError("ERROR: Overloading method");
        }
    }

    public void visit(MethodDeclaration n) {
        current_class.addFunction(VisitorFunctions.methodName(n), VisitorFunctions.methodType(n));
        current_function = current_class.getFunction(VisitorFunctions.methodName(n));
        if (n.f4.node != null) {
            if (VisitorFunctions.checkParameter((FormalParameterList) n.f4.node)) {
                n.f4.accept(this);
            } else {
                error.sendError("ERROR: Parameter names are the same");
            }
        }

        if (VisitorFunctions.checkId(n.f7)) {
            n.f7.accept(this);
        } else {
            error.sendError("ERROR: Id names are the same");
        }
    }

    public void visit(VarDeclaration n) {
        if (current_function == null && current_class != null) {
            current_class.addVariables(VisitorFunctions.getId(n.f1), VisitorFunctions.getType(n.f0));
        } else if (current_function != null) {
            current_function.addLocal(VisitorFunctions.getId(n.f1), VisitorFunctions.getType(n.f0));
        }
    }

    public void visit(FormalParameter n) {
        if (current_function != null && current_class != null) {
            current_function.addParameter(VisitorFunctions.getId(n.f1), VisitorFunctions.getType(n.f0));
        }
    }
}


