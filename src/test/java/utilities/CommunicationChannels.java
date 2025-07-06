package utilities;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class CommunicationChannels {

    public static void sendReportViaMail(String toEmail, String subject, String body, String reportPath) {

        Properties mailProps = CommonFunctions.readPropertiesFile("mailer");

        if(mailProps.getProperty("sendMail").equalsIgnoreCase("true")){
            final String fromEmail = mailProps.getProperty("mail.sender");
            final String password = mailProps.getProperty("mail.password");

            Properties props = CommonFunctions.readPropertiesFile("mailer");

            Authenticator auth = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            };

            Session session = Session.getInstance(props, auth);

            try {
                MimeMessage message = new MimeMessage(session);

                message.setFrom(new InternetAddress(fromEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                message.setSubject(subject);

                // Body + Attachment
                Multipart multipart = new MimeMultipart();

                // Text Body Part
                MimeBodyPart textBodyPart = new MimeBodyPart();
                textBodyPart.setContent(body, "text/html; charset=utf-8");

                // Attachment Body Part
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                File file = new File(reportPath);
                if (!file.exists()) {
                    throw new RuntimeException("Report file not found at: " + reportPath);
                }
                attachmentBodyPart.attachFile(file);

                // Combine parts
                multipart.addBodyPart(textBodyPart);
                multipart.addBodyPart(attachmentBodyPart);

                message.setContent(multipart);

                // Send Email
                Transport.send(message);
                System.out.println("Report emailed successfully!");

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to send email report");
            }
        }
    }

    public static void sendToTeams(String webhookUrl, String messageText) {
        Properties msProps = CommonFunctions.readPropertiesFile("microsoft");

        if(msProps.getProperty("sendReport").equalsIgnoreCase("true")){
            try {
                URL url = new URL(msProps.getProperty("webhookurl"));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set headers
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");

                // JSON payload
                String payload = "{ \"text\": \"" + messageText.replace("\"", "\\\"") + "\" }";

                // Send request
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = payload.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    System.out.println("Message sent to Teams successfully.");
                } else {
                    System.out.println("Failed to send message. Response code: " + responseCode);
                }

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to send message to Teams");
            }
        }
    }
}
