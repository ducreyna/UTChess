/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package fr.utc.data;

/**
 *
 * @author lo23a023
 */
import java.io.File;
import java.util.*;
import java.io.Serializable;
import fr.utc.data.HashtableProfile;
import fr.utc.lobby.LobbyConstant;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Profile implements Serializable{
    
    protected String _surname;
    protected String _name;
    protected String _pseudo;
    protected String _pwd;
    protected int _age;
    protected boolean _inGame;
    protected String _id;
    protected String _avatar;
    protected ArrayList<Game> _gamesSaved;
    protected ArrayList<Game> _gamesCompleted;
    protected int _gamesWon;
    protected int _gamesLost;
    private static final long serialVersionUID = 1;
    public Profile(){}
    
    public Profile(String pseudo, String pwd)
    {
        this._pseudo = pseudo;
        this._pwd = pwd;
        this._gamesWon = 0;
        this._gamesLost = 0;
        this._gamesSaved = new ArrayList<Game> () ;
        this._gamesCompleted = new ArrayList<Game> ();
    }

    
    public void setAge(int _age) {
        this._age = _age;
    }

    public void setAvatar(String _avatar) {
        this._avatar = _avatar;
    }

    public void setGamesCompleted(Game _newGameCompleted) {
        this._gamesCompleted.add(_newGameCompleted);
        //this._gamesCompleted = _gamesCompleted;
    }

    public void setGamesSaved(Game _newGameSaved) {
        this._gamesSaved.add(_newGameSaved);
        //this._gamesSaved = _gamesSaved;
    }

    public void setInGame(boolean _inGame) {
        this._inGame = _inGame;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public void setPseudo(String _pseudo) {
        this._pseudo = _pseudo;
    }

    public void setPwd(String _pwd) {
        this._pwd = _pwd;
    }

    public void setSurname(String _surname) {
        this._surname = _surname;
    }
    /*
     * L'id est généré automatiquement lors de la creation du profil. Information interne a l'appli => l'user n'a pas a la modifier 
    public void setId(String id){
        this._id = id;
    }
    * */
    public int getAge() {
        return _age;
    }

    public String getAvatar() {
        return _avatar;
    }

    public ArrayList<Game> getGamesSaved() {
        return _gamesSaved;
    }

    public boolean isInGame() {
        return _inGame;
    }

    public String getName() {
        return _name;
    }

    public String getPseudo() {
        return _pseudo;
    }

    public String getPwd() {
        return _pwd;
    }

    public String getSurname() {
        return _surname;
    }

    public String getId(){
        return _id;
    }

   /**
     * getter GamesWon
     * @return number of lost game
     */
    public int getGamesLost()
    {
        return _gamesLost;
    }

        /**
     * getter GamesWon
     * @return number of won game
     */
    public int getGamesWon()
    {
        return _gamesWon;
    }

        /**
     * increment the number of lost games
     */
    public void incrGamesLost()
    {
        this._gamesLost++;
    }

       /**
     * increment the number of won games
     */
    public void incrGamesWon()
    {
         this._gamesWon++;
    }

     /**
     * getter ratio game won
     * @return ratio game won, 0 if no game played
     */
    public float getRatio()
    {
        if(_gamesWon+_gamesLost == 0)
        {   return 0; }
        else
        { return ((float)_gamesWon)/((float)_gamesWon+_gamesLost);
        }

    }

       /**
     * getter ID
     * @return the id of the profil
     */
    public String getID()
    {
        return _id;
    }

    public Profile (String pseudo, String pwd, String avatar, String surname, String name, int age)
    {
        this._id = fr.utc.data.Game.genererUUID().toString();
        this._pseudo = pseudo;
        this._pwd = pwd;
        this._avatar = avatar;
        this._surname = surname;
        this._name = name;
        this._age = age;
        this._gamesSaved = new ArrayList<Game> () ;
        this._gamesCompleted = new ArrayList<Game> ();
        this._gamesWon = 0;
        this._gamesLost = 0;
    }
    
    public Profile getNetworkProfile() {
        Profile tmp = new Profile();
        tmp._name = this._name;
        tmp._pseudo = this._pseudo;
       // tmp._pwd = this._pwd ;
        tmp._age = this._age;
        tmp._inGame = this._inGame;
        tmp._id = this._id;
        tmp._avatar = this._avatar;
        tmp._gamesWon = this._gamesWon;
        tmp._gamesLost = this._gamesLost;
        return tmp;
    }
    public boolean load(String pseudo,String mdp)
    {
        return true;
    }
    
    public static boolean importP(String path)
    {
        boolean res = false;
        String num; //directory number 
        String curDir = System.getProperty("user.dir"); //Get current directory path
        HashtableProfile hash = new HashtableProfile ();
        File f1 = new File (curDir+"\\hashtable.ser");
        if (f1.exists()){
            hash = fr.utc.data.HashtableProfile.readHashtable();
        }
        else {
            hash.writeHashtable();
        }
        try {
            File src = new File(path);
            ObjectInputStream ois =  new ObjectInputStream(new FileInputStream(src+"\\profile.ser")); // open stream on the file
             // deserialize object
            Profile p = (Profile)ois.readObject() ;
            System.out.println(p) ; // close stream
            ois.close();
            
            if (p.checkExist()) {//Profile already exists in hashtable
                num = hash.getHashtable(p._pseudo + "," + p._pwd);
                File f = new File(curDir + "\\" + num);
                f.delete(); //delete the old repertory           
                res = src.renameTo(new File(curDir + "\\" + num)); //rename the new repertory
            }
            else { //Add profile to hashtable
                num = hash.putHashtable(p._pseudo + "," + p._pwd);
                File dest = new File(curDir+"\\"+num);
                res = src.renameTo(dest); //rename the new directory
            }
        }

         catch (java.io.IOException e) {
            e.printStackTrace();
         }
         catch (ClassNotFoundException e) {
            e.printStackTrace();
         }

        return res;
    }
    
    public boolean exportP(String path)
    {
        boolean res;
        if (this.checkExist()){
            String curDir = System.getProperty("user.dir"); //Get current directory path
            String num; //directory number 
            HashtableProfile hash = new HashtableProfile ();
            hash = fr.utc.data.HashtableProfile.readHashtable();
            num = hash.getHashtable(this._pseudo + "," + this._pwd);
            File src = new File(curDir+"\\"+num);
            File dest = new File(path+"\\"+num);
            if (dest.exists()){
                dest.delete();
            }
            
            res = src.renameTo(dest);
            //Delete from hashtable
            hash.removeProfileHashtable(this._pseudo + "," + this._pwd);
        }
        else{
            JOptionPane jop3 = new JOptionPane();
            ImageIcon img = new ImageIcon(LobbyConstant.chargeFichier("images/erreur.png"));
            jop3.showMessageDialog(null, "Il n'y a pas de profil à exporter !", "Message d'erreur", JOptionPane.ERROR_MESSAGE,img);
            res = false;
        }
        
        return res;
    }
    
    public void modify(String pwd, String avatar,String name, String surname,String age)
    {
        HashtableProfile hashtable = new HashtableProfile();
        hashtable = fr.utc.data.HashtableProfile.readHashtable();
        /**
         * Password update 
         */
        if(!"".equals(pwd) ){
            this._pwd = pwd;
            hashtable.modifyKeyHashtable(this._pseudo+","+this._pwd,this._pseudo+","+pwd);
            hashtable.writeHashtable();
        }
         /**
         * Avatar update 
         */
        if(!"".equals(avatar)){
            this._avatar = avatar;
        }
         /**
         * Name update 
         */
        if(!"".equals(name)){
            this._name = name; 
        }
        /**
         * Surname update 
         */
        if(!"".equals(surname)){
            this._surname = surname;
        }
         /**
         * Age update 
         */
        if(!"".equals(age) ){
            this._age = Integer.parseInt(age);
        }
        this.writeProfile();
    }
    
    public ArrayList<Game>checkGamesNonCompletedWith(String id_ad,String pseudo_ad)
    {
        ArrayList<Game> result = new ArrayList<Game>();
        String curDir = System.getProperty("user.dir"); //Get current directory path
        HashtableProfile hash = new HashtableProfile ();
        hash = fr.utc.data.HashtableProfile.readHashtable();
        String num;
        num = hash.getHashtable(this._pseudo+","+this._pwd);
        String [] listefichiers;
        String [] gamename;
        int i;
        listefichiers= new File(curDir+"\\"+num+"\\GamesNonCompleted").list();
        for(i=0;i<listefichiers.length;i++){
            System.out.println("J'ai trouvé un fichier");
//            if(listefichiers[i].startsWith(pseudo_ad)==true){
                gamename = listefichiers[i].split("_");
                System.out.println("Voici le pseudo de l'adversaire qu'on cherche: " +gamename[0]);
                if(gamename[0].equals(pseudo_ad)){
                //opponent profile deserialization
                System.out.println("J'ai trouvé un fichier game appartenant a l'adversaire");
                try {
                    FileInputStream fichierAd = new FileInputStream(curDir+"\\"+num+"\\GamesNonCompleted\\"+listefichiers[i]+"\\profile.ser");
                    ObjectInputStream ois = new ObjectInputStream(fichierAd);
                    Profile ad = (Profile) ois.readObject();
                    System.out.println(ad) ; // close stream
                    System.out.println("id 1 " + ad._id);
                    System.out.println("id 2 " + id_ad);
                    if (ad._id.equals(id_ad)){
                        System.out.append("J'ai trouvé le fichier game de l'adversaire et je le deserialise");
                        //Game deserialization
                        FileInputStream gameToAdd = new FileInputStream(curDir+"\\"+num+"\\GamesNonCompleted\\"+listefichiers[i]+"\\game.ser");
                        ObjectInputStream newOis = new ObjectInputStream(gameToAdd);
                        Game game = (Game) newOis.readObject();
                        System.out.println(game) ; // close stream
                        result.add(game);
                    }        
                }
                catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }      
            }
        }
        return result;
    }
    
    public ArrayList<Game> loadGamesCompleted()
    {
        ArrayList<Game> result = new ArrayList<Game>();
        String curDir = System.getProperty("user.dir"); //Get current directory path
        HashtableProfile hash = new HashtableProfile ();
        hash = fr.utc.data.HashtableProfile.readHashtable();
        String num;
        num = hash.getHashtable(this._pseudo+","+this._pwd);
        String [] listefichiers;
        int i;
        listefichiers= new File(curDir+"\\"+num+"\\GamesCompleted").list(); 
        File f = new File(curDir + "\\" + num + "\\GamesCompleted");
        if (f.exists()) // We need to test if the directory (GamesCompleted) exist before performing iteration
        {
            for(i=0;i<listefichiers.length;i++){
                try{ 
                    //Game deserialization
                     FileInputStream gameToAdd = new FileInputStream(curDir+"\\"+num+"\\GamesCompleted\\"+listefichiers[i]+"\\game.ser");
                     ObjectInputStream newOis = new ObjectInputStream(gameToAdd);
                     Game game = (Game) newOis.readObject();
                     System.out.println(game) ; // close stream
                     result.add(game);
                }
                catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }      
            }
        }    
        return result;
    }
    
    public ArrayList<Game> loadGamesNonCompleted()
    {
        ArrayList<Game> result = new ArrayList<Game>()  ;
        String curDir = System.getProperty("user.dir"); //Get current directory path
        HashtableProfile hash = new HashtableProfile ();
        hash = fr.utc.data.HashtableProfile.readHashtable();
        String num;
        num = hash.getHashtable(this._pseudo+","+this._pwd);
        String [] listefichiers;
        int i;
        listefichiers= new File(curDir+"\\"+num+"\\GamesNonCompleted").list(); 
        File f = new File(curDir + "\\" + num + "\\GamesNonCompleted");
        if (f.exists()) // We need to test if the directory (GamesCompleted) exist before performing iteration
        {       
            for(i=0;i<listefichiers.length;i++){
                try{ 
                    //Game deserialization
                     FileInputStream gameToAdd = new FileInputStream(curDir+"\\"+num+"\\GamesNonCompleted\\"+listefichiers[i]+"\\game.ser");
                     ObjectInputStream newOis = new ObjectInputStream(gameToAdd);
                     Game game = (Game) newOis.readObject();
                     System.out.println(game) ; // close stream
                     result.add(game);
                }
                catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }      
            }
        }    
        return result;
    }
    
    
    public ArrayList<Game> getGamesNonCompleted()
    {
        return this._gamesSaved;
    }
    
    public ArrayList<Game> getGamesCompleted()
    {
        return this._gamesCompleted;
    }
    public boolean saveProfileIn(String Path){
          try {
                //Serialization
                FileOutputStream fichierP = new FileOutputStream(Path + "\\profile.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fichierP);
                oos.writeObject(this);
                oos.flush();
                oos.close();
            }
            catch (java.io.IOException e) {
                e.printStackTrace();  
            }
        return true;
    }
    public boolean writeProfile()
    {
        String curDir = System.getProperty("user.dir"); //Get current directory path
        HashtableProfile hash = new HashtableProfile ();
        hash = fr.utc.data.HashtableProfile.readHashtable();
        if (hash.containsKeyHashtable(this._pseudo+ ","+this._pwd)){//Profile already exists in hash table
            String num = hash.getHashtable(this._pseudo + "," + this._pwd); //get the repertory number/name
            File old = new File(curDir + "\\" + num + "\\profile.ser");
            old.delete(); //Delete the old profile file
            try {
                //Serialization
                FileOutputStream fichierP = new FileOutputStream(curDir + "\\" + num + "\\profile.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fichierP);
                oos.writeObject(this);
                oos.flush();
                oos.close();
                fichierP.close();
                hash.writeHashtable();
            }
            catch (java.io.IOException e) {
                e.printStackTrace();  
            }
        }
        
        else{ //New profile
            try {
                File dir = new File (curDir+"\\"+"nouveau"); 
                dir.mkdir(); //Create a new repertory
                String num = hash.putHashtable(this._pseudo + "," + this._pwd); //get a new repertory number/name
               
                dir.renameTo(new File(curDir + "\\" + num)); //rename the new directory 
                //Serialization
                FileOutputStream fichierP = new FileOutputStream(curDir+"\\"+num+"\\profile.ser");
                 //FileOutputStream fichierP = new FileOutputStream(curDir+"\\nouveau\\profile.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fichierP);
                oos.writeObject(this);
                oos.flush();
                oos.close();
                fichierP.flush();
                fichierP.close();
                
            }
            
            catch (java.io.IOException e) {
                e.printStackTrace();
            }

        }
        return true;
    }
    
    public boolean checkExist()
    {
       String login_pwd = this._pseudo+","+this._pwd;
       HashtableProfile hashtable = new HashtableProfile();
       hashtable = fr.utc.data.HashtableProfile.readHashtable();
       if(!hashtable.containsKeyHashtable(login_pwd)){
           return false;
       }
       
       else{
           String profile_path ; 
           profile_path = hashtable.getHashtable(login_pwd);
           System.out.println(profile_path);
           loadProfile(profile_path);
           return true;
       }
        
    }
    
    private boolean loadProfile(String pathToProfile)
    {
        FileInputStream file;
        ObjectInputStream ois;
        Profile profile;

        try {
            String curDir = System.getProperty("user.dir"); //Get current directory path        
            file = new FileInputStream(curDir+"\\"+pathToProfile+"\\profile.ser");
            ois = new ObjectInputStream(file);
            profile = (Profile) ois.readObject();
            this._surname = profile._surname;
            this._name = profile._name;
            this._age = profile._age;
            this._inGame = profile._inGame;
            this._id = profile._id;
            this._avatar = profile._avatar;
            this._gamesSaved = profile.loadGamesNonCompleted();
            this._gamesCompleted = profile.loadGamesCompleted();
            this._gamesWon = profile._gamesWon;
            this._gamesLost = profile._gamesLost;
            file.close();
            ois.close();
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
        }

        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

            
        return true;
    }
}

