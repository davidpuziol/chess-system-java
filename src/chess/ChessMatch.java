package chess;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;
import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn; // vai guardar o numero do turno, ou seja, numero de jogadas totais ate o momento
	private Color currentPlayer;
	private Board board;
	//Nao iniciadas no construtor pois por padrao ja comecam com false
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;
	
	//Lista de pecas sempre serao iniciadas
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	
	//O construtor ja vai iniciar a partida criando um tabuleiro, Colocando as pecas e iniciando o jogador branco para comecar
	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public ChessPiece getEnPassantVulnerable() {
		return this.enPassantVulnerable;
	}
	
	//Metodo que retorna as pecas nas posicoes do tabuleiro
	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i=0; i<board.getRows(); i++) {
			for (int j=0; j<board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}
	
	//Vai pegar os movimentos possivel de uma determinada do xadrez 
	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		//Pega a posicao real da matriz
		Position position = sourcePosition.toPosition();
		//Veja se eh uma posicao valida
		validateSourcePosition(position);
		//retorna a matriz
		return board.piece(position).possibleMoves();
	}
	
	//Vai executar um movimento
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		//Converte para as posicoes reais na matriz
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		//Valida as posicoes
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		//Faz o movimento
		Piece capturedPiece = makeMove(source, target);
		
		//Testa o check, se tiver em check desfaz a jogada pois ele era obrigado a mover o rei.
		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("Voce nao pode se colocar em check");
		}
		ChessPiece movedPiece	= (ChessPiece)board.piece(target);	
		
		//Promocao da peca
		promoted = null;
		//Se a peca movida for peao e se for branca na linha 0 que chegou no fim ou preta na linha 7 que tb chegou no fim vai promover
		if (movedPiece instanceof Pawn) {
			if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0)
					|| (movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
				promoted = (ChessPiece) board.piece(target);
				promoted = replacePromotedPiece("Q");
			}
		}

		//Se a jogada gerou um check seta a variavel
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		//Testa se eh check mate passando a cor do adversario
		if (testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}
		else { //se a partida nao terminou entao incrementa o turno
			nextTurn();
		}
		//Testa se o peao moveu duas casas para testar o enpassant
		if(movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
			enPassantVulnerable = movedPiece;
		}else {
			enPassantVulnerable = null;
		}
		//o retorno vai devolver a peca ou null se nao tiver peca
		return (ChessPiece) capturedPiece;
	}
	
	//Vai receber uma letra correspondendo uma peca que pode ser substituida
	public ChessPiece replacePromotedPiece(String type) {
		if (promoted == null) {
			throw new InputMismatchException("Nao tem peca para ser promovida");
		}
		if (!type.equals("B") 
				&& !type.equals("N") 
				&& !type.equals("R") 
				&& !type.equals("Q")) {
			throw new InputMismatchException("Como a entrada foi invalida foi promovido uma rainha");
		}

		//Pega a posicao da peca a ser promovida
		Position pos = promoted.getChessPosition().toPosition();
		Piece p = board.removePiece(pos); //tira a peca que vai ser promovida
		piecesOnTheBoard.remove(p); // tira a peca do tabuleiro

		//chama o metodo que vai devolver a peca sem estar na posicao e depois seta a posicao e adiciona na lista de pecas do tabuleiro
		ChessPiece newPiece = newPiece(type, promoted.getColor());
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);

		return newPiece;
	}

	//vai criar a peca de acordo com o tipo
	private ChessPiece newPiece(String type, Color color) {
		if (type.equals("B")) return new Bishop(board, color);
		if (type.equals("N")) return new Knight(board, color);
		if (type.equals("Q")) return new Queen(board, color);
		return new Rook(board, color);
	}
	
	//Metodo que executa um movimento no tabuleiro
	private Piece makeMove(Position source, Position target) {
		//Variavel que vai remover a peca da jogada do tabuleiro
		ChessPiece p = (ChessPiece)board.removePiece(source);
		p.increaseMoveCount();
		//Variavel que vai pegar a peca caputada se existir na posicao de destino
		Piece capturedPiece = board.removePiece(target);
		//Vai colocar noa posicao destino
		board.placePiece(p, target);
		
		//Se tinha peca na posicao entao remove da lista de pecas do tabuleiro e coloca nas pecas capturadas
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		//Roque pequeno se for jogada do rei
		if(p instanceof King && target.getColumn() == source.getColumn() +2 ) {
			Position sourceT = new Position(source.getRow(), source.getColumn()+3);
			Position targetT = new Position(source.getRow(), source.getColumn()+1);
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
			this.board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		//Roque grande se for jogada do rei
		if(p instanceof King && target.getColumn() == source.getColumn() -2) {
			Position sourceT = new Position(source.getRow(), source.getColumn()-4);
			Position targetT = new Position(source.getRow(), source.getColumn()-1);
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
			this.board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		
		//EnPassant
		if(p instanceof Pawn) {
			//significa que andou na diagonal
			if(source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position pawnPosition;
				if(p.getColor() == Color.WHITE) {
					pawnPosition = new Position(target.getRow() +1, target.getColumn());
				}else { //vai ser preta
					pawnPosition = new Position(target.getRow() -1, target.getColumn());
				}
				//remove a peca da lista de pecas do tabuleiro e adiciona na lista de pecas capturadas
				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}
		return capturedPiece;
	}
	
	//Metodo que desfaz o movimento feito
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);
		
		//Desfaz o movimento 				*********
		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
		
		//Roque pequeno se for jogada do rei
		if(p instanceof King && target.getColumn() == source.getColumn() +2 ) {
			Position sourceT = new Position(source.getRow(), source.getColumn()+3);
			Position targetT = new Position(source.getRow(), source.getColumn()+1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			this.board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		//Roque grande caso seja jogada do rei
		if(p instanceof King && target.getColumn() == source.getColumn() -2) {
			Position sourceT = new Position(source.getRow(), source.getColumn()-4);
			Position targetT = new Position(source.getRow(), source.getColumn()-1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			this.board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}	
		
		//EnPassant
		//Na hora de desfazer a peca voltou para o lugar errado de onde tem um ********* entao aqui volta pro lugar
		if (p instanceof Pawn) {
			// significa que andou na diagonal
			if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
				ChessPiece pawn = (ChessPiece)this.board.removePiece(target);
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) {
					pawnPosition = new Position(3, target.getColumn());
				} else { // vai ser preta
					pawnPosition = new Position(4, target.getColumn());
				}
				//Coloca a peca de volta
				this.board.placePiece(pawn, pawnPosition);
			}
		}
	}
	
	//Metodo que testa se a posicao de entrada da peca eh valida
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("Nao existe peca na posicao escolhida");
		}
		if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessException("A peca escolhida nao eh sua");
		}
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("Nao existe movimentos possiveis para a peca escolhida");
		}
	}
	
	//Metodo que valida se a posicao de destino da peca eh possivel como uma jogada
	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("A peca escolhida nao pode ser movida para a posicao de destino");
		}
	}
	
	//Metodo para mudar de turno e pegar o jogador do momento
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	//Metodo que vai retornar a cor do adversario passando a cor do jogador 
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	//Metodo que vai varrer a lista de pecas no tabuleiro procurando o rei da cor passada como parametro e retornando
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece)p;
			}
		}
		//Se nao encontrou o rei na lista algo esta errado pode parar o jogo
		throw new IllegalStateException("Nao existe REI " + color + " no tabuleiro");
	}
	
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		//Cria uma lista das pecas do oponente baseada na lista de pecas no tabuleiro
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		
		//vai testar as posibilidade de jogada de cada peca
		for (Piece p : opponentPieces) {	
			//Pega a matriz setada com as posibilidades para aquela pesa do momento
			boolean[][] mat = p.possibleMoves();
			//Testa se a posicao do rei esta setada como possivel se tiver retorna true
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		//Se chegou aqui eh pq nao tem possibilidade
		return false;
	}
	
	//Metodo que testa se o rei da cor informada esta ou nao em checkmate
	private boolean testCheckMate(Color color) {
		//Se nao estiver em check nao tem como ser checkmate
		if (!testCheck(color)) {
			return false;
		}
		//vai pegar a lista de pecas do jogador
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		
		//vai testar o movimento de cada peca se algum movimento tira o check
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			//vai checar toda a matriz de possibilidade de jogada para a peca em questao
			for (int i=0; i<board.getRows(); i++) {
				for (int j=0; j<board.getColumns(); j++) {
					if (mat[i][j]) {
						//Vai pegar a peca e mover para a posicao e testar o check e desmover para nao comprometer o jogo
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						//variavel auxiliar para ser checada depois que desmover a jogada
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		//se nao conseguiu um movimento para tirar o check entao eh checkmate
		return true;
	}	
	
	//Coloca uma peca nova no tabuleiro passando a coluna e linha que ela sera criada pelas regras do xadrez e converte
	//para ter a coluna e linha de uma matriz
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		//Coloca a peca no tabuleiro na posicao de matriz
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		//Adiciona na lista de pecas do tabuleiro
		piecesOnTheBoard.add(piece);
	}
	
	public ChessPiece getPromoted() {
		return promoted;
	}
	
	private void initialSetup() {
		
		//Coloca as pecas no tabuleiro para iniciar a partida
		
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        //O rei em especial precisa da partida para executar jogadas especias como roque
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        //O rei em especial precisa da partida para executar jogadas especias como roque
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
	}
}