/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lobby;

import fr.utc.data.Profile;
import fr.utc.network.exceptions.ErrorAskingLoadGameException;
import fr.utc.network.exceptions.ErrorAskingNewGameException;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author lo23a014
 */
public class WaitingResponsePopUp extends JFrame
{
    private View _parent;
    private OpponentProfileView _parent2;
    private Profile _opponentProfile;
    private int _timer;
    private boolean _color;
    private int _remainingTime;
    private String _opponentName;
    private boolean _isNewGame;
    private JPanel _panelTop;
    private JLabel _JLgameType;
    private JLabel _JLtitle;
    private JLabel _JLcolor;
    private JLabel _JLtimer;
    private JLabel _JLtimeBeforeDecline;
    private Timer timer;
    private UUID _IdGame;

    public WaitingResponsePopUp(View parent, Profile opponentProfile, int timer, boolean color, boolean isNewGame,UUID idGame)
    {
        //this._opponentPseudo = opponentPseudo;
        this._parent = parent;
        this._parent2 = null;
        setIconImage(new ImageIcon(LobbyConstant.chargeFichier(LobbyConstant.getPathLogo())).getImage());
        initVariable(opponentProfile,timer,color,isNewGame,idGame);
        
        if(isNewGame)
        {
            ((MainView) this._parent).setNewGame(this._opponentProfile.getID(), _color, _timer, null);
            try
            {
                parent.getParentFrame().getNetInterface().askNewGame(_opponentProfile.getId(), color, timer, ((MainView) this._parent).getParentFrame().getLastGameRequested().getId());
            } catch (ErrorAskingNewGameException ex)
            {
                Logger.getLogger(WaitingResponsePopUp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            try
            {
                parent.getParentFrame().getNetInterface().askLoadGame(_opponentProfile.getId(), idGame);
            } catch (ErrorAskingLoadGameException ex)
            {
                Logger.getLogger(WaitingResponsePopUp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public WaitingResponsePopUp(OpponentProfileView parent, Profile opponentProfile, int timer, boolean color, boolean isNewGame,UUID idGame)
    {
        //this._opponentPseudo = opponentPseudo;
        this._parent = null;
        this._parent2 = parent;
        
        initVariable(opponentProfile,timer,color,isNewGame,idGame);

        if(isNewGame)
        {
            //ask a Game request to the oppent
            try
            {
                ((MainView) this._parent2.getParent()).getParentFrame().getNetInterface().askNewGame(_opponentProfile.getId(), color, timer, ((MainView) this._parent2.getParent()).getParentFrame().getLastGameRequested().getId());
            } catch (ErrorAskingNewGameException ex)
            {
                Logger.getLogger(WaitingResponsePopUp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            try
            {
                ((MainView) this._parent2.getParent()).getParentFrame().getNetInterface().askLoadGame(_opponentProfile.getId(), idGame);
            } catch (ErrorAskingLoadGameException ex)
            {
                Logger.getLogger(WaitingResponsePopUp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void initVariable(Profile opponentProfile, int timer, boolean color, boolean isNewGame,UUID idGame)
    {
        this._opponentProfile = opponentProfile;
        this._IdGame = idGame;

        this.setTitle("normal Attente de réponse ...");
        this.setResizable(false);
        this.setSize(LobbyConstant.WAITING_RESPONSE_POPUP_SIZE_W, LobbyConstant.WAITING_RESPONSE_POPUP_SIZE_H);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Center the window
        this.setLocationRelativeTo(this.getParent());
        // Récuperer les données...
        _timer = timer;
        _color = color;
        _isNewGame = isNewGame;
        _opponentName = _opponentProfile.getPseudo();
        _remainingTime = 30;
        
        closingWindow();

        initComponents();

        this.setVisible(true);
    }

    private void initComponents()
    {

        this._panelTop = new JPanel();

        this._JLtitle = new JLabel("Attente de réponse pour la " + (_isNewGame ? "nouvelle" : "reprise de") + " partie\n avec le joueur " + this._opponentName + "...");
        this._JLgameType = new JLabel("Type de Partie :");
        /*if(_isNewGame){
         this._JLnewGame = new JLabel("Nouvelle Partie");
         }else{
         this._JLnewGame = new JLabel("Reprise Ancienne Partie");
         }*/
        if (this._color)
        {
            this._JLcolor = new JLabel("Votre couleur : Noir");
        }
        else
        {
            this._JLcolor = new JLabel("Votre couleur : Blanc");
        }
        if (this._timer == -1)
        {
            this._JLtimer = new JLabel("Timer : Non");
        }
        else
        {
            this._JLtimer = new JLabel("Timer : il vous reste " + this._timer + "secondes");
        }

        this._JLtimeBeforeDecline = new JLabel("Temps avant refus... " + String.valueOf(_remainingTime) + "s");
        // General Layout
        this.setLayout(new GridLayout(1, 0));
        this.add(this._panelTop);

        this._panelTop.setLayout(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();


        //Title
        constraint.anchor = GridBagConstraints.CENTER;
        constraint.gridwidth = 0;
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.gridheight = 1;
        constraint.insets = new Insets(0, 0, 30, 0);
        this._JLtitle.setFont(new Font(this._JLtitle.getText(), Font.BOLD, 14));
        this._panelTop.add(this._JLtitle, constraint);

        //PartyType
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.anchor = GridBagConstraints.BASELINE_LEADING;
        constraint.gridwidth = GridBagConstraints.REMAINDER;

        constraint.gridx = 0;
        constraint.gridy += 1;
        constraint.insets = new Insets(0, 30, 0, 0);
        this._JLgameType.setFont(new Font(this._JLtitle.getText(), Font.BOLD, 12));
        this._panelTop.add(this._JLgameType, constraint);

        constraint.gridy += 1;
        this._JLcolor.setFont(new Font(this._JLcolor.getText(), Font.PLAIN, 12));
        this._panelTop.add(this._JLcolor, constraint);

        constraint.gridy += 1;
        this._JLtimer.setFont(new Font(this._JLtimer.getText(), Font.PLAIN, 12));
        this._panelTop.add(this._JLtimer, constraint);

        constraint.gridy += 1;
        constraint.anchor = GridBagConstraints.CENTER;
        constraint.insets = new Insets(30, 0, 0, 0);
        this._JLtimeBeforeDecline.setFont(new Font(this._JLtimeBeforeDecline.getText(), Font.BOLD, 16));
        this._panelTop.add(this._JLtimeBeforeDecline, constraint);

        //Timer
        ActionListener taskPerformer = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent evt)
            {
                _remainingTime--;
                if (_remainingTime < 0)
                {
                    //Give 2 more seconds to resolve network problem
                    if (_remainingTime == -2)
                    {
                        timer.stop();
                        //if time is out :  enable the parent frame and close the popup without doing nothing
                        if (_parent != null)
                        {
                            _parent.getParentFrame().setEnabled(true);
                        }
                        if (_parent2 != null)
                        {
                            _parent2.setEnabled(true);
                        }
                        dispose();
                    }
                }
                else
                {
                    _JLtimeBeforeDecline.setText("Temps avant refus : environ " + String.valueOf(_remainingTime) + "s");
                }
            }
        };

        //Set a timer alarm every 1 second
        timer = new Timer(1000, taskPerformer);
        timer.start();
    }

    private void closingWindow()
    {
        // Window listener for the closing window event
        this.addWindowListener((WindowListener) new WindowAdapter()
        {
            // Callback for the right close event
            @Override
            public void windowClosing(WindowEvent e)
            {
                JOptionPane.showMessageDialog(null, "Erreur : Vous ne pouvez pas quitter la fenêtre !");
            }
        });
    }
}