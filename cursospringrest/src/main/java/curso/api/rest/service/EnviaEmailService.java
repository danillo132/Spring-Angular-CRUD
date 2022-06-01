package curso.api.rest.service;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EnviaEmailService {

	private String userName = "javaevida13@gmail.com";
	private String senha = "javaDeveloper123";
	
	public void enviarEmail(String assunto, String emailDestino, String msg) throws Exception {
		
		Properties properties = new Properties();
		properties.put("mail.smtp.ssl.trust", "*");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				
				return new javax.mail.PasswordAuthentication(userName, senha);
			}
			
		});
		
		Address[] toUser = InternetAddress.parse(emailDestino);
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(userName));
		message.setRecipients(Message.RecipientType.TO, toUser);
		message.setSubject(assunto);
		message.setText(msg);
		
		javax.mail.Transport.send(message);
	}
}
