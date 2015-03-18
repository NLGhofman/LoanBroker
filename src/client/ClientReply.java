package client;

/**
 *
 * This class stores all information about a bank offer
 * as a response to a cleint loan request.
 */
public class ClientReply {

        private double interest; // the interest that the bank offers
        private String quoteID; // the unique quote identification

    public ClientReply( double interest, String quoteID) {
        super();
        this.interest = interest;
        this.quoteID = quoteID;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public String getQuoteID() {
        return quoteID;
    }

    public void setQuoteID(String quoteID) {
        this.quoteID = quoteID;
    }
    @Override
    public String toString(){
        return  " interest="+String.valueOf(interest) + " quoteID="+String.valueOf(quoteID);
    }
}
