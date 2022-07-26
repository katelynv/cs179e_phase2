import syntaxtree.*;

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
                    
                    System.out.println("Program type checked successfully");
                }
            }
        } catch (ParseException e) {
            System.exit(1);
        }
    }
}