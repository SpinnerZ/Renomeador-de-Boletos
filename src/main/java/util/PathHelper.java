package util;

import java.io.File;
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

  private static final String WINDOWS_FRIENDLY_DATE_FORMAT = "dd-MM-yyyy_HH-mm-ss";

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

  public static List<PDDocument> loadFiles(String directory) {

    List<PDDocument> documents = new ArrayList<>();

    for (String filePath : findPdfFiles(directory)) {
      try {
        documents.add(PDDocument.load(new File(directory + filePath)));
      } catch (IOException e) {
        System.out.println("Não é um arquivo PDF ou está corrompido: " + filePath);
      }
    }

    if (documents.isEmpty()) {
      System.out.println("Não foram encontrados arquivos válidos no diretório de origem.");
      System.exit(-1);
    }

    return documents;
  }

  public static void eraseAndFinish(String directory, File file) {
    for (String filePath : findPdfFiles(directory)) {
      try {
        Files.delete(Paths.get(filePath));
      } catch (IOException e) {
        System.out.println("Não foi possível excluir o arquivo: " + filePath);
      }
    }

    // File (or directory) with new name
    File newFile = new File(file.getParent() + windowsFriendlyFormatDateOfNow() + ".txt");
    file.renameTo(newFile);
  }

  public static String windowsFriendlyFormatDateOfNow() {
    return new SimpleDateFormat(WINDOWS_FRIENDLY_DATE_FORMAT).format(new Date());
  }
}
