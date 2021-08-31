package nfe;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import util.PDFHelper;

public class NfeHandler {

  private static final String CLIENT_PREFIX = ": ";

  private NfeHandler() {
  }

  public static void extractTextFromPdf(String saveDirectory, List<PDDocument> pages,
      List<String> clients) throws IOException, TesseractException {
    int pagesCount = 0;
    int count;

    for (PDDocument page : pages) {
      String payer = getClientName(page);
      clients.add(payer);
      count = (int) clients.stream().filter(p -> p.equals(payer)).count();

      PDFHelper.savePdf(page, saveDirectory, payer, count, "NFe - ");

      page.close();

      pagesCount++;
    }

    System.out.println("Clientes encontrados: " + pagesCount);
  }

  private static String getClientName(PDDocument page) throws TesseractException, IOException {
    return splitAndReplace(
        PDFHelper.getPdfLines(extractTextFromScannedDocument(page)));
  }

  private static String splitAndReplace(String[] lines) {
    int linesLength = lines.length;
    int tomadorLine = 11;

    for (int i = 0; i < linesLength; i++) {
      if (lines[i].contains("TOMADOR DE SERVIÇOS")) {
        tomadorLine = i;
        break;
      }
    }

    for (int i = tomadorLine; i < linesLength; i++) {
      if (lines[i].contains("Razão Social:") || lines[i].contains("Razão Socia:")) {
        return lines[i].split(CLIENT_PREFIX)[1].replaceAll("[|/?]", "").trim();
      }
    }

    return "Cliente não encontrado";
  }

  private static String extractTextFromScannedDocument(PDDocument document)
      throws IOException, TesseractException {

    // Extract images from file
    PDFRenderer pdfRenderer = new PDFRenderer(document);
    StringBuilder out = new StringBuilder();

    ITesseract tesseract = new Tesseract();
    tesseract.setDatapath("tessdata");
    tesseract.setLanguage("por");
    tesseract.setTessVariable("user_defined_dpi", "70");

    BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);

    // Create a temp image file
    File tempFile = File.createTempFile("tempfile_", ".png");
    ImageIO.write(bufferedImage, "png", tempFile);

    String result = tesseract.doOCR(tempFile);
    out.append(result);

    // Delete temp file
    tempFile.delete();

    return out.toString();
  }
}
