package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PathHelper {

  private PathHelper() {
  }

  public static void createSaveFolder(String savePath) {
    try {
      Files.createDirectories(Paths.get(savePath));
    } catch (IOException e) {
      System.out.println("A pasta não pôde ser criada!\n" + e.getMessage());
      System.exit(0);
    }
  }
}
