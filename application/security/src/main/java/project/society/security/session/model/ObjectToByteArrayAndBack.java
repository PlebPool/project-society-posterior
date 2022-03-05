package project.society.security.session.model;

import java.io.*;

public class ObjectToByteArrayAndBack {
    /**
     *
     * @param serializable {@link Serializable} object.
     * @return {@link byte[]}
     * @throws IOException Forwarded from {@link ObjectInputStream}.
     */
    public static byte[] objectToByteArray(Serializable serializable) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(serializable);
        oos.flush();
        return bos.toByteArray();
    }

    /**
     *
     * @param bytes {@link byte[]} representing an object of type {@link T}.
     * @param <T> Type of object represented by {@link byte[]}.
     * @return {@link T}
     * @throws IOException Forwarded from {@link ObjectInputStream}.
     * @throws ClassNotFoundException Forwarded from {@link ObjectInputStream}.
     */
    @SuppressWarnings("unchecked")
    public static <T> T byteArrayToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bin);
        return (T) ois.readObject();
    }
}
