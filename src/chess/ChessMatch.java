package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
	
	private Board board;
	private int turn;
	private Color currentPlayer;
	boolean check;
	
	private List<Piece> piecesOnTheBoard;
	private List<Piece> capturedPieces;
	
	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		piecesOnTheBoard = new ArrayList<>();
		capturedPieces = new ArrayList<>();
		check = false;
		
		initialSetup();
	}
	
	public boolean getCheck() {
		return this.check;
	}
	
	public int getTurn() {
		return turn;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}

	public ChessPiece[][] getPieces() {
		
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		
		for (int i=0; i<board.getRows() ;i++) {
			for(int j=0; j<board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}
	public boolean[][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
		
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("Voce nao pode se colocar em check");
		}
		if(testCheck(opponent(currentPlayer))) {
			check = true;
		}
		nextTurn();
		return (ChessPiece) capturedPiece;
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);
		board.PlacePiece(p, target);
		
		if (capturedPiece != null) {
			this.piecesOnTheBoard.remove(capturedPiece);
			this.capturedPieces.add(capturedPiece);
		}
		return capturedPiece;
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		Piece p = board.removePiece(target);
		this.board.PlacePiece(p, source);
		if(capturedPiece != null) {
			this.board.PlacePiece(capturedPiece, target);
			this.capturedPieces.remove(capturedPiece);
			this.piecesOnTheBoard.add(capturedPiece);
		}
		
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.thereIsAPiece(position)) {
			throw new ChessException("Nao tem peca nessa posicao");
		}
		if(currentPlayer != ((ChessPiece)this.board.piece(position)).getColor()) {
			throw new ChessException("A peca escolhida nao e sua");
		}
		if(!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("Nao tem movimentos possiveis para a peca escolhida");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target)) {
			throw new ChessException("A peca escolhida nao pode se mover para a posicao de destino");
		}
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.PlacePiece(piece, new ChessPosition(column, row).toPosition());
		this.piecesOnTheBoard.add(piece);
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? color.BLACK : color.WHITE;
	}
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			if(p instanceof King) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("Nao existe o rei de cor " + color + " no tabuleiro");
	}
	
	private boolean testCheck (Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces =  piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for(Piece p: opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if(mat[kingPosition.getRow()][kingPosition.getCollumn()]) {
				return true;
			}
		}
		return false;
	}
	
	private void initialSetup() {
			placeNewPiece('c', 1, new Rook(board, Color.WHITE));
	        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
	        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
	        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
	        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
	        placeNewPiece('d', 1, new King(board, Color.WHITE));

	        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
	        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
	        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
	        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
	        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
	        placeNewPiece('d', 8, new King(board, Color.BLACK));
	}
}
