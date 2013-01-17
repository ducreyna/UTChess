/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
// TODO récupérer parent en mettant le panel en parametre + récupére monprofil.liste des parties + init la jtable avec ca :)
package fr.utc.lobby;

import fr.utc.data.Game;
import fr.utc.data.Player;
import fr.utc.data.Profile;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * Model use to fill the HistoricalList table
 *
 * @author lo23a016
 */
public class HistoricalListTableModel extends AbstractTableModel
{
    String[] _entete = LobbyConstant.HIST_LIST_HEADER;
    View _parentView;
    ArrayList<Game> _liste;

    /**
     * Default constructor
     *
     * @param parent View parent where the List panel is set
     */
    public HistoricalListTableModel(View parent)
    {

        _parentView = parent;
        _liste = _parentView.getParentFrame().getUserProfile().getGamesCompleted();
    }

    /**
     * Getter number of columns
     *
     * @return Return the number of columns
     */
    public int getColumnCount()
    {
        return _entete.length;
    }

    /**
     * Getter number of rows
     *
     * @return the number of rows
     */
    public int getRowCount()
    {
        return _liste.size();
    }

    /**
     * getter object at a certain cell
     *
     * @param rowIndex
     * @param columnIndex
     * @return the object at the rowIndex row and the columnIndex column
     */
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        switch (columnIndex)
        {
            case 0:
                //Name
                return _liste.get(rowIndex).getPlayerDistant().getPlayer().getPseudo();

            case 1:
                //State
                System.out.println(isWinner(_liste.get(rowIndex), _liste.get(rowIndex).getPlayerDistant()));
                return isWinner(_liste.get(rowIndex), _liste.get(rowIndex).getPlayerDistant());

            case 2:
                return " ";

            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Getter the Column Class Necessary to initialize the JTable
     *
     * @param columnIndex
     * @return the column class
     */
    public Class<?> getColumnClass(int columnIndex)
    {
        switch (columnIndex)
        {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Getter Games List
     *
     * @return the games list
     */
    public ArrayList<Game> getList()
    {
        return this._liste;

    }

    /**
     * Setter Games list
     *
     * @param games, array of games
     */
    public void setList(ArrayList<Game> games)
    {
        this._liste = games;

    }

    /**
     * return if the game is win or not for a certain player
     *
     * @param g, game about we want information
     * @param p, the player whose we wants to know if you won or not the game
     */
    private String isWinner(Game g, Player p)
    {
        String state = g.getWinner();
        boolean myColor = g.getPlayerLocal().getColor(); // Black = true / White = false
        if (state.equals("draw"))
        {
            return "Match nul";
        }
        else
        {
            if (state.equals("blackP"))
            {
                if (myColor)
                {
                    return "Victoire";
                }
                else
                {
                    return "Défaite";
                }
            }
            else
            { // Winner is White
                if (myColor)
                {
                    return "Défaite";
                }
                else
                {
                    return "Victoire";
                }
            }
        }

    }
}
