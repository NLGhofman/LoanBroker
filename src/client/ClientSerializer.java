package client;

import com.thoughtworks.xstream.XStream;

/**
 * This class serializes ClientReply and ClientRequest to and from XML.
 */
public class ClientSerializer {

    private static final String ALIAS_REQUEST = "ClientRequest"; // the tag name for ClientRequest
    private static final String ALIAS_REPLY = "ClientReply"; // the tag name for ClientReply
    private XStream xstream; // for easy XML serialization

    public ClientSerializer() {
        super();
        xstream = new XStream();
         // register aliases (tag names)
        xstream.alias(ALIAS_REQUEST, ClientRequest.class);
        xstream.alias(ALIAS_REPLY, ClientReply.class);
    }
    
    /**
     * This method parses a ClientRequest from an XML string.
     * @param str is the string containing the XML
     * @return the ClientRequest containng the same information like the given XML (str)
     */
    public ClientRequest requestFromString(String str) {
        return (ClientRequest) xstream.fromXML(str);
    }

    /**
     * This method parses a ClientReply from an XML string.
     * @param str is the string containing the XML
     * @return the ClientReply containng the same information like the given XML (str)
     */
    public ClientReply replyFromString(String str) {
        return (ClientReply) xstream.fromXML(str);
    }

    /**
     * Serializes a ClientRequest into XML string.
     * @param request is the ClientRequest to be serialized into XML
     * @return the string containing XML with information about the request
     */
    public String requestToString(ClientRequest request) {
        return xstream.toXML(request);
    }
    
    /**
     * Serializes a ClientReply into XML string.
     * @param reply is the ClientReply to be serialized into XML
     * @return the string containing XML with information about the rereply
     */
    public String replyToString(ClientReply reply) {
        return xstream.toXML(reply);
    }
}
