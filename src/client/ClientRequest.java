package client;

/**
 *
 * This class stores all information about a
 * request that a client submits to get a loan.
 *
 */
public class ClientRequest {

    private int SSN; // unique client number.
    private int amount; // the ammount to borrow
    private int time; // the time-span of the loan

    public ClientRequest() {
        super();
        this.SSN = 0;
        this.amount = 0;
        this.time = 0;
    }

    public ClientRequest(int SSN, int amount, int time) {
        super();
        this.SSN = SSN;
        this.amount = amount;
        this.time = time;
    }

    public int getSSN() {
        return SSN;
    }

    public void setSSN(int SSN) {
        this.SSN = SSN;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ssn=" + String.valueOf(SSN) + " amount=" + String.valueOf(amount) + " time=" + String.valueOf(time);
    }
}
