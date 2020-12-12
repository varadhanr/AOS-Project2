package AOS.Project2;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ApplicationImpl {

  public Node node;
  SharedData sharedDataInstance;

  MutexImpl mutex = new MutexImpl();

  public ApplicationImpl(Node node) {
    this.node = node;
    this.sharedDataInstance = SharedData.getInstance(node.nodeId, node.totalNumberOfNodes);
  }

  public void execute() {

    Random r1 = new Random();
    Random r2 = new Random();
    List<Double> interRequestDelaySeries = new ArrayList<Double>();
    List<Double> executionTimeSeries = new ArrayList<Double>();
    int totalNumberOfRequest = node.totalNumberOfRequest;
    int meanInterDelayRequest = node.interRequestDelay;
    int meanExecutionTime = node.csExecutionTime;

    for (int i = 0; i < totalNumberOfRequest; i++) {
      interRequestDelaySeries.add(r1.nextGaussian() + meanInterDelayRequest);
    }

    for (int i = 0; i < totalNumberOfRequest; i++) {
      executionTimeSeries.add(r2.nextGaussian() + meanExecutionTime);
    }

    node.createServer(sharedDataInstance);

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      throw new RuntimeException("Error while thread sleep");
    }

    // System.out.println("Total number of request :" + totalNumberOfRequest);

    for (int i = 0; i < totalNumberOfRequest; i++) {

      System.out.println("Request : " + i + " for node " + node.nodeId);
      // System.out.println("Request : " + i);

     // long startTime = System.currentTimeMillis();

      sharedDataInstance.hasPendingCSRequest = Boolean.TRUE;

      sharedDataInstance.csRequestTimestamp.getAndSet(sharedDataInstance.scalarClock.get());

      List<Client> clientThread = node.initializeClientThreads(sharedDataInstance);

      csEnter(node, sharedDataInstance);

      csExecute(clientThread, sharedDataInstance, executionTimeSeries.get(i));

      csLeave(sharedDataInstance);

    //  long endTime = System.currentTimeMillis() - startTime;
    //  if (interRequestDelaySeries.get(i).longValue() - endTime > 0) {
        try {
          Thread.sleep(interRequestDelaySeries.get(i).longValue());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
   //   }



    }



  }

  public void csEnter(Node node, SharedData sharedData) {

    mutex.enterCriticalSection(node, sharedData);

  }

  public void csLeave(SharedData sharedData) {

    mutex.exitCriticalSection(sharedData);

  }

  public void csExecute(List<Client> clientThread, SharedData sharedData, Double csExecutionTime) {

    try {
      FileWriter myWriter =
          new FileWriter("Graduate-Studies/AdvanceOS/AOS-Project2/file.out", true);
      PrintWriter out = new PrintWriter(myWriter);
      out.println("Holidng Keys : " + sharedData.holdingKeys.toString());
      out.println("Entered critical section for Node :" + sharedData.nodeId
          + " with scalar clock :" + sharedData.csRequestTimestamp.get());
      out.close();
      myWriter.close();
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    // long startTime = System.currentTimeMillis();
    // System.out.println("Entering critical section for :" + node.nodeId);
    // int maxSequenceNumber = sharedData.sequenceNumber;
    // for (Client c : clientThread) {
    // maxSequenceNumber = Math.max(maxSequenceNumber, c.incomingSeqNumber);
    // }
    //
    // maxSequenceNumber = maxSequenceNumber + 1;
    // // write it to a file for testing
    // sharedData.sequenceNumber = maxSequenceNumber;


    try {
      Thread.sleep(csExecutionTime.longValue());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

}
