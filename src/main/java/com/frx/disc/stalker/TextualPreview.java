
package com.frx.disc.stalker;

import com.frx.disc.stalker.model.DirectoryNode;
import com.frx.disc.stalker.model.FileSystemNode;
import com.frx.disc.stalker.fs.LiveDirectoryTree;
import java.io.IOException;
import java.nio.file.Paths;

// A temporary class for debugging.
public class TextualPreview {
  public static void main(String[] args) throws IOException {
    final var path = Paths.get("/home/jonatanklosko/stuff/tmp/to");

    // Load the directory tree into recursive object structure.
    // The objects are automatically updated whenever the underlying files change.
    final var liveTree = new LiveDirectoryTree(path);
    final var root = liveTree.getRoot();

    printFileSystemNode(root);
    System.out.println("Listening for changes...");

    // The objects consist of observable properties,
    // but here we just re-render the whole tree on every change for demonstration.
    liveTree.getEventSubject().subscribe(event -> {
      System.out.println("------- changed ------");
      printFileSystemNode(root);
    });
  }

  private static void printFileSystemNode(FileSystemNode node) {
    printFileSystemNode(node, 0);
  }

  private static void printFileSystemNode(FileSystemNode node, int indent) {
    if (node.isDirectory()) {
      System.out.printf(
          "%s\uD83D\uDCC2 %s (%d B) [%d files]%n",
          " ".repeat(indent), node.getPath().getFileName(), node.getSize(), node.getNumberOfFiles()
      );
      ((DirectoryNode) node).getChildNodes().forEach(childNode -> {
        printFileSystemNode(childNode, indent + 2);
      });
    } else {
      System.out.printf(
          "%s\uD83D\uDCC4 %s (%d B)%n",
          " ".repeat(indent), node.getPath().getFileName(), node.getSizeProperty().get()
      );
    }
  }
}
