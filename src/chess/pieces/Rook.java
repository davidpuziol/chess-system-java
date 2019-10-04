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

	//Reescrever o metodo possiveis movimentos para a torre
	@Override
	public boolean[][] possibleMoves() {
		//cria uma matriz de possiveis movimentos usando o tabuleiro como tamanho
		//e iniciado todas as posicoes como false por padrao do java
		boolean[][] mat = new boolean[this.getBoard().getRows()][this.getBoard().getColumns()];
		
		//cria uma posicao qualquer para ser auxiliar
		//A classe Rook e heranda de ChessPiece que tb e herenca de Piece, entao tem uma posicao e um tabuleiro
		Position p = new Position (0,0);
		
		//Quando se anda com a linha anda pra cima ou para baixo
		//Quando se anda com a coluna se anda para direita e para esquerda
		
		//verificar acima
		p.setValues(this.position.getRow()-1, this.position.getColumn());
		//Se a posicao existir e nao tiver peca seta true e anda e incrementa para a proxima rodada
		while(getBoard().positionExists(p) && !this.getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow()-1);
		}
		//Se existe a posicao e a peca e de um oponente entao pode mover tb 
		//Essa posicao p é a posicao que sobrou do while
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//Para esquerda
		//E igual a condicao de cima, mas variando a coluna -1 para ir para esquerda
		p.setValues(this.position.getRow(), this.position.getColumn()-1);
		while(getBoard().positionExists(p) && !this.getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn()-1);
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//Para direita
		//E igual a condicao de cima, mas variando a coluna +1 para ir para direita
		p.setValues(this.position.getRow(), this.position.getColumn()+1);
		while(getBoard().positionExists(p) && !this.getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn()+1);
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//Para baixo
		//E igual a condicao de cima, mas variando a coluna +1 para ir para baixo
		//O tabuleiro de xadrez e impresso de cabeca para baixo, para cima eh para baixo e para baixa eh para cima
		p.setValues(this.position.getRow()+1, this.position.getColumn());
		while(getBoard().positionExists(p) && !this.getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow()+1);
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		//retorna a matriz com as posicoes true setadas, as que nao foram setadas sao falses
		return mat;
	}
}
