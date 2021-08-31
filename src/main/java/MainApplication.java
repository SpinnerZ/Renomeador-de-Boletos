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

  static PDDocument boletoDocument;
  static PDDocument nfeDocument;
  static String originDirectory;
  static String saveDirectory;

  private static List<String> bankClients = new ArrayList<>();
  private static List<String> nfeClients = new ArrayList<>();

  public static void main(String[] args) {
    setupPaths(args);

    /*try {
      doBoletoOperations();
    } catch (IOException e) {
      System.out.println("Ocorreu algum erro na operação com boletos: " + e.getMessage());
      e.printStackTrace();
    }*/

    if (containsNFe) {
      try {
        doNFeOperations();
      } catch (Exception e) {
        System.out.println("Ocorreu algum erro na operação com NFe: " + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  static void doNFeOperations() throws IOException, TesseractException {
    List<PDDocument> nfePages = splitPDF(nfeDocument);

    if (nfePages.isEmpty() || nfePages.size() <= 1) {
      System.out.println("O arquivo .pdf não contém páginas com Notas Fiscais Eletrônicas");
      System.exit(0);
    }

    nfePages.remove(0);

    NfeHandler.extractTextFromPdf(saveDirectory, createUserFolder, nfePages, nfeClients);
  }

  static void doBoletoOperations() throws IOException {
    List<PDDocument> documentPages = splitPDF(boletoDocument);

    if (documentPages.isEmpty()) {
      System.out.println("O arquivo .pdf não contém páginas");
      System.exit(0);
    }

    BoletoHandler.boletoHandler(saveDirectory, documentPages, bankClients);
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
