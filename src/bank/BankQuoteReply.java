package bank;

/**
 * This class stores information about the bank reply
 *  to a loan request of the specific client
 * 
 */
public class BankQuoteReply {

    private double interest; // the loan interest
    private String quoteId; // the nunique quote Id
    private int error; // code of the possible error (0 if no errors)

    public BankQuoteReply(double interest, String quoteId, int error) {
        this.interest = interest;
        this.quoteId = quoteId;
        this.error = error;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    @Override
    public String toString() {
        return "quote=" + this.quoteId + " interest=" + this.interest + " error=" + this.error;
    }
}
