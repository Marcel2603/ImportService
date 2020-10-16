package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class TestController {

    @RequestMapping(value= "/test/{id}", method = POST, consumes = "multipart/form-data")
    public ResponseEntity<List<String>> posttest(@PathVariable("id") String id, @RequestParam(value = "file") MultipartFile file) {
        List<String> list = new ArrayList<>();
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(file.getBytes());
            ZipInputStream zis = new ZipInputStream(bais);
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                list.add(String.format("File: %s Size: %d \n",
                        ze.getName(), ze.getSize()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(list, HttpStatus.ACCEPTED);
    }
}
