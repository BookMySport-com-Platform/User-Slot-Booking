package com.bookmysport.userslotbooking.Ananda.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.bookmysport.userslotbooking.MiddleWares.GetSPDetailsMW;
import com.bookmysport.userslotbooking.Models.BookSlotSPModel;
import com.bookmysport.userslotbooking.Repository.BookSlotRepo;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class PdfEmailService {

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private GetSPDetailsMW getSPDetailsMW;

    @Autowired
    private BookSlotRepo bookSlotRepo;

    public void generatePdfAndSendEmail(String recipientEmail,String token,String role) throws Exception {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            export(System.getenv("PATH_TO_INDEXHTML"), outputStream,token,role);

            sendEmailWithAttachment(recipientEmail, outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void export(String htmlFilePath, ByteArrayOutputStream outputStream,String token,String role) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        HTMLWorker htmlWorker = new HTMLWorker(document);

        try (BufferedReader br = new BufferedReader(new FileReader(htmlFilePath))) {
            String line;
            StringBuilder htmlContent = new StringBuilder();
            while ((line = br.readLine()) != null) {
                htmlContent.append(line);
            }

            Map<String,Object> userDetails=getSPDetailsMW.getSPDetailsByToken(token, role).getBody();

            String userName = userDetails.get("userName").toString();
            String userId=userDetails.get("id").toString();
            List<BookSlotSPModel> totalPriceObject=bookSlotRepo.findByUserId(UUID.fromString(userId));

            String htmlWithDynamicValue = htmlContent.toString().replace("$username$", userName)
            .replace("$totalPrice$", String.valueOf(totalPriceObject.get(0).getPriceToBePaid()));

            htmlWorker.parse(new StringReader(htmlWithDynamicValue));          
        }

        document.close();
    }

    public void sendEmailWithAttachment(String recipientEmail, byte[] pdfContent) throws MessagingException, jakarta.mail.MessagingException {
        MimeMessage message=emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Set email details
        helper.setSubject("Court Booking");
        helper.setFrom(sender);
        helper.setTo(recipientEmail);
        helper.setText("Please find the attached PDF.");

        // Attach the PDF
        helper.addAttachment("Booking_Info.pdf", new ByteArrayResource(pdfContent));

        // Send the email
        emailSender.send(message);
    }
}
