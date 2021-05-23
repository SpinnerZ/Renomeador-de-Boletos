package boleto.banks;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import util.PDFHandler;

public class Itau implements BankInterface {

  private static final int PAYER_LINE = 17;

  @Override
  public String getPayer(PDDocument document) throws IOException {

    return PDFHandler.getPdfLines(document)[PAYER_LINE].split(" -")[0].trim();
  }
}
