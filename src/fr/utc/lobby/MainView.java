/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//TODO méthode pour récupérer les infos dans l'arrayList de profils et faire un tableau[][]
//TODO méthode de mise à jour de _listPanel
package fr.utc.lobby;

import fr.utc.data.Game;
import fr.utc.data.Player;
import fr.utc.data.Profile;
import fr.utc.exceptions.PositionConstructorLineException;
import fr.utc.grid.board.GridController;
import fr.utc.network.exceptions.ErrorSendingGameResponseException;
import fr.utc.network.exceptions.ErrorSendingInGameStateException;
import fr.utc.network.profile.ManagerListPlayer;
import fr.utc.network.services.NetworkImplementation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

/**
 * Generation and management of the MainView window
 *
 * @author lo23a014
 */
public class MainView extends View implements IHMConnexion2Reseau
{
    private ProfilePlayerPan _panelPlayer;
    private ListOpponentPan _panelListOp;
    private ProfileOpponentPan _panelOpponent;
    private HistoricalList _panelListHist;
    private HistoricalDetail _panelDetailHist;
    private JMenuBar _menuBar;
    private JMenu _menu;
    private boolean _historicalView;

    public boolean isHistoricalView()
    {
        return _historicalView;
    }
    private ManagerListPlayer _listOpponents;

    /**
     * Default constructor
     */
    public MainView(GameFrame frame)
    {
        super(LobbyConstant.MAIN_VIEW_SIZE_H, LobbyConstant.MAIN_VIEW_SIZE_W, frame);

        this._historicalView = false;
        try
        {
            getParentFrame().getNetInterface().sendGameState(false);
        } catch (ErrorSendingInGameStateException ex)
        {
            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
        }
        initView();
        initComponents();
    }

    /**
     * View initialization
     *
     */
    private void initView()
    {
        // Panels
        _panelPlayer = new ProfilePlayerPan(this);
        _panelListHist = new HistoricalList(this);
        _panelDetailHist = new HistoricalDetail(this);
        _panelListOp = new ListOpponentPan(this);
        _panelOpponent = new ProfileOpponentPan(this);

        // Menu bar
        _menuBar = new JMenuBar();
        _menu = new JMenu("Deconnexion");

        // Mouse Listener pour la deconnexion
        _menu.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                // TODO Auto-generated method stub
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getSource() != null)
                {
                    // Asking if we must quit the app
                    int confirmation = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de vouloir vous deconnecter?",
                            "Information", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (confirmation == JOptionPane.OK_OPTION)
                    {
                        NetworkImplementation.sendLogout();
                        getParentFrame().setJMenuBar(null);
                        getParentFrame().loadView(new LogView(getParentFrame()));
                    }
                }

            }
        });


        NetworkImplementation.getListOfProfiles().addObserver(_panelListOp);
    }

    /**
     * Components initialization and positioning in the window
     *
     */
    private void initComponents()
    {
        //_menu.setMnemonic(KeyEvent.VK_A);
        _menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        _menuBar.add(_menu);
        this.getParentFrame().setJMenuBar(_menuBar);

        this.setLayout(new GridBagLayout());
        GridBagConstraints contrainte = new GridBagConstraints();

        //contrainte géneral
        contrainte.fill = GridBagConstraints.BOTH;

        // _panelPlayer = new ProfilePlayerPan(this);
        contrainte.gridx = 0;
        contrainte.gridy = 0;
        contrainte.gridheight = 2;
        contrainte.weightx = LobbyConstant.PROFILE_PLAY_PAN_WX;
        this.add(_panelPlayer, contrainte);

        //contraintes commune aux deux autres panels
        contrainte.gridheight = 1;
        contrainte.gridx = 1;
        contrainte.weightx = LobbyConstant.LIST_OPP_PAN__PROFILE_OPP_PAN_WX;


        if (_historicalView)
        {
            _panelListHist = new HistoricalList(this);
            contrainte.gridy = 0;
            contrainte.weighty = LobbyConstant.LIST_OPP_PAN_WY;
            //_panelListHist.setBackground(Color.red);
            this.add(_panelListHist, contrainte);


            _panelDetailHist = new HistoricalDetail(this);
            contrainte.gridy = 1;
            contrainte.weighty = LobbyConstant.PROFILE_OPP_PAN_WY;
            // _panelDetailHist.setBackground(Color.yellow);
            this.add(_panelDetailHist, contrainte);
        }
        else
        {
            _panelListOp = new ListOpponentPan(this);

            contrainte.gridy = 0;
            contrainte.weighty = LobbyConstant.LIST_OPP_PAN_WY;
            this.add(_panelListOp, contrainte);

            _panelOpponent = new ProfileOpponentPan(this);
            contrainte.gridy = 1;
            contrainte.weighty = LobbyConstant.PROFILE_OPP_PAN_WY;
            this.add(_panelOpponent, contrainte);
        }
        this.updateUI();

    }

    /**
     * Setter for the attribute '_historicalView'
     *
     * @param historicalView
     */
    public void setHistoricalView(boolean ishistoricalView)
    {
        this._historicalView = ishistoricalView;
        this.removeAll();
        initComponents();
    }

    /**
     * Setter the game to the detail panel'
     *
     * @param Game : game we want display detail
     */
    public void setGameDetail(Game game)
    {
        _panelDetailHist.setGame(game);
    }

    /**
     * Setter the game to the detail panel'
     *
     * @param Game : game we want display detail
     */
    public void setOppDetail(Profile opp)
    {
        _panelOpponent.setOpponentProfile(opp);
    }

    /**
     * Interface method IHMConnexion2Reseau to create a new game
     *
     * @param idOpponent String The opponent identifiant
     * @param color int The color wanted by the opponent
     * @param timer int The timer wanted by the opponent
     */
    @Override
    public void newGameEvent(String idOpponent, boolean color, int timer, UUID _IDGame)
    {
        // Refresh in the case of a connection between two refreshments

        this._listOpponents = this.getParentFrame().getNetInterface().getListOfUsers();

        for (int i = 0; i < this._listOpponents.getPlayerList().size(); i++)
        {
            if (idOpponent.equals(this._listOpponents.getPlayerList().get(i).getProfile().getID()))
            {
                /*
                 if(this.popup==null)
                 {
                 this.popup = new ResponseRequestPopUp(this._listOpponents.getPlayerList().get(i).getProfile(), color, timer, true, "", this,_IDGame);
                 }
                 else
                 {
                 this.getParentFrame().setEnabled(true);
                 this.popup = new ResponseRequestPopUp(this._listOpponents.getPlayerList().get(i).getProfile(), color, timer, true, "", this,_IDGame);
                 }*/
                if (this.popup != null)
                {
                    this.popup.setEnabled(false);//on désactive la popup actuel
                    if (this.popup instanceof OpponentProfileView)
                    {
                        if (((OpponentProfileView) (this.popup)).popup != null);
                        ((OpponentProfileView) (this.popup)).popup.setEnabled(false);
                    }
                }
                new ResponseRequestPopUp(this._listOpponents.getPlayerList().get(i).getProfile(), color, timer, true, "", this, _IDGame, this.popup);
                break;
            }
        }
    }

    /**
     * Method which send the response to network about the game creation event
     *
     * @param response Boolean The response
     */
    public void sendResponseNetwork(String idOpponent, boolean response)
    {
        try
        {
            this.getParentFrame().getNetInterface().sendGameResponse(idOpponent, response);
        } catch (ErrorSendingGameResponseException ex)
        {
            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Interface method IHMConnexionToReseau to treat the opponent response
     *
     * @param id_opponent String The opponent identifiant
     * @param response Boolean The response
     */
    @Override
    public void receiveResponse(String idOpponent, boolean response)
    {
        try
        {
            super.popup.dispose(); // bah ouais les gars...
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        System.out.println(idOpponent + " - " + response);

        //NetworkImplementation.get

        this._listOpponents = this.getParentFrame().getNetInterface().getListOfUsers();
        for (int i = 0; i < this._listOpponents.getPlayerList().size(); i++)
        {
            if (idOpponent.equals(this._listOpponents.getPlayerList().get(i).getProfile().getID()))
            {
                if (response)
                {
                    super.getParentFrame().setEnabled(true);
                    try
                    {
                        try
                        {
                            this.getParentFrame().getNetInterface().sendGameState(true);//state = ingame !
                        } catch (ErrorSendingInGameStateException ex)
                        {
                            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        GridController piw = new GridController(super.getParentFrame().getLastGameRequested(), false, this.getParentFrame());
                        this.getParentFrame().loadView(piw);
                    } catch (Exception ex)
                    {
                        Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Le joueur "
                            + this._listOpponents.getPlayerList().get(i).getProfile().getPseudo() + " a refusé la partie",
                            "Réponse: Partie refusée", JOptionPane.INFORMATION_MESSAGE);
                    super.getParentFrame().setEnabled(true);
                }
            }
        }
    }

    /**
     * Interface method IHMConnexionToReseau to load a game
     *
     * @param idOpponent String The opponent identifiant
     * @param idGame String The game identifiant
     */
    @Override
    public void loadGameEvent(String idOpponent, UUID idGame)
    {
        // Refresh in the case of a connection between two refreshments 
        this._listOpponents = this.getParentFrame().getNetInterface().getListOfUsers();

        ArrayList<Game> gamesSaved = this.getParentFrame().getUserProfile().getGamesNonCompleted();

        // Cross the list of opponents
        for (int i = 0; i < this._listOpponents.getPlayerList().size(); i++)
        {
            if (idOpponent.equals(this._listOpponents.getPlayerList().get(i).getProfile().getPseudo()))
            {
                // Cross the list of games saved and non completed
                for (int j = 0; j < gamesSaved.size(); j++)
                {
                    if (idGame.equals(gamesSaved.get(j).getId()))
                    {
                        new ResponseRequestPopUp(this._listOpponents.getPlayerList().get(i).getProfile(),
                                gamesSaved.get(j).getPlayerLocal().getColor(),
                                gamesSaved.get(j).getPlayerLocal().getTimer(), false,
                                gamesSaved.get(j).getDateCreat(), this, idGame, this.popup);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Setter for a new game
     *
     * @param idOpponent String The opponent identifiant
     * @param color your color for the game
     * @param timer the timer of the game
     * @param idGame String The game identifiant
     */
    public Game setNewGame(String idOpponent, boolean color, int timer, UUID IDGame)
    {
        this._listOpponents = this.getParentFrame().getNetInterface().getListOfUsers();

        for (int i = 0; i < this._listOpponents.getPlayerList().size(); i++)
        {
            if (idOpponent.equals(this._listOpponents.getPlayerList().get(i).getProfile().getID()))
            {
                Player p1 = new Player(this.getParentFrame().getUserProfile(), color, timer);
                Player p2 = new Player(this._listOpponents.getPlayerList().get(i).getProfile(), !color, timer);
                Game game = new Game(p1, p2);
                if (IDGame == null)
                {
                    game.setId(IDGame);
                }
                this.getParentFrame().setLastGameRequested(game);
                return game;
            }
        }
        return null;
    }

    /**
     * start a Game
     *
     * @param idOpponent String The opponent identifiant
     * @param idGame String The game identifiant
     */
    public void launchGame(String idOpponent, UUID IDGame)
    {
        if (this.getParentFrame().getLastGameRequested() != null) //newgame donc on prend la partie deja initialisé
        {
            try
            {
                try
                {
                    this.getParentFrame().getNetInterface().sendGameState(true);//state = ingame !
                } catch (ErrorSendingInGameStateException ex)
                {
                    Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
                }
                GridController piw = new GridController(this.getParentFrame().getLastGameRequested(), false, this.getParentFrame());
                this.getParentFrame().loadView(piw);
            } catch (PositionConstructorLineException ex)
            {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else //loadgame
        {
            ArrayList<Game> listGame = this.getParentFrame().getUserProfile().checkGamesNonCompletedWith(this.getParentFrame().getUserProfile().getID(), idOpponent);
            for (Iterator<Game> it = listGame.iterator(); it.hasNext();)
            {
                Game game = it.next();
                if (game.getId() == IDGame)//on retrouve la partie avec le même ID pour lancer la grille
                {
                    try
                    {
                        try
                        {
                            this.getParentFrame().getNetInterface().sendGameState(true);//state = ingame !
                        } catch (ErrorSendingInGameStateException ex)
                        {
                            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        GridController piw = new GridController(game, false, this.getParentFrame());
                        this.getParentFrame().loadView(piw);
                    } catch (PositionConstructorLineException ex)
                    {
                        Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        /*try
         {
         // Lance la partie avec IHM grille
         GridController piw = new GridController(, false);
         //sendInGameState
         } catch (PositionConstructorLineException ex)
         {
         Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
         }*/

    }

    /**
     * Interface method IHMConnexionToReseau to receive an error when a message
     * is sent
     *
     * @param idOpponent String The opponent identifiant
     * @param idGame String The game identifiant
     */
    //@Override
    public void receiveErrorSendingMessage(String description)
    {
        JOptionPane.showMessageDialog(null, description, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * getter Player panel
     *
     * @return return the player panel
     */
    public ProfilePlayerPan getProfilePlayerPan()
    {
        return this._panelPlayer;
    }
}
