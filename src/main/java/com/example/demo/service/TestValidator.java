package com.example.demo.service;

import com.example.demo.controller.TestController;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class TestValidator {

    @Async
    public CompletableFuture<String> validateXMLSchema(String xsdPath, String xmlPath, String fileName){

        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(TestController.class.getResource(xsdPath));
            javax.xml.validation.Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xmlPath)));
        } catch (IOException | SAXException e) {
            //System.out.println("Exception: "+e.getMessage());
            System.out.println("Finish: false");
            return CompletableFuture.completedFuture(String.format("Status von %s ist %s",fileName,"false")); // + e.getMessage();
        }
        System.out.println("Finish: true");
        return CompletableFuture.completedFuture(String.format("Status von %s ist %s",fileName,"true")); // + e.getMessage();
    }
}
