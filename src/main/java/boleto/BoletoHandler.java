package boleto;

import boleto.banks.BankInterface;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import util.PDFHandler;

public class BoletoHandler {

  private BoletoHandler() {
  }

  public static void boletoHandler(String saveDirectory, boolean userFolder, List<PDDocument> pages)
      throws IOException {
    final BankInterface BANK = BankInterface.getBank(pages.get(0));
    List<String> payers = new ArrayList<>();
    int count;

    for (PDDocument page : pages) {
      String payer = BANK.getPayer(page).replace("/", "");
      payers.add(payer);
      count = (int) payers.stream().filter(p -> p.equals(payer)).count();

      PDFHandler.savePdf(page, saveDirectory, userFolder, payer, count, "Boleto - ");

      page.close();
    }
  }
}
