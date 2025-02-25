import static util.PDFHelper.splitPDF;
import static util.PathHelper.windowsFriendlyFormatDateOfNow;

import boleto.BoletoHandler;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
  private static List<PDDocument> pdfPages;

  private static final List<String> bankClients = new ArrayList<>();
  private static final List<String> nfeClients = new ArrayList<>();

  public static void main(String[] args) {

    setupPaths(args);

    List<PDDocument> loadedFiles = PathHelper.loadFiles(originDirectory, writer);

    processFiles(loadedFiles);

    cleanupPhase();
  }

  private static void processFiles(List<PDDocument> loadedFiles) {
    for (PDDocument pdfFile : loadedFiles) {
      try {
        writer.write("\n\nSeparando páginas");
        pdfPages = splitPDF(pdfFile);
        writer.write("\nArquivo com " + pdfPages.size() + " páginas");

        if (isValidAmountOfPages(pdfPages)) {
          if (PDFHelper.isNFe(pdfPages.get(0))) {
            doNFeOperations();
          } else {
            doBoletoOperations();
          }
        }

        closePdf(pdfFile);
      } catch (IOException | TesseractException e) {
        System.out.println(
            "Não foi possível processar o arquivo pdf: " + pdfFile + "\n" + e.getMessage());
      }
    }
  }

  private static void cleanupPhase() {
    try {
      writer.write("\n\nFim do processamento, excluindo arquivos pdf na pasta de origem.");

      PathHelper.eraseAndFinish(originDirectory, writer);

      writer.close();

      Path filePath = Paths.get(file.getCanonicalPath());
      Files.move(filePath,
          filePath.resolveSibling("CONCLUÍDO " + windowsFriendlyFormatDateOfNow() + ".txt"));

    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  private static boolean isValidAmountOfPages(List<PDDocument> pdfPages) throws IOException {
    if (pdfPages.isEmpty() || pdfPages.size() <= 1) {
      writer.write("\nO arquivo .pdf contém uma ou menos páginas");
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

  static void doBoletoOperations() throws IOException {
    writer.write("\nÉ um Boleto");
    BoletoHandler.handleBoleto(saveDirectory, pdfPages, bankClients);
  }

  static void doNFeOperations() throws IOException, TesseractException {
    writer.write("\nÉ uma NFe");
    pdfPages.remove(0);
    NfeHandler.extractTextFromPdf(saveDirectory, pdfPages, nfeClients);
  }

  static void setupPaths(String[] args) {

    switch (args.length) {
      case 0:
        originDirectory = System.getProperty("user.dir") + "\\";
        saveDirectory = originDirectory + "Processados\\";
        PathHelper.createSaveFolder(saveDirectory);
        break;

      case 1:
        originDirectory = args[0] + "\\";
        saveDirectory = originDirectory + "Processados\\";
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
        originDirectory + "PROCESSANDO " + windowsFriendlyFormatDateOfNow() + ".txt");
    try {
      file.createNewFile();
      writer = new FileWriter(file.getCanonicalPath());

      writer.write("Diretório de origem: " + originDirectory);
      writer.write("\nDiretório de destino: " + saveDirectory + "\n");
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
