package creditbureau;

/**
 * This class stores infomration about the credit history fr a specific client.
 */
public class CreditReply {

    private int SSN; // unique client number
    private int creditScore; // current credit ammount of the client
    private int history; // credit history of the client

    public CreditReply(int SSN, int creditScore, int history) {
        this.SSN = SSN;
        this.creditScore = creditScore;
        this.history = history;
    }

    public int getSSN() {
        return SSN;
    }

    public void setSSN(int SSN) {
        this.SSN = SSN;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public int getHistory() {
        return history;
    }

    public void setHistory(int history) {
        this.history = history;
    }

    @Override
    public String toString(){
        return "ssn="+String.valueOf(SSN) +" score="+String.valueOf(creditScore) + " history="+String.valueOf(history);
    }
}
