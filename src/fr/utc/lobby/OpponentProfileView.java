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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Generation and management of the OpponentProfileView window
 *
 * @author lo23a014
 */
public class OpponentProfileView extends JFrame
{
    //
    OpponentProfileView theFrame;
    private View parent;
    public JFrame popup;
    //opponent's Profile
    private Profile opponentProfile;
    private ArrayList<Game> saveGameList;
    //Panels
    private JPanel _panelTop;
    private JPanel _panelLeft;
    private JPanel _panelRight;
    // Components
    private JLabel _JLpseudo;
    private JLabel _JLetat;
    private JLabel _JLstat;
    private JLabel _JLgameWon;
    private JLabel _JLgameLost;
    private JLabel _JLgameRatio;
    private JLabel _JLgeneralInfo;
    private JLabel _JLlname;
    private JLabel _JLfname;
    private JLabel _JLage;
    private JLabel _JLavatar;
    private JLabel _JLsavegameTitle;
    private JLabel _JLgameInfoTitle;
    private JLabel _JLgameInfoDate;
    private JLabel _JLgameInfoNBtour;
    private JLabel _JLgameInfoPlayerWhite;
    private JLabel _JLgameInfoPlayerBlack;
    private JLabel _JLgameInfoTimer;
    private JLabel _JLgameInfoTimerLeftWhite;
    private JLabel _JLgameInfoTimerLeftBlack;
    private JButton _JBclose;
    private JButton _JBnewGame;
    private JButton _JBloadGame;
    private JList _JLsavegame;
    private JScrollPane _JSsavegameScroll;

    /**
     * Default constructor
     */
    public OpponentProfileView(View frame, Profile opponent, ArrayList<Game> saveGameList)
    {
        //super(LobbyConstant.OPPONENT_PROFILE_VIEW_SIZE_H,LobbyConstant.OPPONENT_PROFILE_VIEW_SIZE_W,frame);
        this.parent = frame;
        this.theFrame = this;
        this.opponentProfile = opponent;
        this.popup = null;
        if (saveGameList != null)
        {
            this.saveGameList = saveGameList;
        }

        this.setTitle("Details du profil adverse");
        this.setResizable(false);
        this.setSize(LobbyConstant.OPPONENT_PROFILE_VIEW_SIZE_W, LobbyConstant.OPPONENT_PROFILE_VIEW_SIZE_H);
        setIconImage(new ImageIcon(LobbyConstant.chargeFichier(LobbyConstant.getPathLogo())).getImage());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        // Center the window
        this.setLocationRelativeTo(this.getParent());

        closingWindow();

        initComponents();

        this.setVisible(true);
    }

    /**
     * Components initialization and positioning in the panel
     *
     */
    private void initComponents()
    {
        //Init of the components

        //Labels
        this._JLpseudo = new JLabel(opponentProfile.getPseudo());
        this._JLpseudo.setFont(new Font(this._JLpseudo.getName(), Font.BOLD, 16));
        this._JLsavegameTitle = new JLabel("Sauvegardes ...");
        this._JLsavegameTitle.setFont(new Font(this._JLsavegameTitle.getName(), Font.ITALIC, 12));

        //Init of the stat of the Opponent
        if (opponentProfile.isInGame())
        {
            this._JLetat = new JLabel("Etat : En jeu");
        }
        else
        {
            this._JLetat = new JLabel("Etat : libre");
        }

        //Labels Stats
        this._JLstat = new JLabel("Statistiques");
        this._JLavatar = new JLabel("");
        this._JLgameWon = new JLabel("Parties Gagnées : " + opponentProfile.getGamesWon());
        this._JLgameLost = new JLabel("Parties Perdues : " + opponentProfile.getGamesLost());
        this._JLgameRatio = new JLabel("Ratio : " + opponentProfile.getRatio());
        //Labels Generals Information
        this._JLgeneralInfo = new JLabel("Informations Générales");
        this._JLlname = new JLabel("Nom : " + opponentProfile.getName());
        this._JLfname = new JLabel("Prénom : " + opponentProfile.getSurname());
        this._JLage = new JLabel("Age : " + opponentProfile.getAge());

        //Resize of the Avatar
        this._JLavatar.setIcon(LobbyConstant.Resize(LobbyConstant.PROFILEOPPONENT_PAN_AVATARSIZE, LobbyConstant.PROFILEOPPONENT_PAN_AVATARSIZE, LobbyConstant.chargeFichier(LobbyConstant.PATH_PICTURE_AVATAR + "/" + opponentProfile.getAvatar() + ".png")));

        //Buttons
        this._JBclose = new JButton("Fermer");
        this._JBclose.addActionListener(new OpponentProfileView.TreatmentButtonClose());
        this._JBnewGame = new JButton("Nouvelle Partie");
        this._JBnewGame.addActionListener(new OpponentProfileView.TreatmentJBnewGame());
        this._JBloadGame = new JButton("Charger Partie");
        this._JBloadGame.addActionListener(new OpponentProfileView.TreatmentJBloadGame());

        if (opponentProfile.isInGame())
        {
            this._JBnewGame.setEnabled(false);
            this._JBloadGame.setEnabled(false);
        }
        //Lables of the SaveGame Information
        if (saveGameList == null || saveGameList.isEmpty())
        {
            this._JBloadGame.setEnabled(false);
            this._JLgameInfoTitle = new JLabel("Aucune Partie Selectionnée");
            this._JLgameInfoDate = new JLabel("Date : ");
            this._JLgameInfoDate.setVisible(false);
            this._JLgameInfoNBtour = new JLabel("NbTour : ");
            this._JLgameInfoNBtour.setVisible(false);
            this._JLgameInfoPlayerWhite = new JLabel("PlayerWhite :");
            this._JLgameInfoPlayerWhite.setVisible(false);
            this._JLgameInfoPlayerBlack = new JLabel("PlayerBlack :");
            this._JLgameInfoPlayerBlack.setVisible(false);
            this._JLgameInfoTimer = new JLabel("InfoTimer :");
            this._JLgameInfoTimer.setVisible(false);
            this._JLgameInfoTimerLeftWhite = new JLabel("TimerLeftWhite");
            this._JLgameInfoTimerLeftWhite.setVisible(false);
            this._JLgameInfoTimerLeftBlack = new JLabel("TimerLeftBlack");
            this._JLgameInfoTimerLeftBlack.setVisible(false);
        }

        //Init of the saveGameList
        if (saveGameList != null)
        {
            Vector<String> tempo = new Vector<String>();
            for (Iterator<Game> it = saveGameList.iterator(); it.hasNext();)
            {
                Game game = it.next();
                tempo.add(game.getNameGame());
            }
            _JLsavegame = new JList(tempo);
        }
        else
        {
            _JLsavegame = new JList();
        }
        _JLsavegame.setFixedCellHeight(20);
        if (saveGameList == null || saveGameList.isEmpty())
        {
            _JLsavegame.setFixedCellWidth(120);//to reduce the size of the list
        }
        _JLsavegame.setVisibleRowCount(10);
        _JLsavegame.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _JLsavegame.addListSelectionListener(new ListSelectionHandler());

        //Init of the scrollBar
        _JSsavegameScroll = new JScrollPane(_JLsavegame);
        _JSsavegameScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        _JSsavegameScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // General Layout 
        this.setLayout(new GridBagLayout());
        GridBagConstraints contrainte = new GridBagConstraints();

        //Top Layout
        contrainte.fill = GridBagConstraints.BOTH;
        _panelTop = new JPanel();
        contrainte.gridx = 0;
        contrainte.gridy = 0;
        contrainte.gridheight = 1;
        contrainte.gridwidth = 2;
        //contrainte.weighty = LobbyConstant.OPPONENTPROFILEVIEW_TOP_PAN_WY;
        this.add(_panelTop, contrainte);
        _panelTop.setLayout(new GridBagLayout());

        //LeftLayout
        _panelLeft = new JPanel();
        contrainte.gridx = 0;
        contrainte.gridy = 1;
        contrainte.gridwidth = 1;
        contrainte.weightx = LobbyConstant.OPPONENTPROFILEVIEW_LEFT_PAN_WY;
        contrainte.weighty = 1 - LobbyConstant.OPPONENTPROFILEVIEW_TOP_PAN_WY;
        this.add(_panelLeft, contrainte);
        _panelLeft.setLayout(new GridBagLayout());

        //RightLayout
        _panelRight = new JPanel();
        contrainte.gridx = 1;
        contrainte.weightx = 1 - LobbyConstant.OPPONENTPROFILEVIEW_LEFT_PAN_WY;
        this.add(_panelRight, contrainte);
        _panelRight.setLayout(new GridBagLayout());

        // ================ top panel =======================

        contrainte.fill = GridBagConstraints.VERTICAL;
        contrainte.anchor = GridBagConstraints.BASELINE_LEADING;
        contrainte.gridwidth = GridBagConstraints.REMAINDER;
        contrainte.gridheight = 1;
        contrainte.gridx = 1;
        contrainte.gridy = 0;
        contrainte.insets = new Insets(0, 0, 0, 0);
        _panelTop.add(this._JLavatar, contrainte);


        contrainte.anchor = GridBagConstraints.CENTER;
        contrainte.gridx = 2;
        contrainte.insets = new Insets(10, 0, 0, 0);
        _panelTop.add(this._JLpseudo, contrainte);

        contrainte.gridx = 3;
        contrainte.fill = GridBagConstraints.RELATIVE;
        contrainte.anchor = GridBagConstraints.BASELINE_TRAILING;
        contrainte.insets = new Insets(46, 300, 0, 20);
        _panelTop.add(this._JLetat, contrainte);


        // ================ left panel =======================
        contrainte.fill = GridBagConstraints.NONE;
        contrainte.gridwidth = GridBagConstraints.REMAINDER;
        contrainte.gridheight = 1;
        contrainte.gridy = 0;
        contrainte.gridx = 0;
        contrainte.anchor = GridBagConstraints.BASELINE_LEADING;
        contrainte.insets = new Insets(10, 10, 0, 0);
        _panelLeft.add(this._JLgeneralInfo, contrainte);

        contrainte.gridy += 1;
        contrainte.insets = new Insets(5, 30, 0, 0);
        _panelLeft.add(this._JLlname, contrainte);

        contrainte.gridy += 1;
        _panelLeft.add(this._JLfname, contrainte);

        contrainte.gridy += 1;
        _panelLeft.add(this._JLage, contrainte);

        contrainte.gridy += 1;
        contrainte.insets = new Insets(10, 10, 0, 0);
        _panelLeft.add(this._JLstat, contrainte);

        contrainte.gridy += 1;
        contrainte.insets = new Insets(5, 30, 0, 0);
        _panelLeft.add(this._JLgameWon, contrainte);

        contrainte.gridy += 1;
        _panelLeft.add(this._JLgameLost, contrainte);

        contrainte.gridy += 1;
        _panelLeft.add(this._JLgameRatio, contrainte);

        if (saveGameList != null && !saveGameList.isEmpty())
        {
            this._JLsavegame.setSelectedIndex(0);
            contrainte.gridy += 1;
            contrainte.insets = new Insets(10, 10, 0, 0);
            _panelLeft.add(this._JLgameInfoTitle, contrainte);

            contrainte.gridy += 1;
            contrainte.insets = new Insets(5, 30, 0, 0);
            _panelLeft.add(this._JLgameInfoDate, contrainte);

            contrainte.gridy += 1;
            _panelLeft.add(this._JLgameInfoNBtour, contrainte);

            contrainte.gridy += 1;
            _panelLeft.add(this._JLgameInfoPlayerWhite, contrainte);

            contrainte.gridy += 1;
            _panelLeft.add(this._JLgameInfoPlayerBlack, contrainte);

            contrainte.gridy += 1;
            _panelLeft.add(this._JLgameInfoTimer, contrainte);

            contrainte.gridy += 1;
            _panelLeft.add(this._JLgameInfoTimerLeftWhite, contrainte);

            contrainte.gridy += 1;
            _panelLeft.add(this._JLgameInfoTimerLeftBlack, contrainte);
        }



        //RightPanel
        contrainte.fill = GridBagConstraints.HORIZONTAL;
        contrainte.anchor = GridBagConstraints.BASELINE_LEADING;
        contrainte.insets = new Insets(0, 0, 0, 10);
        contrainte.gridx = 0;
        contrainte.gridy = 0;
        _panelRight.add(_JLsavegameTitle, contrainte);

        contrainte.gridy += 1;
        contrainte.gridheight = 1;
        _panelRight.add(_JSsavegameScroll, contrainte);

        contrainte.gridy += 1;
        contrainte.fill = GridBagConstraints.HORIZONTAL;
        contrainte.anchor = GridBagConstraints.BASELINE_LEADING;
        contrainte.insets = new Insets(20, 0, 0, 10);
        _panelRight.add(this._JBnewGame, contrainte);

        contrainte.gridy += 1;
        contrainte.insets = new Insets(5, 0, 0, 10);
        _panelRight.add(this._JBloadGame, contrainte);

        contrainte.gridy += 1;
        contrainte.fill = GridBagConstraints.NONE;
        contrainte.gridwidth = GridBagConstraints.RELATIVE;
        contrainte.anchor = GridBagConstraints.BASELINE_TRAILING;
        contrainte.insets = new Insets(25, 0, 0, 20);
        _panelRight.add(this._JBclose, contrainte);
    }

    /**
     * closing window action
     *
     */
    private void closingWindow()
    {
        // Window listener for the closing window event
        this.addWindowListener(new WindowAdapter()
        {
            // Callback for the right close event
            @Override
            public void windowClosing(WindowEvent e)
            {
                if (parent != null)
                {
                    parent.popup = null;
                    parent.getParentFrame().setEnabled(true);
                }
                dispose();
            }
        });
    }

    /**
     * getter parent
     *
     * @return parent view
     */
    public View getParent()
    {
        return parent;
    }

    /**
     * ListSelectionHandler
     *
     */
    private class ListSelectionHandler implements ListSelectionListener
    {
        private OpponentProfileView parent;

        /**
         * default constructor
         *
         * @parm parent view
         */
        public void ListSelectionHandler(OpponentProfileView parent)
        {
            this.parent = parent;
        }

        /**
         * valueChanged
         *
         */
        public void valueChanged(ListSelectionEvent e)
        {

            String game = _JLsavegame.getSelectedValue().toString();
            _JLgameInfoTitle.setText(game);
            _JLgameInfoDate.setVisible(true);
            _JLgameInfoDate.setVisible(true);
            _JLgameInfoNBtour.setVisible(true);
            _JLgameInfoPlayerWhite.setVisible(true);
            _JLgameInfoPlayerBlack.setVisible(true);
            _JLgameInfoTimer.setVisible(true);
            _JLgameInfoTimerLeftWhite.setVisible(true);
            _JLgameInfoTimerLeftBlack.setVisible(true);

        }
    }

    /**
     * ActionListener TreatmentButtonClose
     *
     */
    private class TreatmentButtonClose implements ActionListener
    {
        /**
         * Handler method of the action performed on the button 'Creat' Required
         * because the class implements the interface ActionListener
         *
         * @param e event linking to the Action performed
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (parent != null)
            {
                parent.popup = null;
                parent.getParentFrame().setEnabled(true);
            }
            dispose();
        }
    }

    /**
     * Handler class for 'New Game' button actions
     */
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
            //getParentFrame().setEnabled(false);
            theFrame.setEnabled(false);
            theFrame.popup = new CreateGamePopUp(theFrame, opponentProfile);
        }
    }

    /**
     * treatment event
     *
     */
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
            theFrame.setEnabled(false);
            //parent.getParentFrame().getNetInterface().askLoadGame(null, null);
            //theFrame.popup = new WaitingResponsePopUp(_parent, _opponent, timer, color, true);
        }
    }
}
