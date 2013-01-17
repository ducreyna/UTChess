/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lobby;

import fr.utc.data.Game;
import fr.utc.data.Profile;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author lo23a014
 */
public class ProfileOpponentPan extends JPanel
{
    private View parentView;
    private Profile _opponentProfile;
    private ArrayList<Game> saveGameList;
    private JPanel _panelmiddle;
    private JPanel _panelLeft;
    private JPanel _panelRight;
    // Components
    private JLabel _JLpseudo;
    private JLabel _JLstat;
    private JLabel _JLgameWon;
    private JLabel _JLgameLost;
    private JLabel _JLgameRatio;
    private JLabel _JLavatar;
    private JLabel _JLsavegameTitle;
    private JButton _JBdetails;
    private JButton _JBnewGame;
    private JButton _JBloadGame;
    private JList _JLsavegame;
    private JScrollPane _JSsavegameScroll;

    /**
     * Default constructor
     */
    public ProfileOpponentPan(View parentView)
    {
        this.parentView = parentView;
        //opponentProfile = new Profile("XOOOOOOOOOOOOOOOOOOOOOOOO", "passwd", "/bender.png", "OOOOOOOOOO", "name", 22);
        //ArrayList<String> sg = new ArrayList<String>();
        //sg.add("Game 1");
        //sg.add("Game 2");
        //this.saveGameList = new ArrayList<String>(sg);
        initComponents();
        //setOpponentProfile(opponentProfile);
    }

    /**
     * Components initialization and positioning in the panel
     *
     */
    private void initComponents()
    {

        if (_opponentProfile == null)
        {
            this.setLayout(new GridBagLayout());
            GridBagConstraints contrainte = new GridBagConstraints();
            /*--------------Partie haute du panel : titre--------------*/
            JLabel _labelTitre = new JLabel("<html><i>Selectionner un joueur pour avoir des détails... </i></html>");
            contrainte.fill = GridBagConstraints.HORIZONTAL;
            contrainte.anchor = GridBagConstraints.CENTER;
            contrainte.gridx = 0;
            contrainte.gridy = 0;
            contrainte.gridwidth = GridBagConstraints.REMAINDER;
            contrainte.gridheight = 1;
            contrainte.insets = new Insets(15, 10, 0, 10);  // Marge à gauche et droite de 100 et marge au dessus de 10.
            this.add(_labelTitre, contrainte);
        }
        else
        {
            this._JLpseudo = new JLabel(_opponentProfile.getPseudo());
            this._JLpseudo.setFont(new Font(this._JLpseudo.getName(), Font.BOLD, 14));
            this._JLsavegameTitle = new JLabel("Sauvegardes ...");
            this._JLsavegameTitle.setFont(new Font(this._JLsavegameTitle.getName(), Font.ITALIC, 12));

            this._JLgameWon = new JLabel("Parties Gagnées : " + _opponentProfile.getGamesWon());
            this._JLgameLost = new JLabel("Parties Perdues : " + _opponentProfile.getGamesLost());
            this._JLgameRatio = new JLabel("Ratio : " + _opponentProfile.getRatio());

            //Resize of the Avatar
            this._JLavatar = new JLabel();
            this._JLavatar.setIcon(LobbyConstant.Resize(LobbyConstant.PROFILEOPPONENT_PAN_AVATARSIZE, LobbyConstant.PROFILEOPPONENT_PAN_AVATARSIZE, LobbyConstant.chargeFichier( LobbyConstant.PATH_PICTURE_AVATAR + "/" + _opponentProfile.getAvatar() + ".png")));

            //Buttons
            this._JBdetails = new JButton("Details");
            this._JBdetails.addActionListener(new ProfileOpponentPan.TreatmentJBdetails());
            this._JBnewGame = new JButton("Nouvelle Partie");
            this._JBnewGame.addActionListener(new ProfileOpponentPan.TreatmentJBnewGame());
            if(this._opponentProfile.isInGame())
                this._JBnewGame.setEnabled(false);
            else
                this._JBnewGame.setEnabled(true);
            this._JBloadGame = new JButton("Charger Partie");
            this._JBloadGame.addActionListener(new ProfileOpponentPan.TreatmentJBloadGame());

            //Init of the saveGameList
            _JLsavegame = new JList();
            _JLsavegame.setFixedCellHeight(20);
          //  this.saveGameList = this.parentView.getParentFrame().getUserProfile().checkGamesNonCompletedWith(_opponentProfile.getID(), _opponentProfile.getPseudo());
           this.saveGameList = null;
            if (saveGameList == null || saveGameList.isEmpty())
            {
                this._JBloadGame.setEnabled(false);
                _JLsavegame.setFixedCellWidth(120);//to reduce the size of the list
            }
            else
            {
                Vector<String> tempo = new Vector<String>();
                for (Iterator<Game> it = saveGameList.iterator(); it.hasNext();)
                {
                    Game game = it.next();
                    tempo.add(game.getDateCreat());

                }
                _JLsavegame = new JList(tempo);
                if(this._opponentProfile.isInGame())
                    this._JBloadGame.setEnabled(false);
                else
                    this._JBloadGame.setEnabled(true);
            }
            _JLsavegame.setVisibleRowCount(10);
            _JLsavegame.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            //_JLsavegame.addListSelectionListener(new ProfileOpponentPan.ListSelectionHandler());

            //Init of the scrollBar
            _JSsavegameScroll = new JScrollPane(_JLsavegame);
            _JSsavegameScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            _JSsavegameScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            this.setLayout(new GridBagLayout());
            GridBagConstraints contrainte = new GridBagConstraints();

            //LeftLayout
            _panelLeft = new JPanel();
            contrainte.fill = GridBagConstraints.BOTH;
            contrainte.gridx = 0;
            contrainte.gridy = 0;
            contrainte.gridheight = 1;
            contrainte.gridwidth = 1;
            contrainte.weightx = 0.05f;
            this.add(_panelLeft, contrainte);
            _panelLeft.setLayout(new GridBagLayout());

            //middle layout
            _panelmiddle = new JPanel();
            contrainte.gridx = 1;
            contrainte.weightx = LobbyConstant.PROFILEOPPONENTPAN_MIDDLE_PAN_WX;
            this.add(_panelmiddle, contrainte);
            _panelmiddle.setLayout(new GridBagLayout());

            //RightLayout
            _panelRight = new JPanel();
            contrainte.gridx = 2;
            contrainte.weightx = LobbyConstant.PROFILEOPPONENTPAN_LEFT_PAN_WX;
            this.add(_panelRight, contrainte);
            _panelRight.setLayout(new GridBagLayout());

            // ================ left panel =======================

            contrainte.fill = GridBagConstraints.VERTICAL;
            contrainte.anchor = GridBagConstraints.CENTER;
            contrainte.gridwidth = GridBagConstraints.REMAINDER;
            contrainte.gridheight = 1;
            contrainte.gridx = 0;
            contrainte.gridy = 0;
            contrainte.insets = new Insets(0, 0, 0, 0);
            _panelLeft.add(this._JLavatar, contrainte);

            contrainte.insets = new Insets(20, 0, 0, 0);
            contrainte.anchor = GridBagConstraints.CENTER;
            contrainte.gridy += 1;
            _panelLeft.add(this._JLpseudo, contrainte);

            contrainte.gridy += 1;
            _panelLeft.add(this._JLgameWon, contrainte);
            contrainte.insets = new Insets(0, 0, 0, 0);
            contrainte.gridy += 1;
            _panelLeft.add(this._JLgameLost, contrainte);
            contrainte.gridy += 1;
            _panelLeft.add(this._JLgameRatio, contrainte);
            contrainte.insets = new Insets(20, 0, 0, 0);
            contrainte.gridy += 1;
            _panelLeft.add(this._JBdetails, contrainte);

            // ================ Middle panel =======================

            contrainte.fill = GridBagConstraints.HORIZONTAL;
            contrainte.anchor = GridBagConstraints.CENTER;
            contrainte.gridwidth = GridBagConstraints.REMAINDER;
            contrainte.gridheight = 1;
            contrainte.gridx = 0;
            contrainte.gridy = 0;
            contrainte.insets = new Insets(0, 10, 0, 10);
            _panelmiddle.add(this._JBnewGame, contrainte);

            contrainte.gridy = 1;
            contrainte.insets = new Insets(30, 10, 0, 10);
            _panelmiddle.add(this._JBloadGame, contrainte);

            // ================ Right panel =======================
            contrainte.fill = GridBagConstraints.HORIZONTAL;
            contrainte.anchor = GridBagConstraints.BASELINE_LEADING;
            contrainte.insets = new Insets(0, 10, 0, 10);
            contrainte.gridx = 0;
            contrainte.gridy = 0;
            _panelRight.add(_JLsavegameTitle, contrainte);

            contrainte.gridy += 1;
            contrainte.gridheight = 1;
            _panelRight.add(_JSsavegameScroll, contrainte);
        }
    }

  

    public void setOpponentProfile(Profile OppProfile)
    {
        if (this._opponentProfile == null || OppProfile == null)
        {
            
            this._opponentProfile = OppProfile;
            //this.saveGameList = opponentProfile.checkGamesNonCompletedWith("toto", opponentProfile.getPseudo());
            this.removeAll();
            initComponents();
            this.updateUI();
            /*this._JLpseudo.setText("Aucun adversaire selectioné");
             this._JLgameWon.setText("Parties Gagnées : ?");
             this._JLgameLost.setText("Parties Perdues : ?");
             this._JLgameRatio.setText("Ratio : ?");
             this._JLavatar.setIcon(LobbyConstant.Resize(LobbyConstant.PROFILEOPPONENT_PAN_AVATARSIZE, LobbyConstant.PROFILEOPPONENT_PAN_AVATARSIZE, this.getPath() + LobbyConstant.PATH_PICTURE_AVATAR +"/utchess.png" ));
             */
        }
        else
        {
            
            this._opponentProfile = OppProfile;
            //this.saveGameList = opponentProfile.checkGamesNonCompletedWith("toto", opponentProfile.getPseudo());
            this._JBdetails.setEnabled(true);
            if(this._opponentProfile.isInGame())
                this._JBnewGame.setEnabled(false);
            else
                this._JBnewGame.setEnabled(true);
            this._JLavatar.setIcon(LobbyConstant.Resize(LobbyConstant.PROFILEOPPONENT_PAN_AVATARSIZE, LobbyConstant.PROFILEOPPONENT_PAN_AVATARSIZE, LobbyConstant.chargeFichier( LobbyConstant.PATH_PICTURE_AVATAR + "/" + _opponentProfile.getAvatar() + ".png")));

            this._JLpseudo.setText(_opponentProfile.getPseudo());

            this._JLgameWon.setText("Parties Gagnées : " + _opponentProfile.getGamesWon());
            this._JLgameLost.setText("Parties Perdues : " + _opponentProfile.getGamesLost());
            this._JLgameRatio.setText("Ratio : " + _opponentProfile.getRatio());

            
          //  this.saveGameList = this.parentView.getParentFrame().getUserProfile().checkGamesNonCompletedWith(_opponentProfile.getID(), _opponentProfile.getPseudo());
            this.saveGameList = null;
            if (saveGameList == null || saveGameList.isEmpty())
            {
                this._JBloadGame.setEnabled(false);
                _JLsavegame.setFixedCellWidth(120);
                _JLsavegame.setListData(new Vector<String>());
            }
            else
            {
                //TODO methode pour avoir le nom de la partie dans Game !
                Vector<String> tempo = new Vector<String>();
                for (Iterator<Game> it = saveGameList.iterator(); it.hasNext();)
                {
                    Game game = it.next();
                    tempo.add(game.getDateCreat());

                }
                _JLsavegame = new JList(tempo);
                if(this._opponentProfile.isInGame())
                    this._JBloadGame.setEnabled(false);
                else
                    this._JBloadGame.setEnabled(true);
            }
        }
    }

    private class TreatmentJBdetails implements ActionListener
    {
        /**
         * Handler method of the action performed on the button New Game
         * Required because the class implements the interface ActionListener
         *
         * @param e event linking to the Action performed
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (_opponentProfile != null)
            {
                parentView.getParentFrame().setEnabled(false);
                parentView.popup = new OpponentProfileView(parentView, _opponentProfile,saveGameList);
            }
        }
    }

    private class TreatmentJBnewGame implements ActionListener
    {
        /**
         * Handler method of the action performed on the button New Game
         * Required because the class implements the interface ActionListener
         *
         * @param e event linking to the Action performed
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            parentView.getParentFrame().setEnabled(false);
            parentView.popup = new CreateGamePopUp(parentView, _opponentProfile);
        }
    }

    private class TreatmentJBloadGame implements ActionListener
    {
        /**
         * Handler method of the action performed on the button New Game
         * Required because the class implements the interface ActionListener
         *
         * @param e event linking to the Action performed
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            //getParentFrame().setEnabled(false);
            parentView.getParentFrame().setEnabled(false);
            //parentView.getParentFrame().getNetInterface().askLoadGame(null, null);//id_opponent - Id Game
            //parentView.popup = new WaitingResponsePopUp(parentView, _opponentProfile, timer, color, false);
        }
    }
}
