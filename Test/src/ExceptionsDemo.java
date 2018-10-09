import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ExceptionsDemo {

    public static void main(String[] args) {
        try {
            // demoRuntimeExceptions();
            // demoPlainException();
            System.out.println(demoWeird());
        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }
    }

    private static void demoPlainException() {
        Scanner sc = null;
        try {
            sc = new Scanner(new File("Moogah.java"));
            return;
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } finally {
            System.out.println("Cleaning up");
            if (sc != null) {
                sc.close();
            }
        }
    }

    private static void demoRuntimeExceptions() {
        try {
            int[] myArray = new int[10];
            myArray[12] = -42;
            // throw new ArrayIndexOutOfBoundsException("What array?");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // Do something to either recover, or
            // give the user a chance to save their state.
            throw new ArithmeticException("I can't math today.");
        }
    }

    // Eww
    private static String demoWeird1() {
        try {
            return "Moogah";
        } finally {
            return "Frooble";
        }

    }

    private static String doesthiswork(ArithmeticException e) {
        return "huh";
    }

    private static String demoWeird() {
        try {
            Scanner sc = new Scanner(new File("Moogah.java"));
            return "try" + String.valueOf(3 / 0);
            // You can catch more than one exception type in a single catch block.
        } catch (ArithmeticException | FileNotFoundException aoe) {
//      doesthiswork(aoe); nope
            if (aoe instanceof ArithmeticException) {
                System.out.println("ArithmeticException");
            } else {
                System.out.println("FileNotFoundException");
            }
            // aoe.printStackTrace();
        } finally {
            // return "finally";
            System.out.println("finally");
        }

        System.out.println("after finally");
        return "after finally";

    }

}
