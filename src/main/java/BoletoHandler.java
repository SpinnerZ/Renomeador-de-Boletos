import banks.BankInterface;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import util.FiltroPDF;

import java.io.IOException;
import java.util.*;
import java.io.File;

public class BoletoHandler {

    public static void main(String[] args) throws IOException {
        final String DIRECTORY = args[0] + "\\";
        final PDDocument DOCUMENT = loadFile(DIRECTORY);
        final BankInterface BANK = BankInterface.getBank(DOCUMENT);

        List<String> payers = new ArrayList<>();
        Iterator<PDDocument> iterator = splitPDF(DOCUMENT).listIterator();
        int count;

        while(iterator.hasNext()) {
            PDDocument document = iterator.next();

            String payer = BANK.getPayer(document).replace("/", "");
            payers.add(payer);
            count = (int)
                    payers.stream()
                            .filter(p -> p.equals(payer))
                            .count();

            if (count > 1) {
                saveBoleto(document, DIRECTORY, payer, count);
            } else {
                saveBoleto(document, DIRECTORY, payer);
            }

            document.close();
        }

        DOCUMENT.close();
    }

    public static List<PDDocument> splitPDF(PDDocument document) throws IOException {
        Splitter splitter = new Splitter();
        return splitter.split(document);
    }

    public static void saveBoleto (PDDocument document, String path, String payer, int count) throws IOException {
        document.save(path
                + payer
                + " "
                + count
                +".pdf");
    }

    public static void saveBoleto (PDDocument document, String path, String payer) throws IOException {
        document.save(path
                + payer
                +".pdf");
    }

    //Retorna o nome do arquivo .pdf na pasta ou finaliza o programa
    static String findPdfFile(String path) {
        File directory = new File(path);

        // store all names with same name
        // with/without extension
        String[] flist = directory.list(new FiltroPDF());

        if (flist == null || flist.length == 0) {
            System.out.println("Não há arquivo .pdf na pasta.");
            System.exit(0);
        }

        if (flist.length > 1) {
            System.out.println("Mais de um arquivo .pdf na pasta.");
            System.exit(0);
        }

        return flist[0];
    }

    static PDDocument loadFile(String directory) {
        PDDocument document = null;
        try {
            document = PDDocument.load(new File(directory + findPdfFile(directory)));
        } catch (IOException e) {
            System.out.println("Não é um arquivo PDF ou está corrompido.");
            System.exit(0);
        }

        return document;
    }

    //Deprecated
    static String getPath(String inputFile) {
        String[] fullPath = inputFile.split("\\\\");
        StringBuilder path = new StringBuilder();

        for (int i = 0; i < fullPath.length; i++) {
            String partial = fullPath[i] + "\\";
            path.append(partial);
        }

        return path.toString();
    }
}
