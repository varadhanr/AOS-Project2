package AOS.Project2;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MutexImpl {

  public void enterCriticalSection(Node node, SharedData sharedData) {

    while (true) {

      List<Client> clientThread = node.initializeClientThreads(sharedData);
      List<Client> clientRequestingKeysList = new ArrayList<Client>();
      for (Client c : clientThread) {
        if (!sharedData.holdingKeys.contains(c.nodeIdToContact)) {
          clientRequestingKeysList.add(c);
          c.start();
        }
      }

      for (Client client : clientRequestingKeysList) {
        try {
          client.join();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

//      if (sharedData.holdingKeys.size() != clientThread.size()) {
//        continue;
//      }
//
//      sharedData.isExecutingCriticalSection = Boolean.TRUE;
//      return;
      if(sharedData.holdingKeys.size() == clientThread.size()) {
        sharedData.isExecutingCriticalSection = Boolean.TRUE;
        return;
      }
    }

  }

  public void exitCriticalSection(SharedData sharedData) {

    try {

      FileWriter myWriter =
          new FileWriter("Graduate-Studies/AdvanceOS/AOS-Project2/file.out", true);
      PrintWriter out = new PrintWriter(myWriter);
      out.println("Leaving critical section for Node :" + sharedData.nodeId + " with scalar clock :"
          + sharedData.csRequestTimestamp.get());
      out.close();
      myWriter.close();


    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    sharedData.isExecutingCriticalSection = Boolean.FALSE;
    sharedData.hasPendingCSRequest = Boolean.FALSE;
    

//    System.out.println("Leaving critical section for :" + sharedData.nodeId);

  }

}
