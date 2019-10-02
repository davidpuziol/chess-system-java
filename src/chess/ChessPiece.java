package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece {
	
	private Color color;

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(this.position);
	}
	
	protected boolean isThereOpponentPiece(Position pos) {
		ChessPiece p = (ChessPiece) this.getBoard().piece(pos);
		if (p != null && p.getColor() != color) {
			return true;
		}else {
			return false;
		}
	}
}
