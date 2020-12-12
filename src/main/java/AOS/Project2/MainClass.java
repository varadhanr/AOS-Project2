package AOS.Project2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainClass {
  static int NUMBER_OF_NODES = 0;
  static int INTER_REQUEST_DELAY = 0;
  static int EXECUTION_TIME = 0;
  static int TOTAL_REQUESTS_COUNT = 0;

  static List<Integer> portsList = new ArrayList<>();
  static List<String> hostNamesList = new ArrayList<>();

  public static void main(String[] args) {

    Scanner inputFileScanner;

    try {
      // inputFileScanner = new Scanner(new
      // File("/home/010/d/dx/dxe180003/Project1/AOS-Gradle/config.txt"));
      inputFileScanner = new Scanner(
          new File("/home/011/v/vr/vrr180003/Graduate-Studies/AdvanceOS/AOS-Project2/config.dat"));
    } catch (FileNotFoundException e) {
      System.out.println("Error occurred while opening the file with exception :" + e);
      return;
    }

    List<String> inputList = new ArrayList<>();
    while (inputFileScanner.hasNext()) {
      String nextLine = inputFileScanner.nextLine();
      if (nextLine.isEmpty())
        continue;
      inputList.add(nextLine);
    }
    String tokensLine = inputList.get(0);
    String[] tokensLineArray = tokensLine.split("\\s+");
    NUMBER_OF_NODES = Integer.parseInt(tokensLineArray[0]);
    INTER_REQUEST_DELAY = Integer.parseInt(tokensLineArray[1]);
    EXECUTION_TIME = Integer.parseInt(tokensLineArray[2]);
    TOTAL_REQUESTS_COUNT = Integer.parseInt(tokensLineArray[3]);

    for (int i = 1; i < inputList.size(); i++) {
      String nodeLine = inputList.get(i);
      String[] nodeLineArray = nodeLine.split("\\s+");
      portsList.add(Integer.parseInt(nodeLineArray[2]));
      hostNamesList.add(nodeLineArray[1]);
    }

    int inputNodeId = Integer.parseInt(args[0]);

    Node inputNode = new Node(inputNodeId, portsList, hostNamesList, NUMBER_OF_NODES,
        INTER_REQUEST_DELAY, EXECUTION_TIME, TOTAL_REQUESTS_COUNT);

    ApplicationImpl app = new ApplicationImpl(inputNode);
    

    app.execute();


    inputFileScanner.close();
  }

}
