import java.io.*;
import java.util.*;

public class Symbol {
    private String name;

    private Symbol(String n) {
        name = n;
    }

    private static java.util.Dictionary dict = new java.util.Hashtable();

    public String toString() {
        return name;
    }

    public static Symbol symbol(String sym) {
        String symTemp = sym.intern();
        Symbol sVar = (Symbol) dict.get(symTemp);
        if (sVar == null) {
            sVar = new Symbol(symTemp);
            dict.put(symTemp, sVar);
        }
        return sVar;
    }
}