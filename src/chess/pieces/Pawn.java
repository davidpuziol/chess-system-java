package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

	private ChessMatch chessMatch;
	
	public Pawn(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0, 0);
		
		//Testa se o peao eh branco ou preto, pois o branco so anda pra cimae o preto so pra baixo

		if (getColor() == Color.WHITE) {
			//Testa se pode andar 1 pra frente
			p.setValues(position.getRow() - 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			//Testa se pode andar 2 pra frente caso seja a 1 jogada.
			//Nao pode ter peca na frente nem depois
			p.setValues(position.getRow() - 2, position.getColumn());
			Position p2 = new Position(position.getRow() - 1, position.getColumn());
			if (getBoard().positionExists(p) 
					&& !getBoard().thereIsAPiece(p) 
					&& getBoard().positionExists(p2) 
					&& !getBoard().thereIsAPiece(p2) 
					&& getMoveCount() == 0) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			//Como o peao so come em diagonal ele testa se tem um adversaio na diagonal dele, nas duas diagonais
			p.setValues(position.getRow() - 1, position.getColumn() - 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}			
			p.setValues(position.getRow() - 1, position.getColumn() + 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}	
			
			//Enpassant para os brancos
			//A posicao 3 da matriz é quando o peao preto moveu duas pecas entao ele tem que esta ali
			if(this.position.getRow() == 3){
				//teste para direita
				Position left = new Position(position.getRow(), position.getColumn()-1);
				if (this.getBoard().positionExists(left) 
						&& isThereOpponentPiece(left) 
						&& this.getBoard().piece(left) == this.chessMatch.getEnPassantVulnerable()) {
					//-1 pq o peao branco anda pra cima
						mat[left.getRow()-1][left.getColumn()] = true;
				}
				//teste para a esquerca
				Position right = new Position(position.getRow(), position.getColumn()+1);
				if (this.getBoard().positionExists(right) 
						&& isThereOpponentPiece(right) 
						&& this.getBoard().piece(right) == this.chessMatch.getEnPassantVulnerable()) {
						mat[right.getRow()-1][right.getColumn()] = true;
				}
			}
		}
		//Mesmo codigo para peca preta 
		else {
			p.setValues(position.getRow() + 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow() + 2, position.getColumn());
			Position p2 = new Position(position.getRow() + 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow() + 1, position.getColumn() - 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}			
			p.setValues(position.getRow() + 1, position.getColumn() + 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}	
			
			//Enpassant para os pretos
			//A posicao 4 da matriz é quando o peao branco moveu duas pecas entao ele tem que esta ali
			if(this.position.getRow() == 4){
				//teste para direita
				Position left = new Position(position.getRow(), position.getColumn()-1);
				if (this.getBoard().positionExists(left) 
						&& isThereOpponentPiece(left) 
						&& this.getBoard().piece(left) == this.chessMatch.getEnPassantVulnerable()) {
					//+1 pq o peao preto anda pra baixo
						mat[left.getRow()+1][left.getColumn()] = true;
				}
				//teste para a esquerca
				Position right = new Position(position.getRow(), position.getColumn()+1);
				if (this.getBoard().positionExists(right) 
						&& isThereOpponentPiece(right) 
						&& this.getBoard().piece(right) == this.chessMatch.getEnPassantVulnerable()) {
						mat[right.getRow()+1][right.getColumn()] = true;
				}
			}
		}
		return mat;
	}
	
	@Override
	public String toString() {
		return "P";
	}
}