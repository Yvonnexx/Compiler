//package cool;

import java.io.*;
import java.util.*;
import beaver.Symbol;
import beaver.Scanner;

public class CoolError{
	static int errorNum = 0;
	static public void errorReport(){
		System.err.printf("ERROR OCCURS!\n");
		System.exit(1);
	}
	static public void errorReport(String reason, String errtext){
		System.err.printf("ERROR %s : %s\n",reason,errtext);
		if ( errorNum == 20 ) {
			System.err.println("MAXIMUM ERROR REACHED.");
			System.exit(1);
		} else {
			errorNum++;
		}
	}
	static public void errorReport(String reason ){
		System.err.printf("ERROR : %s\n",reason);
		if ( errorNum == 20 ) {
			System.err.println("");
			System.err.println("MAXIMUM 20 ERROR REACHED.");
			System.exit(1);
		} else {
			errorNum++;
		}
	}
}
