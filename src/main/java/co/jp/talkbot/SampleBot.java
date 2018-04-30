package co.jp.talkbot;

import co.jp.talkbot.Entity.ChatResponse;
import fastily.jwiki.core.Wiki;
import me.ramswaroop.jbot.core.slack.Bot;
import me.ramswaroop.jbot.core.slack.Controller;
import me.ramswaroop.jbot.core.slack.EventType;
import me.ramswaroop.jbot.core.slack.models.Event;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketSession;

@Component
public class SampleBot extends Bot {

    @Value("${slackBotToken}")
    private String slackToken;

    @Value("${slackBotUser}")
    private String slackBotUser;

    @Value("${slackWebhookUrl}")
    private String slackWebhookUrl;

    @Value("${wikiUrl}")
    private String wikiUrl;

    @Value("${talkApiUrl}")
    private String talkapi;

    @Value("${talkApiKey}")
    private String talkApiKey;

    @Override
    public String getSlackToken() {
        return slackToken;
    }

    @Override
    public Bot getSlackBot() {
        return this;
    }

    @Controller(events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveDM(WebSocketSession session, Event event) {
        String input = event.getText().replaceAll("\\<.*\\> ", "");
        Wiki wiki = new Wiki(wikiUrl);
        reply(wiki.exists(input) ? wiki.getTextExtract(input) : sendChatBotRequest(input), event.getChannelId());
    }

    private String sendChatBotRequest(String input) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, String> entity = new LinkedMultiValueMap<>();
            entity.add("apikey", talkApiKey);
            entity.add("query", input);
            ResponseEntity<ChatResponse> result = restTemplate.postForEntity(talkapi, entity, ChatResponse.class);
            return result.getBody().getResults().get(0).getReply();
        } catch (Exception e) {
            e.printStackTrace();
            return "I'm sorry. I don't know.";
        }
    }

    private void reply(String message, String channel) {
        SlackMessage slackMessage = new SlackMessage();
        slackMessage.setIcon(":dog2:");
        slackMessage.setChannel(channel);
        slackMessage.setText(message);
        SlackApi api = new SlackApi(slackWebhookUrl);
        api.call(slackMessage);
    }
}
