package io.ilya.mailbot.component;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailMapper {
    String mapEmailToPlainText(Message email) throws MessagingException, IOException;
}
