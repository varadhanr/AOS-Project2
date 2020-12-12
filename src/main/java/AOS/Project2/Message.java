package AOS.Project2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;


public class Message implements Serializable {

  private static final long serialVersionUID = 1L;

  public Integer nodeId;
  public String message;
  public Integer sequenceNumber;
  public AtomicInteger scalarClock;
  public AtomicInteger csRequestTimestamp;
  public MsgType msgType;

  public Message(Integer nodeId, String msg, Integer sNum, AtomicInteger scalarClock,
      MsgType msgType) {
    this.nodeId = nodeId;
    this.message = msg;
    this.sequenceNumber = sNum;
    this.scalarClock = scalarClock;
    this.msgType = msgType;
  }

  public Message(Integer nodeId, String msg, Integer sNum, AtomicInteger scalarClock,
      AtomicInteger csRequestTimestamp, MsgType msgType) {
    this.nodeId = nodeId;
    this.message = msg;
    this.sequenceNumber = sNum;
    this.scalarClock = scalarClock;
    this.csRequestTimestamp = csRequestTimestamp;
    this.msgType = msgType;
  }

  public ByteBuffer toByteBuffer() throws Exception {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeObject(this);
    oos.flush();

    ByteBuffer buf = ByteBuffer.allocateDirect(bos.size());
    buf.put(bos.toByteArray());

    oos.close();
    bos.close();

    buf.flip();
    return buf;
  }


  public static Message fromByteBuffer(ByteBuffer buf) throws Exception {

    buf.flip();
    byte[] data = new byte[buf.limit()];
    buf.get(data);
    buf.clear();

    ByteArrayInputStream bis = new ByteArrayInputStream(data);
    ObjectInputStream ois = new ObjectInputStream(bis);
    Message msg = (Message) ois.readObject();

    bis.close();
    ois.close();

    return msg;
  }
}
