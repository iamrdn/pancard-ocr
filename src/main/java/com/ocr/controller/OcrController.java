package com.ocr.controller;

import com.ocr.service.OcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/ocr")
public class OcrController {

	@Autowired
	private OcrService ocrService;

	@PostMapping("/extract-pan")
	public ResponseEntity<String> extractPanCardNumber(@RequestParam("file") MultipartFile file) {
		try {
			String panCardNumber = ocrService.extractPanCardNumber(file);
			return ResponseEntity.ok(panCardNumber);
		} catch (IOException e) {
			return ResponseEntity.status(500).body("Error processing the file");
		}
	}
}
