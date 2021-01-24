package com.example.demo.Service;


import com.example.demo.DomainModel.Person;

import javax.mail.MessagingException;
import java.util.List;

/**
 * Service for creating and manipulate with an email message, mail that is sent to new employees and
 * mail that is sent to workers in Human Capital Management that need to be notified that some passport
 * will not be valid for the next month
 */
public interface EmailSenderService {
    /**
     * Create email structure and send email
     *
     * @param email address that we will use to send auto-generated mail
     * @throws MessagingException if message not sent
     */
    void sendMessageWithAttachment(String email) throws MessagingException;

    /**
     * Create email structure for a message that need to inform for not valid passport and send that mail
     *
     * @param personList list of employees whose passport expires in one month
     * @throws MessagingException throw an exception if the message is not send
     */
    void sendNotificationMessageForInvalidPassport(List<? extends Person> personList) throws MessagingException;

    /**
     * Create email structure for a message that need to inform for not valid insurance card and send that mail
     *
     * @param personList list of employees whose insurance card expires for some period of time
     */
    void sendNotificationMessageForAlmostInvalidInsuranceCard(List<? extends Person> personList);


}
