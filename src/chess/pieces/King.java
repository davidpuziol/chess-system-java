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

	// metodo privado para uso somente de apoio da classe afim de saber se pode ou
	// nao mover a posicao caso ela exista
	// e seja do adversario
	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece) this.getBoard().piece(position);
		if (p == null || p.getColor() != this.getColor()) {
			return true;
		} else {
			return false;
		}
	}

	// teste se eh possivel fazer o roque que eh uma jogada especial do xadrez
	private boolean testRookCastling(Position position) {
		// Pega uma peca que seria passada como referencia, nesse caso para fazer o
		// roque eh obrigado que fosse a torre
		// Se for uma torre, que nao moveu ainda, e for a torre do jogador, e ela existe
		// entao retorne que eh possivel fazer
		// Caso contrario volta false
		ChessPiece p = (ChessPiece) this.getBoard().piece(position);
		return p != null && p instanceof Rook && p.getColor() == this.getColor() && p.getMoveCount() == 0;
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[this.getBoard().getRows()][this.getBoard().getColumns()];
		Position p = new Position(0, 0);

		// O rei sempre anda uma posicao em todos os lados, entao são 8 possiveis
		// movimentos
		// Os ifs são igual mudando so a posicao da coluna e linha

		// acima
		p.setValues(position.getRow() - 1, position.getColumn());
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// abaixo
		p.setValues(position.getRow() + 1, position.getColumn());
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// esquerda
		p.setValues(position.getRow(), position.getColumn() - 1);
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// direita
		p.setValues(position.getRow(), position.getColumn() + 1);
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// noroeste
		p.setValues(position.getRow() - 1, position.getColumn() - 1);
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// nordeste
		p.setValues(position.getRow() - 1, position.getColumn() + 1);
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// sudoeste
		p.setValues(position.getRow() + 1, position.getColumn() - 1);
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// sudeste
		p.setValues(position.getRow() + 1, position.getColumn() + 1);
		if (this.getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// Jogada especial de roque
		//Se nao moveu ainda o rei e nao esta em cheque testa
		if (this.getMoveCount() == 0 && !this.chessMatch.getCheck()) {
			// Roque pequeno testa a torre do lado direito		
			Position posT1 = new Position(this.position.getRow(), this.position.getColumn() + 3);
			if (testRookCastling(posT1)) {
				//testa se as duas casas entre o rei e a torre nao tem peca
				Position p1 = new Position(this.position.getRow(), this.position.getColumn() + 1);
				Position p2 = new Position(this.position.getRow(), this.position.getColumn() + 2);
				if (this.getBoard().piece(p1) == null && this.getBoard().piece(p2) == null) {
					mat[position.getRow()][position.getColumn() + 2] = true;
				}
			}

			// Roque grande testa a torre do lado esquerdo
			Position posT2 = new Position(this.position.getRow(), this.position.getColumn() - 4);
			if (testRookCastling(posT2)) {
				//testa se as tres casas entre o rei e a torre nao tem peca
				Position p1 = new Position(this.position.getRow(), this.position.getColumn() - 1);
				Position p2 = new Position(this.position.getRow(), this.position.getColumn() - 2);
				Position p3 = new Position(this.position.getRow(), this.position.getColumn() - 3);
				if (this.getBoard().piece(p1) == null && this.getBoard().piece(p2) == null
						&& this.getBoard().piece(p3) == null) {
					mat[position.getRow()][position.getColumn() - 2] = true;
				}
			}
		}
		return mat;
	}
}
