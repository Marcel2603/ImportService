package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class TestController {

    @RequestMapping(value= "/test/{id}", method = POST, consumes = "multipart/form-data")
    public ResponseEntity<String> posttest(@PathVariable("id") String id, @RequestParam(value = "file") MultipartFile file) {
        long start = System.currentTimeMillis();
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(file.getBytes());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipInputStream zis = new ZipInputStream(bais);
            ZipEntry entry;
            int read = 0;
            byte[] buffer = new byte[2048];
            System.out.println("Data size: " + file.getSize() + " Bytes");
            while ((entry = zis.getNextEntry()) != null) {
                while ((read = zis.read(buffer, 0, buffer.length)) > 0) {
                    baos.write(buffer, 0, read);
                }
                String content = baos.toString();

                System.out.printf("File: %s, valid: %s \n \n",entry.getName(),validateXMLSchema("/schema2.xsd",content));
                baos.reset();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("Time needed: " + (end-start));
        return new ResponseEntity<>("Time needed: " + (end-start), HttpStatus.ACCEPTED);
    }

    private String validateXMLSchema(String xsdPath, String xmlPath){

        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(TestController.class.getResource(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xmlPath)));
        } catch (IOException | SAXException e) {
            //System.out.println("Exception: "+e.getMessage());
            return "false"; // + e.getMessage();
        }
        return "true";
    }
}
