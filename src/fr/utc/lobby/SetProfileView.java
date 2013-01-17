package fr.utc.lobby;

import fr.utc.data.*;
import fr.utc.network.exceptions.ErrorSendingProfileException;
import fr.utc.network.services.NetworkImplementation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Xavier Rodriguez
 * @class SetProfileView
 *
 * This class provide the interface of the form to create or modified a profile
 */
public class SetProfileView extends View
{
    // Management
    private boolean _newProfile;
    // Panels 
    private JPanel _leftPan;
    private JPanel _rightPan;
    // Components
    private JLabel _JLtitle;
    private JLabel _JLavatar;
    private JButton _JBcancel;
    private JButton _JBsave;
    private JComboBox _JCavatarChoice;
    private List<InformationField> _IFinfoList;
    private PasswordFieldCustom _psw;
    private PasswordFieldCustom _pswConf;
    private JLabel _JLinfo;
    ////////////////////////////////////////////////////////////////////////////
    /////////////////////////// CONSTRUCTORS ///////////////////////////////////

    /**
     * Conctructor
     *
     * @param frame in which this panel is displayed
     */
    SetProfileView(GameFrame frame)
    {
        super(LobbyConstant.SET_PROFILE_VIEW_SIZE_H,
                LobbyConstant.SET_PROFILE_VIEW_SIZE_W, frame);
        this.initComponents();
    }
    ////////////////////////////////////////////////////////////////////////////
    ///////////////////////////// METHODS //////////////////////////////////////

    /**
     * Components Initilization
     */
    private void initComponents()
    {
        // Components creation
        this.createComp();
        // Properties setting
        this.setCompProperties();
        // Add components in the panel
        this.addComp();
    }

    /**
     * Creation of components
     */
    private void createComp()
    {
        // Creation
        this._leftPan = new JPanel();
        this._rightPan = new JPanel();

        this._JLtitle = new JLabel("Votre Profil :");
        this._JLavatar = new JLabel("");
        this._JBcancel = new JButton("Annuler");
        this._JBsave = new JButton("Enregistrer");

        this._JLinfo = new JLabel("");

        // Creation of the avatar selection combo box
        this.createComboBoxAvatar();

        // Password info
        this._psw = new PasswordFieldCustom("Mot de passe");
        this._pswConf = new PasswordFieldCustom("Confirmation");

        this._IFinfoList = new ArrayList<InformationField>();
        this._IFinfoList.add(new InformationField("Nom"));
        this._IFinfoList.add(new InformationField("Prénom"));
        this._IFinfoList.add(new InformationField("Age"));
        this._IFinfoList.add(new InformationField("Pseudo"));
    }

    /**
     * Avatar Combo box creation
     */
    private void createComboBoxAvatar()
    {
        this._JCavatarChoice = new JComboBox();
        this._JCavatarChoice.addActionListener(new actionComboBoxAvatar(this));
        
      /*  File file = null;
        try
        {
            file = new File(LobbyConstant.chargeFichier(LobbyConstant.PATH_PICTURE_AVATAR).toURI());  //NE FONCTIONNE PAS AVEC LE JAR
        } catch (URISyntaxException ex)
        {
            Logger.getLogger(SetProfileView.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (file == null)
        {
            System.err.print("Erreur : "  + LobbyConstant.PATH_PICTURE_AVATAR + " non trouvé");
        }

        File[] files = file.listFiles();
        if (files != null)
        {
            for (int i = 0; i < files.length; i++)
            {
                if (files[i].getName().compareToIgnoreCase(".svn") != 0)
                {
                    String str = files[i].getName();
                    if (LobbyConstant.isAutorized(str))
                    {
                        str = str.substring(0, str.lastIndexOf('.'));
                        this._JCavatarChoice.addItem(str);
                    }

                }
            }
        }*/
        
            for (int i = 0; i < LobbyConstant.LIST_AVATAR.length; i++)
            {
                    String str = LobbyConstant.LIST_AVATAR[i];
                    if (LobbyConstant.isAutorized(str))
                    {
                        str = str.substring(0, str.lastIndexOf('.'));
                        this._JCavatarChoice.addItem(str);
                    }

            }
  
        
    }

    /**
     * Initialize the properties of components
     */
    private void setCompProperties()
    {
        // Set properties
        for (int i = 0; i < this._IFinfoList.size(); i++)
        {
            this._IFinfoList.get(i).setMiniPrefSizeText(new Dimension(50, 25), new Dimension(50, 25));
            this._IFinfoList.get(i).setSizeTextField(new Dimension(100, 25));
            this._IFinfoList.get(i).setPreferredSize(new Dimension(380, 30));
        }

        this._psw.setMiniPrefSizeText(new Dimension(50, 25), new Dimension(50, 25));
        this._psw.setSizeTextField(new Dimension(100, 25));
        this._psw.setPreferredSize(new Dimension(380, 30));

        this._pswConf.setMiniPrefSizeText(new Dimension(50, 25), new Dimension(50, 25));
        this._pswConf.setSizeTextField(new Dimension(100, 25));
        this._pswConf.setPreferredSize(new Dimension(380, 30));

        // Listener
        this._IFinfoList.get(0).getTextField().addFocusListener(new focusName(this));
        this._IFinfoList.get(1).getTextField().addFocusListener(new focusPrenom(this));
        this._IFinfoList.get(2).getTextField().addFocusListener(new focusAge(this));
        this._IFinfoList.get(3).getTextField().addFocusListener(new focusPseudo(this));

        this._psw.getTextField().addFocusListener(new focusPassword(this));
        this._pswConf.getTextField().addFocusListener(new focusPasswordBis(this));

        this._JBsave.addActionListener(new buttonSave(this));
        this._JBcancel.addActionListener(new buttonCancel(this));

        // Check where we come from
        Object lastview = this.getParentFrame().getLastView();
        Profile player = this.getParentFrame().getUserProfile();

        // Here the loading of the last profile or the creation of a new one
        if (player != null)
        {
            this._newProfile = false;
            this._JLinfo.setText("Profile trouvé");
            this._JLinfo.setForeground(java.awt.Color.GREEN);

            this._IFinfoList.get(0).getTextField().setText(player.getName());
            this._IFinfoList.get(1).getTextField().setText(player.getSurname());
            this._IFinfoList.get(2).getTextField().setText(String.valueOf(player.getAge()));
            this._IFinfoList.get(3).getTextField().setText(player.getPseudo());

            this._IFinfoList.get(3).getTextField().setEditable(false);

            this._psw.getTextField().setText(player.getPwd());
            this._pswConf.getTextField().setText(player.getPwd());

            this._JCavatarChoice.setSelectedIndex(getIndexOfPicture(player.getAvatar()));
            this.loadAvatar(importName(player.getAvatar()));
        }
        else
        {
            this._newProfile = true;
            this._JLinfo.setText("Création d'un nouveau profil");
            this._JLinfo.setForeground(java.awt.Color.RED);
        }
    }

    /**
     * Recover the index picture in the combo box from the name images
     *
     * @return
     */
    private int getIndexOfPicture(String pic)
    {
        File file = new File(LobbyConstant.PATH_PICTURE_AVATAR);
        if (file == null)
        {
            System.err.print("Erreur : " + LobbyConstant.PATH_PICTURE_AVATAR + " non trouvé");
        }

        File[] files = file.listFiles();
        if (files != null)
        {
            for (int i = 0; i < files.length; i++)
            {
                if (files[i].getName().compareToIgnoreCase(pic + ".png") == 0)
                {
                    return i;
                }
            }
        }

        return 0;
    }

    /**
     * to save a profile lol mdr xpdr kikoulol loulilol
     */
    private void saveProfile()
    {
        // infos
        String name = this._IFinfoList.get(0).getTextField().getText();
        String surname = this._IFinfoList.get(1).getTextField().getText();
        int age = Integer.valueOf(this._IFinfoList.get(2).getTextField().getText());
        String pseudo = this._IFinfoList.get(3).getTextField().getText();
        String psw = this._psw.getTextField().getText();
        String avat = this.getAvatarSelected();

        // Create or update a profile
        Profile player;
        if (this._newProfile)
        {
            player = new Profile(pseudo, psw, avat, surname, name, age);
            this.getParentFrame().setUserProfile(player);

            // write with data
            player.writeProfile();
        }
        else
        {
            player = this.getParentFrame().getUserProfile();

            // find the change
            String agebis = "";
            if (player.getName().compareTo(name) == 0)
            {
                name = "";
            }
            if (player.getSurname().compareTo(surname) == 0)
            {
                surname = "";
            }
            if (player.getPwd().compareTo(psw) == 0)
            {
                psw = "";
            }
            else if (player.getAvatar().compareTo(avat) == 0)
            {
                avat = "";
            }
            if (player.getAge() == age)
            {
            }
            else
            {
                agebis = String.valueOf(age);
                player.setAge(age);
            }

            player.modify(psw, avat, name, surname, agebis);
        }

        // update network
        if (this._newProfile)
        {
            NetworkImplementation netInterface;
            try
            {
                Profile test = getParentFrame().getUserProfile();
                netInterface = new NetworkImplementation(test.getNetworkProfile());
                netInterface.startThreadListenNetwork();
                getParentFrame().setNetInterface(netInterface);

            } catch (Exception ex)
            {
                ex.printStackTrace();
            }

            // load main view
            getParentFrame().loadView(new MainView(getParentFrame()));
        }
        else
        {
            try
            {
                this.getParentFrame().getNetInterface().sendProfileUpdate(this.getParentFrame().getUserProfile());
            } catch (ErrorSendingProfileException ex)
            {
                Logger.getLogger(SetProfileView.class.getName()).log(Level.SEVERE, null, ex);
            }

            // load main view
            this.getParentFrame().loadView(new MainView(this.getParentFrame()));
        }
    }

    /**
     *
     */
    private void addComp()
    {
        // General Layout
        this.setLayout(new GridLayout(0, 2));

        this.add(this._leftPan);
        this.add(this._rightPan);

        // ================ Left panel =======================
        this._leftPan.setLayout(new GridBagLayout());
        GridBagConstraints contrainte = new GridBagConstraints();

        // Title
        contrainte.fill = GridBagConstraints.HORIZONTAL;
        contrainte.anchor = GridBagConstraints.BASELINE_LEADING;
        contrainte.gridx = 0;
        contrainte.gridy = 0;
        contrainte.gridwidth = GridBagConstraints.REMAINDER;
        contrainte.gridheight = 1;
        contrainte.insets = new Insets(20, 2, 0, 2);
        _leftPan.add(this._JLtitle, contrainte);

        // Information Fields
        for (int i = 0; i < this._IFinfoList.size(); i++)
        {
            contrainte.gridy += 1;
            this._leftPan.add(this._IFinfoList.get(i), contrainte);
        }

        // Password Fields
        contrainte.gridy += 1;
        this._leftPan.add(this._psw, contrainte);
        contrainte.gridy += 1;
        this._leftPan.add(this._pswConf, contrainte);

        contrainte.gridy += 1;
        this._leftPan.add(this._JLinfo, contrainte);


        // ================= Right panel ==================
        this._rightPan.setLayout(new GridBagLayout());

        contrainte.fill = GridBagConstraints.HORIZONTAL;
        contrainte.anchor = GridBagConstraints.BASELINE_LEADING;
        contrainte.gridwidth = GridBagConstraints.REMAINDER;
        contrainte.gridheight = 1;
        contrainte.insets = new Insets(20, 2, 0, 2);

        contrainte.gridx = 0;
        contrainte.gridy = 0;
        _rightPan.add(this._JLavatar, contrainte);
        contrainte.gridy += 1;
        _rightPan.add(this._JCavatarChoice, contrainte);
        contrainte.gridy += 1;
        //_rightPan.add(this._JLstat, contrainte);

        contrainte.fill = GridBagConstraints.NONE;
        contrainte.gridwidth = GridBagConstraints.RELATIVE;
        contrainte.gridx = 0;
        contrainte.gridy += 1;
        _rightPan.add(this._JBcancel, contrainte);
        contrainte.gridx = 1;
        _rightPan.add(this._JBsave, contrainte);
    }

    /**
     * Load filename image in the avatar zone
     *
     * @param filename
     */
    public void loadAvatar(URL filename)
    {
        this._JLavatar.setIcon(new ImageIcon(filename));
    }

    /**
     *
     *
     * @param picture name
     * @return picture path
     */
    private URL importName(String name)
    {
        return LobbyConstant.chargeFichier(LobbyConstant.PATH_PICTURE_AVATAR + "/" + name + ".png");
    }

    /**
     * Get a string to the avatar icon selected
     *
     * @return
     */
    public String getAvatarSelected()
    {
        String name = this._JCavatarChoice.getItemAt(this._JCavatarChoice.getSelectedIndex()).toString();
        return name;
    }


    /**
     *
     */
    private List<InformationField> getListInformationField()
    {
        return this._IFinfoList;
    }

    /**
     *
     */
    private PasswordFieldCustom getPassword()
    {
        return this._psw;
    }

    /**
     *
     */
    private PasswordFieldCustom getPasswordBis()
    {
        return this._pswConf;
    }

    ///////////////////////////////////////////////////////////////////////////
    //////////////////////// CheckFunctions  //////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Check the user name
     *
     * @return
     */
    private boolean checkName()
    {
        String s = this._IFinfoList.get(0).getTextField().getText();

        if (s.length() < 1)
        {
            return false;
        }
        if (s.length() > 20)
        {
            return false;
        }
        return true;
    }

    /**
     * Check first name
     *
     * @return
     */
    private boolean checkFirstName()
    {
        String s = this._IFinfoList.get(1).getTextField().getText();

        if (s.length() < 1)
        {
            return false;
        }

        if (s.length() > 20)
        {
            return false;
        }
        return true;
    }

    /**
     * Check data password field
     *
     * @param s
     * @return
     */
    private boolean checkAge()
    {
        String s = this._IFinfoList.get(2).getTextField().getText();
        int age;

        try
        {
            age = Integer.valueOf(s);
        } catch (Exception e)
        {
            age = -1;
        }

        // ---
        if (age < 0)
        {
            return false;
        }
        if (s.length() > 999)
        {
            return false;
        }

        return true;
    }

    /**
     * Check data password field
     *
     * @param s
     * @return
     */
    private boolean checkPseudo()
    {
        String s = this._IFinfoList.get(3).getTextField().getText();
        if (s.length() < 1)
        {
            return false;
        }

        if (s.length() > 20)
        {
            return false;
        }
        return true;
    }

    /**
     * Check data password field
     *
     * @param s
     * @return
     */
    private boolean checkMDP()
    {
        String s = this.getPassword().getTextField().getText();
        if (s.length() < 4)
        {
            return false;
        }
        if (s.length() > 10)
        {
            return false;
        }
        return true;
    }

    /**
     * Check data password bis field
     *
     * @param s
     * @return
     */
    private boolean checkMDPbis()
    {
        String s = this.getPassword().getTextField().getText();
        String sbis = this.getPasswordBis().getTextField().getText();
        if (sbis.length() < 4)
        {
            return false;
        }
        if (sbis.length() > 10)
        {
            return false;
        }
        if (s.compareTo(sbis) != 0)
        {
            return false;
        }
        return true;
    }
    ////////////////////////////////////////////////////////////////////////////
    //////////////////////// ActionListener ////////////////////////////////////

    /**
     * Handler class for combo box avatar
     */
    private class actionComboBoxAvatar implements ActionListener
    {
        private SetProfileView parent;

        actionComboBoxAvatar(SetProfileView parent)
        {
            this.parent = parent;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            String p = this.parent.getAvatarSelected();
            this.parent.loadAvatar(importName(p));
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //////////////////////// FocusListener  ////////////////////////////////////
    /**
     * Handler class for button save
     *
     */
    private class buttonSave implements ActionListener
    {
        private SetProfileView parent;

        buttonSave(SetProfileView parent)
        {
            this.parent = parent;
        }

        public void actionPerformed(ActionEvent e)
        {
            boolean flag = true;

            if (!this.parent.checkName())
            {
                parent._JLinfo.setText("Erreur dans le champs Nom");
                parent._JLinfo.setForeground(java.awt.Color.red);
                flag = false;
            }
            if (!this.parent.checkFirstName())
            {
                parent._JLinfo.setText("Erreur dans le champs Prenom");
                parent._JLinfo.setForeground(java.awt.Color.red);
                flag = false;
            }
            if (!this.parent.checkAge())
            {
                parent._JLinfo.setText("Erreur dans le champs Age");
                parent._JLinfo.setForeground(java.awt.Color.red);
                flag = false;
            }
            if (!this.parent.checkPseudo())
            {
                parent._JLinfo.setText("Erreur dans le champs Pseudo");
                parent._JLinfo.setForeground(java.awt.Color.red);
                flag = false;
            }
            if (!this.parent.checkMDP())
            {
                parent._JLinfo.setText("Erreur dans le champs Mot de passe");
                parent._JLinfo.setForeground(java.awt.Color.red);
                flag = false;
            }
            if (!this.parent.checkMDPbis())
            {
                parent._JLinfo.setText("Erreur Les mots de passes doivent étre identiques");
                parent._JLinfo.setForeground(java.awt.Color.red);
                flag = false;
            }

            if (flag)
            {
                if (parent._newProfile)
                {
                    System.out.println("Ajout du nouveau profile");
                }
                else
                {
                    System.out.println("Modification du profile");

                }
                saveProfile();
            }
        }
    }

    /**
     * Handler class for Textfield password
     *
     */
    private class buttonCancel implements ActionListener
    {
        private SetProfileView parent;

        buttonCancel(SetProfileView parent)
        {
            this.parent = parent;
        }

        public void actionPerformed(ActionEvent e)
        {

            if (0 == parent.getParentFrame().getLastView().toString().compareTo(("class fr.utc.lobby.LogView")))
            {
                parent.getParentFrame().loadView(new LogView(parent.getParentFrame()));
            }

            if (0 == parent.getParentFrame().getLastView().toString().compareTo(("class fr.utc.lobby.MainView")))
            {
                parent.getParentFrame().loadView(new MainView(parent.getParentFrame()));
            }

        }
    }

    /**
     * Handler class for Textfield password
     */
    private class focusPassword implements FocusListener
    {
        private SetProfileView parent;

        focusPassword(SetProfileView parent)
        {
            this.parent = parent;
        }

        @Override
        public void focusGained(FocusEvent e)
        {
            if (parent.checkPseudo())
            {

                parent._JLinfo.setText("Password au moins 4 caractéres et 10 max");
                parent._JLinfo.setForeground(java.awt.Color.darkGray);
            }
            else
            {
                parent._JLinfo.setText("Pseudo 20 caractéres max");
                parent._JLinfo.setForeground(java.awt.Color.red);

            }
        }

        @Override
        public void focusLost(FocusEvent e)
        {
        }
    }

    /**
     * Handler class for Textfield password
     */
    private class focusPasswordBis implements FocusListener
    {
        private SetProfileView parent;

        focusPasswordBis(SetProfileView parent)
        {
            this.parent = parent;
        }

        @Override
        public void focusGained(FocusEvent e)
        {
            if (checkMDP())
            {
                parent._JLinfo.setText("Confirmer le mot de passe");
                parent._JLinfo.setForeground(java.awt.Color.darkGray);
            }
            else
            {
                parent._JLinfo.setText("Password au moins 4 caractéres et 10 max !!!");
                parent._JLinfo.setForeground(java.awt.Color.RED);
            }
        }

        @Override
        public void focusLost(FocusEvent e)
        {
        }
    }

    /**
     * Handler class for Textfield password
     */
    private class focusName implements FocusListener
    {
        private SetProfileView parent;

        focusName(SetProfileView parent)
        {
            this.parent = parent;
        }

        @Override
        public void focusGained(FocusEvent e)
        {
            parent._JLinfo.setText("Nom 20 caractéres max");
            parent._JLinfo.setForeground(java.awt.Color.darkGray);

        }

        @Override
        public void focusLost(FocusEvent e)
        {
        }
    }

    /**
     * Handler class for Textfield password
     */
    private class focusPrenom implements FocusListener
    {
        private SetProfileView parent;

        focusPrenom(SetProfileView parent)
        {
            this.parent = parent;
        }

        @Override
        public void focusGained(FocusEvent e)
        {
            if (parent.checkName())
            {
                parent._JLinfo.setText("Prenom 20 caractéres max");
                parent._JLinfo.setForeground(java.awt.Color.darkGray);
            }
            else
            {
                parent._JLinfo.setText("Nom 20 caractéres max");
                parent._JLinfo.setForeground(java.awt.Color.red);

            }
        }

        @Override
        public void focusLost(FocusEvent e)
        {
        }
    }

    /**
     * Handler class for Textfield password
     */
    private class focusAge implements FocusListener
    {
        private SetProfileView parent;

        focusAge(SetProfileView parent)
        {
            this.parent = parent;
        }

        @Override
        public void focusGained(FocusEvent e)
        {
            if (parent.checkFirstName())
            {
                parent._JLinfo.setText("Age nombre de 0 à 999 pour farnsworth");
                parent._JLinfo.setForeground(java.awt.Color.darkGray);
            }
            else
            {
                parent._JLinfo.setText("Prenom 20 caractéres max");
                parent._JLinfo.setForeground(java.awt.Color.red);

            }
        }

        @Override
        public void focusLost(FocusEvent e)
        {
        }
    }

    /**
     * Handler class for Textfield password
     */
    private class focusPseudo implements FocusListener
    {
        private SetProfileView parent;

        focusPseudo(SetProfileView parent)
        {
            this.parent = parent;
        }

        @Override
        public void focusGained(FocusEvent e)
        {
            // System.out.println("pseudo bis in");



            if (parent.checkAge())
            {
                parent._JLinfo.setText("Pseudo 20 caractéres max");
                parent._JLinfo.setForeground(java.awt.Color.darkGray);
            }
            else
            {
                parent._JLinfo.setText("Age nombre de 0 à 150");
                parent._JLinfo.setForeground(java.awt.Color.red);
            }

        }

        @Override
        public void focusLost(FocusEvent e)
        {
        }
    }
}
