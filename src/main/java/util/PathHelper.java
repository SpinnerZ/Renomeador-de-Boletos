package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;

public class PathHelper {

  private PathHelper() {
  }

  private static final String WINDOWS_FRIENDLY_DATE_FORMAT = "dd-MM-yyyy HH-mm-ss";

  public static void createSaveFolder(String savePath) {
    try {
      Files.createDirectories(Paths.get(savePath));
    } catch (IOException e) {
      System.out.println("A pasta não pôde ser criada!\n" + e.getMessage());
      System.exit(0);
    }
  }

  //Returns the .pdf filename in the folder or finishes the program
  private static String[] findPdfFiles(String path) {
    File directory = new File(path);

    // store all names with same name
    // with/without extension
    String[] flist = directory.list(new PDFFilter());

    if (flist == null || flist.length == 0) {
      System.out.println("Não há arquivo .pdf no caminho " + path);
      System.exit(-1);
    }

    return flist;
  }

  public static List<PDDocument> loadFiles(String directory, FileWriter writer) {

    List<PDDocument> documents = new ArrayList<>();

    for (String filePath : findPdfFiles(directory)) {
      try {
        documents.add(PDDocument.load(new File(directory + filePath)));
        writer.write("\nDocumento carregado: " + filePath);
      } catch (IOException e) {
        System.out.println("Não é um arquivo PDF ou está corrompido: " + filePath);
      }
    }

    if (documents.isEmpty()) {
      try {
        writer.write(
            "\nNão foram encontrados arquivos válidos no diretório de origem: " + directory);
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
      System.exit(-1);
    }

    return documents;
  }

  public static void eraseAndFinish(String directory, File file, FileWriter writer) {
    for (String filePath : findPdfFiles(directory)) {
      try {
        writer.write("\nTentando apagar o arquivo " + filePath);
        Files.delete(Paths.get(filePath));
        writer.write("\n" + filePath + " apagado");
      } catch (IOException e) {
        System.out.println("Não foi possível excluir o arquivo: " + filePath);
      }
    }

    // File (or directory) with new name
    File newFile = new File(
        file.getParent() + "\\CONCLUÍDO " + windowsFriendlyFormatDateOfNow() + ".txt");
    file.renameTo(newFile);
  }

  public static String windowsFriendlyFormatDateOfNow() {
    return new SimpleDateFormat(WINDOWS_FRIENDLY_DATE_FORMAT).format(new Date());
  }
}
