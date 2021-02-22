package pace;

import java.rmi.*;
//import java.rmi.server.RMIClassLoaderSpi;

public class HelloClient {

	public static void main(String[] args) {
		char[] a = {'0', '1', '2', '3', '4', '5', '6', '7', '8'};
		boolean gameOver = false;
		int moveCount = 0, moveRes;
		while(gameOver == false) {
			//a[2] set to the Z character is a flag from the server that server has won and the game is over
			if(a[2] == 'Z') return;
			//all the cells have been taken, the game is a stalemate
			if(moveCount == 9) {
				System.out.println("No winner");
				return;
			}
			
			//no winner thus far, increment the move counter for client and server
			moveCount += 2;
			printBoard(a);
			moveRes = clientMove(a);
			//checks if move was successful, loops until a successful move is made
			while(moveRes == 0) {
				moveRes = clientMove(a);
			}
		    //clientMove takes place, then check if the game has been won
		    if(checkWinner(a) != '0') {
		    	System.out.println("Winner is X");
		    	gameOver = true;
		    }
		    //if game has not been won, remote call made to server to prompt for move
		    if(gameOver == false) {
				try{
					int port = 16790;         
				    String host = "localhost";
				    String registryURL = "rmi://" + host + ":" + port + "/hello";  
				    HelloInterface h = (HelloInterface)Naming.lookup(registryURL);
				    a = h.board(a);
				 } 
				 catch (Exception e){
				    e.printStackTrace();
				 } 
		    }
		}
	}
	
	
	
	private static void printBoard(char[] board) {
		   for(int i = 0; i < 9; i++) {
			   if (i != 0 && i % 3 == 0) { System.out.println(); }
			   System.out.print(board[i] + " ");
		   }
		   System.out.println();
	   }
	
	//prompts for user input, checks for validity of cell entered
	//returns 0 for unsuccessful move, 1 for successful placement
	 private static int clientMove(char[] board) {
		   System.out.print("Enter a cell number: ");
		   try {
		   java.util.Scanner in = new java.util.Scanner(System.in);
		  
			   int move = in.nextInt();
		   
		   
			   if(move < 0 || move > 8) {
				   System.out.println("Cell number invalid.");
				   return 0;
			   }
			   if(board[move] != 'X' && board[move] != 'O') {
				   board[move] = 'X';
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
	 private static char checkWinner(char[] a) {
		 
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
		 //if execution has reached here all possibilities for 3 in a row have been checked
		 //no current winner
		return '0';
		 
	 }
	
}
