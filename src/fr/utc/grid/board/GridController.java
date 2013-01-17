
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.grid.board;

import fr.utc.data.Game;
import fr.utc.data.GameHistoric;
import fr.utc.data.Grid;
import fr.utc.data.Player;
import fr.utc.data.Position;
import fr.utc.data.historique.Action;
import fr.utc.data.historique.Message;
import fr.utc.data.historique.Move;
import fr.utc.data.historique.Transformation;
import fr.utc.exceptions.PositionConstructorColumnException;
import fr.utc.exceptions.PositionConstructorLineException;
import fr.utc.grid.GridInterface;
import fr.utc.grid.PawnTransformationPopup;
import fr.utc.grid.board.player.PlayerInfoView;
import fr.utc.grid.chatbox.ChatboxView;
import fr.utc.lobby.GameFrame;
import fr.utc.lobby.LobbyConstant;
import fr.utc.lobby.MainView;
import fr.utc.lobby.View;
import fr.utc.network.exceptions.ErrorSendingAbandonException;
import fr.utc.network.exceptions.ErrorSendingDrawException;
import fr.utc.network.exceptions.ErrorSendingDrawResponseException;
import fr.utc.network.exceptions.ErrorSendingMoveException;
import fr.utc.network.exceptions.ErrorSendingSaveRequestException;
import fr.utc.network.exceptions.ErrorSendingSaveResponseException;
import fr.utc.network.services.NetworkImplementation;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

/**
 * Implemation of GridInterface representing the main class of the grid, allowing to access all of the other elements linking them to each other
 * @author Maxime Bornemann & Etienne Gadeski
 */
public class GridController extends View implements GridInterface, ActionListener
{

    private BoardController _boardController;
    /**
     * The info view for the local player
     */
    private PlayerInfoView _localPlayer;
    /**
     * The info view for the remote player
     */
    private PlayerInfoView _remotePlayer;
    /**
     * The button used to ask for a draw
     */
    private JButton _askForDrawButton;
    /**
     * The button used to give up
     */
    private JButton _giveUpButton;
    /**
     * The button used to save and quit
     */
    private JButton _saveQuitButton;
    /**
     * Current game
     */
    public static Game _currentGame;
    /**
     * The chat view
     */
    private ChatboxView _chat;
    /**
     * The layered pane
     */
    private JLayeredPane _layeredPane = new JLayeredPane();
    /**
     * Button representing the queen in the transformation popup
     */
    private JButton queen;
    /**
     * Button representing the bishop in the transformation popup
     */
    private JButton bishop;
    /**
     * Button representing the rook in the transformation popup
     */
    private JButton rook;
    /**
     * Button representing the knight in the transformation popup
     */
    private JButton knight;
    /**
     * The pawn transformation popup
     */
    private PawnTransformationPopup _ptp;
    /**
     * The old position of the pawn on the verge of being transformed
     */
    private Position _oldPawnPosition;
    /**
     * The new position of the pawn on the verge of being transformed
     */
    private Position _newPawnPosition;
    private JButton _forwardButton;
    private JButton _bigForwardButton;
    /**
     * Constants needed for displaying the views
     */
    private final int VIEW_WIDTH = 1150;
    private final int VIEW_HEIGHT = 742;
    private final int PLAYER_INFO_X_PADDING = 724;
    private final int LOCAL_PLAYER_INFO_Y_PADDING = 194;
    private final int REMOTE_PLAYER_INFO_Y_PADDING = 35;
    private final int BUTTON_WIDTH = 120;
    private final int BUTTON_HEIGHT = 53;
    private final int BUTTON_Y_PADDING = 350;
    private final int ASK_DRAW_BUTTON_X_PADDING = 734;
    private final int FORWARD_BUTTON_X_PADDING = 794;
    private final int BIGFORWARD_BUTTON_X_PADDING = 935;
    private final int GIVE_UP_BUTTON_X_PADDING = 864;
    private final int SAVE_QUIT_BUTTON_X_PADDING = 994;
    /**
     * if true then the player can't play, if false he can
     */
    private boolean _freeze = false;
    private boolean _isReplay = false;

    public GridController(Game game, boolean isReplay, GameFrame gf) throws PositionConstructorLineException
    {
        super(742, 1150, gf);
        _currentGame = game;
         if (isReplay)
        {
            //si on replay on réinitialise la grille ici pour ne pas avoir la grille finale directement
            Grid g=new Grid();
            _currentGame.setGrid(g);
        }
        _layeredPane = new JLayeredPane();
        _isReplay = isReplay;

        // Background
        JLabel background = new JLabel(new ImageIcon(LobbyConstant.chargeFichier("fr/utc/ressources/background.png")));
        background.setBounds(0, 0, VIEW_WIDTH, VIEW_HEIGHT);



        // Chessboard
        _boardController = new BoardController(this, _currentGame.getGrid());
        _boardController.setOpaque(false);
        _boardController.setBounds(0, 0, _boardController.COMPONENT_WIDTH, _boardController.COMPONENT_HEIGHT);

        // Local player info
        _localPlayer = new PlayerInfoView(_currentGame.getPlayerLocal(), isReplay);
        _localPlayer.setBounds(PLAYER_INFO_X_PADDING, LOCAL_PLAYER_INFO_Y_PADDING, _localPlayer.COMPONENT_WIDTH, _localPlayer.COMPONENT_HEIGHT);
        _localPlayer.setOpaque(false);
        _localPlayer.resumeTimer();

        // Remote player info
        _remotePlayer = new PlayerInfoView(_currentGame.getPlayerDistant(), isReplay);
        _remotePlayer.setBounds(PLAYER_INFO_X_PADDING, REMOTE_PLAYER_INFO_Y_PADDING, _localPlayer.COMPONENT_WIDTH, _localPlayer.COMPONENT_HEIGHT);
        _remotePlayer.setOpaque(false);

        // Freeze the timer for the players
        // if one player has the timer == -1, then it a party without timer and we don't stop it
        if (_currentGame.getPlayerLocal().getTimer() >= 0
                && _currentGame.getPlayerDistant().getTimer() >= 0)
        {
        	// we pause the local player's timer
            _localPlayer.pauseTimer();
            // we pause the opponent player's timer
            _remotePlayer.pauseTimer();
            // we freeze the player with black color
            setFreeze(_currentGame.getPlayerLocal().getColor());
        }




        // Initialize buttons
        if (!isReplay)
        {
            initGameButtons();
        }
        else
        {
            initReplayButtons();
        }

        // Chat
        _chat = new ChatboxView(this);
        _chat.setBounds(PLAYER_INFO_X_PADDING + 10, _localPlayer.COMPONENT_WIDTH + REMOTE_PLAYER_INFO_Y_PADDING, _chat.getMinimumSize().width, _chat.getMinimumSize().height);

        // Adding layers
        _layeredPane.add(_chat, JLayeredPane.DEFAULT_LAYER);
        _layeredPane.add(_boardController, JLayeredPane.DEFAULT_LAYER);
        _layeredPane.add(_localPlayer, JLayeredPane.DEFAULT_LAYER);
        _layeredPane.add(_remotePlayer, JLayeredPane.DEFAULT_LAYER);

        // if the game is not a replay
        if (!isReplay)
        {
            _layeredPane.add(_askForDrawButton, JLayeredPane.DEFAULT_LAYER);
            _layeredPane.add(_giveUpButton, JLayeredPane.DEFAULT_LAYER);
            _layeredPane.add(_saveQuitButton, JLayeredPane.DEFAULT_LAYER);
        }
        // it is a replay
        else
        {
            _layeredPane.add(_forwardButton, JLayeredPane.DEFAULT_LAYER);
            _layeredPane.add(_bigForwardButton, JLayeredPane.DEFAULT_LAYER);
        }
        _layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(_layeredPane, GroupLayout.DEFAULT_SIZE, VIEW_WIDTH, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(_layeredPane, GroupLayout.DEFAULT_SIZE, VIEW_HEIGHT, Short.MAX_VALUE));


        getNetwork().setIHMGrilleInterface(this);

        /*_currentGame.getHisto().pushBack(new Message(new Date(System.currentTimeMillis()), _currentGame.getPlayerLocal(), "I ain't strapped. I ain't involved, yo. I ain't involved in none of that gangster bullshit."));
        _currentGame.getHisto().pushBack(new Move(new Date(System.currentTimeMillis()),_currentGame.getPlayerLocal(),new Position(1, 7), new Position(1, 6)));

        
        _currentGame.getHisto().pushBack(new Message(new Date(System.currentTimeMillis()+300000), _currentGame.getPlayerLocal(), "What y'all niggers want, man? Huh? Money?"));
        _currentGame.getHisto().pushBack(new Message(new Date(System.currentTimeMillis()+300000), _currentGame.getPlayerLocal(), "IS THAT IT? Cause if it is, I can be a better friend to y'all alive."));
        _currentGame.getHisto().pushBack(new Message(new Date(System.currentTimeMillis()+500000), _currentGame.getPlayerDistant(), "You still don't get it, do you? This ain't about your money, bro. Your boy gave you up. That's right. And we ain't had to torture his ass neither!"));
        _currentGame.getHisto().pushBack(new Message(new Date(System.currentTimeMillis()+300000), _currentGame.getPlayerLocal(), "Doesn't seem like I could do anything to change y'all minds."));
        _currentGame.getHisto().pushBack(new Message(new Date(System.currentTimeMillis()+300000), _currentGame.getPlayerLocal(), "Well, get on with it, motherfu.."));*/


    }

    private void initGameButtons()
    {
        // Ask for draw button
        String label = "<html>" + "<center>Demander" + "<br>" + "match nul</center>" + "</html>";
        AbstractAction action = new AbstractAction(label)
        {

            public void actionPerformed(ActionEvent evt)
            {
            	// if the game is not freeze and not finished
                if (!isFreeze() && !_currentGame.isCompleted())
                {
                    // if the player chose YES
                    int res = JOptionPane.showConfirmDialog(GridController.this, "Etes vous sûr de vouloir demander un match nul", "Fin de partie", JOptionPane.YES_NO_OPTION);
                    // if the player chose YES
                    if (res == JOptionPane.YES_OPTION)
                    {
                        try
                        {
                            // TODO Pour les tests (à enlever en prod)
                            setFreeze(true);

                            if (getNetwork() != null)
                            {
                                getNetwork().sendDraw();
                                //freezes the game while waiting the answer
                                setFreeze(true);
                            }

                        } catch (ErrorSendingDrawException ex)
                        {
                            Logger.getLogger(GridController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(GridController.this, "Ce n'est pas à vous de jouer", "Attention", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };
        _askForDrawButton = new JButton(action);
        _askForDrawButton.setBounds(ASK_DRAW_BUTTON_X_PADDING, BUTTON_Y_PADDING, BUTTON_WIDTH, BUTTON_HEIGHT);

        // Give up button
        label = "<html>" + "Abandonner" + "</html>";
        action = new AbstractAction(label)
        {

            public void actionPerformed(ActionEvent evt)
            {
                // if the game is not freeze and not finished
                if (!isFreeze() && !_currentGame.isCompleted())
                {
                	// the int which contains the choice of the player (Yes or No)
                    int res = JOptionPane.showConfirmDialog(GridController.this, "Etes vous sûr de vouloir abandonner la partie", "Fin de partie", JOptionPane.YES_NO_OPTION);
                    // if the player chose YES
                    if (res == JOptionPane.YES_OPTION)
                    {
                        try
                        {
                        	// we call the surrender method
                            _currentGame.surrender();
                            // we send the surrender to the opponent player
                            getNetwork().sendAbandon();
                            _currentGame.surrender();
                            // we freeze the game
                            setFreeze(true);
                        } catch (ErrorSendingAbandonException ex)
                        {
                            Logger.getLogger(GridController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                // it is not the local player's turn to play
                else
                {
                	// we display a popup to inform the player that it is not his turn
                    JOptionPane.showMessageDialog(GridController.this, "Ce n'est pas à vous de jouer", "Attention", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        };
        _giveUpButton = new JButton(action);
        _giveUpButton.setBounds(GIVE_UP_BUTTON_X_PADDING, BUTTON_Y_PADDING, BUTTON_WIDTH, BUTTON_HEIGHT);

        // Save and quit 
        label = "<html>" + "<center>Sauvegarder <br/> et quitter</center>" + "</html>";
        action = new AbstractAction(label)
        {

            public void actionPerformed(ActionEvent evt)
            {
            	// if the game is not freeze and not finished
                if (!isFreeze() && !_currentGame.isCompleted())
                {
                	// the int which contains the choice of the player (Yes or No)
                    int res = JOptionPane.showConfirmDialog(GridController.this, "Proposer de sauvegarder et quitter la partie", "Fin de partie", JOptionPane.YES_NO_OPTION);
                 // if the player chose YES
                    if (res == JOptionPane.YES_OPTION)
                    {
                        try
                        {
                        	// we save the request
                            getNetwork().sendSaveRequest();
                            //freezes the game while waiting the answer
                            setFreeze(true);
                        } catch (ErrorSendingSaveRequestException ex)
                        {
                            Logger.getLogger(GridController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                // if the game is completed
                else if (_currentGame.isCompleted())
                {
                	// the int which contains the choice of the player (Yes or No)
                    int res = JOptionPane.showConfirmDialog(GridController.this, "Sauvegarder et quitter la partie ?", "Fin de partie", JOptionPane.YES_NO_OPTION);
                    // if the player chose YES
                    if (res == JOptionPane.YES_OPTION)
                    {
                    	// we save and quit
                        saveQuit();
                    }
                }
                else
                {
                	// we display a popup to inform the player that it is not his turn
                    JOptionPane.showMessageDialog(GridController.this, "Ce n'est pas à vous de jouer", "Attention", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };
        _saveQuitButton = new JButton(action);
        _saveQuitButton.setBounds(SAVE_QUIT_BUTTON_X_PADDING, BUTTON_Y_PADDING, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    private void initReplayButtons()
    {
        // Forward buttonµ
        String label = "<html>" + "<center><h1><strong>►</strong></h1></center>" + "</html>";
        final GameHistoric _history = _currentGame.getHisto();
        AbstractAction action = new AbstractAction(label)
        {

            public void actionPerformed(ActionEvent evt)
            {
                // Perform action
                Action msg1 = _history.popFront();
                if (msg1 instanceof Move)
                {
                    Move mov = (Move) msg1;
                    try
                    {
                        _currentGame.doMove(mov.getOldPos(), mov.getNewPos());
                    } catch (PositionConstructorColumnException ex)
                    {
                        Logger.getLogger(GridController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try
                    {
                        _boardController.UpdateGrid();
                    } catch (PositionConstructorLineException ex)
                    {
                        Logger.getLogger(GridController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                // if the author of the message is the local player
                if (msg1.getEmitter() == _currentGame.getPlayerLocal())
                {
                	// we write the message and indicate that the author is the local player
                    _chat.writePostPlayer(1, msg1);
                }
                // the author is the opponent player
                else
                {
                	// we write the message and indicate that the author is the opponent player
                    _chat.writePostPlayer(2, msg1);

                }
            }
        };
        _forwardButton = new JButton(action);
        _forwardButton.setBounds(FORWARD_BUTTON_X_PADDING, BUTTON_Y_PADDING, BUTTON_WIDTH, BUTTON_HEIGHT);

        // Big forward button
        label = "<html>" + "<center><h1><strong>►►</strong></h1></center>" + "</html>";
        action = new AbstractAction(label)
        {
            public void actionPerformed(ActionEvent evt)
            {
                // Perform action
                Action msg1 = _history.popFront();
                while (msg1 instanceof Message)
                {
                    msg1 = _currentGame.getHisto().popFront();
                    System.out.println("test" + msg1 );
                }
                Move mov = (Move) msg1;
                System.out.println("test2" + mov );
                try
                {
                    // Erreur ci dessous Data renvoie un mouvement null donc la grid ne se met pas à jour
                     _currentGame.doMove(mov.getOldPos(), mov.getNewPos());
                } catch (PositionConstructorColumnException ex)
                {
                    Logger.getLogger(GridController.class.getName()).log(Level.SEVERE, null, ex);
                }
                try
                {
                    _boardController.UpdateGrid();
                } catch (PositionConstructorLineException ex)
                {
                    Logger.getLogger(GridController.class.getName()).log(Level.SEVERE, null, ex);
                }
                // if the author of the message is the local player
                if (msg1.getEmitter() == _currentGame.getPlayerLocal())
                {
                	// we write the message and indicate that the author is the local player
                    _chat.writePostPlayer(1, msg1);
                }
                // the author is the opponent player
                else
                {
                	// we write the message and indicate that the author is the opponent player
                    _chat.writePostPlayer(2, msg1);

                }
            }
        };
        _bigForwardButton = new JButton(action);
        _bigForwardButton.setBounds(BIGFORWARD_BUTTON_X_PADDING, BUTTON_Y_PADDING, BUTTON_WIDTH, BUTTON_HEIGHT);

    }

    /**
     * the function which handles the disconnection of the opponent
     */
    public void receiveDisconnectionOpponent()
    {
    	// we end the game with an information popup
        endGame("Le joueur adverse c'est déconnecté.");
    }

    /**
     * the function which handles the reception of a message
     */
    public void receiveMessage(Message msg)
    {
    	// we write the message and indicate that the author is the opponent
        _chat.writePostPlayer(2, msg);
    }

    /**
     * the function which handles the reception of a move
     */
    public void receiveMove(Move deplacement)
    {
        System.out.println("ttt");
        try
        {
            Move displacement;

            if (deplacement != null)
            {
                System.out.println("deplacement : " + deplacement.toString());
            }
            else
            {
                System.out.println("Error deplacement null");
            }
            if (deplacement instanceof Transformation)
            {
                Transformation transf = (Transformation) deplacement;
                displacement = this._currentGame.doMoveWithTransformation(transf.getOldPos(), transf.getNewPos(), transf.getNewPieceName());
            }
            else
            {
                displacement = this._currentGame.doMove(deplacement.getOldPos(), deplacement.getNewPos());
            }
            System.out.println("Déplacement autorisé " + displacement);
            if (displacement != null)
            {
                try
                {
                    this._boardController.UpdateGrid();
                    this.setFreeze(false);
                } catch (PositionConstructorLineException ex)
                {
                    Logger.getLogger(BoardCell.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // write the move in the Chatbox
            _chat.writePostPlayer(2, deplacement);
        } catch (PositionConstructorColumnException ex)
        {
            Logger.getLogger(GridController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * the function which handles the reception of the surrender
     */
    public void receiveAbandon()
    {
    	// we call the function that receive the surrender
        _currentGame.receiveSurrender();
        // we end the game with an information popup
        endGame("Le joueur adverse a abandonné la partie.");
        _currentGame.receiveSurrender();
        //freeze the game 
        setFreeze(true);
    }

    /**
     * the function which handles the reception of a draw
     */
    public void receiveDraw()
    {
    	// the int which contains the choice of the player (Yes or No)
        int res = JOptionPane.showConfirmDialog(GridController.this, "Le joueur adverse propose de déclarer un match nul.", "Match nul ?", JOptionPane.YES_NO_OPTION);
        // if the player chose YES
        if (res == JOptionPane.YES_OPTION)
        {
            try
            {
                //send response
                getNetwork().sendDrawResponse(true);
                // we set the game with draw
                _currentGame.setWinner(Game.winner.draw);

            } catch (ErrorSendingDrawResponseException ex)
            {
                Logger.getLogger(GridController.class.getName()).log(Level.SEVERE, null, ex);
            }
            endGame("Match nul");
        }
        else
        {
            try
            {
                //send response
                getNetwork().sendDrawResponse(false);
            } catch (ErrorSendingDrawResponseException ex)
            {
                Logger.getLogger(GridController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @return the _boardController
     */
    public BoardController getBoardController()
    {
        return _boardController;
    }

    /**
     * @param _boardController the _boardController to set
     */
    public void setBoardController(BoardController boardController)
    {
        this._boardController = boardController;
    }

    /**
     * the function which handles the reception of a save request
     */
    public void receiveSaveRequest()
    {
    	// the int which contains the choice of the player (Yes or No)
        int res = JOptionPane.showConfirmDialog(GridController.this, "Le joueur adverse propose de sauvegarder et quitter la partie. Terminer la partie ", "Sauvegarder / quitter", JOptionPane.YES_NO_OPTION);
        // if the player chose YES
        if (res == JOptionPane.YES_OPTION)
        {
            try
            {
                //send response
                getNetwork().sendSaveResponse(true);
            } catch (ErrorSendingSaveResponseException ex)
            {
                Logger.getLogger(GridController.class.getName()).log(Level.SEVERE, null, ex);
            }

            //save the game and quit
            saveQuit();
        }
        else
        {
            try
            {
                //send response
                getNetwork().sendSaveResponse(false);
            } catch (ErrorSendingSaveResponseException ex)
            {
                Logger.getLogger(GridController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        try
        {
            String transformedPieceName = "";
            if (e.getSource() == queen)
            {
                transformedPieceName = "Queen";
            }

            if (e.getSource() == bishop)
            {
                transformedPieceName = "Bishop";
            }

            if (e.getSource() == rook)
            {
                transformedPieceName = "Rook";
            }

            if (e.getSource() == knight)
            {
                transformedPieceName = "Knight";
            }
            _layeredPane.remove(_ptp);
            _layeredPane.repaint();
            //_currentGame.doMoveWithTransformation(_oldPawnPosition, _newPawnPosition, transformedPieceName);
            Move displacement = _currentGame.doMoveWithTransformation(_oldPawnPosition, _newPawnPosition, transformedPieceName);
            System.out.println("Déplacement et transformation autorisé " + displacement);
            if (displacement != null)
            {
                try
                {
                    this._boardController.UpdateGrid();
                    try
                    {
                        this.getNetwork().sendMove(displacement);
                        this.setFreeze(true);
                    } catch (ErrorSendingMoveException ex)
                    {
                        Logger.getLogger(GridController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } catch (PositionConstructorLineException ex)
                {
                    Logger.getLogger(BoardCell.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (PositionConstructorColumnException ex)
        {
            Logger.getLogger(GridController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * the function that handles the response of a draw request
     */
    public void receiveDrawResponse(boolean response)
    {
    	// if the response is true
        if (response == true)
        {
        	// we end the game with a information popup
            endGame("Votre adversaire à accepté de déclarer le match nul");
            // we set the winner as draw
            _currentGame.setWinner(Game.winner.draw);
        }
        // if the response is false
        else
        {
        	// we display a popup to inform the player that the opponent disagreed
            JOptionPane.showMessageDialog(GridController.this, "Le joueur adverse a refusé de déclarer le match nul.", "Match nul", JOptionPane.INFORMATION_MESSAGE);
            //Unfreeze the game
            setFreeze(false);
        }
    }

    /**
     * the function which handles the reception of an error
     */
    public void receiveError(String msg)
    {
        //show the error message in a popup
        JOptionPane.showMessageDialog(GridController.this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * the function which handles the reponse for Save
     */
    public void receiveSaveResponse(boolean response)
    {
    	// if the response is YES
        if (response == true)
        {
        	// we save and quit
            saveQuit();
        }
        // if the response is NO
        else
        {
        	// we display a popup to inform the player that the opponent disagreed
            JOptionPane.showMessageDialog(GridController.this, "Le joueur adverse a refusé de sauvegarder et quitter la partie.", "Sauvegarder / quitter", JOptionPane.INFORMATION_MESSAGE);
            //Unfreeze the game
            setFreeze(false);
        }
    }

    /**
     * the function which handles the reception of a timeout event
     */
    public void receiveTimeout()
    {
        //_currentGame.setWinner();
        endGame("Le joueur adverse a écoulé sont temps de jeu. Vous avez gagné !");

    }

    /**
     * the function which ends the game and display an information message
     */
    public void endGame(String msg)
    {
    	// we display a popup to inform the player that the game has ended
        JOptionPane.showMessageDialog(GridController.this, msg, "Fin de partie", JOptionPane.INFORMATION_MESSAGE);
        // if the game is not a replay
        if (!_isReplay)
        {
            //freezes the game while waiting
            setFreeze(true);
        }
    }

    /**
     * the function that save and quit
     */
    public void saveQuit()
    {
        // save the current game
        _currentGame.save();
        // serialize the game
        _currentGame.Serializable();
    	// we display a popup to inform the player that the game has been saved
        JOptionPane.showMessageDialog(GridController.this, "La partie a été sauvegardée.", "Sauvegarder / quitter", JOptionPane.INFORMATION_MESSAGE);
        // we load the View with the lobby View
        this.getParentFrame().loadView(new MainView(this.getParentFrame()));
    }

    public void transformPawn(Position oldPos, Position newPos, boolean color)
    {
        _oldPawnPosition = oldPos;
        _newPawnPosition = newPos;
        _ptp = new PawnTransformationPopup(color);
        _ptp.setBounds(575 - _ptp.getPOPUP_WIDTH() / 2, 300, _ptp.getPOPUP_WIDTH(), _ptp.getPOPUP_HEIGHT());
        _layeredPane.add(_ptp, JLayeredPane.POPUP_LAYER);
        queen = _ptp.getQueen();
        queen.addActionListener(this);

        bishop = _ptp.getBishop();
        bishop.addActionListener(this);

        rook = _ptp.getRook();
        rook.addActionListener(this);

        knight = _ptp.getKnight();
        knight.addActionListener(this);

        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * getter of _freeze attribut
     */
    public boolean isFreeze()
    {
        return _freeze;
    }

    /**
     * set the freeze on _freeze and freezes the timers or unfreezes them
     * @param freeze
     * 			boolean that indicates which player we freeze and unfreeze
     */
    public void setFreeze(boolean freeze)
    {
    	// set the freeze value for _freeze
        this._freeze = freeze;
        // if the freez is true
        if (freeze)
        {
            // freeze the timer of the local player
            _localPlayer.pauseTimer();
            // unfreeze the timer of the opponent player
            _remotePlayer.resumeTimer();
        }
        else
        {
            // unfreeze the timer of the local player
            _localPlayer.resumeTimer();
            // freeze the timer of the local player
            _remotePlayer.pauseTimer();
        }
    }

    /**
     * freezes the timers or unfreezes them based on the color of the local player
     */
    public void setFreeze()
    {
        // set the boolean _freeze with the boolean which represents the local player's color
        this._freeze = _currentGame.getPlayerLocal().getColor();
        // if the local player is black
        if (_currentGame.getPlayerLocal().getColor())
        {
        	// we freeze the local player's timer
            _localPlayer.pauseTimer();
        	// we unfreeze the opponent player's timer
            _remotePlayer.resumeTimer();
        }
        else
        {
        	// we unfreeze the local player's timer
            _localPlayer.resumeTimer();
            // we freeze the local player's timer
            _remotePlayer.pauseTimer();

        }
    }

    public NetworkImplementation getNetwork()
    {
        if (this != null && this.getParentFrame() != null && this.getParentFrame().getNetInterface() != null)
        {
            return this.getParentFrame().getNetInterface();
        }
        else
        {
            return null;
        }
    }
}
