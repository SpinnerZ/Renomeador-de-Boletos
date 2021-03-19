import boleto.BoletoHandler;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;

import static util.PDFHandler.loadFile;
import static util.PDFHandler.splitPDF;

public class MainApplication {

    static PDDocument boletoDocument;
    static PDDocument nfeDocument;
    static String saveDirectory;
    static boolean createUserFolder;
    static boolean containsNFe;

    public static void main(String[] args) {
        setupPaths(args);

        try {
            doBoletoOperations();
        } catch (IOException e) {
            System.out.println("Ocorreu algum erro na operação com boletos: " + e.getMessage());
            e.printStackTrace();
        }

//        try {
//            doNFeOperations();
//        }
    }

//    static void doNFeOperations() {
//
//    }

    static void doBoletoOperations() throws IOException {
        Iterator<PDDocument> iterator = splitPDF(boletoDocument).listIterator();

        if (iterator.hasNext()) {
            BoletoHandler.boletoHandler(saveDirectory, createUserFolder, iterator);
        } else {
            System.out.println("O arquivo .pdf não contém páginas");
            System.exit(0);
        }
        boletoDocument.close();
    }

    static void setupPaths(String[] args) {

        if (args.length < 1) {
            System.out.println("Precisa inserir por argumentos ao menos a pasta de origem do arquivo que contém todos os boletos");
            System.exit(0);
        }

        String boletoOriginDirectory = args[0] + "\\";
        boletoDocument = loadFile(boletoOriginDirectory);

        switch (args.length) {
            case 1:
                saveDirectory = boletoOriginDirectory;
                createUserFolder = false;
                containsNFe = false;
                break;

            case 2:
                saveDirectory = args[1] + "\\" + (Calendar.getInstance().get(Calendar.MONTH) + 1) +"\\";
                createUserFolder = true;
                containsNFe = false;
                break;

            case 3:
                saveDirectory = args[1] + "\\" + (Calendar.getInstance().get(Calendar.MONTH) + 1) +"\\";
                createUserFolder = true;
                containsNFe = true;
                String nfeOriginDirectory = args[2] + "\\";
                nfeDocument = loadFile(nfeOriginDirectory);
                break;

            default:
                System.out.println("O programa aceita 1, 2 ou 3 argumentos, sendo eles em ordem:\n" +
                        "Pasta de origem do arquivo que contém todos os boletos\n" +
                        "Pasta de destino se quiser salvar automaticamente o mês e uma pasta por cliente\n" +
                        "Pasta de origem do arquivo que contém o talão de NFe");
        }
    }
}
