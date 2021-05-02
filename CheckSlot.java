import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Scanner;

public class CheckSlot {
    public static void main(String[] args) throws Exception {

        //URL district_id = new URL("https://cdn-api.co-vin.in/api/v2/admin/location/districts/21");
        //Mumbai - 395 thane - 392
        String mainUrl = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=XXX&date=";
        String districtID = args[0];
        int ageLimit = Integer.parseInt(args[1]);
        String emailId = args[2];
        String password = args[3];
        String toEmailId = args[4];

        int minAvailability = 1;
        StringBuilder message = new StringBuilder("");
        boolean flag = false;

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        //Date checkDate = df.parse("03-05-2021");
        Date checkDate = new Date();
        String modifiedURL = mainUrl.replace("XXX", districtID) + "" + df.format(checkDate);
        System.out.println(modifiedURL);
        URL url = new URL(modifiedURL);
        String oldMessage = "";
        while (true) {
            {

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                Scanner sc = new Scanner(url.openStream());
                StringBuilder input = new StringBuilder("");
                while (sc.hasNext()) {
                    input.append(sc.nextLine());
                }

                sc.close();
                JSONParser parse = new JSONParser();
                JSONObject jObj = (JSONObject) parse.parse(input.toString());
                JSONArray centers = (JSONArray) jObj.get("centers");
                Iterator<JSONObject> centerItr = centers.iterator();
                while (centerItr.hasNext()) {

                    JSONObject centerObj = centerItr.next();

                    JSONArray sessions = (JSONArray) centerObj.get("sessions");

                    Iterator<JSONObject> sessionItr = sessions.iterator();
                    while (sessionItr.hasNext()) {
                        JSONObject sessionObj = sessionItr.next();
                        long age = (Long) sessionObj.get("min_age_limit");
                        long availability = (Long) sessionObj.get("available_capacity");
                        if (availability >= minAvailability && age == ageLimit) {
                            flag = true;
                            message.append("\n");
                            String centerID = centerObj.get("center_id").toString();
                            String name = centerObj.get("name").toString();
                            String date = sessionObj.get("date").toString();
                            String pincode = centerObj.get("pincode").toString();
                            String blockName = centerObj.get("block_name").toString();
                            message.append("\n-----------------------------------------------------\n");
                            message.append(centerID + " : " + name + " --- " + date + "-- Slots - ");
                            message.append(sessionObj.get("available_capacity"));
                            message.append("\n" + pincode + " : " + blockName);
                            message.append("\n-----------------------------------------------------\n");
                        }

                    }
                }
                if (flag) {
                    if (!oldMessage.equals(message.toString())) {
                        sendEmail(message, emailId, password, toEmailId);
                    }
                    oldMessage = message.toString();
                    flag = false;
                    message = new StringBuilder("");
                    //System.exit(1);
                }
            }
            System.out.print(".");
            Thread.sleep(300000);
        }
    }

    private static void sendEmail(StringBuilder message, String emailId, String password, String toEmailId) throws MessagingException {

        final String[] receiver = toEmailId.split(","); //receiver's email address

        Properties pr = new Properties();

        pr.put("mail.smtp.auth", "true");    //for username and password authentication
        pr.put("mail.smtp.starttls.enable", "true");
        pr.put("mail.smtp.host", "smtp.gmail.com");  //here host is gmail.com
        pr.put("mail.smtp.port", "587");             //port no.

        Session gs = Session.getInstance(pr, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailId, password);  //pass your email id and password here

            }
        });
        for (String s : receiver) {
            messageContent(gs, emailId, s, message.toString());
            System.out.println("Sending Email from " + emailId + " to " + s);
        }

        System.out.println("Message sent! ");

    }

    private static Message messageContent(Session gs, String emailId, String reciever, String message) throws MessagingException {


        Message msg = new MimeMessage(gs);
        msg.setFrom(new InternetAddress(emailId));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(reciever));
        msg.setSubject("Slots for vaccine"); //to set the subject (not mandatory)
        msg.setText(message);
        Transport.send(msg);
        return msg;


    }
}

