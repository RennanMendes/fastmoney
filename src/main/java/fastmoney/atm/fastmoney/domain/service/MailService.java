package fastmoney.atm.fastmoney.domain.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import fastmoney.atm.fastmoney.domain.dto.mail.EmailDto;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MailService {

    public void depositEmail(EmailDto data) {
        composeEmail("Depósito recebido", "Depósito", data);
    }

    public void withdrawEmail(EmailDto data) {
        composeEmail("Saque efetuado com sucesso!", "Saque", data);
    }

    public void transferEmail(EmailDto data) {
        composeEmail("Transferência realizada com sucesso!", "Transferência", data);
    }

    private void composeEmail(String subject, String operation, EmailDto data) {
        Email from = new Email(System.getenv("FROM_EMAIL"));
        String message = String.format("Olá %s,\n\n%s no valor de R$: %s realizado com sucesso!", data.name(), operation, data.amount());
        Content content = new Content("text/plain", message);
        Mail mail = new Mail(from, subject, new Email(data.to()), content);
        sendEmail(mail);
    }

    public void sendEmail(Mail mail)  {
        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
        } catch (IOException ex) {
            throw new RuntimeException("Email não envidado");
//            throw ex;
        }
    }

}
