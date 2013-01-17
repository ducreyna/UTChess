/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.network.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 *
 * @author Robi
 */
public class Utils
{
    /**
     * Converts an array of bytes into an Object
     * @param bytes : array of bytes
     * @return The corresponding object
     */
    public static synchronized Object byteArrayToObject(byte[] bytes) throws IOException, ClassNotFoundException
    {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = new ObjectInputStream(bis);
        Object o = in.readObject();
        bis.close();
        in.close();
        return o;
    }
    
    
     /**
     * transforms an object into an array of bytes to be sent by UDP
     * @param o : The object to be transformed
     */
    public static synchronized byte[] objectToByteArray(Object o) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(o);
        byte[] yourBytes = bos.toByteArray();
        out.close();
        bos.close();
        return yourBytes;
    }
}
