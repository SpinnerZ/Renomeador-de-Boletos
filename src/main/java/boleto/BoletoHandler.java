package boleto;

import boleto.banks.BankInterface;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import util.PDFHelper;

public class BoletoHandler {

  private BoletoHandler() {
  }

  public static void handleBoleto(String saveDirectory, List<PDDocument> pages,
      List<String> payers) throws IOException {
    final BankInterface bank = BankInterface.getBank(pages.get(0));
    int count;

    for (PDDocument page : pages) {
      String payer = bank.getPayer(page).replace("/", "");
      payers.add(payer);
      count = (int) payers.stream().filter(p -> p.equals(payer)).count();

      PDFHelper.savePdf(page, saveDirectory, payer, count, "Boleto - ");

      page.close();
    }
  }
}
