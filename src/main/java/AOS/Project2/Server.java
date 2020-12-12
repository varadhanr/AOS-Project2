package AOS.Project2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.SctpChannel;
import com.sun.nio.sctp.SctpServerChannel;


public class Server extends Thread {

  int nodeId;
  int portNumber;

  int MAX_MSG_SIZE = 4096;

  SharedData sharedData;

  Server(int nodeId, int portNumber, SharedData sharedData) {
    this.nodeId = nodeId;
    this.portNumber = portNumber;
    this.sharedData = sharedData;
  }

  @Override
  public void run() {
    InetSocketAddress address = new InetSocketAddress(portNumber);

    try {
      // System.out.println(" Server started for node " + nodeId + " with address : " + portNumber);
      SctpServerChannel serverChannel = SctpServerChannel.open();
      serverChannel.bind(address);

      while (true) {

        SctpChannel sctpChannel = serverChannel.accept();
        ServerThread s = new ServerThread(sctpChannel, nodeId);
        s.start();

        // System.out.println("Client connected");

      }
    } catch (IOException e) {
      System.out.println("Exception in server Thread" + e);
    }
  }

  public class ServerThread extends Thread {

    SctpChannel sctpChannel;
    int nodeId;

    public ServerThread(SctpChannel sctpChannel, int nodeId) {
      this.sctpChannel = sctpChannel;
      this.nodeId = nodeId;
    }

    public void run() {
      ByteBuffer buf = ByteBuffer.allocateDirect(MAX_MSG_SIZE);
      try {

//        while (true) {

          sctpChannel.receive(buf, null, null);

          MessageInfo messageInfo = null;
          Message msgBuf = Message.fromByteBuffer(buf);

          sharedData.scalarClock
              .getAndSet(Math.max(sharedData.scalarClock.get(), msgBuf.scalarClock.get()) + 1);

//          System.out.println(
//              "Key request came from :" + msgBuf.nodeId + " with " + msgBuf.scalarClock.get()
//                  + " current node :" + nodeId + " with " + sharedData.scalarClock.get());

          while (sharedData.hasPendingCSRequest
              && (msgBuf.csRequestTimestamp.get() > sharedData.csRequestTimestamp.get()
                  || sharedData.isExecutingCriticalSection)) {
            
            Thread.sleep(100);

          }

            messageInfo = MessageInfo.createOutgoing(null, 0);
            sharedData.holdingKeys.remove(msgBuf.nodeId);
            // sharedData.scalarClock
            // .getAndSet(Math.max(sharedData.scalarClock.get(), msgBuf.scalarClock.get()) + 1);
            Message sndMsg = new Message(this.nodeId, "sending back keys",
                sharedData.sequenceNumber, sharedData.scalarClock, MsgType.RESPONSE_KEY_SUCCESS);
            sctpChannel.send(sndMsg.toByteBuffer(), messageInfo);
            
            sctpChannel.close();
      } catch (

      Exception e) {
        e.printStackTrace();
      }



    }
  }
}
