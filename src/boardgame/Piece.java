package boardgame;

public abstract class Piece {

	protected Position position;
	private Board board;
	
	public Piece(Board board) {
		//A peca tem um tabuleiro vinculado a ela
		this.board = board;
		//A posicao da peca inicia como null pois quem vai iniciar a posicao da peca e a partida de xadrez
		position = null;
	}

	protected Board getBoard() {
		return board;
	}
	
	//Quem herdar de peca tem que implemntar esse metodo
	public abstract boolean[][] possibleMoves();
	
	
	public boolean possibleMove(Position position) {
		//chama o metodo possible moves que refere a cada tipo de peca e retorna o possivel movimento para o ponto informado
		return possibleMoves()[position.getRow()][position.getColumn()];
	}
	
	public boolean isThereAnyPossibleMove() {
		//Pega a matrix com o metodo
		boolean[][] mat = possibleMoves();
		//Roda a matrix procurando se tem algum movimento possivel
		for (int i=0; i<mat.length; i++) {
			for (int j=0; j<mat[i].length; j++) {
				if (mat[i][j]) {
					return true;
				}
			}
		}
		return false;
	}
}