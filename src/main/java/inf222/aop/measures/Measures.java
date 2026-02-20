package inf222.aop.measures;

public class Measures {

    private double l_ft;
    private double w_in;
    private double h_cm;

    private double a_m;
    private double b_yd;

    public Measures() {
        l_ft = 5.0;
        w_in = 43.0;
        h_cm = 130.0;

        a_m = 0.7;
        b_yd = 1.6;
    }

    public void runExample() {
        System.out.print("Initial Values:\n");
        printValues();

        w_in += a_m * 2;
        h_cm -= 1;
        l_ft *= 1.05;
        a_m /= 2;
        b_yd += 1.5;
        // b_yd = -2; // illegal

        System.out.print("\nAfter First Operations:\n");
        printValues();

        double volume = l_ft * w_in * h_cm;
        double sum = l_ft + w_in + h_cm + a_m + b_yd;
        double average = sum / 5;
        double difference = l_ft - a_m;

        System.out.print("\nComputed Values:\n");
        System.out.print("Volume of box: " + volume + "\n");
        System.out.print("Sum of all distances: " + sum + "\n");
        System.out.print("Average value: " + average + "\n");
        System.out.print("Difference (ft - m): " + difference + "\n");
    }

    private void printValues() {
        System.out.print("l_ft: " + l_ft + "\n");
        System.out.print("w_in: " + w_in + "\n");
        System.out.print("h_cm: " + h_cm + "\n");
        System.out.print("a_m: " + a_m + "\n");
        System.out.print("b_yd: " + b_yd + "\n");
    }

    public void assignNegativeValue() {
        b_yd = -27;
    }

    public static void main(String[] args) {
        (new Measures()).runExample();
    }
    /*
    Initial Values:
    l_ft: 1.524
    w_in: 1.0922
    h_cm: 1.3
    a_m: 0.7
    b_yd: 1.4630400000000001

    After First Operations:
    l_ft: 1.6002
    w_in: 2.4922
    h_cm: 0.30000000000000004
    a_m: 0.35
    b_yd: 2.9630400000000003

    Computed Values:
    Volume of box: 1.1964055320000002
    Sum of all distances: 7.705439999999999
    Average value: 1.5410879999999998
    Difference (ft - m): 1.2502
    */
}
