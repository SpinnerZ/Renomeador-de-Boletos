package boleto.banks;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import util.PDFHandler;

public class Santander implements BankInterface {

  private static final int PAYER_LINE = 83;

  //    String[] pdfLines = PDFHandler.getPdfLines(document);
//
//            for (int i = 0; i < pdfLines.length; i++) {
//                System.out.println(i + ": " + pdfLines[i]);
//            }
  @Override
  public String getPayer(PDDocument document) throws IOException {
    String pagador = "Pagador ";
    try {
      return PDFHandler.getPdfLines(document)[PAYER_LINE].split(pagador)[1].split(" -")[0].trim();
    } catch (ArrayIndexOutOfBoundsException e) {
      try {
        return PDFHandler.getPdfLines(document)[PAYER_LINE + 1].split(pagador)[1].split(" -")[0]
            .trim();
      } catch (ArrayIndexOutOfBoundsException ex) {
        return PDFHandler.getPdfLines(document)[PAYER_LINE + 2].split(pagador)[1].split(" -")[0]
            .trim();
      }
    }
  }
}
