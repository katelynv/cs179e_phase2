parser: minijava.jj.txt jtb.jar
		java -jar jtb.jar minijava.jj.txt
		javacc jtb.out.jj

J2V: J2V.java	
		javac J2V.java
		java J2V.java < P.java > P.vapor

vapor: vapor.jar p.vapor
		java -jar vapor.jar run p.vapor

test: J2V.java
		mkdir hw2
		cp J2V.java ClassSymbol.java ErrorMsg.java FirstPassVisitor.java FunctionSymbol.java Symbol.java SymbolTable.java VisitorFunctions.java SecondPassVisitor.java hw2/
		tar zcf hw2.tgz hw2
		rm -rf hw2
		chmod +x Phase2Tester/run
		Phase2Tester/run Phase2Tester/SelfTestCases hw2.tgz

vapor: vapor.jar p.vapor
		java -jar vapor.jar run p.vapor

clean folder:
	rm -rf *.class
	rm -rf *.tgz

