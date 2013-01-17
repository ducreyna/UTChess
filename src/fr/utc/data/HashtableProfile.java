/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author fkouyate
 */



public class HashtableProfile implements Serializable {

    protected Hashtable _profileTable;
    private static final long serialVersionUID = 1;

    public HashtableProfile (){

        _profileTable = new Hashtable();
        

    }
/**
 * Add profile to hashtable
 *
 *
 * @param key - concatenation of the login and the password ("pseudo,mdp")
 * @param value the profile path
 * @return String return the incremented path number 
 */
    
    public String putHashtable(String key){
        int max = getmaxvalue();String value;
        max++; 
        value = String.valueOf(max);
        this._profileTable.put(key,value);
        this.writeHashtable();
        return value;      
    }
    public int getmaxvalue(){
        
         Enumeration e = this._profileTable.elements();
         int max = 0; int tmp = 0 ; String tmp2;
         String value;       
         while( e.hasMoreElements()) {
             tmp2 = (String)e.nextElement();
             tmp = Integer.parseInt(tmp2);
             if( tmp > max){ 
                 max = tmp; 
             }               
         }
         return max;
    }
    
/**
 * Return the profile path
 *
 * @param key - concatenation of the login and the password ("pseudo,mdp")
 * @return  the profile path
 */
    public String getHashtable(String key){

        return (String)this._profileTable.get(key);
    }
    
    public boolean containsKeyHashtable(String key){
        return this._profileTable.containsKey(key);
    }
    
    public boolean containsValueHashtable(String value){
        return this._profileTable.containsValue(value);
    }
/**
 * Modify hashtable key
 * 
 * PS: Upstream processing of the modication (traitement en amont si modification mot de passe / pseudo)
 *
 *
 * @param key_old Old Key ("login,password")
 * @param key_new New Key ("login,password")
 * @return  boolean false if hashtable does not contain the key
 */
    public boolean modifyKeyHashtable(String key_old, String key_new){
        String tmp_value;
        if(this._profileTable.containsKey(key_old)){
            tmp_value = (String) this._profileTable.get(key_old);
            this._profileTable.remove(key_old);
            this._profileTable.put(key_new,tmp_value);
            this.writeHashtable();
            return true;
        }
        return false;// The key is not in the hashtable
    }
/**
 * Mofify hashtable value
 * @param key_old  Old Key ("login,password")
 * @param value_new New Key ("login_new,password_new")
 * @return  boolean
 */
    public boolean modifyValueHashtable(String key, String value_new){
        if(Integer.parseInt(value_new) < getmaxvalue()){
            return false;
        }
        if(this._profileTable.containsKey(key)){
            this._profileTable.remove(key);
            this._profileTable.put(key,value_new);
            this.writeHashtable();
            return true;
        }
        return false;// The key is not in the hashtable
    }
/**
 *  Delete an item from the hashtable
 *
 * @param key  Key ("login,password")
 * @return  void
 */
    public void removeProfileHashtable(String key){
        this._profileTable.remove(key);
        this.writeHashtable();
    }
    /**
 *  Test if the hashtable was created 
 *
 * @param 
 * @return  true it's ok and false if it was not created
 */
    
    public static boolean hashtableExist(){
        String curDir = System.getProperty("user.dir");
        File f = new File(curDir + "\\hashtable.ser");
        return f.exists();
    
    }
    
    
    /**
 
 * This function allow you to create a persistent object
  *
 * @param hashtableprofile The object to make persistant
 * @param path The path of the file in which you want to store your persistent object
 * @return Returns a boolean true or throws an exception if the writing was aborted
 */
 public boolean writeHashtable(){
     String curDir = System.getProperty("user.dir"); //Get current directory path   
        try {
            FileOutputStream file = new FileOutputStream(curDir+ "\\" + "hashtable.ser" );
            ObjectOutputStream oos = new ObjectOutputStream(file);
            oos.writeObject(this);
            oos.flush();
            oos.close();
            return true;
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return true;
    }
 /**
 * 
 *  This function allow you to read persistent hasthable object
 *
 * @param path path of the persistent hash table object
 *
 * @return Returns deserialized hasthable object
 */

    public static HashtableProfile readHashtable()
    {
        String curDir = System.getProperty("user.dir"); //Get current directory path
        FileInputStream file;
        ObjectInputStream ois;
        HashtableProfile hashtableprofile;

        try {
            file = new FileInputStream(curDir + "\\" +"hashtable.ser" );
            ois = new ObjectInputStream(file);
            hashtableprofile = (HashtableProfile) ois.readObject();
            hashtableprofile.afficheHashtable();
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
    public void afficheHashtable(){
        System.out.println(this._profileTable.toString());
//        Enumeration e1 = this._profileTable.elements();
//        System.out.println("Contenu de la hashtable");
//        while (e1.hasMoreElements()){
//            System.out.println(e1.nextElement());
//        }
    }
}
