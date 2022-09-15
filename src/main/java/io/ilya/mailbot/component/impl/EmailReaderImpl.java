package io.ilya.mailbot.component.impl;

import com.sun.mail.imap.IMAPFolder;
import io.ilya.mailbot.component.EmailReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Component
@Slf4j
public class EmailReaderImpl implements EmailReader {

    @Value("${email.login}")
    private String LOGIN;
    @Value("${email.password}")
    private String PASSWORD;
    @Value("${email.domen}")
    private String DOMEN;
    private final static String DEBUG = "false";


    @Override
    public List<Message> getNewEmails() {
        try {
            Properties properties = getDefaultProperties();
            Session session = Session.getDefaultInstance(properties, null);
            Store store = session.getStore("imap");
            log.info(String.format("IMAP connection attempt. Login: %s  Password: %s  Domen: %s\n", LOGIN, PASSWORD, DOMEN));
            store.connect(DOMEN, LOGIN, PASSWORD);
            IMAPFolder inbox = (IMAPFolder) store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);
            return findUnreadEmails(inbox);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private Properties getDefaultProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", "imap");
        properties.setProperty("mail.debug", DEBUG);
        properties.setProperty("mail.imap.host", DOMEN);
        properties.setProperty("mail.imap.port", "993");
        properties.setProperty("mail.imap.ssl.enable", "true");
        return properties;
    }

    private List<Message> findUnreadEmails(IMAPFolder inbox) throws MessagingException {
        return Arrays.asList(inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false)));
    }
}
