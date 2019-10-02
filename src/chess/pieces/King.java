package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {

	public King(Board board, Color color) {
		super(board, color);
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
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[this.getBoard().getRows()][this.getBoard().getColumns()];
		Position p = new Position(0,0);
		
		//acima
		p.setValues(position.getRow()-1, position.getCollumn());
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getCollumn()] = true;
		}
		
		//abaixo
		p.setValues(position.getRow()+1, position.getCollumn());
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getCollumn()] = true;
		}
		
		//esquerda
		p.setValues(position.getRow(), position.getCollumn()-1);
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getCollumn()] = true;
		}
			
		//direita
		p.setValues(position.getRow(), position.getCollumn()+1);
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getCollumn()] = true;
		}
		
		//noroeste
		p.setValues(position.getRow()-1, position.getCollumn()-1);
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getCollumn()] = true;
		}
		
		//nordeste
		p.setValues(position.getRow()-1, position.getCollumn()+1);
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getCollumn()] = true;
		}
		
		//sudoeste
		p.setValues(position.getRow()+1, position.getCollumn()-1);
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getCollumn()] = true;
		}
		
		//sudeste
		p.setValues(position.getRow()+1, position.getCollumn()+1);
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getCollumn()] = true;
		}
		
		return mat;
	}
}
