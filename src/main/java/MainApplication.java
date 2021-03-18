import boleto.BoletoHandler;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;

import static util.PDFHandler.loadFile;
import static util.PDFHandler.splitPDF;

public class MainApplication {
    public static void main(String[] args) throws IOException {
        if(args.length < 1)
        {
            System.out.println("Precisa inserir por argumentos ao menos a pasta de origem");
            System.exit(0);
        }

        final String PDF_DIRECTORY = args[0] + "\\";
        final PDDocument DOCUMENT = loadFile(PDF_DIRECTORY);
        Iterator<PDDocument> iterator = splitPDF(DOCUMENT).listIterator();

        final String SAVE_DIRECTORY;
        final boolean USER_FOLDER;
        if (args.length > 1) {
            SAVE_DIRECTORY = args[1] + "\\" + (Calendar.getInstance().get(Calendar.MONTH) + 1) +"\\";
            USER_FOLDER = true;
        } else {
            SAVE_DIRECTORY = PDF_DIRECTORY;
            USER_FOLDER = false;
        }

        if (iterator.hasNext()) {
            BoletoHandler.boletoHandler(SAVE_DIRECTORY, USER_FOLDER, iterator);
        } else {
            System.out.println("O arquivo .pdf não contém páginas");
            System.exit(0);
        }
        DOCUMENT.close();
    }
}
