import banks.BankInterface;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import util.FiltroPDF;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.File;

public class BoletoHandler {

    public static void main(String[] args) throws IOException {
        if(args.length < 2)
        {
            System.out.println("Precisa inserir por argumentos a pasta de origem e a pasta de destino");
            System.exit(0);
        }

        final String PDF_DIRECTORY = args[0] + "\\";
        final String SAVE_DIRECTORY = args[1] + "\\" + (Calendar.getInstance().get(Calendar.MONTH) + 1) +"\\";
        final PDDocument DOCUMENT = loadFile(PDF_DIRECTORY);
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

            saveBoleto(document, SAVE_DIRECTORY, payer, count);
            document.close();
        }

        DOCUMENT.close();
    }

    public static List<PDDocument> splitPDF(PDDocument document) throws IOException {
        Splitter splitter = new Splitter();
        return splitter.split(document);
    }

    public static void saveBoleto (PDDocument document, String saveDirectory, String payer, int count) throws IOException {
        saveDirectory += payer + "\\";

        //Adds the user name to the save path
        try {
            Path path = Paths.get(saveDirectory);
            Files.createDirectories(path);
        } catch (IOException e) {
            System.out.println("A pasta não pôde ser criada!\n" + e.getMessage());
            System.exit(0);
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
}
