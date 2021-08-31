package boleto.banks;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import util.PDFHelper;

public class Santander implements BankInterface {

  private static final int PAYER_LINE = 83;

  @Override
  public String getPayer(PDDocument document) throws IOException {
    String pagador = "Pagador ";
    try {
      return PDFHelper.getPdfLines(document)[PAYER_LINE].split(pagador)[1].split(" -")[0].trim();
    } catch (ArrayIndexOutOfBoundsException e) {
      try {
        return PDFHelper.getPdfLines(document)[PAYER_LINE + 1].split(pagador)[1].split(" -")[0]
            .trim();
      } catch (ArrayIndexOutOfBoundsException ex) {
        return PDFHelper.getPdfLines(document)[PAYER_LINE + 2].split(pagador)[1].split(" -")[0]
            .trim();
      }
    }
  }
}
