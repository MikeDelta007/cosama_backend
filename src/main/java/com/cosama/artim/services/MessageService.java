package com.cosama.artim.services;

import com.cosama.artim.models.SingleMessageDetails;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import com.cosama.artim.models.SmsRecipient;
import java.util.List;

@Service
public class MessageService
{
    private static final Logger logger = LoggerFactory.getLogger(FretService.class);

    public Integer sendSms(String token, SingleMessageDetails singleMessageDetails) throws Exception{


        System.out.println(
                this.makeRecipientList(singleMessageDetails.getRecipients()));
        System.out.println("----------------------------------------");

        String documentJSON =
                "{"
                        + "\"messages\":["
                        +     "{"
                        +         "\"signature\":\"" + singleMessageDetails.getSignature() + "\","
                        +         "\"subject\":\"" + singleMessageDetails.getSubject() + "\","
                        +         "\"content\":\"" + singleMessageDetails.getContent() + "\","
                        +        "\"recipients\":[" + makeRecipientList(singleMessageDetails.getRecipients()) + "]"
                        +     "}"
                        + "]"
                        + "}";



        String key_private="243683525984b88b2f46e0faed9d1f2e";
//        String token="106a2c4c696db75fa775c0eb1643e017";
        String login = "cosama";
        String inputString = null;
        int responseCode = 0;
        String password = token;
        String authString = login + ":" + password;
        String authStringEnc = Base64.encodeBase64String(authString.getBytes());


        try {
            long  timestamp = System.currentTimeMillis()/1000;

            String msgToEncrypt=token+documentJSON+timestamp;
            String key=hmacSha(key_private, msgToEncrypt); // HMAC
            String URLAddress =
                    "https://api.orangesmspro.sn:8443/api/json?token="+token+"&key="+key+"&timestamp="+timestamp;

            /* Si vous preferez utiliser MD5
             *   String key = getMD5(msgToEncrypt + key_private);
             *  String URLAddress =
             *"https://api.orangesmspro.sn:8443/api/xml?token="+token+"&key="+key+"&timestamp="+timestamp+"&algo=md5";
             */
            URL url = new URL(URLAddress);

            try {

                // Get an HttpURLConnection subclass object instead of URLConnection
                HttpsURLConnection myHttpConnection = (HttpsURLConnection) url.openConnection();
                myHttpConnection.setRequestMethod("POST");
                myHttpConnection.setDoOutput(true);
                myHttpConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
                myHttpConnection.setRequestProperty("content-type", "application/json; charset=utf-8");

                // Output the results
                OutputStream output = myHttpConnection.getOutputStream();
                output.write(documentJSON.getBytes(StandardCharsets.UTF_8));
// output.write(queryParam.toString().getBytes("UTF-8"));
                output.flush();
// get the response-code from the response
                responseCode = myHttpConnection.getResponseCode();

                if (responseCode == 401) {

                    throw new RuntimeException("LOGIN ou TOKEN incorrect: Acces non autorise HTTP error code : "+ responseCode);
                }
                else if (responseCode == 400) {

                    throw new RuntimeException("Erreur 103: Acces non autorise HTTP error code : "+ responseCode);
                }
// open the contents of the URL as an inputStream and print to stdout

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        myHttpConnection.getInputStream()));

                while ((inputString = in.readLine()) != null) {

                    System.out.println(inputString);
                }

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return responseCode;
    }
    public static String hmacSha(String SECRETKEY, String VALUE) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(SECRETKEY.getBytes("UTF-8"), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(VALUE.getBytes("UTF-8"));
            byte[] hexArray = {
                    (byte)'0', (byte)'1', (byte)'2', (byte)'3',
                    (byte)'4', (byte)'5', (byte)'6', (byte)'7',
                    (byte)'8', (byte)'9', (byte)'a', (byte)'b',
                    (byte)'c', (byte)'d', (byte)'e', (byte)'f'
            };
            byte[] hexChars = new byte[rawHmac.length * 2];
            for ( int j = 0; j < rawHmac.length; j++ ) {
                int v = rawHmac[j] & 0xFF; hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
            return new String(hexChars);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }
    public static String getMD5(String input) {
        byte[] source;
        try {
            //Get byte according by specified coding.
            source = input.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            source = input.getBytes();
        }
        String result = null;

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); md.update(source);
            //The result should be one 128 integer
            byte temp[] = md.digest();
            char str[] = new char[16 * 2]; int k = 0;

            for (int i = 0; i < 16; i++) {
                byte byte0 = temp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; str[k++] = hexDigits[byte0 & 0xf];
            }
            result = new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public  String makeRecipientList(List<SmsRecipient> recipientList){

        StringBuilder recipientString = new StringBuilder();

        logger.info("recipientList: " + recipientList);

        recipientList.stream().forEach(recipient->{

            recipientString.append( "{\"id\":" + recipient.getId() + ",\"value\":\"" + recipient.getValue() + "\"},");
        });

        // Ici je supprime la virgule après le dernier élément

        if (!recipientString.isEmpty()) {
            recipientString.setLength(recipientString.length() - 1);
        }
        return recipientString.toString();
    }
}
