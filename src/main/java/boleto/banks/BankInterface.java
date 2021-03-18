package boleto.banks;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;

public interface BankInterface {
    String getPayer(PDDocument document) throws IOException;

    static String[] getPdfLines(PDDocument document) throws IOException {
        PDFTextStripper pdfStripper = new PDFTextStripper();

        return pdfStripper.getText(document).split("\\r?\\n");
    }

    static BankInterface getBank(PDDocument document) throws IOException {
        String[] pdfLines = getPdfLines(document);

        if (pdfLines[40].contains("Itaú")) {
            return new Itau();
        } else {
            return new Santander();
        }
    }
}
