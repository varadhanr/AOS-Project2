package AOS.Project2;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.SctpChannel;

public class Client extends Thread {

  int nodeId;
  int nodeIdToContact;
  int portNumber;
  String hostName;
  SharedData sharedData;
  public int incomingSeqNumber;
  static final int MAX_MSG_SIZE = 4096;

  Client(int nodeId, int nodeIdToContact, int portNumber, String hostName, SharedData sharedData) {
    this.nodeId = nodeId;
    this.nodeIdToContact = nodeIdToContact;
    this.portNumber = portNumber;
    this.hostName = hostName;
    this.sharedData = sharedData;
  }

  @Override
  public void run() {

    // System.out.println(
    // "Starting client for : " + nodeId + " to: " + nodeIdToContact + " hostName: " + hostName);
    InetSocketAddress addr = new InetSocketAddress(hostName, portNumber);
    try {
      SctpChannel sc = SctpChannel.open(addr, 0, 0);

      MessageInfo messageInfo = MessageInfo.createOutgoing(null, 0);

      Message msg = new Message(nodeId, "Requesting for keys", sharedData.sequenceNumber,
          sharedData.scalarClock, sharedData.csRequestTimestamp, MsgType.REQUEST_KEY);

      // request for keys
      sc.send(msg.toByteBuffer(), messageInfo);

      ByteBuffer buf = ByteBuffer.allocateDirect(MAX_MSG_SIZE);

      // receive response from the server
      sc.receive(buf, null, null);

      Message msgBuf = Message.fromByteBuffer(buf);

      sharedData.scalarClock
          .getAndSet(Math.max(sharedData.scalarClock.get(), msgBuf.scalarClock.get()) + 1);

      sharedData.holdingKeys.add(msgBuf.nodeId);

      this.incomingSeqNumber = msgBuf.sequenceNumber;
      sc.close();


    } catch (Exception e) {
      e.printStackTrace();
    }

  }


}
