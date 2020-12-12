package AOS.Project2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SharedData {

  private static SharedData shared_instance = null;
  public int nodeId;
  public int totalNumberOfNodes;
  public AtomicInteger scalarClock;
  public AtomicInteger csRequestTimestamp;
  public int sequenceNumber;
  public boolean isExecutingCriticalSection;
  public boolean hasPendingCSRequest;
  public BlockingQueue<Integer> holdingKeys;

  private SharedData(int nodeId, int totalNumberOfNodes) {
    this.nodeId = nodeId;
    scalarClock = new AtomicInteger(nodeId);
    csRequestTimestamp = new AtomicInteger(nodeId);
    sequenceNumber = 0;
    isExecutingCriticalSection = Boolean.FALSE;
    hasPendingCSRequest = Boolean.FALSE;
    holdingKeys = new ArrayBlockingQueue<Integer>(1000);
    this.totalNumberOfNodes = totalNumberOfNodes;
    
    for(int i=nodeId + 1;i<totalNumberOfNodes;i++) {
      holdingKeys.add(i);
    }

  }

  public static SharedData getInstance(int nodeId, int totalNumberOfNodes) {
    if (shared_instance == null)
      shared_instance = new SharedData(nodeId, totalNumberOfNodes);

    return shared_instance;
  }


}
