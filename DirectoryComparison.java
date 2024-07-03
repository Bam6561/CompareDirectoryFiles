import java.io.File;
import java.util.*;

/**
 * DirectoryComparison compares two directories' and their
 * subdirectories' contents and lists differences between both.
 * <p>
 * When referring to contents, files' paths are compared.
 *
 * @author Danny Nguyen
 * @version 1.2
 * @since 1.0
 */
public class DirectoryComparison {
  /**
   * First directory.
   */
  private static final File directory1 = new File("DIRECTORY 1");

  /**
   * Second directory.
   */
  private static final File directory2 = new File("DIRECTORY 2");

  /**
   * First directory's contents.
   */
  private static final Set<String> contents1 = new HashSet<>();

  /**
   * Second directory's contents.
   */
  private static final Set<String> contents2 = new HashSet<>();

  /**
   * Checks if the {@link #directory1} and {@link #directory2}
   * are valid before parsing the file system.
   *
   * @param args user provided parameters.
   */
  public static void main(String[] args) {
    if (!directory1.isDirectory()) {
      System.out.println("First directory does not exist.");
      return;
    }
    if (!directory2.isDirectory()) {
      System.out.println("Second directory does not exist.");
      return;
    }

    parseDirectory(directory1, directory1.listFiles(), contents1);
    parseDirectory(directory2, directory2.listFiles(), contents2);
    compareContents();
    printUniqueFiles(directory1, contents1);
    printUniqueFiles(directory2, contents2);
  }

  /**
   * Recursively parses the directory to map its contents.
   *
   * @param source    source directory
   * @param directory directory
   * @param files     directory contents
   */
  private static void parseDirectory(File source, File[] directory, Set<String> files) {
    for (File file : directory) {
      if (file.isFile()) {
        files.add(file.getPath().substring(source.getPath().length()));
      } else {
        parseDirectory(source, file.listFiles(), files);
      }
    }
  }

  /**
   * Compares the directories' contents.
   */
  private static void compareContents() {
    Set<String> files1Copy = new HashSet<>(contents1);
    contents1.removeAll(contents2);
    contents2.removeAll(files1Copy);
  }

  /**
   * Prints unique file paths that only exist within the directory and not the other.
   *
   * @param directory directory
   * @param contents  directory contents
   */
  private static void printUniqueFiles(File directory, Set<String> contents) {
    List<String> sortedFiles = new ArrayList<>(contents);
    Collections.sort(sortedFiles);
    contents.clear();
    for (String file : sortedFiles) {
      System.out.println(directory.getPath() + file);
    }
  }
}