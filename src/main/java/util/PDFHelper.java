package util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFHelper {

  private PDFHelper() {
  }

  //Returns the .pdf filename in the folder or finishes the program
  public static String findPdfFile(String path) {
    File directory = new File(path);

    // store all names with same name
    // with/without extension
    String[] flist = directory.list(new PDFFilter());

    if (flist == null || flist.length == 0) {
      System.out.println("Não há arquivo .pdf na pasta.");
      System.exit(0);
    }

    if (flist.length > 1) {
      System.out.println("Mais de um arquivo .pdf na pasta.");
      System.exit(0);
    }

    return flist[0];
  }

  public static PDDocument loadFile(String directory) {
    PDDocument document = null;
    try {
      document = PDDocument.load(new File(directory + findPdfFile(directory)));
    } catch (IOException e) {
      System.out.println("Não é um arquivo PDF ou está corrompido.");
      System.exit(0);
    }

    return document;
  }

  public static List<PDDocument> splitPDF(PDDocument document) throws IOException {
    Splitter splitter = new Splitter();
    return splitter.split(document);
  }

  public static void savePdf(PDDocument document, String saveDirectory, String payer, int count,
      String pdfType) throws IOException {

    if (count > 1) {
      document.save(saveDirectory + payer + " " + count + pdfType + ".pdf");
    } else {
      document.save(saveDirectory + payer + pdfType + ".pdf");
    }
  }

  public static String[] getPdfLines(PDDocument document) throws IOException {
    PDFTextStripper pdfStripper = new PDFTextStripper();

    return pdfStripper.getText(document).split("\\r?\\n");
  }

  public static String[] getPdfLines(String lines) {
    return lines.split("\\r?\\n");
  }
}
