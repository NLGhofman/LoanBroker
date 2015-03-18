package creditbureau;

import com.thoughtworks.xstream.XStream;

/**
 * This class serializes CreditReply and CreditRequest to and from XML.
 */
public class CreditSerializer{

    private static final String ALIAS_REQUEST = "CreditRequest"; // tag name for CreditRequest
    private static final String ALIAS_REPLY = "CreditReply"; // tag name for CreditReply
    private XStream xstream; // serializer to/from XML

    public CreditSerializer() {
        super();
        xstream = new XStream();
         // register aliases (i.e., tag names)
        xstream.alias(ALIAS_REQUEST, CreditRequest.class);
        xstream.alias(ALIAS_REPLY, CreditReply.class);
    }
    
    /**
     * This method parses a CreditRequest from an XML string.
     * @param str is the string containing the XML
     * @return the CreditRequest containng the same information like the given XML (str)
     */
    public CreditRequest requestFromString(String str) {
        return (CreditRequest)xstream.fromXML(str);
    }

    /**
     * This method parses a CreditReply from an XML string.
     * @param str is the string containing the XML
     * @return the CreditReply containng the same information like the given XML (str)
     */
    public CreditReply replyFromString(String str) {
        return (CreditReply) xstream.fromXML(str);
    }
    
    /**
     * Serializes a CreditRequest into XML string.
     * @param request is the CrditRequest to be serialized into XML
     * @return the string containing XML with information about the request
     */
    public String requestToString(CreditRequest request) {
        return xstream.toXML(request);
    }

    /**
     * Serializes a CreditReply into XML string.
     * @param reply is the CreditReply to be serialized into XML
     * @return the string containing XML with information about the rereply
     */
    public String replyToString(CreditReply reply) {
        return xstream.toXML(reply);
    }
}
