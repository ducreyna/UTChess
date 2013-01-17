/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.data;

import java.io.FileInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;

/**
 *
 * @author fkouyate
 */


public class SerializationHashtable {

 /**
 * 
 *  This function allow you to read persistent hasthable object
 *
 * @param path path of the persistent hash table object
 *
 * @return Returns deserialized hasthable object
 */

    public HashtableProfile readHashtable(String path)
    {
        FileInputStream file;
        ObjectInputStream ois;
        HashtableProfile hashtableprofile;

        try {
            file = new FileInputStream(path);
            ois = new ObjectInputStream(file);
            hashtableprofile = (HashtableProfile) ois.readObject();
            return hashtableprofile;
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
        }

        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }
 /**
 
 * This function allow you to create a persistent object
  *
 * @param hashtableprofile The object to make persistant
 * @param path The path of file in which you want to store your persistent object
 * @return Returns a boolean true or throws an exception if the writing was aborted
 */
    public boolean writeHashtable(HashtableProfile hashtableprofile,String path){
        try {
            FileOutputStream file = new FileOutputStream(path+".ser");
            ObjectOutputStream oos = new ObjectOutputStream(file);
            oos.writeObject(hashtableprofile);
            oos.flush();
            oos.close();
            return true;
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return true;
    }



}
