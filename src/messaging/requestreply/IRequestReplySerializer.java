package messaging.requestreply;

/**
 *
 * @author Maja Pesic
 */
public interface IRequestReplySerializer<REQUEST, REPLY> {

    public REQUEST requestFromString(String str);

    public REPLY replyFromString(String str);

    public String requestToString(REQUEST request);

    public String replyToString(REPLY reply);
}
