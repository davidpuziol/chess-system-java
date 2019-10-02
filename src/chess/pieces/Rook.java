package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece {

	public Rook(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "R";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[this.getBoard().getRows()][this.getBoard().getColumns()];
		
		Position p = new Position (0,0);
		
		//verificar acima
		p.setValues(this.position.getRow()-1, this.position.getCollumn());
		while(getBoard().positionExists(p) && !this.getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getCollumn()] = true;
			p.setRow(p.getRow()-1);
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getCollumn()] = true;
		}
		
		//para esquerda
		p.setValues(this.position.getRow(), this.position.getCollumn()-1);
		while(getBoard().positionExists(p) && !this.getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getCollumn()] = true;
			p.setCollumn(p.getCollumn()-1);
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getCollumn()] = true;
		}
		
		//para direita
		p.setValues(this.position.getRow(), this.position.getCollumn()+1);
		while(getBoard().positionExists(p) && !this.getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getCollumn()] = true;
			p.setCollumn(p.getCollumn()+1);
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getCollumn()] = true;
		}
		
		//para baixo
		p.setValues(this.position.getRow()+1, this.position.getCollumn());
		while(getBoard().positionExists(p) && !this.getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getCollumn()] = true;
			p.setRow(p.getRow()+1);
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getCollumn()] = true;
		}
		
		
		
		return mat;
	}
	
	
}
