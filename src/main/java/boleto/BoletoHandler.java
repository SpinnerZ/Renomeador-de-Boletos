package boleto;

import boleto.banks.BankInterface;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;

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

      saveBoleto(page, saveDirectory, userFolder, payer, count);

      page.close();
    }
  }

  static void saveBoleto(PDDocument document, String saveDirectory, boolean userFolder,
      String payer, int count)
      throws IOException {
    if (userFolder) {
      saveDirectory += payer + "\\";

      //Adds the user name to the save path
      try {
        Path path = Paths.get(saveDirectory);
        Files.createDirectories(path);
      } catch (IOException e) {
        System.out.println("A pasta não pôde ser criada!\n" + e.getMessage());
        System.exit(0);
      }
    }

    if (count > 1) {
      document.save(saveDirectory + payer + " " + count + ".pdf");
    } else {
      document.save(saveDirectory + payer + ".pdf");
    }
  }
}
