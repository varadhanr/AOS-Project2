package AOS.Project2;

import java.util.ArrayList;
import java.util.List;


public class Node {

  int nodeId;

  int size;

  List<Integer> portsList;

  List<String> hostNamesList;

  int totalNumberOfNodes;

  int interRequestDelay;

  int csExecutionTime;

  int totalNumberOfRequest;

  Node(int nodeId, List<Integer> portsList, List<String> hostNamesList, int totalNumberOfNodes,
      int interRequestDelay, int csExecutionTime, int totalNumberOfRequest) {
    this.nodeId = nodeId;
    this.size = portsList.size();
    this.portsList = portsList;
    this.hostNamesList = hostNamesList;
    this.totalNumberOfNodes = totalNumberOfNodes;
    this.interRequestDelay = interRequestDelay;
    this.csExecutionTime = csExecutionTime;
    this.totalNumberOfRequest = totalNumberOfRequest;

  }

  public void createServer(SharedData sharedData) {
    Server serverThread = new Server(this.nodeId, this.portsList.get(this.nodeId), sharedData);
    serverThread.start();
    return;
  }

  public List<Client> initializeClientThreads(SharedData sharedData) {
    List<Client> clientThreads = new ArrayList<Client>();

    for (int i = 0; i < portsList.size(); i++) {
      if (i != this.nodeId) {
        Client clientThread = new Client(this.nodeId, i, this.portsList.get(i),
            this.hostNamesList.get(i), sharedData);
        clientThreads.add(clientThread);
      }
    }

    return clientThreads;
  }

}
