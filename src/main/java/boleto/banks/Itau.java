package boleto.banks;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;

public class Itau implements BankInterface {
    private static final int PAYER_LINE = 17;

    @Override
    public String getPayer(PDDocument document) throws IOException {

        return BankInterface
                .getPdfLines(document)[PAYER_LINE]
                .split(" -")[0]
                .trim();
    }
}
