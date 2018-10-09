
// Demonstrates the difference between "checked" and "unchecked"
// exceptions.
public class DemoCheckedAndUnchecked {

    public static void main(String[] args) {
        useMethods();
    }

    // This method might throw a checked exception.  Java requires that
    // it declare this fact.  If we don't, we get a compilation error.
    public static void method1(int i) throws UnexpectedNegativeException {
        System.out.println("method1");
        if (i < 0) {
            throw new UnexpectedNegativeException("You'd better deal with me.");
        }
    }

    // This method might throw an unchecked exception.  Java does NOT
    // require that it declare this fact.
    public static void method2(int i) {
        System.out.println("method2");
        if (i % 2 == 1) {
            throw new UnexpectedOddException("You can safely ignore me");
        }
    }

    public static void useMethods() {
        //method2(1);

        try {
            // We're calling a method that may throw a checked exception.
            // Java requires that we handle it in one of two ways:
            // either try-catch it, or declare that
            // we might pass it on to our own caller (by saying
            // "throws UnexpectedNegativeException") in our method header.
            // If we don't, we get a compilation error.
            // Notice that this is the case even though we can see that
            // this particular call will not throw the exception.
            // Java can't determine this at compile time.
            method1(-7);
            //method2(1);
            method1(7);

        } catch (UnexpectedNegativeException e) {
            System.out.println("Darn, we had an exception.");
            //e.printStackTrace();
        }
        finally {
            System.out.println("finally");
        }
        // Now we're calling a method that may throw an unchecked
        // exception. Java does NOT require that we handle it.
        // Notice that this is the case even though we can see that
        // this particular call will indeed throw an exception.  Java can't
        // determine at compile time whether it will or will not be thrown.
        // But it doesn't care even if it will. Unchecked exceptions do
        // not need to be handled.
        // Since the exception does occur in method2, and we do not
        // catch it, we throw it to our caller (the main method).
        // We don't have to declare that we might throw it -- it's an
        // unchecked exception. The main method, in turn, does not catch
        // the exception either, so it throws it (again without having to
        // declare that it might) and the exception is shown to the user.
//        try {
//            method2(11);
//        }
//        catch (UnexpectedOddException e){
//            System.out.println("odd ex");
//            e.printStackTrace();
//        }
//        finally {
//            System.out.println("finally");
//        }
    }
}
