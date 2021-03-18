package boleto.banks;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;

public class Santander implements BankInterface {
    private static final int PAYER_LINE = 83;

    @Override
    public String getPayer(PDDocument document) throws IOException {

        try {
            return BankInterface
                    .getPdfLines(document)[PAYER_LINE]
                    .split("Pagador ")[1]
                    .split(" -")[0]
                    .trim();

        } catch (ArrayIndexOutOfBoundsException e) {
            return BankInterface
                    .getPdfLines(document)[PAYER_LINE+1]
                    .split("Pagador ")[1]
                    .split(" -")[0]
                    .trim();
        }
    }
}
