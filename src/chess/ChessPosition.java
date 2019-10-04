package chess;

import boardgame.Position;

public class ChessPosition {
	private char column;
	private int row;
	
	//O construtor checa se a posicao de entrada eh valida
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
	
	//Metodo para converter a posicao do jogo de xadrez para a posicao real da matriz
	protected Position toPosition() {
		return new Position((8-row), (column - 'a'));
	}
	//Metodo para converter a real da matriz para a posicao do jogo de xadrez
	protected static ChessPosition fromPosition(Position pos) {
		return new ChessPosition((char)('a' + pos.getColumn()), 8 - pos.getRow());
	}
	
	@Override
	public String toString() {
		return "" + column + row;
	}
}
