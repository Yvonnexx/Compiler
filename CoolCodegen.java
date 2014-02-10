//package cool;

import java.io.*;
import java.util.*;
import beaver.Symbol;
import beaver.Scanner;
import beaver.Parser;

import ast.*;

public class CoolCodegen{
	CoolChecker checker;
	int outputType;
	FileWriter fstream;
	BufferedWriter out;

	// Constructor, take a done checker to take advantage of the work finished in type checking phase.
	public CoolCodegen(CoolChecker c, int t){
		this.checker = c;
		this.outputType = t;
		// Create output file 
		if(t == 1){
			try{
				fstream = new FileWriter("cool.ll");
				out = new BufferedWriter(fstream);
			}catch (Exception e){//Catch exception if any
				System.err.println("File Creating Error: " + e.getMessage());
			}
		}
	}

	public void generateCode(){

		// 0. generate initial code for LLVM
		genInit();

		// 1. generate class definitions
		// DONE
		genClassesDef();

		// 2. generate methods definitions
		// TODO: method definitions
//		genMethodsDef();

		// 3. generate methods implementations
//		genMethodsImp();

	}

	private void genInit(){
	}

	private void genClassesDef(){
		Set set = this.checker.classes.entrySet();
		Iterator i = set.iterator();
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			CoolChecker.CheckerClass c = (CoolChecker.CheckerClass)me.getValue();

			String classname = ((String)((CoolClass)c.node).sb.value);

			// Class definition
			output("%class_"+classname+" = type {");
			genClassMethods(c,1);
			genClassAttributes(c,1);
			output("}");
			
			// Class allocation
			output("@"+classname+" = global %class_"+classname+" {");
			genClassMethods(c,2);
			genClassAttributes(c,2);
			output("}");

			genClassMethods(c,3);
		}


	}

	private void genClassMethods(CoolChecker.CheckerClass c, int t){
			Iterator mi = c.methods.values().iterator();
			while(mi.hasNext()){
				CoolChecker.CheckerMethod m = (CoolChecker.CheckerMethod)(mi.next());
				String argv = getMethodArguments(m);
				if(t==1){
					output("\t%class_"+m.type+" ("+argv+")*,");
				} else if (t==2) {
					output("\t%class_"+m.type+" ("+argv+")* @"+m.name+",");
				} else if (t==3) {
					output("define %"+m.type+" @"+m.name+"("+argv+"){");
					genMethodImp(m, c);
					output("}");
				}
			}
	}
	
	private String getMethodArguments(CoolChecker.CheckerMethod m){
		String arguments = new String();
		for(int i=0;i<m.arguments.size();i++){
			arguments += m.arguments.get(i).type;
			if(i<m.arguments.size()-1)
				arguments += ", ";
		}
		return arguments;
	}

	private void genClassAttributes(CoolChecker.CheckerClass c, int t){
		Iterator ai = c.attributes.values().iterator();
		while(ai.hasNext()){
			CoolChecker.CheckerAttribute a = (CoolChecker.CheckerAttribute)(ai.next());
			if(t==1){
				output("\t%class_"+a.type+" ,");
			} else if (t==2) {
				output("\t%class_"+a.type+" @"+a.id+" ,");
			}
		}

	}

	private void genMethodsImp(CoolChecker.CheckerMethod m, CoolChecker.CheckerClass c){
		// Each method in grammar is either:
		//	1 or 2 type of "feature" node.
		// Refer to CoolChecker
		genCode(m.root,c);
	}

	private void genCode(CoolNode n, CoolChecker.CheckerClass c){
		// generate actual code
//		CoolChecker.CheckerClass tmp;
		genChildren(n, c);
		switch(n.nodeType){
			case 27:
				break;

			// 31 - Sum
			case 31:
				if(n.num==1){
					// add
					/*
					1. use global variable CurrentRegister to define a new register store the add result.
						%sum = add i32 %xval, i32 %delta
					2. TODO: should save the register number for left and right non-terminal, during the walk.
						e.g. %xval should be %13 and %delta should be %14. These numbers should be also set by the global counter.
					3. i32 is an example type, could use other type.
					*/
				}
				break;
		}
	}

	private void genChildren(CoolNode n, CoolChecker.CheckerClass c){
		if(n.a!=null)
			genCode(n.a,c);
		if(n.b!=null)
			genCode(n.b,c);
		if(n.c!=null)
			genCode(n.c,c);
	}

	private void output(String str){
		switch(this.outputType){
			case 0:
				System.out.println(str);
				break;
			case 1:
				try{
					out.write(str);
					out.newLine();
					//out.close();
				}catch (Exception e){//Catch exception if any
					System.err.println("File Writing Error: " + e.getMessage());
				}
				break;
		}
	}
}
