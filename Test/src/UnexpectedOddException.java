
//Because of where I put this in the object hierarchy, exceptions of
//this type will be "checked".  Hover over the keyword "Exception" below
//for more info.
public class UnexpectedOddException extends RuntimeException {

    public UnexpectedOddException(String message) {
        super(message);
    }

}
