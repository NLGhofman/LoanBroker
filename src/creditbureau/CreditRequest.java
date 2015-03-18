package creditbureau;

/**
 *
 * This class stores all information about a
 * request to get the credit history of a specific client.
 *
 */
public class CreditRequest {

    int SSN; // unique client nummber

    public CreditRequest(int SSN) {
        super();
        this.SSN = SSN;
    }

    public int getSSN() {
        return SSN;
    }

    public void setSSN(int SSN) {
        this.SSN = SSN;
    }

    @Override
    public String toString() {
        return "ssn=" + String.valueOf(SSN);
    }
}
