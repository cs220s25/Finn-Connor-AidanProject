package exceptions;

public class GameNotStartedException extends RuntimeException {
    public GameNotStartedException(String message) {
        super("Game not started");
    }
}
