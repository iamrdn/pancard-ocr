package com.ocr.service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OcrService {

    private static final String PAN_CARD_REGEX = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

    public String extractPanCardNumber(MultipartFile file) throws IOException {
        File convFile = convertMultipartFileToFile(file);
        String result = performOCR(convFile);
        return validateAndExtractPanNumber(result);
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        Files.write(convFile.toPath(), file.getBytes());
        return convFile;
    }

    private String performOCR(File file) {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("tessdata"); // Set the path to the tessdata directory
        tesseract.setLanguage("eng");

        try {
            return tesseract.doOCR(file);
        } catch (TesseractException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String validateAndExtractPanNumber(String ocrText) {
        Pattern pattern = Pattern.compile(PAN_CARD_REGEX);
        Matcher matcher = pattern.matcher(ocrText);
        if (matcher.find()) {
            return matcher.group();
        } else {
            throw new IllegalArgumentException("PAN card number not found or invalid");
        }
    }
}
