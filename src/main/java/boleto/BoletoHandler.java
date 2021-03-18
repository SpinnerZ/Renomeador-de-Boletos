package boleto;

import boleto.banks.BankInterface;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BoletoHandler {

    private BoletoHandler(){}

    public static void boletoHandler(String saveDirectory, boolean userFolder,
                                     Iterator<PDDocument> startIterator) throws IOException {

        Iterator<PDDocument> iterator = startIterator;
        final BankInterface BANK = BankInterface.getBank(startIterator.next());
        List<String> payers = new ArrayList<>();
        int count;

        while(iterator.hasNext()) {
            PDDocument document = iterator.next();
            String payer = BANK.getPayer(document).replace("/", "");
            payers.add(payer);
            count = (int)
                    payers.stream()
                            .filter(p -> p.equals(payer))
                            .count();

            saveBoleto(document, saveDirectory, userFolder, payer, count);
            document.close();
        }
    }

    static void saveBoleto(PDDocument document, String saveDirectory, boolean userFolder, String payer, int count) throws IOException {
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
            document.save(saveDirectory
                    + payer
                    + " "
                    + count
                    +".pdf");
        } else {
            document.save(saveDirectory
                    + payer
                    +".pdf");
        }
    }
}
