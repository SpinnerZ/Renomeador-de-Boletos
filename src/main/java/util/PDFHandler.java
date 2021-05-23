package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFHandler {

  private PDFHandler() {
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

  public static void savePdf(PDDocument document, String saveDirectory, boolean userFolder,
      String payer, int count, String pdfType) throws IOException {
//    if (userFolder) {
//      saveDirectory += payer + "\\";
//
//      //Adds the user name to the save path
//      try {
//        Files.createDirectories(Paths.get(saveDirectory));
//      } catch (IOException e) {
//        System.out.println("A pasta não pôde ser criada!\n" + e.getMessage());
//        System.exit(0);
//      }
//    }

    if (count > 1) {
      document.save(saveDirectory + pdfType + payer + " " + count + ".pdf");
    } else {
      document.save(saveDirectory + pdfType + payer + ".pdf");
    }
  }

  public static String[] getPdfLines(PDDocument document) throws IOException {
    PDFTextStripper pdfStripper = new PDFTextStripper();

    return pdfStripper.getText(document).split("\\r?\\n");
  }

  public static String[] getPdfLines(String lines) throws IOException {
    return lines.split("\\r?\\n");
  }
}
