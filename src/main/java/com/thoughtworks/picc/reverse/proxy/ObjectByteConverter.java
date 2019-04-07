package com.thoughtworks.picc.reverse.proxy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class ObjectByteConverter {

    public static Object fromString(String byteString)  {
        byte [] data = Base64.getDecoder().decode(byteString);
        try(ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(  data ) )) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static String toString(Object obj) throws IOException {
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream( byteArrayOutputStream )) {
            oos.writeObject(obj);
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        }
    }
}
