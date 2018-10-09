import java.util.Observable;

public class MyProgram {
    public static void throwit() {
        throw new RuntimeException();
    }

    public static void main(String args[]) {
        try {
            int w = 5;
            int x = 5;
            Integer y = new Integer(5);
            Integer z = new Integer(5);
            System.out.println(w == x);
            System.out.println(w == y);
            System.out.println(y == z);
            System.out.println(y.equals(z));

            Integer i = new Integer(42);
            Integer j = new Integer(42);
            System.out.println((i == j) + " " + i.equals(j));
            System.out.println("Hello!");
            throwit();
            System.out.println("Done!");
        } finally {
            System.out.println("Finally!");
        }
        System.out.println("test");
    }
}

class Document {
    public static int quantity = 0;

    public Document() {
        System.out.println("d");
        quantity = quantity + 1;
    }

    public static int getQuantity() {
        return quantity;
    }
}

class Book extends Document {
    private int numChapters;

    public Book(int numChapters) {
        this.numChapters = numChapters;
    }

    public static void main(String[] args) {
        Book b1 = new Book(4);
        Book b2 = new Book(6);
        // Document d1 = new Document();
        System.out.println(Document.getQuantity());
    }
}

interface Circle {
    public double getArea();
}

class ThisCircle implements Circle {
    private double radius;

    public static double getArea(double r) {
        return Math.PI * r * r;
    }

    public double getArea() {
        return getArea(this.radius);
    }
}

abstract class Triangle {

    public static double getArea(double b,
                                 double h) {
        return b * h / 2;
    }
}

class IsoscelesTriangle extends Triangle {
    private double base, height;
}

interface ColouredCircle extends Circle {
//    ThisCircle c = (ThisCircle) new Circle();
}

class ParentClass {
    private int pVar;

    public ParentClass(int x) {
        pVar = x;
    }

    public void pMethod() {
    }
}

class ChildClass extends ParentClass{
    private int cVar;

    public ChildClass() {
        super(5);
    }

    public void cMethod(ParentClass p) {
        System.out.println(p);
    }
}

class Demo {
    private static ChildClass tunnelMethod(ParentClass c) {
        return (ChildClass)c;
    }

    public static void main(String[] args) {


        ChildClass child = new ChildClass();
        ParentClass parent = new ParentClass(5);
        ParentClass both = new ChildClass();
        (tunnelMethod(child)).cMethod(parent);
        //both.cMethod(child);

        tunnelMethod(parent);
    }
}

class BigClass {
    public int ivar = 20;
    private static int svar = 2;
    public BigClass() {
        svar++;
    }
    public static int getSvar() {
        System.out.println("BigClass method");
        return svar;
    }
    public void printArg(int x) throws WrongMethodException {
        System.out.println(x);
        LittleClass temp = new LittleClass();
        temp.printOtherArg(x, 3);
    }
        public String toString() {
            return "BigClass variables: " + ivar + " " + svar;
    }
        public static BigClass doLittle(BigClass be) {
            return be;
        }
        public LittleClass tunnelMethod(LittleClass lc) {
            return lc;
        }      public void noReturn(String s) {
            LittleClass temp = new LittleClass();
            temp.nextMethod(s);
    }
}

class LittleClass extends BigClass {
    public int ivar = 10;
    private static int svar = 1;
    public LittleClass(){}

    public static int getSvar(){
        System.out.println("LittleClass method");
        return svar;
    }
    public void printOtherArg(int x, int y) throws WrongMethodException {
        System.out.println(y);
        throw new WrongMethodException();
    }
    public BigClass tunnelMethod(BigClass lc) {
        return lc;
    }
    public String toString() {
        return "LittleClass Variables: " + svar + " " + ivar;
    }
    public void nextMethod(String s) {
        throw new NullPointerException();
    }
}

class WrongMethodException extends Exception {
}

class ExamDemo {
    public static void main(String[] args) throws Exception {
        System.out.println(BigClass.getSvar());
        BigClass objl = new BigClass();
        System.out.println(BigClass.getSvar());
        LittleClass obj2 = new LittleClass();
        System.out.println(BigClass.getSvar());
        System.out.println(objl.ivar);
        System.out.println(obj2.ivar);
        System.out.println(objl.toString());
        System.out.println(obj2.toString());

        LittleClass lcl= new LittleClass();
        System.out.println(BigClass.doLittle(lcl) + "yryd");

        LittleClass lc2 = new LittleClass();
        System.out.println(BigClass.doLittle(lc2.tunnelMethod(new BigClass())));

        BigClass bel = new BigClass();
        System.out.println(BigClass.doLittle((BigClass) bel.tunnelMethod(new LittleClass())));

        BigClass be8l = new BigClass();
       // System.out.println(BigClass.doLittle((Object) be8l.tunnelMethod(new LittleClass())));

        LittleClass lca2 = new LittleClass();
        System.out.println(BigClass.doLittle((LittleClass) lca2.tunnelMethod(new BigClass())));

        //BigClass bel = new BigClass();
        System.out.println(BigClass.doLittle(bel.tunnelMethod(new LittleClass())));


        try {
            objl.printArg(7);
        } catch (WrongMethodException e) {
        }
       // objl.noReturn(new String());
        int q = BigClass.getSvar();
        System.out.println("Done");
    }
}