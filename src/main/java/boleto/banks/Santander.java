package boleto.banks;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;

public class Santander implements BankInterface {
    private static final int PAYER_LINE = 83;

//    String[] pdfLines = BankInterface.getPdfLines(document);
//
//            for (int i = 0; i < pdfLines.length; i++) {
//                System.out.println(i + ": " + pdfLines[i]);
//            }
    @Override
    public String getPayer(PDDocument document) throws IOException {
        String pagador = "Pagador ";
        try {
            return BankInterface
                    .getPdfLines(document)[PAYER_LINE]
                    .split(pagador)[1]
                    .split(" -")[0]
                    .trim();

        } catch (ArrayIndexOutOfBoundsException e) {
            try {
                return BankInterface
                        .getPdfLines(document)[PAYER_LINE+1]
                        .split(pagador)[1]
                        .split(" -")[0]
                        .trim();
            } catch (ArrayIndexOutOfBoundsException ex) {
                return BankInterface
                        .getPdfLines(document)[PAYER_LINE+2]
                        .split(pagador)[1]
                        .split(" -")[0]
                        .trim();
            }
        }
    }
}
