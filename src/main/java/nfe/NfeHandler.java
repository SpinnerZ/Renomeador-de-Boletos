package nfe;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import util.PDFHandler;

public class NfeHandler {

  private static final int CLIENT_LINE = 18;
  private static final int CLIENT_LINE_SECOND = 12;
  private static final String CLIENT_PREFIX = ": ";

  private NfeHandler() {
  }

  public static void extractTextPdf(String saveDirectory, boolean userFolder,
      List<PDDocument> pages) throws IOException, TesseractException {
    List<String> clients = new ArrayList<>();
    int count;

    for (PDDocument page : pages) {
      String payer = getClientName(page);
      clients.add(payer);
      count = (int) clients.stream().filter(p -> p.equals(payer)).count();

      PDFHandler.savePdf(page, saveDirectory, userFolder, payer, count, "NFe - ");

      page.close();
    }
  }

  private static String getClientName(PDDocument page) throws TesseractException, IOException {
    try {
      return splitAndReplace(PDFHandler.getPdfLines(extractTextFromScannedDocument(page))[CLIENT_LINE]);
    } catch (ArrayIndexOutOfBoundsException e) {
      return splitAndReplace(PDFHandler.getPdfLines(extractTextFromScannedDocument(page))[CLIENT_LINE_SECOND]);
    }
  }

  private static String splitAndReplace(String line) {
    return line.split(CLIENT_PREFIX)[1].replaceAll("[|/?]", "").trim();
  }

  private static String extractTextFromScannedDocument(PDDocument document)
      throws IOException, TesseractException {

    // Extract images from file
    PDFRenderer pdfRenderer = new PDFRenderer(document);
    StringBuilder out = new StringBuilder();

    ITesseract _tesseract = new Tesseract();
    _tesseract.setDatapath("tessdata");
    _tesseract.setLanguage("por");
    _tesseract.setTessVariable("user_defined_dpi", "70");

    BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);

    // Create a temp image file
    File tempFile = File.createTempFile("tempfile_", ".png");
    ImageIO.write(bufferedImage, "png", tempFile);

    String result = _tesseract.doOCR(tempFile);
    out.append(result);

    // Delete temp file
    tempFile.delete();

    return out.toString();
  }
}
