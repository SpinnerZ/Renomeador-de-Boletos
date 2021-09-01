import static util.PDFHelper.splitPDF;

import boleto.BoletoHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.tess4j.TesseractException;
import nfe.NfeHandler;
import org.apache.pdfbox.pdmodel.PDDocument;
import util.PathHelper;

public class MainApplication {

  private static String originDirectory;
  private static String saveDirectory;

  private static final List<String> bankClients = new ArrayList<>();
  private static final List<String> nfeClients = new ArrayList<>();
  private static List<PDDocument> pdfPages;

  public static void main(String[] args) {
    List<PDDocument> pdfFiles;

    setupPaths(args);

    try {
      pdfPages = splitPDF();
    } catch (IOException e) {
      System.out.println("Não foi possível separar o pdf em páginas: " + e.getMessage());
      System.exit(-1);
    }

    if (pdfPages.isEmpty() || pdfPages.size() <= 1) {
      System.out.println("O arquivo .pdf contém uma ou menos páginas");
      System.exit(-1);
    }

  }

  static void doNFeOperations() throws IOException, TesseractException {

    pdfPages.remove(0);

    NfeHandler.extractTextFromPdf(saveDirectory, createUserFolder, pdfPages, nfeClients);
  }

  static void doBoletoOperations() throws IOException {

    BoletoHandler.handleBoleto(saveDirectory, pdfPages, bankClients);
    boletoDocument.close();
  }

  static void setupPaths(String[] args) {

    switch (args.length) {
      case 0:
        originDirectory = "";
        saveDirectory = originDirectory + "\\resultado\\";
        PathHelper.createSaveFolder(saveDirectory);
        break;

      case 1:
        originDirectory = args[0] + "\\";
        saveDirectory = originDirectory + "resultado\\";
        PathHelper.createSaveFolder(saveDirectory);
        break;

      case 2:
        originDirectory = args[0] + "\\";
        saveDirectory = args[1] + "\\";
        break;

      default:
        System.out.println("O programa aceita 0, 1 ou 2 argumentos, sendo eles:\n" +
            "0 -> Pasta de origem do arquivo é o mesmo em que o programa se encontra. A pasta de destinho será criada dentro da pasta atual.\n"
            +
            "1 -> Pasta de origem especificada. A pasta de destino será criada dentro da pasta de origem.\n"
            +
            "2 -> Pastas de origem e destino especificadas. Nenhuma outra pasta será criada.");
    }
  }
}
