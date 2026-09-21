package practice.exception;

public class GraphException extends IllegalArgumentException {

    public GraphException() {}

    public GraphException(String message){
        super(message);
    }

}
