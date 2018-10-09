
// Because of where I put this in the object hierarchy, exceptions of
// this type will be "checked".




public class UnexpectedNegativeException extends Exception {

    public UnexpectedNegativeException(String message) {
        super(message);
    }

}
