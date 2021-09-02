import static util.PDFHelper.splitPDF;

import boleto.BoletoHandler;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.tess4j.TesseractException;
import nfe.NfeHandler;
import org.apache.pdfbox.pdmodel.PDDocument;
import util.PDFHelper;
import util.PathHelper;

public class MainApplication {

  private static String originDirectory;
  private static String saveDirectory;
  private static File file;
  private static FileWriter writer;

  private static final List<String> bankClients = new ArrayList<>();
  private static final List<String> nfeClients = new ArrayList<>();
  private static List<PDDocument> pdfPages;

  public static void main(String[] args) {

    setupPaths(args);

    for (PDDocument pdfFile : PathHelper.loadFiles(originDirectory)) {
      try {
        writer.write("\n\nSeparando páginas");
        pdfPages = splitPDF(pdfFile);
        writer.write("Arquivo com " + pdfPages.size() + " páginas");

        if (!isValidAmountOfPages(pdfPages)) {
          writer.write("Não tem quantidade válida de paginas");
          closePdf(pdfFile);
          continue;
        }

        if (PDFHelper.isNFe(pdfPages.get(0))) {
          writer.write("É uma NFe");
          doNFeOperations();
        } else {
          writer.write("É um Boleto");
          BoletoHandler.handleBoleto(saveDirectory, pdfPages, bankClients);
        }

      } catch (IOException | TesseractException e) {
        System.out.println(
            "Não foi possível processar o arquivo pdf: " + pdfFile + "\n" + e.getMessage());
        closePdf(pdfFile);
        continue;
      }

      closePdf(pdfFile);
    }

    try {
      writer.write("Fim do processamento, excluindo arquivos pdf na pasta de origem.");
      writer.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    PathHelper.eraseAndFinish(originDirectory, file);
  }

  private static boolean isValidAmountOfPages(List<PDDocument> pdfPages) {
    if (pdfPages.isEmpty() || pdfPages.size() <= 1) {
      System.out.println("O arquivo .pdf contém uma ou menos páginas");
      return false;
    }

    return true;
  }

  static void closePdf(PDDocument pdfFile) {
    try {
      pdfFile.close();
    } catch (IOException e) {
      System.out.println("Não foi possível fechar o arquivo: " + e.getMessage());
    }
  }

  static void doNFeOperations() throws IOException, TesseractException {

    pdfPages.remove(0);
    NfeHandler.extractTextFromPdf(saveDirectory, pdfPages, nfeClients);
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

    file = new File(
        originDirectory + "processando " + PathHelper.windowsFriendlyFormatDateOfNow() + ".txt");
    try {
      file.createNewFile();
      writer = new FileWriter(file.getCanonicalPath());

      writer.write("Diretório de origem: " + originDirectory);
      writer.write("Diretório de destino: " + saveDirectory);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
