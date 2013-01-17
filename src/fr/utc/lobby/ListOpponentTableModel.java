/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lobby;

import fr.utc.network.profile.*;
import fr.utc.network.services.NetworkImplementation;
import javax.swing.table.AbstractTableModel;

/**
 * Model use to fill the ListOpponentPan table
 *
 * @author lo23a016
 */
public class ListOpponentTableModel extends AbstractTableModel
{
    String[] _entete = LobbyConstant.JLIST_HEADER;
    private ManagerListPlayer _playerList;

    public void setPlayerList(ManagerListPlayer _playerList)
    {
        this._playerList = _playerList;
    }

    public ListOpponentTableModel()
    {
        _playerList = NetworkImplementation.getListOfProfiles();

        //Tesing data

//        _playerList = new ManagerListPlayer();
//        Profile profile1 = new Profile("max", "max");
//        Profile profile2 = new Profile("tar", "tar");
//        Profile profile3 = new Profile("clem", "clem");
//        Profile profile4 = new Profile("clemclemclemclemclem", "clemclemclemclemclem");
//
//        IpProfile ipProfile1 = new IpProfile(profile1, "0.0.0.0", false);
//        IpProfile ipProfile4 = new IpProfile(profile4, "0.0.0.0", false);
//        IpProfile ipProfile2 = new IpProfile(profile2, "1.1.1.1", true);
//        IpProfile ipProfile3 = new IpProfile(profile3, "2.2.2.2", true);
//        _playerList.addPlayer(ipProfile1);
//        _playerList.addPlayer(ipProfile2);
//        _playerList.addPlayer(ipProfile3);
//        _playerList.addPlayer(ipProfile4);

    }

    /**
     * Return the number of columns
     *
     * @return
     */
    public int getColumnCount()
    {
        return _entete.length;
    }

    /**
     * Return the number of rows
     *
     * @return
     */
    public int getRowCount()
    {
        return _playerList.getPlayerList().size();
    }

    /**
     * Return the objetc at the rowIndex row and the columnIndex column
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        switch (columnIndex)
        {
            case 0:
                //Name
                return _playerList.getPlayerList().get(rowIndex).getProfile().getPseudo();

            case 1:
                //State
                boolean _state = _playerList.getPlayerList().get(rowIndex).getProfile().isInGame();
                if (_state)
                {
                    return "En jeu";
                }
                else
                {
                    return "En attente";
                }

            case 2:
                // Information
                String _tmp = "Victoires : ";
                _tmp = _tmp + String.valueOf(_playerList.getPlayerList().get(rowIndex).getProfile().getGamesWon());
                _tmp = _tmp + " // Défaites : " + String.valueOf(_playerList.getPlayerList().get(rowIndex).getProfile().getGamesLost());
                _tmp = _tmp + " // Ratio : " + String.format("%.2f", _playerList.getPlayerList().get(rowIndex).getProfile().getRatio());
                return _tmp;
            //                return " ";

            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Necessary to initialize the JTable
     *
     * @param columnIndex
     * @return
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
     * Getter Players List
     *
     * @return
     */
    public ManagerListPlayer getList()
    {
        return _playerList;

    }
}
