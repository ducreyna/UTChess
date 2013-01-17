/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lobby;

import java.awt.Image;
import java.net.URL;
import java.net.URLClassLoader;
import javax.swing.ImageIcon;

/**
 * Class of constants used in the Lobby package
 *
 * @author Louison
 */
public class LobbyConstant
{
    /**
     * Default size of windows
     */
    static final int DEFAULT_SIZE_H = 500;
    static final int DEFAULT_SIZE_W = 500;
    /**
     * Window size : LogView
     */
    static final int LOG_VIEW_SIZE_H = 600;
    static final int LOG_VIEW_SIZE_W = 800;
    /**
     * Window size : SetProfileView
     */
    static final int SET_PROFILE_VIEW_SIZE_H = 500;
    static final int SET_PROFILE_VIEW_SIZE_W = 800;
    /**
     * Window size : CreatGamePopUp
     */
    static final int CREATE_GAME_POPUP_SIZE_H = 300;
    static final int CREATE_GAME_POPUP_SIZE_W = 500;
    /**
     * Window size : MainView
     */
    static final int MAIN_VIEW_SIZE_H = 830;
    static final int MAIN_VIEW_SIZE_W = 1000;
    static final int PROFILEOPPONENT_PAN_AVATARSIZE = 100;
    /**
     * Window size : OpponentProfileView
     */
    static final int OPPONENT_PROFILE_VIEW_SIZE_H = 500;
    static final int OPPONENT_PROFILE_VIEW_SIZE_W = 500;
    static final int OPPONENT_PROFILE_VIEW_AVATARSIZE = 100;
    /**
     * Window size : ResponseRequestPopUp
     */
    static final int RESPONSE_REQUEST_POPUP_SIZE_H = 500;
    static final int RESPONSE_REQUEST_POPUP_SIZE_W = 500;
    /**
     * Window size : WaitingResponsePopUp
     */
    static final int WAITING_RESPONSE_POPUP_SIZE_H = 250;
    static final int WAITING_RESPONSE_POPUP_SIZE_W = 500;
    /**
     * percentage of horizontal size of the panel ProfilPlayerPan in the
     * MainView
     */
    static final float PROFILE_PLAY_PAN_WX = 0.3f;
    /**
     * percentage of vertical size of the panel ListOpponentPan in the MainView
     */
    static final float LIST_OPP_PAN_WY = 0.7f;
    /**
     * Size of the JTable of the Opponent List
     */
    static final int LIST_OPP_TAB_COL_PSEUDO = 140;
    static final int LIST_OPP_TAB_COL_STATE = 60;
    /**
     * percentage of vertical size of the panel ProfilOpponentPan in the
     * MainView
     */
    static final float PROFILE_OPP_PAN_WY = 1f - LIST_OPP_PAN_WY;
    /**
     * percentage of horizontal size of the panel ListOpponentPan and
     * ProfilOpponentPan in the MainView
     */
    static final float LIST_OPP_PAN__PROFILE_OPP_PAN_WX = 1f - PROFILE_PLAY_PAN_WX;
    /**
     * percentage of vertical size of the Top panel in the OpponentProfileView
     */
    static final float OPPONENTPROFILEVIEW_TOP_PAN_WY = 0.2f;
    /**
     * percentage of Horizontale size of the Left panel in the
     * OpponentProfileView
     */
    static final float OPPONENTPROFILEVIEW_LEFT_PAN_WY = 0.5f;
    /**
     * percentage of Horizontale size of the Left panel in the
     * OpponentProfileView
     */
    static final float PROFILEOPPONENTPAN_LEFT_PAN_WX = 0.4f;
    /**
     * percentage of Horizontale size of the Left panel in the
     * OpponentProfileView
     */
    static final float PROFILEOPPONENTPAN_MIDDLE_PAN_WX = 0.2f;
    /**
     * Default limit for the editing field
     */
    static final int DEFAULT_COLUMNS_FIELD = 16;
    /**
     * Path to the Title Picture
     */
    private static final String PATH_PICTURE_TITLE = "fr/utc/ressources/logo_transparent.png";
    /**
     * Path to the special Title Picture
     */
    private static final String PATH_PICTURE_TITLE_SURP = "fr/utc/ressources/logo_transparent_Surp.png";
    /**
     * Path to the special Title Picture
     */
    private static final String NAME_KONAMI_AVATAR = "zzzzhh.png";
    /**
     * Path to avatar pictures
     */
    static final String PATH_PICTURE_AVATAR = "fr/utc/ressources/avatar";
    /**
     * Header of the Opponent JList
     */
    static final String[] LIST_AVATAR =
    {
        "amy.png",
        "bender.png",
        "farnsworth.png",
        "fry.png",
        "happy.png",
        "hermes.png",
        "leela.png",
        "nibbler.png",
        "robien.png",
        "scruffy.png",
        "utchess.png",
        "zap.png",
        "zoidberg.png",
        "zzzzhh.png"
    };
    /**
     * Header of the Opponent JList
     */
    static final String[] JLIST_HEADER =
    {
        "Pseudo", "Etat", "Informations"
    };
    /**
     * Header of the Historical JList
     */
    static final String[] HIST_LIST_HEADER =
    {
        "Adversaire", "Victoire ?", "Informations"
    };
    /**
     * Konami Code is actived or not
     */
    private static Boolean _Konami_Actived = false;

    /**
     * exchange the value of the _Konami_Actived variable
     */
    static void toggleActivedKonami()
    {
        _Konami_Actived = !_Konami_Actived;
    }

    /**
     * say if a picture can be choose
     */
    static Boolean isAutorized(String name)
    {
        if (_Konami_Actived)
        {
            return true;
        }
        else
        {
            return !(name.equalsIgnoreCase(NAME_KONAMI_AVATAR));
        }
    }

    /**
     * getter, Path to the logo picture
     *
     * @return logo path
     */
    static String getPathLogo()
    {
        if (_Konami_Actived)
        {
            return PATH_PICTURE_TITLE_SURP;
        }
        else
        {
            return PATH_PICTURE_TITLE;
        }
    }
    /**
     * urlLoader
     *
     */
    private static URLClassLoader urlLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();

    /**
     * Private method returning the URL to access sources file
     *
     * @return URL of the file
     */
    public static URL chargeFichier(String fichier)
    {
        URL fileLoc = urlLoader.findResource(fichier);
        return fileLoc;
    }

    /**
     * Static method to resize an image stored in one of packages 'Ressources'
     *
     * @param w The new width
     * @param h The new height
     * @param path The absolute path of the image
     * @return ImageIcon An image resizing
     */
    static ImageIcon Resize(int w, int h, URL path)
    {
        Image tempo = new ImageIcon(path).getImage().getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(tempo);
    }
}
