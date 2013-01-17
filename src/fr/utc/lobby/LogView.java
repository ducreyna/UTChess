package fr.utc.lobby;

import fr.utc.data.HashtableProfile;
import fr.utc.data.Profile;
import fr.utc.network.services.NetworkImplementation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Generation and management of the logView window It manage loading Profil and
 * connection to the network This this the first windows that have to be load
 *
 *
 * @author lo23a014
 */
public class LogView extends View
{
    private JLabel _labelLogo, _labelError;
    private InformationField _infoPseudo;
    private JButton _buttonConnexion, _buttonImport, _buttonCreate;
    private PasswordFieldCustom _passwordField;

    /**
     * Default constructor
     */
    public LogView(GameFrame frame)
    {
        super(LobbyConstant.LOG_VIEW_SIZE_H, LobbyConstant.LOG_VIEW_SIZE_W, frame);

        // Install
        installApp();

        //initialisation des components
        initComponents();

    }

    /**
     * Installation phase
     */
    private void installApp()
    {

        if (!HashtableProfile.hashtableExist())
        {
            /*DEBUG*/
            System.out.println("Installation Hashtable");
            HashtableProfile instance = new HashtableProfile();
            instance.writeHashtable();
        }
    }

    /**
     * Components initialization and positioning in the window
     */
    private void initComponents()
    {
        this.setLayout(new GridBagLayout());
        GridBagConstraints contrainte = new GridBagConstraints();


        /*--------------Partie haute de la fenetre avec le logo--------------*/
        _labelLogo = new JLabel(LobbyConstant.Resize(211, 224, LobbyConstant.chargeFichier(LobbyConstant.getPathLogo())));
        contrainte.fill = GridBagConstraints.BOTH;
        contrainte.anchor = GridBagConstraints.BASELINE_LEADING;
        contrainte.gridx = 1;
        contrainte.gridy = 0;
        contrainte.gridwidth = GridBagConstraints.REMAINDER;
        contrainte.gridheight = 1;
        contrainte.insets = new Insets(-100, 100, 0, 100);  // Marge à gauche et droite de 100 et marge au dessus de 10.
        this.add(_labelLogo, contrainte);


        /*--------------partie centrale de la fenetre avec la zone de saisie pseudo, mot de passe et le bouton connexion et message d'erreur--------------*/
        contrainte.gridwidth = GridBagConstraints.REMAINDER;
        contrainte.fill = GridBagConstraints.HORIZONTAL;
        contrainte.anchor = GridBagConstraints.BASELINE_LEADING;

        //field pseudo
        _infoPseudo = new InformationField("Pseudo", 16);
        contrainte.gridy = 1;
        contrainte.gridx = 1;
        contrainte.insets = new Insets(15, 10, 0, 10);  // Marge à gauche et droite de 10 et marge au dessus de 15.
        this.add(_infoPseudo, contrainte);

        //ActionListener pour la connexion Connexion
        TreatmentButtonConnexion traiterConnexion = new TreatmentButtonConnexion();
        //field password
        _passwordField = new PasswordFieldCustom("Mot de Passe", 16, traiterConnexion); //la methode de traitement est envoyer pour gerer la touche ENTER comme si on appuyait sur le bouton connexion
        contrainte.gridy = 2;
        contrainte.gridx = 1;
        this.add(_passwordField, contrainte);
        _infoPseudo.setMiniPrefSizeText(_passwordField.getMinimumSizeText(), _passwordField.getPreferredSizeText()); //permet d'avoir des taille de label identique


        //bouton connexion
        _buttonConnexion = new JButton("Connexion");
        contrainte.gridy = 3;
        contrainte.gridx = 1;
        contrainte.anchor = GridBagConstraints.CENTER;
        contrainte.fill = GridBagConstraints.NONE;
        _buttonConnexion.addActionListener(traiterConnexion);
        this.add(_buttonConnexion, contrainte);

        //label Error
        _labelError = new JLabel(" ");
        contrainte.gridy = 4;
        contrainte.gridwidth = GridBagConstraints.REMAINDER;
        contrainte.anchor = GridBagConstraints.CENTER;
        contrainte.fill = GridBagConstraints.NONE;
        contrainte.gridx = 1;
        cleanLabelError(); //vide le label d'erreur
        this.add(_labelError, contrainte);


        /*--------------partie basse de la fenetre avec les boutons importer et creer--------------*/
        contrainte.gridy = 5;
        contrainte.fill = GridBagConstraints.NONE;
        contrainte.anchor = GridBagConstraints.BASELINE_LEADING;
        contrainte.insets = new Insets(15, 10, 10, 10);
        contrainte.gridwidth = GridBagConstraints.RELATIVE;

        //button import
        _buttonImport = new JButton("Importer Profil");
        contrainte.gridx = 0;
        _buttonImport.addActionListener(new TreatmentButtonImport());
        this.add(_buttonImport, contrainte);

        //button creat
        _buttonCreate = new JButton("Creer Profil");
        contrainte.anchor = GridBagConstraints.BASELINE_TRAILING;
        contrainte.gridx = 2;
        _buttonCreate.setPreferredSize(_buttonImport.getPreferredSize()); //permet d'avoir une taille de bouton identique à celle du bouton importe
        _buttonCreate.setMinimumSize(_buttonImport.getMinimumSize()); //permet d'avoir une taille de bouton identique à celle du bouton importe
        _buttonCreate.addActionListener(new TreatmentButtonCreate());
        this.add(_buttonCreate, contrainte);

    }

    /**
     * Method clean the error label
     */
    public void cleanLabelError()
    {
        _labelError.setText(" ");
    }

    /**
     * Method set notification message
     *
     * @param nt Notification message that have to be set to the label
     */
    public void setNotification(String nt)
    {
        _labelError.setForeground(java.awt.Color.GRAY);
        _labelError.setText(nt);
    }

    /**
     * Method set error message
     *
     * @param e error message that have to be set to the label
     */
    public void setError(String e)
    {
        _labelError.setForeground(java.awt.Color.RED);
        _labelError.setText(e);
    }

    /**
     * Handler class for 'Import' button actions
     */
    private class TreatmentButtonImport implements ActionListener
    {
        /**
         * Handler method of the action performed on the button 'Import' Open a
         * dialog window for select the Profil folder witch you want import The
         * folder have to be in the same hard disk Required because the class
         * implements the interface ActionListener
         *
         * @param e event linking to the Action performed
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            cleanLabelError();
            String directory = new String("");
            JFileChooser fDial = new JFileChooser();
            fDial.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int retour = fDial.showOpenDialog(new JFrame());
            if (retour == JFileChooser.APPROVE_OPTION)
            {
                directory = fDial.getSelectedFile().getAbsolutePath();
                File fileImport = new File(directory + "/profile.ser");
                //verifier que le dossier contient un profil
                if (fileImport.exists())
                {
                    Profile.importP(directory);
                    setNotification("dossier de profil importé : " + directory);
                }
                else
                {
                    setError("Import impossible : Le dossier " + directory + " ne contient pas de profil (profile.ser).");
                }
            }
            else
            {
                setError("Aucun Profil importé");
            }

        }
    }

    /**
     * Handler class for 'Create' button actions
     */
    private class TreatmentButtonCreate implements ActionListener
    {
        /**
         * Handler method of the action performed on the button 'Creat' Open the
         * creat profile window Required because the class implements the
         * interface ActionListener
         *
         * @param e event linking to the Action performed
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            cleanLabelError(); //effacer le message d'erreur
            getParentFrame().loadView(new SetProfileView(getParentFrame()));
        }
    }

    /**
     * Handler class for 'Connexion' button actions and Enter Key from Password
     * field
     */
    private class TreatmentButtonConnexion implements ActionListener
    {
        /**
         * Handler method of the action performed on the button 'Connexion' Load
         * the profile and connect the app' to the network Required because the
         * class implements the interface ActionListener
         *
         * @param e event linking to the Action performed
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Profile player;// = getParentFrame().getUserProfile();  
            player = new Profile(_infoPseudo.getValue(), _passwordField.getValue());

            //suppression des messages d'erreurs
            cleanLabelError();

            //Verification de l'existance du profil et chargement du profil
            if (player.checkExist())
            {

                /*DEBUG*/
                System.out.println("ID profil " + player.getID());

                getParentFrame().setUserProfile(player);

                //Création de l'interface Reseau
                NetworkImplementation netInterface; // tout ce code ne doit être appelée qu'une fois
                try
                {
                    Profile test = getParentFrame().getUserProfile();

                    netInterface = new NetworkImplementation(test.getNetworkProfile());
                    netInterface.startThreadListenNetwork();
                    getParentFrame().setNetInterface(netInterface);

                    //création de la MainView
                    MainView myview = new MainView(getParentFrame());
                    netInterface.setIHMConnexionInterface(myview);
                    //chargement de la fenetre mainView
                    getParentFrame().loadView(myview);
                } catch (Exception ex)
                {
                    //Problème de connexion reseau
                    setError("Problème de connexion reseau");
                    ex.printStackTrace();
                }
            }
            else
            {
                setError("Pseudo ou mot de passe incorrect.");
            }
        }
    }
}
