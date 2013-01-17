/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.data;
import fr.utc.lobby.LobbyConstant;
import javax.swing.*;
import java.io.*;

/**
 *
 * @author lo23a023
 */
public class CopyRepertory
{
    String src,dest;
    private File initial, laCopie;
    private JFileChooser chooser = null; //création d'un nouveau filechooser
    boolean creerDossier=true;
    private Icon image = new ImageIcon((LobbyConstant.chargeFichier(".Image/question.png")));
    
    /** Copie le fichier source dans le fichier résultat
    * retourne vrai si cela réussit
    */

    private boolean CopierLaBase( File source, File destination )
    {
        boolean resultat = false;
        //Déclaration des streams d'entrée sortie
        FileInputStream sourceFile=null;
        FileOutputStream destinationFile=null;
        try{
            // Création du fichier :
            destination.createNewFile();
            // Ouverture des flux
            sourceFile = new FileInputStream(source);
            destinationFile = new FileOutputStream(destination);

            // Lecture par segment de 0.5Mo
            byte buffer[]=new byte[512*1024];
            int nbLecture;

            while( (nbLecture = sourceFile.read(buffer)) != -1 ){
                destinationFile.write(buffer, 0, nbLecture);
            }
            // si tout va bien
            resultat = true;
        }
        catch( java.io.FileNotFoundException f ) {}
        catch( java.io.IOException e ) {}

        finally{
            //On ferme les flux
            try{
                sourceFile.close();
                destinationFile.close();
            }
            catch(Exception e) { }

        }
        return( resultat );

    }

    public CopyRepertory(String src, String dest)
    {
        this.src=src;
        this.dest=dest;
        this.initial = new File (src);
        this.laCopie = new File (dest);
        if (chooser == null){
            chooser = new JFileChooser ();
        }
        //On associe la copie de notre fichier au JFileChooser
        chooser.setSelectedFile(laCopie);
        // Si le répertoire de destination n'existe pas, et que notre source est un repertoire
        if (!laCopie.exists()){
            if (initial.isDirectory()){
                //Si le dossier est celui que l'on doit copier

                
            //   if(dest.equals(BarreDeMenu.getNomDossierDestination())){



                    if (chooser.showSaveDialog (null) == JFileChooser.APPROVE_OPTION){
                        //Si le dossier de destination existe déjà à l'endroit spécifié
                        if (chooser.getSelectedFile().exists()){
                            int reponse=JOptionPane.showConfirmDialog (null, "Le dossier "+chooser.getSelectedFile().getAbsolutePath()+" existe déjà!\n Voulez-vous le remplacer?", "Attention", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,image);
                            //A moins que l'utilisateur ne confirme l'écrasement du dossier existant
                            if(reponse==JOptionPane.YES_OPTION){
                                //Création du dossier de destination
                                chooser.getSelectedFile().mkdir();
                            }
                            if(reponse==JOptionPane.NO_OPTION){
                                creerDossier=false;
                            }
                        }
                        else{
                            //Création du dossier de destination s'il n'existe pas
                            chooser.getSelectedFile().mkdir();
                        }
                    }
               // }

                //Si ce sont les sous-dossiers
                else{
                    //On crée les sous-dossiers s'il n'existe pas
                    chooser.getSelectedFile().mkdir();
                }
            }

        }
        // Si c'est un fichier, on fait un simple copie
        if (initial.isFile()){
            if(creerDossier){
                CopierLaBase(initial, laCopie);
            }
        }
        // et si notre source est un répertoire que l'on doit copier
        else if (initial.isDirectory()){
            // on parcourt tout les éléments du répertoire
            for (File f:initial.listFiles()){
                if(creerDossier){
                    // on fait un appel récursif à cette classe en mettant à jour les chemins de source et de destination
                  //this.CopierLaBase(f.getAbsolutePath(),chooser.getSelectedFile().getAbsoluteFile()+"/"+f.getName());
         
                    this.CopierLaBase(f.getAbsoluteFile(), chooser.getSelectedFile().getAbsoluteFile());
                }
            }
        } 
    }
}
