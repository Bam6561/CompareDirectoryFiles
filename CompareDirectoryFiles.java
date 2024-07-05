import java.io.File;
import java.util.*;

/**
 * Lists different relative file paths between two directories and their subdirectories.
 *
 * @author Danny Nguyen
 * @version 2.0
 * @since 1.0
 */
public class CompareDirectoryFiles {
  /**
   * First directory.
   */
  private static final File directory1 = new File("FIRST DIRECTORY");

  /**
   * Second directory.
   */
  private static final File directory2 = new File("SECOND DIRECTORY");

  /**
   * Checks if the {@link #directory1} and {@link #directory2}
   * inputs are valid before traversing the file system.
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

    Set<String> paths1 = new HashSet<>();
    Set<String> paths2 = new HashSet<>();

    addPaths(paths1, paths2);
    comparePaths(paths1, paths2);
    printPaths(directory1, paths1);
    printPaths(directory2, paths2);
  }

  /**
   * Adds the directories' files' paths.
   *
   * @param paths1 {@link #directory1} file paths
   * @param paths2 {@link #directory2} file paths
   */
  private static void addPaths(Set<String> paths1, Set<String> paths2) {
    Thread task1 = new Thread(() -> parseDirectory(directory1, directory1, paths1));
    Thread task2 = new Thread(() -> parseDirectory(directory2, directory2, paths2));
    task1.start();
    task2.start();

    try {
      task1.join();
      task2.join();
    } catch (InterruptedException ex) {
      System.out.println("Tasks interrupted.");
    }
  }

  /**
   * Compares the directories' file paths and keeps unique file paths.
   *
   * @param paths1 {@link #directory1} file paths
   * @param paths2 {@link #directory2} file paths
   */
  private static void comparePaths(Set<String> paths1, Set<String> paths2) {
    Set<String> paths1Copy = new HashSet<>(paths1);
    paths1.removeAll(paths2);
    paths2.removeAll(paths1Copy);
  }

  /**
   * Sorts the file paths before printing them.
   *
   * @param directory directory
   * @param paths     file paths
   */
  private static void printPaths(File directory, Set<String> paths) {
    List<String> sortedPaths = new ArrayList<>(paths);
    Collections.sort(sortedPaths);
    paths.clear();

    StringBuilder pathsBuilder = new StringBuilder();
    for (String file : sortedPaths) {
      pathsBuilder.append(directory.getPath()).append(file).append("\n");
    }
    if (!pathsBuilder.isEmpty()) {
      System.out.print(pathsBuilder);
    }
  }

  /**
   * Recursively traverses the directory to add its file paths.
   *
   * @param source    source directory
   * @param directory directory
   * @param paths     file paths
   */
  private static void parseDirectory(File source, File directory, Set<String> paths) {
    for (File file : directory.listFiles()) {
      if (file.isFile()) {
        paths.add(file.getPath().substring(source.getPath().length()));
      } else {
        parseDirectory(source, file, paths);
      }
    }
  }
}
