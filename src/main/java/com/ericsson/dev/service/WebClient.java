package com.ericsson.dev.service;

import com.ericsson.dev.domain.Discounts;
import com.squareup.okhttp.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

public class WebClient {

    public String createRequest(Discounts discounts) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("request.xml").getFile());
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.parse(file);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        String output = writer.toString();
        return output;
    }

    public void submitRequest(String endpoint, Discounts discounts) {
        System.out.println("Executing Discount - ");
        OkHttpClient client = new OkHttpClient();
        String responseString;

        MediaType mediaType = MediaType.parse("text/xml;charset=UTF-8");
        RequestBody body;
        try {
            String output = createRequest(discounts);
            body = RequestBody.create(mediaType, output);

            Request request = new Request.Builder()
                .url(endpoint)
                .post(body)
                .addHeader("Content-Type", "text/xml;charset=UTF-8")
                .addHeader("cache-control", "no-cache")
                .build();

            client.setReadTimeout(2, TimeUnit.MINUTES);
            client.setConnectTimeout(2, TimeUnit.MINUTES);
            client.setWriteTimeout(2, TimeUnit.MINUTES);
            Response response = client.newCall(request).execute();
            responseString = response.body().string();
            if (responseString != null) {
                if (responseString.contains("<faultcode")) {
                    //error
                    System.out.printf(responseString);
                }

            } else {
                System.out.printf("No answer from service.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
