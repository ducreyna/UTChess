/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lobby;

import fr.utc.data.Game;
import fr.utc.data.Profile;
import fr.utc.exceptions.PositionConstructorLineException;
import fr.utc.grid.board.GridController;
import fr.utc.network.exceptions.ErrorSendingInGameStateException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel use to show detail about game
 *
 * @author Seg_fault_
 */
public class HistoricalDetail extends JPanel
{
    private JLabel _labelTitre;
    private JButton _buttonDetail, _buttonReview;
    private Profile _opponentProfil;
    private Game _game;
    private View _parentView;

    /**
     * Default constructor
     */
    public HistoricalDetail(View parent)
    {
        _parentView = parent;
        _opponentProfil = null;
        _game = null;
        initComponents();

    }

    /**
     * Components initialization and positioning in the window
     */
    private void initComponents()
    {
        this.setLayout(new GridBagLayout());
        GridBagConstraints contrainte = new GridBagConstraints();
        if (this._opponentProfil == null || this._game == null)
        {

            /*--------------Partie haute du panel : titre--------------*/
            _labelTitre = new JLabel("<html><i>Selectionner une partie pour voir les détails... </i></html>");
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

            /*--------------Partie haute du panel : titre--------------*/
            _labelTitre = new JLabel("<html><b><u>Détail partie : " + _parentView.getParentFrame().getUserProfile().getPseudo() + " vs " + _opponentProfil.getPseudo() + "</u></b></html>");
            contrainte.fill = GridBagConstraints.HORIZONTAL;
            contrainte.anchor = GridBagConstraints.CENTER;
            contrainte.gridx = 0;
            contrainte.gridy = 0;
            contrainte.gridwidth = GridBagConstraints.REMAINDER;
            contrainte.gridheight = 1;
            contrainte.insets = new Insets(15, 10, 10, 10);  // Marge à gauche et droite de 100 et marge au dessus de 10.
            this.add(_labelTitre, contrainte);


            /*--------------partie gauche du panel : detail adversaire--------------*/
            contrainte.gridx = 0;
            contrainte.fill = GridBagConstraints.NONE;
            contrainte.weightx = 0.5f;
            contrainte.anchor = GridBagConstraints.BASELINE_LEADING;
            contrainte.insets = new Insets(15, 30, 0, 30);
            contrainte.gridwidth = GridBagConstraints.RELATIVE;

            JLabel _labelPseudo = new JLabel("Pseudo Adversaire : " + _opponentProfil.getPseudo());
            contrainte.gridy = 1;
            this.add(_labelPseudo, contrainte);

            JLabel _labelPrenom = new JLabel("Prénom : " + _opponentProfil.getName());
            contrainte.gridy = 2;
            this.add(_labelPrenom, contrainte);

            JLabel _labelGamesWon = new JLabel("Parties gagnées : " + _opponentProfil.getGamesWon());
            contrainte.gridy = 3;
            this.add(_labelGamesWon, contrainte);

            JLabel _labelGamesLost = new JLabel("Parties perdues : " + _opponentProfil.getGamesLost());
            contrainte.gridy = 4;
            this.add(_labelGamesLost, contrainte);

            // Limiting doubles with 2 decimals
            DecimalFormat f = new DecimalFormat();
            f.setMaximumFractionDigits(2);
            JLabel _labelRatio = new JLabel("Ratio : " + f.format(_opponentProfil.getRatio()));
            contrainte.gridy = 5;
            this.add(_labelRatio, contrainte);

            //button detail
            _buttonDetail = new JButton("Détail");
            contrainte.gridy = 6;
            contrainte.anchor = GridBagConstraints.CENTER;
            // contrainte.anchor = GridBagConstraints.BASELINE_LEADING;
            _buttonDetail.addActionListener(new HistoricalDetail.TreatmentButtonDetail());
            this.add(_buttonDetail, contrainte);


            /*--------------partie gauche du panel : detail adversaire--------------*/
            contrainte.gridx = 1;
            contrainte.fill = GridBagConstraints.NONE;
            contrainte.anchor = GridBagConstraints.BASELINE_LEADING;
            contrainte.insets = new Insets(15, 30, 10, 30);
            contrainte.gridwidth = GridBagConstraints.RELATIVE;

            JLabel _labelDetailGameJeu = new JLabel("Detail jeu : ");
            contrainte.gridy = 1;
            this.add(_labelDetailGameJeu, contrainte);

            JLabel _labelWhite = new JLabel("Joueur Blanc : " + _game.getBlackPlayer().getPlayer().getPseudo());
            contrainte.gridy = 2;
            this.add(_labelWhite, contrainte);

            JLabel _labelBlack = new JLabel("Joueur Noir : " + _game.getWhitePlayer().getPlayer().getPseudo());
            contrainte.gridy = 3;
            this.add(_labelBlack, contrainte);

            JLabel _labelWinner = new JLabel("Vainqueur : " + (_game.getTurn() ? "Blanc" : "Noir"));
            contrainte.gridy = 4;
            this.add(_labelWinner, contrainte);

            JLabel _labelCounter = new JLabel("Date : " + _game.getDateCreat());
            contrainte.gridy = 5;
            this.add(_labelCounter, contrainte);

            //button review
            _buttonReview = new JButton("Revoir Partie");
            contrainte.gridy = 6;
            contrainte.anchor = GridBagConstraints.CENTER;
            // contrainte.anchor = GridBagConstraints.BASELINE_TRAILING;
            _buttonDetail.setPreferredSize(_buttonReview.getPreferredSize()); //permet d'avoir une taille de bouton identique à celle du bouton review
            _buttonDetail.setMinimumSize(_buttonReview.getMinimumSize()); //permet d'avoir une taille de bouton identique à celle du bouton review
            _buttonReview.addActionListener(new HistoricalDetail.TreatmentButtonReview());
            this.add(_buttonReview, contrainte);


        }
    }

    /**
     * getter game showed in the detail window
     *
     * @return Game
     */
    public Game getGame()
    {
        return _game;
    }

    /**
     * setter game showed in the detail window
     *
     * @param game which have to be show in the detail window
     */
    public void setGame(Game game)
    {
        //changer la partie a afficher
        this._game = game;
        if (this._game != null)
        {
            this._opponentProfil = this._game.getPlayerDistant().getPlayer();

        }
        else
        {
            this._opponentProfil = null;
        }

        this.removeAll();
        initComponents();
        this.updateUI();


    }

    /**
     * Handler class for 'Create' button actions
     */
    private class TreatmentButtonDetail implements ActionListener
    {
        /**
         * Handler method of the action performed on the button 'Detail'
         * Required because the class implements the interface ActionListener
         *
         * @param e event linking to the Action performed
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            //System.out.println("todo : action bouton");
          /*TODO remove*/ ArrayList<String> sg = new ArrayList<String>();
            /*TODO remove*/ sg.add("Game 1");
            _parentView.getParentFrame().setEnabled(false);
            OpponentProfileView window = new OpponentProfileView(_parentView, _opponentProfil, null);
            //_parentView.getParentFrame().loadView(new OpponentProfileView(_parentView.getParentFrame(), _opponentProfil, sg));
        }
    }

    /**
     * Handler class for 'Create' button actions
     */
    private class TreatmentButtonReview implements ActionListener
    {
        /**
         * Handler method of the action performed on the button 'Detail'
         * Required because the class implements the interface ActionListener
         *
         * @param e event linking to the Action performed
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            /*DEBUG*/
            System.out.println("todo : action bouton");
            //  _parentView.getParentFrame().loadView(new SetProfileView(_parentView.getParentFrame()));

            GridController piw;
            try
            {
                piw = new GridController(_game, true, _parentView.getParentFrame());
                try
                {
                    _parentView.getParentFrame().getNetInterface().sendGameState(true);
                } catch (ErrorSendingInGameStateException ex)
                {
                    Logger.getLogger(HistoricalDetail.class.getName()).log(Level.SEVERE, null, ex);
                }
                _parentView.getParentFrame().loadView(piw);
            } catch (PositionConstructorLineException ex)
            {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            }


        }
    }
}
