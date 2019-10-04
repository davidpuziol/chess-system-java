package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
		//Classe para leitura do teclado
		Scanner sc = new Scanner(System.in);
		//Classe que vai cuidar da partida de xadrez
		ChessMatch chessMatch = new ChessMatch();
		//Lista de pecas capturadas
		List<ChessPiece> captured = new ArrayList<>();
		
		//Jogo so ira terminar enquanto nao tiver checkmate
		while(!chessMatch.getCheckMate()) {
			try {
				//Chama a classe que cuida do view enviado a partida e a lista de pecas capturadas
				UI.clearScreen();
				UI.printMatch(chessMatch, captured);
				//Inicianda uma jogada pegando uma posicao
				System.out.println();
				System.out.print("Source: ");
				//Pega a posicao do xadrez que vai de a1 a h8
				ChessPosition source = UI.readChessPosition(sc);
				
				//Matriz de movimentos possiveis com a peca escolhida passando a posicao da peca
				boolean[][] possibleMoves = chessMatch.possibleMoves(source);
				
				///Imprime o jogo com os possiveis movimentos
				UI.clearScreen();
				UI.printBoard(chessMatch.getPieces(), possibleMoves);
					
				//Pega o proximo movimento
				System.out.println();
				System.out.print("Target: ");
				ChessPosition target = UI.readChessPosition(sc);
				
				//Faz o movimento da peca de origem para o destino
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
				
				//Se o movimento tirar uma peca do tabuleiro coloca na lista de peca capturada
				if (capturedPiece != null) {
					captured.add(capturedPiece);
				}
			} //Pega excessao lancada caso alguma coisa saia fora dos conformes 
			catch(ChessException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		//Imprime a partida e o vencedor
		UI.clearScreen();
		UI.printMatch(chessMatch, captured);
	}	
}