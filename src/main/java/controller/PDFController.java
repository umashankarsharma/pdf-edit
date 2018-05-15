package controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import model.ImageScale;

@RestController
public class PDFController {

	// @PostMapping("/merge-pdf-content")
	@RequestMapping(path = "/merge-pdf-content", method = RequestMethod.POST)
	public String mergePDFImage(@RequestPart("pdf") MultipartFile pdf, @RequestPart("image") MultipartFile image, RedirectAttributes redirectAttributes) {

		InputStream pdfInputStream;
		InputStream imageInputStream;
		String base64 = null;

		int x = Integer.parseInt("60");
		int y = Integer.parseInt("70");
		float scale = Float.parseFloat("0.18");
		System.out.println("Hello There");
		// int x = Integer.parseInt(scaleImage.getX());
		// int y = Integer.parseInt(scaleImage.getY());
		// float scale = Float.parseFloat(scaleImage.getScale());

		try {
			pdfInputStream = pdf.getInputStream();

			imageInputStream = image.getInputStream();

			byte[] b1 = PDFController.readFully(imageInputStream);
			// store output file as stream
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			PDDocument doc = PDDocument.load(pdfInputStream);
			PDPage page = doc.getPage(0);
			PDImageXObject pdImage = PDImageXObject.createFromByteArray(doc, b1, null);
			// creating the PDPageContentStream object
			PDPageContentStream contents = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND,
					false);
			contents.drawImage(pdImage, x, y, pdImage.getWidth() * scale, pdImage.getHeight() * scale);
			// Closing the PDPageContentStream object
			contents.close();
			// Saving the document
			doc.save(baos);
			// Closing the document
			doc.close();

			base64 = Base64.getEncoder().encodeToString(baos.toByteArray());

		} catch (IOException e) {
			return e.getMessage();
		}

		return base64;
	}

	@RequestMapping(value = "/merge-pdf-content")
	public ResponseEntity<ImageScale> get() {

		ImageScale imageScale = new ImageScale("A", "B", "C");

		return new ResponseEntity<ImageScale>(imageScale, HttpStatus.OK);
	}

	public static byte[] readFully(InputStream input) throws IOException {
		byte[] buffer = new byte[8192];
		int bytesRead;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
		return output.toByteArray();
	}
}
