import java.util.HashSet;
import java.util.Set;

import syntaxtree.*;

public class VisitorFunctions {
    public static boolean checkClass(NodeList names) {
        Set<String> temp_set = new HashSet<>();
        for (int i = 0; i < names.size(); i++) {
            String temp_string = VisitorFunctions.className(names.elementAt(i));
            if (temp_set.contains(temp_string)) {
                return false;
            } else {
                temp_set.add(temp_string);
            }
        }
        return true;
    }

    public static String className(Node n) {
        String className = "";
        if (n instanceof MainClass) {
            className = ((MainClass) n).f1.f0.toString();
        } else if (n instanceof ClassDeclaration) {
            className = ((ClassDeclaration) n).f1.f0.toString();
        } else if (n instanceof ClassExtendsDeclaration) {
            className = ((ClassExtendsDeclaration) n).f1.f0.toString();
        }
        return className;
    }

    public static boolean checkMethod (NodeListOptional n) {
        Set<String> temp_set = new HashSet<>();

        for (int i = 0; i < n.size(); i++) {
            String temp_string = VisitorFunctions.methodName((MethodDeclaration) n.elementAt(i));
            if (temp_set.contains(temp_string)) {
                return false;
            } else {
                temp_set.add(temp_string);
            }
        }
        return true;
    }

    public static String methodName(MethodDeclaration m) {
        return m.f2.f0.toString();
    }

    public static boolean checkId(NodeListOptional n) {
        Set<String> temp_set = new HashSet<>();

        for (int i = 0; i < n.size(); i++) {
            String temp_string = VisitorFunctions.getId(((VarDeclaration) n.elementAt(i)).f1);
            if (temp_set.contains(temp_string)) {
                return false;
            } else {
                temp_set.add(temp_string);
            }
        }
        return true;
    }

    public static String getId(Identifier i) {
        return i.f0.toString();
    }

    public static boolean checkSetContains(Set<String> set, NodeListOptional n) {
        for (int i = 0; i < n.size(); i++) {
            String temp = VisitorFunctions.methodName((MethodDeclaration) n.elementAt(i));
            if (set.contains(temp)) {
                return true;
            }
        }
        return true;
    }

    public static boolean checkParameter(FormalParameterList p) {
        FormalParameter temp_fp = p.f0;
        NodeListOptional temp_nlo = p.f1;

        Set<String> temp_set = new HashSet<>();
        temp_set.add(getId(temp_fp.f1));

        for (int i = 0; i < temp_nlo.size(); i++) {
            String temp_string = VisitorFunctions.getId(((FormalParameterRest) temp_nlo.elementAt(i)).f1.f1);
            if (temp_set.contains(temp_string)) {
                return false;
            } else {
                temp_set.add(temp_string);
            }
        }
        return true;
    }

    public static String methodType(MethodDeclaration m) {
        return getType(m.f1);
    }

    public static String getIntegerType(IntegerType i) {
        return i.f0.toString();
    }

    public static int getListSize(Node n) {
        return ((ExpressionList)n).f1.size() + 1;
    }

    public static String getType(Type t) {
        Node n = t.f0.choice;
        String type = "";
        if (n instanceof IntegerType) {
            type = ((IntegerType) n).f0.toString();
        } else if (n instanceof BooleanType) {
            type = ((BooleanType) n).f0.toString();
        } else if (n instanceof ArrayType) {
            type = "int []";
        } else if (n instanceof Identifier) {
            type = getId((Identifier) n);
        }
        return type;
    }

}