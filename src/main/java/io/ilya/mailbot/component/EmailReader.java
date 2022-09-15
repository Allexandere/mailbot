package io.ilya.mailbot.component;

import javax.mail.Message;
import java.util.List;

public interface EmailReader {
    List<Message> getNewEmails();
}
