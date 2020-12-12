import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;


public class TestOutFile {

  public static void main(String[] str) {

    Map<Integer, Integer> totalRequestMap = new HashMap<Integer, Integer>();
    Scanner inputFileScanner, configFileScanner;
    int totalNumberOfRequest;
    int numberOfNodes;

    try {
      // inputFileScanner = new Scanner(new
      // File("/home/010/d/dx/dxe180003/Project1/AOS-Gradle/config.txt"));
      configFileScanner = new Scanner(
          new File("/home/011/v/vr/vrr180003/Graduate-Studies/AdvanceOS/AOS-Project2/config.dat"));
    } catch (FileNotFoundException e) {
      System.out.println("Error occurred while opening the file with exception :" + e);
      return;
    }

    String configLine = configFileScanner.nextLine();
    totalNumberOfRequest = Integer.parseInt(configLine.split("\\s+")[3]);
    numberOfNodes = Integer.parseInt(configLine.split("\\s+")[0]);

    System.out.println("Total number of nodes " + numberOfNodes);
    System.out.println("Request count per node " + totalNumberOfRequest);

    try {
      // inputFileScanner = new Scanner(new
      // File("/home/010/d/dx/dxe180003/Project1/AOS-Gradle/config.txt"));
      inputFileScanner = new Scanner(
          new File("/home/011/v/vr/vrr180003/Graduate-Studies/AdvanceOS/AOS-Project2/file.out"));
    } catch (FileNotFoundException e) {
      System.out.println("Error occurred while opening the file with exception :" + e);
      return;
    }

    while (inputFileScanner.hasNext()) {
      String holdingIds = inputFileScanner.nextLine();
      String enteredLine = inputFileScanner.nextLine();
      String leavingLine = inputFileScanner.nextLine();
      String i = enteredLine.split(":")[1].split("\\s+")[0];
      String j = leavingLine.split(":")[1].split("\\s+")[0];
      if (!enteredLine.split("\\s+")[0].equals("Entered"))
        throw new RuntimeException("Not matching");
      if (!leavingLine.split("\\s+")[0].equals("Leaving"))
        throw new RuntimeException("Not matching");
      if (!i.equals(j))
        throw new RuntimeException("Not matching");
      if (totalRequestMap.containsKey(Integer.parseInt(i))) {
        totalRequestMap.put(Integer.parseInt(i), totalRequestMap.get(Integer.parseInt(i)) + 1);
      } else {
        totalRequestMap.put(Integer.parseInt(i), 1);
      }
    }

    if (totalRequestMap.keySet().size() != numberOfNodes)
      throw new RuntimeException("Not matching");
    for (Integer in : totalRequestMap.values()) {
      if (in != totalNumberOfRequest)
        throw new RuntimeException("Not matching");
    }

    System.out.println("Mutual exclusion is verified");
  }

}
