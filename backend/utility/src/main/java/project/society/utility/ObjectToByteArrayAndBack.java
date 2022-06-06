package project.society.utility;

import java.io.*;

public class ObjectToByteArrayAndBack {
  /**
   * Turns an object into a byte array.
   *
   * @param serializable {@link Serializable} object.
   * @return {@link byte[]}
   */
  public static byte[] objectToByteArray(Serializable serializable) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos;
    try {
      oos = new ObjectOutputStream(bos);
    } catch (IOException e) {
      throw new RuntimeException("Failed to create " + ObjectOutputStream.class.getName());
    }
    try {
      oos.writeObject(serializable);
    } catch (IOException e) {
      throw new RuntimeException("Failed to serialize " + serializable);
    }
    try {
      oos.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return bos.toByteArray();
  }

  /**
   * Turns a byte array into an object.
   *
   * @param bytes {@link byte[]} representing an object of type {@link T}.
   * @param <T> Type of object represented by {@link byte[]}.
   * @return {@link T}
   * @throws IOException Forwarded from {@link ObjectInputStream}.
   * @throws ClassNotFoundException Forwarded from {@link ObjectInputStream}.
   */
  @SuppressWarnings("unchecked")
  public static <T> T byteArrayToObject(byte[] bytes) {
    ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
    ObjectInputStream ois;
    try {
      ois = new ObjectInputStream(bin);
    } catch (IOException e) {
      throw new RuntimeException(String.format("ObjectInputStream(%s) failed", bin));
    }
    try {
      return (T) ois.readObject();
    } catch (IOException e) {
      throw new RuntimeException("IOException reading ois.readObject();");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("ClassNotFoundException reading ois.readObject();");
    }
  }
}
