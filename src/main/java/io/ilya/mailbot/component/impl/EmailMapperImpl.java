package io.ilya.mailbot.component.impl;

import io.ilya.mailbot.component.EmailMapper;
import org.springframework.stereotype.Component;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;

@Component
public class EmailMapperImpl implements EmailMapper {

    private static final String PATTERN = "From name: %s\n" +
            "From address: %s\n" +
            "Subject: %s\n" +
            "Date: %s\n" +
            "Text: %s\n";

    @Override
    public String mapEmailToPlainText(Message email) throws MessagingException, IOException {
        InternetAddress fromAddress = (InternetAddress) email.getFrom()[0];
        String fromMail = fromAddress.getAddress();
        String fromName = fromAddress.getPersonal();
        String subject = email.getSubject();
        String date = email.getReceivedDate().toString();
        String text = getTextFromEmail(email);
        return String.format(PATTERN, fromName, fromMail, subject, date, text);
    }

    private String getTextFromEmail(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append("\n").append(bodyPart.getContent());
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result.append("\n").append(org.jsoup.Jsoup.parse(html).text());
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }
}
