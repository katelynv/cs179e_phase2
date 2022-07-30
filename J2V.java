import syntaxtree.*;
// I will change and import code for ClassSymbol.java VisitorFunctions.java FirstPassVisitor.java SecondPassVisitor.java PrintOutput.java and Translation.java

public class J2V {
    public static void main(String[] args) {
        try {
            MiniJavaParser parser = new MiniJavaParser(System.in);
            Node root = parser.Goal();
            FirstPassVisitor first = new FirstPassVisitor();
            root.accept(first);
            if (first.errors()) {
                System.exit(1);
            } else {
                SecondPassVisitor second = new SecondPassVisitor(first.getSymbolTable());
                root.accept(second, first.getSymbolTable());
                if (second.errors()) {
                    System.exit(1);
                } else {
                    Translation t = new Translation(first.getSymbolTable());
                    root.accept(t);
                }
            }
        } catch (ParseException e) {
            System.exit(1);
        }
    }
}