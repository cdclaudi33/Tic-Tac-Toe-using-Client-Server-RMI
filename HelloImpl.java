package pace;

import java.rmi.*;
import java.rmi.server.*;

public class HelloImpl extends UnicastRemoteObject implements HelloInterface {
	char[] curBoard = new char[9];
	
	
	public HelloImpl() throws RemoteException {
	      super( );
	   }
	
	public char[] board(char[] a) throws RemoteException {
		//this is the method we call from the client program
		//board is passed into this class
		int moveRes;
		this.curBoard = a;
		this.printBoard();
		moveRes = this.serverMove();
		//checks if move was successful, loops until a successful move is made
		while(moveRes == 0) {
			moveRes =this.serverMove();
		}
		//serverMove takes place, then check if the game has been won
		if(this.checkWinner() != '0') {
			System.out.println("Winner is O");
			//this is marker for the client to check if a winner has been declared
			curBoard[2] = 'Z';
		}
		//board is passed back to caller
		return this.curBoard;
	
	}   

   private void printBoard() {
	   for(int i = 0; i < 9; i++) {
		   if (i != 0 && i % 3 == 0) { System.out.println(); }
		   System.out.print(this.curBoard[i] + " ");
		   
	   }
	   System.out.println();
   }
   
   
   //prompts for user input, checks for validity of cell entered
 //returns 0 for unsuccessful move, 1 for successful placement
   private int serverMove() {
	   System.out.print("Enter a cell number: ");
	   try {
		   java.util.Scanner in = new java.util.Scanner(System.in);
		   int move = in.nextInt();
		   if(move < 0 || move > 8) {
			   System.out.println("Cell number invalid.");
			   return 0;
		   }
		   if(curBoard[move] != 'X' && curBoard[move] != 'O') {
			   curBoard[move] = 'O';
			   return 1;
		   }
		   else {
			   System.out.println("This spot is taken already.");
			   return 0;
		   } 
	   }
	   catch (Exception e){
		   System.out.println("Enter a valid number not a character!");
	   }
	return 0;
	   
	   
   }
   
	 //checkWinner returns a 0 character when there is no winner on the board yet
	 //otherwise returns the character of the winner
	 private char checkWinner() {
		 char[] a = this.curBoard;
		 //checking the rows for three in a row
		 for(int i = 0; i < 7; i+=3) {
			 for(int j = i+1, t=0; t<2; t++, j++) {
				 if(a[j] != a[j-1]) break;
				 if(t == 1) return a[j];
			 }
		 }
		 //checking the columns for three in a row
		 for(int i = 0; i < 3; i++) {
			 for(int j = i + 3, t=0; t < 2; t++, j += 3) {
				 if(a[j] != a[j-3]) break;
				 if(t == 1) return a[j];
			 }
		 }
		 //checking a diagonal
		 for(int i = 4; i < 9; i += 4) {
			 if(a[i] != a[i-4]) break;
			 if(i == 8) return a[i];
		 }
		 //checking the other diagonal
		 for(int i = 4; i < 7; i += 2) {
			 if(a[i] != a[i-2]) break;
			 if(i == 6) return a[i];
		 }
		return '0';
		 
	 }
   
}

