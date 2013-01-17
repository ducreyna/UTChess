/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lobby;

import fr.utc.data.Profile;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.UUID;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author lo23a014
 */
public class ResponseRequestPopUp extends JFrame {

    private View _parent;
    private String _opponentPseudo;
    private UUID _IDGame;
    private boolean _isNewGame;
    private boolean _color;
    private int _timer;
    private String _opponentName;
    private String _idOpponent;
    private int _opponentWins;
    private int _opponentLoses;
    private double _opponentRatio;
    private int _remainingTime;
    private String _dateGame;
    private Timer timer;
    private JFrame _lastPopUp;
    
    private JPanel _panelTop;
    private JLabel _JLgameType;
    private JLabel _JLnewGame;
    private JLabel _JLtimeBeforeDecline;
    private JLabel _JLopponentName;
    private JLabel _JLopponentWins;
    private JLabel _JLopponentLoses;
    private JLabel _JLratio;
    private JLabel _JLplayerInfos;
    private JLabel _JLtitle;
    private JLabel _JLcolor;
    private JLabel _JLtimer;
    private JButton _JBaccept;
    private JButton _JBdecline;

    public ResponseRequestPopUp(Profile profile, boolean color, int timer, boolean isNewGame, String dateGame, View parent,UUID IDGame,JFrame lastPopUp) {
        this._parent = parent;
        this._lastPopUp = lastPopUp;

        this.setTitle("Invitation à jouer");
        setIconImage(new ImageIcon(LobbyConstant.chargeFichier(LobbyConstant.getPathLogo())).getImage());
        this.setResizable(false);
        this.setSize(LobbyConstant.RESPONSE_REQUEST_POPUP_SIZE_W, LobbyConstant.RESPONSE_REQUEST_POPUP_SIZE_H);
        // Center the window
        this.setLocationRelativeTo(this.getParent());
        // Settings when the window is closing
        //closingWindow(); on n'est pas autorisé a fermer la fenetre de cette façon !

        // Récuperer les données...
        this._IDGame=IDGame;
        this._dateGame = dateGame;
        this._timer = timer;
        this._color = color;
        this._isNewGame = isNewGame;
        this._idOpponent = profile.getID();
        this._opponentPseudo = profile.getPseudo();
        this._opponentName = profile.getName();
        this._opponentWins = profile.getGamesWon();
        this._opponentLoses = profile.getGamesLost();


        this._opponentRatio = profile.getRatio();


        initComponents();

        this.setVisible(true);
    }

    /**
     * Private method to set the default closing of the window
     
    private void closingWindow() {
        // Skipping the default close
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // Window listener for the closing window event
        this.addWindowListener(new WindowAdapter() {
            // Callback for the right close event
            @Override
            public void windowClosing(WindowEvent e) {
//                // Informer le réseau du refus de la partie
//                ((MainView) _parent).sendResponseNetwork(_idOpponent, false);
                dispose();
            }
        });
    }*/

    /**
     * Components initialization and positioning in the window
     *
     */
    private void initComponents() {

        this._remainingTime = 30;
        this._panelTop = new JPanel();

        this._JLgameType = new JLabel("Type de Partie :");
        this._JLplayerInfos = new JLabel("Informations " + this._opponentPseudo + " :");
        if (_isNewGame) {
            this._JLtitle = new JLabel("Le joueur " + this._opponentPseudo + " vous invite à jouer une partie");
            this._JLnewGame = new JLabel("Nouvelle partie");
        } else {
            this._JLtitle = new JLabel("<html>Le joueur " + this._opponentPseudo + " vous invite à reprendre une partie<br>"
                    + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;sauvegardée le " 
                    + this._dateGame + "</html>");
            this._JLnewGame = new JLabel("Reprise d'une partie");
        }
        if (!this._color) {
            this._JLcolor = new JLabel("Couleur : Noir");
        } else {
            this._JLcolor = new JLabel("Couleur : Blanc");
        }
        if (this._timer == -1) {
            this._JLtimer = new JLabel("Timer : Non");
        } else {
            this._JLtimer = new JLabel("Timer : " + this._timer + "secondes");
        }

        this._JLopponentName = new JLabel("Prénom : " + _opponentName);
        this._JLopponentLoses = new JLabel("Parties perdues : " + String.valueOf(_opponentLoses));
        this._JLopponentWins = new JLabel("Parties gagnées : " + String.valueOf(_opponentWins));

        // Limiting doubles with 2 decimals
        DecimalFormat f = new DecimalFormat();
        f.setMaximumFractionDigits(2);
        this._JLratio = new JLabel("Ratio : " + f.format(_opponentRatio));

        this._JBdecline = new JButton("Refuser");
        this._JBaccept = new JButton("Accepter");


        this._JLtimeBeforeDecline = new JLabel("Temps avant refus... " + String.valueOf(_remainingTime) + "s");

        // General Layout
        this.setLayout(new GridLayout(1, 0));
        this.add(this._panelTop);

        /* ================ Set panel ================ */
        this._panelTop.setLayout(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();

        // Title
        constraint.anchor = GridBagConstraints.CENTER;
        constraint.gridwidth = 0;
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.gridheight = 1;
        constraint.insets = new Insets(0, 0, 30, 0);
        this._JLtitle.setFont(new Font(this._JLtitle.getText(), Font.BOLD, 14));
        this._panelTop.add(this._JLtitle, constraint);

        //Subtitles
        constraint.fill = GridBagConstraints.NONE;
        constraint.gridwidth = GridBagConstraints.RELATIVE;
        constraint.anchor = GridBagConstraints.WEST;
        constraint.gridx = 0;
        constraint.gridy += 1;
        constraint.insets = new Insets(0, 25, 0, 20);
        //Player Info
        this._JLplayerInfos.setFont(new Font(this._JLtitle.getText(), Font.BOLD, 12));
        this._panelTop.add(this._JLplayerInfos, constraint);
        //Game Type
        constraint.gridx = 5;
        this._JLgameType.setFont(new Font(this._JLtitle.getText(), Font.BOLD, 12));
        this._panelTop.add(this._JLgameType, constraint);


        // Line 1
        //Opponent Name
        constraint.gridx = 0;
        constraint.gridy += 1;
        this._JLopponentName.setFont(new Font(this._JLopponentName.getText(), Font.PLAIN, 12));
        this._panelTop.add(this._JLopponentName, constraint);
        //IsNewGame
        constraint.gridx = 5;
        this._JLnewGame.setFont(new Font(this._JLnewGame.getText(), Font.PLAIN, 12));
        this._panelTop.add(this._JLnewGame, constraint);

        // Line 2
        //Opponent Wins
        constraint.gridx = 0;
        constraint.gridy += 1;
        this._JLopponentWins.setFont(new Font(this._JLopponentWins.getText(), Font.PLAIN, 12));
        this._panelTop.add(this._JLopponentWins, constraint);
        //Timer
        constraint.gridx = 5;
        this._JLtimer.setFont(new Font(this._JLtimer.getText(), Font.PLAIN, 12));
        this._panelTop.add(this._JLtimer, constraint);

        // Line 3
        //Opponent Loses
        constraint.gridx = 0;
        constraint.gridy += 1;
        this._JLopponentLoses.setFont(new Font(this._JLopponentLoses.getText(), Font.PLAIN, 12));
        this._panelTop.add(this._JLopponentLoses, constraint);
        //Color
        constraint.gridx = 5;
        this._JLcolor.setFont(new Font(this._JLcolor.getText(), Font.PLAIN, 12));
        this._panelTop.add(this._JLcolor, constraint);

        // Line 4
        //Ratio
        constraint.gridx = 0;
        constraint.gridy += 1;
        this._JLratio.setFont(new Font(this._JLratio.getText(), Font.PLAIN, 12));
        this._panelTop.add(this._JLratio, constraint);


        // Buttons
        constraint.anchor = GridBagConstraints.WEST;
        constraint.gridx = 0;
        constraint.gridy += 1;
        constraint.insets = new Insets(20, 25, 0, 0);
        // Cancel
        this._JBdecline.addActionListener(new ResponseRequestPopUp.TreatmentButtonDecline());
        this._JBdecline.setPreferredSize(this._JBaccept.getPreferredSize());
        this._JBdecline.setMinimumSize(this._JBaccept.getMinimumSize());
        this._panelTop.add(this._JBdecline, constraint);
        // Create
        constraint.gridx = 5;
        this._JBaccept.addActionListener(new ResponseRequestPopUp.TreatmentButtonAccept());
        this._panelTop.add(this._JBaccept, constraint);

        // Time to leave...
        constraint.anchor = GridBagConstraints.LINE_START;
        //     constraint.gridwidth = GridBagConstraints.RELATIVE;
        //     constraint.gridheight = GridBagConstraints.RELATIVE;
        constraint.insets = new Insets(40, 120, 0, 0);
        constraint.gridx = 0;
        constraint.gridy += 1;
        this._JLtimeBeforeDecline.setFont(new Font(this._JLtimeBeforeDecline.getText(), Font.PLAIN, 12));
        this._panelTop.add(this._JLtimeBeforeDecline, constraint);



        //Timer
        ActionListener taskPerformer = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                _remainingTime--;
                _JLtimeBeforeDecline.setText("Temps avant refus... " + String.valueOf(_remainingTime) + "s");
                if (_remainingTime == 0) {
                    timer.stop();
                    ((MainView) _parent).sendResponseNetwork(_idOpponent, false);
                    dispose();
                }
            }
        };
        timer = new Timer(1000, taskPerformer);
        timer.start();

    }

    /**
     * Handler class for 'Cancel' button actions
     */
    private class TreatmentButtonDecline implements ActionListener {

        /**
         * Handler method of the action performed on the button decline Required
         * because the class implements the interface ActionListener
         *
         * @param e event linking to the Action performed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            ((MainView) _parent).sendResponseNetwork(_idOpponent, false);
             timer.stop();
             if (_lastPopUp != null)
            {
                if(_lastPopUp instanceof OpponentProfileView)
                {
                    if(((OpponentProfileView)(_lastPopUp)).popup!=null);
                        ((OpponentProfileView)(_lastPopUp)).popup.setEnabled(true);
                }
                else
                {
                    _lastPopUp.setEnabled(true);
                }
            }
            dispose();
        }
    }

    /**
     * Handler class for 'Create' button actions
     */
    private class TreatmentButtonAccept implements ActionListener {

        /**
         * Handler method of the action performed on the button accept Required
         * because the class implements the interface ActionListener
         *
         * @param e event linking to the Action performed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            ((MainView) _parent).sendResponseNetwork(_idOpponent, true);
            if(_isNewGame)
            {
                // _color est la couleur choisie par l'adversaire, donc on a la couleur inverse
                ((MainView) _parent).setNewGame(_idOpponent, !_color, _timer,_IDGame);
            }
            else
            {
                ((MainView) _parent).getParentFrame().setLastGameRequested(null);
            }
            ((MainView) _parent).launchGame(_idOpponent,_IDGame);
            timer.stop();
            if(_lastPopUp!=null)
            {
                if(_lastPopUp instanceof OpponentProfileView)
                {
                    if(((OpponentProfileView)(_lastPopUp)).popup!=null);
                        ((OpponentProfileView)(_lastPopUp)).popup.dispose();
                }
                _lastPopUp.dispose();
            }
            _parent.getParentFrame().setEnabled(true);
            dispose();
        }
    }
}
