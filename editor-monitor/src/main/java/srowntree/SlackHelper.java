package srowntree;

import com.slack.api.Slack;
import com.slack.api.SlackConfig;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.model.Conversation;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;

@ApplicationScoped
public class SlackHelper {

    static String mAuthToken = "xoxb-1786487673910-2903619591762-iVvP11yKgkryH01CXZFss30X";

    static String findConversation(String name) {
        var client = Slack.getInstance().methods();
        try {
            var result = client.conversationsList(r -> r
                    .token(mAuthToken)
            );
            for (Conversation channel : result.getChannels()) {
                if (channel.getName().equals(name)) {
                    var conversationId = channel.getId();
                    return conversationId;
                }
            }
        } catch (IOException | SlackApiException ignored) {
        }
        return "0";
    }

    public static void publishMessage(String text) {
        var client = Slack.getInstance().methods();
        try {
            client.chatPostMessage(r -> r
                    .token(mAuthToken)
                    .channel(findConversation("general"))
                    .text(text)
            );
        } catch (IOException | SlackApiException e) {
            System.out.println(e);
        }
    }

}

