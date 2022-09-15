package io.ilya.mailbot.scheduled;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import io.ilya.mailbot.component.EmailMapper;
import io.ilya.mailbot.component.EmailReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class EmailSender {

    @Autowired
    private EmailReader emailReader;
    @Autowired
    private EmailMapper emailMapper;

    @Value("${bot.group.access_token}")
    private String ACCESS_TOKEN;
    @Value("${bot.user.id}")
    private int USER_ID;
    @Value("${bot.group.id}")
    private int GROUP_ID;

    //Check and send new emails every 3 minutes
    private static final long FIXED_DELAY = 1000 * 60 * 3;
    private static final Random random = new Random();


    @Scheduled(fixedDelay = FIXED_DELAY)
    public void sendNewEmails() {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        GroupActor actor = new GroupActor(GROUP_ID, ACCESS_TOKEN);
        List<Message> newEmails = emailReader.getNewEmails();
        if (newEmails == null) {
            log.error("Won't send emails");
            return;
        }
        log.info("Found " + newEmails.size() + " email(s).");
        for (Message email : newEmails) {
            try {
                String emailInPlainText = emailMapper.mapEmailToPlainText(email);
                vk.messages().send(actor).message(emailInPlainText).userId(USER_ID).randomId(random.nextInt(1000)).execute();
            } catch (Exception e) {
                log.error("Cannot send or parse email");
                log.error(e.getMessage());
            }
        }
    }

}
