package chess;

import boardgame.Position;

public class ChessPosition {
	private char column;
	private int row;
	
	public ChessPosition (char column, int row) {
		if(column < 'a' || column > 'h' || row < 1 || row > 8) {
			throw new ChessException("Error de posicao de xadrez. Valores validos sao de a1 ate h8 ");
		}
		this.row = row;
		this.column = column;
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}
	
	protected Position toPosition() {
		return new Position((8-row), (column - 'a'));
	}
	
	protected static ChessPosition fromPosition(Position pos) {
		return new ChessPosition((char)('a' + pos.getCollumn()), 8 - pos.getRow());
	}
	
	@Override
	public String toString() {
		return "" + column + row;
	}
}
