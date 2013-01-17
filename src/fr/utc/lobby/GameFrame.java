/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lobby;

import fr.utc.data.*;
import fr.utc.network.services.NetworkImplementation;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.net.URLClassLoader;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author lo23a014
 */
public class GameFrame extends JFrame implements IHMConnexion2IHMGrille
{
    private JPanel _mainContainer;
    private Profile _userProfile;
    private Class _lastView;
    private Game _lastGameRequested;
    private NetworkImplementation _netInterface;

    /**
     * Default constructor
     */
    public GameFrame()
    {
        this.setTitle("UTChess");
        this.setResizable(false);
        //LobbyConstant.Resize(32, 32, getPath() /*+ LobbyConstant.getPathLogo()*/).getImage()
        this.setIconImage(new ImageIcon(LobbyConstant.chargeFichier(LobbyConstant.getPathLogo())).getImage());
        // Settings when the window is closing
        closingWindow();

        this._mainContainer = new JPanel();
        this.setContentPane(this._mainContainer);

    }

    /**
     * Private method to set the default closing of the window
     */
    private void closingWindow()
    {
        // Skipping the default close
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // Window listener for the closing window event
        this.addWindowListener(new WindowAdapter()
        {
            // Callback for the right close event
            @Override
            public void windowClosing(WindowEvent e)
            {
                // Asking if we must quit the app
                int confirmation = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de vouloir quitter UTChess?",
                        "Information", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirmation == JOptionPane.OK_OPTION)
                {
                    // Informer le réseau de l'arrêt de l'appli
                    NetworkImplementation.sendLogout();
                    System.exit(0);
                }
                //NetworkImplementation.sendLogout();
                //System.exit(0); // A supprimer quand on décommente au-dessus ;)
            }
        });
    }

    /**
     * Method loading a view in the main frame
     *
     * @param v The View
     */
    public void loadView(View v)
    {
        this.setVisible(false);

        // To figure out on which view we were before
        _lastView = this._mainContainer.getClass();
        this.setSize(v.getSizeViewWidth(), v.getSizeViewHeight());
        // this._mainContainer.removeAll();
        this._mainContainer = v;
        this.setContentPane(this._mainContainer);
        // Center the window
        this.setLocationRelativeTo(this.getParent());

        this.setVisible(true);
    }

    /**
     * @return the last View used
     */
    public Class getLastView()
    {
        return this._lastView;
    }

    /**
     * Set the _lastView parameters with the view v
     *
     * @param v
     *
     */
    public void setLastView(Class v)
    {
        this._lastView = v;
    }

    /**
     * @return the _userProfile
     */
    public Profile getUserProfile()
    {
        return _userProfile;
    }

    /**
     * @param userProfile the _userProfile to set
     */
    public void setUserProfile(Profile userProfile)
    {
        this._userProfile = userProfile;
    }

    /**
     * @param _netInterface the networkInterface to set
     */
    public void setNetInterface(NetworkImplementation _netInterface)
    {
        this._netInterface = _netInterface;
    }

    /**
     * @return the _netInterface
     */
    public NetworkImplementation getNetInterface()
    {
        return _netInterface;
    }

    /**
     * Set the last Game instence used
     *
     * @param _lastGameRequested
     */
    public void setLastGameRequested(Game _lastGameRequested)
    {
        this._lastGameRequested = _lastGameRequested;
    }

    /**
     *
     * @return the Game instence saved
     */
    public Game getLastGameRequested()
    {
        return _lastGameRequested;
    }
  
    
}
