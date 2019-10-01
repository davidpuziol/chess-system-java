package boardgame;

public class Board {

	private int rows;
	private int columns;
	private Piece[][] pieces;
	
	public Board(int rows, int columns) {
		if(rows<1 || columns<1) {
			throw new BoardException("Error de criacao tabuleiro - "
					+ "Deve ser criado com pelo menos 1 linha e 1 coluna");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
	
	public Piece piece(int row, int column) {
		if (!positionExists(row,column)) {
			throw new BoardException("Essa posicao nao esta no tabuleiro");
		}
		return pieces[row][column];
	}
	
	public Piece piece(Position pos) {
		if (!positionExists(pos)) {
			throw new BoardException("Essa posicao nao esta no tabuleiro");
		}
		return pieces[pos.getRow()][pos.getCollumn()];
	}	
	
	public void PlacePiece(Piece piece, Position pos) {
		if (thereIsAPiece(pos)) {
			throw new BoardException("Ja existe uma peca na posicao "+ pos);
		}
		pieces[pos.getRow()][pos.getCollumn()] = piece;
		piece.position = pos;
	}
	
	public Piece removePiece(Position pos) {
		if (!positionExists(pos)) {
			throw new BoardException("Essa posicao nao esta no tabuleiro");
		}
		if (piece(pos) == null) {
			return null;
		}
		Piece aux = piece(pos);
		aux.position = null;
		pieces[pos.getRow()][pos.getCollumn()] = null;
		return aux;
	}
	
	public boolean positionExists(Position pos) {
		return positionExists(pos.getRow(), pos.getCollumn());	
	}
	
	public boolean positionExists(int row, int column) {
		return row >= 0 && row < rows && column >=0 && column < columns;
	}
	
	public boolean thereIsAPiece(Position pos) {
		if (!positionExists(pos)) {
			throw new BoardException("Essa posicao nao esta no tabuleiro");
		}
		return piece(pos) != null;
	}	
}
