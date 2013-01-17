/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lobby;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author lo23a014
 */
public class ProfilePlayerPan extends JPanel
{
    private View _parent;
    private JLabel _JLlogo;
    private JLabel _JLpseudo;
    private JLabel _JLavatar;
    private JLabel _JLnameTitle;
    private JLabel _JLname;
    private JLabel _JLfirstNameTitle;
    private JLabel _JLfirstName;
    private JLabel _JLwinsTitle;
    private JLabel _JLwins;
    private JLabel _JLlosesTitle;
    private JLabel _JLloses;
    private JLabel _JLratioTitle;
    private JLabel _JLratio;
    private JButton _JBhistorical;
    private JButton _JBmodifyProfile;
    private JButton _JBexport;
    private ImageIcon _logo;
    private ImageIcon _avatar;
    private int _ratioMargeJLabel;
   // private Image image;

    /**
     * Default constructor
     */
    public ProfilePlayerPan(View parent)
    {
      //  image = Toolkit.getDefaultToolkit().getImage(LobbyConstant.chargeFichier("/fr/utc/ressources/background_IHM.png");LobbyConstant.chargeFichier(LobbyConstant.getPathLogo())

        this._parent = parent;
        initComponents();
    }



    public void updateComponents()
    {
        this._JLpseudo.setText("Profil de " + this._parent.getParentFrame().getUserProfile().getPseudo());
        this._avatar = LobbyConstant.Resize(150, 150, this.getAvatarSelected(this._parent.getParentFrame().getUserProfile().getAvatar()));
        this._JLavatar.setIcon(this._avatar);
        this._JLname.setText(this._parent.getParentFrame().getUserProfile().getName());
        this._JLfirstName.setText(this._parent.getParentFrame().getUserProfile().getSurname());
        this._JLwins.setText(this._parent.getParentFrame().getUserProfile().getGamesWon() + "");
        this._JLloses.setText(this._parent.getParentFrame().getUserProfile().getGamesLost() + "");
        this._JLratio.setText(this._parent.getParentFrame().getUserProfile().getRatio() + "");
    }

    /**
     * Components initialization and positioning in the window
     *
     */
    private void initComponents()
    {

        this._logo = LobbyConstant.Resize(180, 180, LobbyConstant.chargeFichier(LobbyConstant.getPathLogo()));

        this._JLlogo = new JLabel(this._logo);
        this._JLpseudo = new JLabel("Profil de " + this._parent.getParentFrame().getUserProfile().getPseudo());
        this._avatar = LobbyConstant.Resize(150, 150, this.getAvatarSelected(this._parent.getParentFrame().getUserProfile().getAvatar()));
        this._JLavatar = new JLabel(this._avatar);
        this._JLnameTitle = new JLabel("<html><u>Nom</u>:</html>");
        this._JLname = new JLabel(this._parent.getParentFrame().getUserProfile().getName());
        this._JLfirstNameTitle = new JLabel("<html><u>Prénom</u>:</html>");
        this._JLfirstName = new JLabel(this._parent.getParentFrame().getUserProfile().getSurname());
        this._JLwinsTitle = new JLabel("<html><u>Victoires</u>:</html>");
        this._JLwins = new JLabel(this._parent.getParentFrame().getUserProfile().getGamesWon() + "");
        this._JLlosesTitle = new JLabel("<html><u>Défaites</u>:</html>");
        this._JLloses = new JLabel(this._parent.getParentFrame().getUserProfile().getGamesLost() + "");
        this._JLratioTitle = new JLabel("<html><u>Ratio</u>:</html>");
        this._JLratio = new JLabel(this._parent.getParentFrame().getUserProfile().getRatio() + "");
        // ********************
        this._JBhistorical = new JButton("Historique");
        this._JBmodifyProfile = new JButton("Modifier Profil");
        this._JBmodifyProfile.setPreferredSize(new Dimension(this._JBmodifyProfile.getMinimumSize().width, 40));
        this._JBexport = new JButton("Exporter Profil");
        this._JBexport.setPreferredSize(new Dimension(this._JBexport.getMinimumSize().width, 40));

        // Calculation of the marge ratio for JLabels
        this.calculateMargeJLabel();

        // Set Layout
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();

        // Logo
        constraint.anchor = GridBagConstraints.CENTER;
        constraint.gridwidth = 0;
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.insets = new Insets(-50, 0, 25, 0);
        this.add(this._JLlogo, constraint);

        // Pseudo
        constraint.gridy += 1;
        constraint.insets = new Insets(0, 0, 15, 0);
        this._JLpseudo.setFont(new Font("", Font.BOLD, 24));
        this.add(this._JLpseudo, constraint);

        // Avatar
        constraint.gridy += 1;
        constraint.insets = new Insets(0, 0, 30, 0);
        this.add(this._JLavatar, constraint);

        // Name
        constraint.fill = GridBagConstraints.NONE;
        constraint.gridwidth = GridBagConstraints.RELATIVE;
        constraint.anchor = GridBagConstraints.BASELINE_TRAILING;
        constraint.gridy += 1;
        constraint.insets = new Insets(0, this._ratioMargeJLabel, 0, 0);
        this._JLnameTitle.setFont(new Font("", Font.BOLD, 18));
        this.add(this._JLnameTitle, constraint);
        constraint.gridx = 2;
        constraint.anchor = GridBagConstraints.BASELINE_LEADING;
        this._JLname.setFont(new Font("", Font.PLAIN, 18));
        this.add(this._JLname, constraint);

        // First Name
        constraint.gridx = 0;
        constraint.gridy += 1;
        this._JLfirstNameTitle.setFont(new Font("", Font.BOLD, 18));
        this.add(this._JLfirstNameTitle, constraint);
        constraint.gridx = 2;
        this._JLfirstName.setFont(new Font("", Font.PLAIN, 18));
        this.add(this._JLfirstName, constraint);

        // Wins
        constraint.gridx = 0;
        constraint.gridy += 1;
        constraint.insets = new Insets(10, this._ratioMargeJLabel + 5, 0, 0);
        this._JLwinsTitle.setFont(new Font("", Font.BOLD, 14));
        this.add(this._JLwinsTitle, constraint);
        constraint.gridx = 2;
        this._JLwins.setFont(new Font("", Font.PLAIN, 14));
        this.add(this._JLwins, constraint);

        // Loses
        constraint.gridx = 0;
        constraint.gridy += 1;
        constraint.insets = new Insets(0, this._ratioMargeJLabel + 5, 0, 0);
        this._JLlosesTitle.setFont(new Font("", Font.BOLD, 14));
        this.add(this._JLlosesTitle, constraint);
        constraint.gridx = 2;
        this._JLloses.setFont(new Font("", Font.PLAIN, 14));
        this.add(this._JLloses, constraint);

        // Ratio
        constraint.gridx = 0;
        constraint.gridy += 1;
        constraint.anchor = GridBagConstraints.BASELINE;
        constraint.insets = new Insets(0, this._ratioMargeJLabel + 5, 0, 0);
        this._JLratioTitle.setFont(new Font("", Font.BOLD, 14));
        this.add(this._JLratioTitle, constraint);
        constraint.gridx = 2;
        constraint.anchor = GridBagConstraints.BASELINE_LEADING;
        this._JLratio.setFont(new Font("", Font.PLAIN, 14));
        this.add(this._JLratio, constraint);

        // Button Historical
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.gridx = 1;
        constraint.gridwidth = 0;
        constraint.anchor = GridBagConstraints.CENTER;
        constraint.gridy += 1;
        constraint.insets = new Insets(35, 0, 0, 0);
        this._JBhistorical.setPreferredSize(this._JBexport.getPreferredSize());
        this._JBhistorical.setMinimumSize(this._JBexport.getMinimumSize());
        this._JBhistorical.addActionListener(new TreatmentButtonHistorical());
        this.add(this._JBhistorical, constraint);

        // Button Modify Profile
        constraint.gridy += 1;
        constraint.insets = new Insets(5, 0, 0, 0);
        this._JBmodifyProfile.addActionListener(new TreatmentButtonModifyProfile());
        this.add(this._JBmodifyProfile, constraint);

        // Button Export
        constraint.gridy += 1;
        this._JBexport.addActionListener(new TreatmentButtonExport());
        this.add(this._JBexport, constraint);
    }

 
    /**
     * Get a string to the avatar icon selected
     *
     * @return
     */
    private URL getAvatarSelected(String name)
    {
        return LobbyConstant.chargeFichier(LobbyConstant.PATH_PICTURE_AVATAR + "/" + name + ".png");
    }

    /**
     * Method which calculate the marge ratio for JLabels
     *
     * @return int The ratio
     */
    private void calculateMargeJLabel()
    {
        if (this._JLname.getText().length() > this._JLfirstName.getText().length())
        {
            this._ratioMargeJLabel = this._JLfirstName.getText().length() * 3;
        }
        else
        {
            this._ratioMargeJLabel = this._JLname.getText().length() * 3;
        }
    }

    /**
     * Handler class for 'Historique' button actions
     */
    private class TreatmentButtonHistorical implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            _JBhistorical.setText("Retour Accueil");
            _JBhistorical.removeActionListener(_JBhistorical.getActionListeners()[0]);
            _JBhistorical.addActionListener(new TreatmentButtonWelcomeReturn());

            ((MainView) _parent).setHistoricalView(true);
        }
    }

    /**
     * Handler class for 'Modifier Profil' button actions
     */
    private class TreatmentButtonModifyProfile implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            //((MainView) _parent).getParentFrame().loadView(new SetProfileView(((MainView) _parent).getParentFrame()));
            ((MainView) _parent).popup = new SetProfilePopUp(_parent);

        }
    }

    /**
     * Handler class for 'Exporter Profil' button actions
     */
    private class TreatmentButtonExport implements ActionListener
    {
        /**
         * Handler method of the action performed on the button 'Exporter'
         * Profil' Required because the class implements the interface
         * ActionListener
         *
         * @param e event linking to the Action performed
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {

            String directory = new String("");
            JFileChooser fDial = new JFileChooser();
            fDial.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int retour = fDial.showOpenDialog(new JFrame());
            if (retour == JFileChooser.APPROVE_OPTION)
            {
                directory = fDial.getSelectedFile().getAbsolutePath();
                System.out.println("fichier à exporter : " + directory);
                ((MainView) _parent).getParentFrame().getUserProfile().exportP(directory);
            }

        }
    }

    private class TreatmentButtonWelcomeReturn implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            _JBhistorical.setText("Historique");
            _JBhistorical.removeActionListener(_JBhistorical.getActionListeners()[0]);
            _JBhistorical.addActionListener(new TreatmentButtonHistorical());

            ((MainView) _parent).setHistoricalView(false);
        }
    }
}
