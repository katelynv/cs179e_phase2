parser: minijava.jj.txt jtb.jar
		java -jar jtb.jar minijava.jj.txt
		javacc jtb.out.jj

J2V: J2V.java	
		javac J2V.java
		java J2V.java < P.java > P.vapor

vapor: vapor.jar p.vapor
		java -jar vapor.jar run p.vapor

tester: J2V.java
		mkdir hw2
		cp ClassSymbol.java ErrorMsg.java FirstPassVisitor.java FunctionSymbol.java J2V.java PrintOutput.java SecondPassVisitor.java Symbol.java SymbolTable.java Translation.java VisitorFunctions.java hw2/
		tar zcf hw2.tgz hw2
		rm -rf hw2
		chmod u+x Phase2Tester/run
		Phase2Tester/run Phase2Tester/SelfTestCases hw2.tgz

vapor: vapor.jar p.vapor
		java -jar vapor.jar run p.vapor

clean folder:
	rm -rf *.class
	rm -rf *.tgz
	rm -rf *.vapor

test1:
		java J2V.java < Phase2Tester/SelfTestCases/1-PrintLiteral.java > PrintLiteral.vapor
		java -jar vapor.jar run PrintLiteral.vapor

test2:
		java J2V.java < Phase2Tester/SelfTestCases/2-Add.java > Add.vapor
		java -jar vapor.jar run Add.vapor

test3:
		java J2V.java < Phase2Tester/SelfTestCases/3-Call.java > Call.vapor
		java -jar vapor.jar run Call.vapor

test4:
		java J2V.java < Phase2Tester/SelfTestCases/4-Vars.java > Vars.vapor
		java -jar vapor.jar run Vars.vapor

test5:
		java J2V.java < Phase2Tester/SelfTestCases/5-OutOfBounds.java > OutOfBounds.vapor
		java -jar vapor.jar run OutOfBounds.vapor

test6:
		java J2V.java < Phase2Tester/SelfTestCases/BinaryTree.java > BinaryTree.vapor
		java -jar vapor.jar run BinaryTree.vapor

test7:
		java J2V.java < Phase2Tester/SelfTestCases/BubbleSort.java > BubbleSort.vapor
		java -jar vapor.jar run BubbleSort.vapor

test8:
		java J2V.java < Phase2Tester/SelfTestCases/Factorial.java > Factorial.vapor
		java -jar vapor.jar run Factorial.vapor

test9:
		java J2V.java < Phase2Tester/SelfTestCases/LinearSearch.java > LinearSearch.vapor
		java -jar vapor.jar run LinearSearch.vapor

test10:
		java J2V.java < Phase2Tester/SelfTestCases/LinkedList.java > LinkedList.vapor
		java -jar vapor.jar run LinkedList.vapor

test11:
		java J2V.java < Phase2Tester/SelfTestCases/MoreThan4.java > MoreThan4.vapor
		java -jar vapor.jar run MoreThan4.vapor

test12:
		java J2V.java < Phase2Tester/SelfTestCases/QuickSort.java > QuickSort.vapor
		java -jar vapor.jar run QuickSort.vapor

test13:
		java J2V.java < Phase2Tester/SelfTestCases/TreeVisitor.java > TreeVisitor.vapor
		java -jar vapor.jar run TreeVisitor.vapor
