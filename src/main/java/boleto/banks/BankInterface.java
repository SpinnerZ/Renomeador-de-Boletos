package boleto.banks;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import util.PDFHelper;

public interface BankInterface {

  String getPayer(PDDocument document) throws IOException;

  static BankInterface getBank(PDDocument document) throws IOException {
    String[] pdfLines = PDFHelper.getPdfLines(document);

    if (pdfLines[40].contains("Itaú")) {
      return new Itau();
    } else {
      return new Santander();
    }
  }
}
