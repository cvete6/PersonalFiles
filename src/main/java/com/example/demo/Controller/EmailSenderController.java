package com.example.demo.Controller;

import com.example.demo.Service.EmailSenderServiceImpl.EmailSenderServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;

/**
 * Controller for creating and manipulate with email message
 */
@Controller
public class EmailSenderController {

    private EmailSenderServiceImpl emailService;

    public EmailSenderController(EmailSenderServiceImpl emailService) {
        this.emailService = emailService;
    }

    /**
     * Show view where user can insert email address
     *
     * @return view with email input field
     */
    @GetMapping("/email")
    public String showFormForInsertEmail(){
        return "insertEmail";
    }

    /**
     * Send auto-generated email message to address
     *
     * @param email address that we want to send auto-generated message
     * @param model if email is successful send
     * @return view to confirm that message is send to the input email address
     * @throws MessagingException
     */
    @PostMapping("/send")
    public String sendMail(@ModelAttribute("email") String email, Model model) throws MessagingException {
        emailService.sendMessageWithAttachment(email);
        model.addAttribute("successfulSendMail", true );
        return "insertEmail";
    }
}
