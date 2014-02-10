//package cool;

import java.io.*;
import java.util.*;
import beaver.Symbol;
import beaver.Scanner;
import beaver.Parser;
//import CoolChecker;

import ast.*;

public class CoolDriver{
	public static void main (String[] args) throws IOException{
		File[] file;
		Vector<InputStream> inputStreams = new Vector<InputStream>();
		for(int i=0; i < args.length; i++){
			inputStreams.add(new FileInputStream(new File(args[i])));
		}
		Enumeration<InputStream> enu = inputStreams.elements();
		SequenceInputStream sis = new SequenceInputStream(enu);
		CoolScanner scanner = new CoolScanner (sis);
		/*
		   while (true) {
		   try{
		   Symbol t = scanner.nextToken();
		   if (t.getId() == Terminals.EOF) {
		   System.out.println ("EOF");
		   return;
		   }

		   System.out.printf ("%s\t\t%d\t%d\t%s\n",Terminals.NAMES[t.getId()],t.getStart(),t.getEnd(),t.value);
		   } catch (Exception e){

		   }
		   }
		 */

		//CoolParser parser = new CoolParser(scanner);
		Myjson printer = new Myjson();
		try
		{
			// Create AST Tree
			CoolNode node = (CoolNode) new CoolParser().parse(scanner);

			// Type Checking
			CoolChecker checker = new CoolChecker(node);
			checker.typeChecking();

			CoolCodegen gen = new CoolCodegen(checker, 0);
			gen.generateCode();

			/*
			   printer.printStartProgram();
			   node.accept();
			   printer.printEndProgram();
			 */

//			System.out.println("");
		}
		catch (IOException e)
		{
			System.out.println("IO ERROR!");
			System.err.println("Failed to read expression: " + e.getMessage());
		}
		catch (Parser.Exception e)
		{
			System.out.println("Parsing ERROR!");
			System.err.println("Invalid expression: " + e.getMessage());
		}
	}
}
