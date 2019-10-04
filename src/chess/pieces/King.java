package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {
	
	private ChessMatch chessMatch;

	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}
	
	@Override
	public String toString() {
		return "K";
	}

	private boolean canMove (Position position) {
		ChessPiece p = (ChessPiece)this.getBoard().piece(position);
		if(p == null || p.getColor() != this.getColor()) {
			return true;
		}else {
			return false;
		}
	}
	
	private boolean testRookCastling (Position position) {
		ChessPiece p = (ChessPiece)this.getBoard().piece(position);
		return p != null && p instanceof Rook && p.getColor() == this.getColor() && p.getMoveCount() == 0;
	}
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[this.getBoard().getRows()][this.getBoard().getColumns()];
		Position p = new Position(0,0);
		
		//acima
		p.setValues(position.getRow()-1, position.getColumn());
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//abaixo
		p.setValues(position.getRow()+1, position.getColumn());
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//esquerda
		p.setValues(position.getRow(), position.getColumn()-1);
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
			
		//direita
		p.setValues(position.getRow(), position.getColumn()+1);
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//noroeste
		p.setValues(position.getRow()-1, position.getColumn()-1);
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//nordeste
		p.setValues(position.getRow()-1, position.getColumn()+1);
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//sudoeste
		p.setValues(position.getRow()+1, position.getColumn()-1);
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//sudeste
		p.setValues(position.getRow()+1, position.getColumn()+1);
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//Special Movie Castling
		if(this.getMoveCount() == 0 && !this.chessMatch.getCheck()) {
			//Rock lado direito
			Position posT1 = new Position(this.position.getRow(), this.position.getColumn()+3);
			if(testRookCastling(posT1)) {
				Position p1 = new Position(this.position.getRow(), this.position.getColumn()+1);
				Position p2 = new Position(this.position.getRow(), this.position.getColumn()+2);
				if(this.getBoard().piece(p1) == null && this.getBoard().piece(p2) == null) {
					mat[position.getRow()][position.getColumn()+2] = true;
				}
			}
			
			//Rock lado esquerda
			Position posT2 = new Position(this.position.getRow(), this.position.getColumn()-4);
			if(testRookCastling(posT2)) {
				Position p1 = new Position(this.position.getRow(), this.position.getColumn()-1);
				Position p2 = new Position(this.position.getRow(), this.position.getColumn()-2);
				Position p3 = new Position(this.position.getRow(), this.position.getColumn()-3);
				if(this.getBoard().piece(p1) == null 
						&& this.getBoard().piece(p2) == null 
						&& this.getBoard().piece(p3) == null) {
					mat[position.getRow()][position.getColumn()-2] = true;
				}
			}	
		}
		return mat;
	}
}
