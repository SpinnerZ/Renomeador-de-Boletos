package util;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFHelper {

  private PDFHelper() {
  }

  public static List<PDDocument> splitPDF(PDDocument document) throws IOException {
    Splitter splitter = new Splitter();
    return splitter.split(document);
  }

  public static void savePdf(PDDocument document, String saveDirectory, String payer, int count,
      String pdfType) throws IOException {

    if (count > 1) {
      document.save(saveDirectory + payer + " " + count + " " + pdfType + ".pdf");
    } else {
      document.save(saveDirectory + payer + " " + pdfType + ".pdf");
    }
  }

  public static String[] getPdfLines(PDDocument document) throws IOException {
    PDFTextStripper pdfStripper = new PDFTextStripper();

    return pdfStripper.getText(document).split("\\r?\\n");
  }

  public static String[] getPdfLines(String lines) {
    return lines.split("\\r?\\n");
  }

  public static boolean isNFe(PDDocument pdfPage) throws IOException {
    return getPdfLines(pdfPage)[0].contains("PREFEITURA DO RECIFE");
  }

  public static void printLines(String[] lines) {
    for (int i = 0; i < lines.length; i++) {
      System.out.println(i + ": " + lines[i]);
    }
  }
}
