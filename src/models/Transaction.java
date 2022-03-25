package models;

public class Transaction {
    private final Person from;
    private final Person to;
    private double sum;

    public Transaction(Person from, Person to, double sum) {
        this.from = from;
        this.to = to;
        this.sum = sum;
    }

    public Person getFrom() {
        return from;
    }

    public Person getTo() {
        return to;
    }

    public double getSum() {
        return sum;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "from=" + from +
                ", to=" + to +
                ", sum=" + sum +
                "}\n";
    }
}
