package bank;

/**
 *
 * This class stores all information about an request from a bank to offer a
 * loan to a specific client.
 */
public class BankQuoteRequest {

    private int SSN; // unique client number
    private int credit; // the current credit of the client
    private int history; // the creit history of the client
    private int amount; // the requested loan amount
    private int time; // the requested loan period

    public BankQuoteRequest() {
        super();
        this.SSN = 0;
        this.credit = 0;
        this.history = 0;
        this.amount = 0;
        this.time = 0;
    }

    public BankQuoteRequest(int SSN, int credit, int history, int amount, int time) {
        super();
        this.SSN = SSN;
        this.credit = credit;
        this.history = history;
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

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getHistory() {
        return history;
    }

    public void setHistory(int history) {
        this.history = history;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return " ssn=" + this.SSN + " amount=" + amount + " time=" + time + " credit=" + credit + " history" + history;
    }
}
