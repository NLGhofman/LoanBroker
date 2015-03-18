package messaging.requestreply;

/**
 *
 * @author Maja Pesic
 */
public interface IReplyListener<REQUEST,REPLY> {
    public void onReply(REQUEST request,REPLY reply);
}
