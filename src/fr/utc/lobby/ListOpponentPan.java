/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
// On récupère une ArrayList() avec les profils connectés
package fr.utc.lobby;

import fr.utc.network.profile.*;
import fr.utc.network.services.NetworkImplementation;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

/**
 * Panel use to show the list of connected players
 *
 * @author lo23a014
 */
public class ListOpponentPan extends JPanel implements Observer
{
    // For initializing the JTable
    Object[][] _data =
    {
        {
            "", "", ""
        },
    };
    JTable _opponentsList;
    ListOpponentTableModel _model;
    String[] _title = LobbyConstant.JLIST_HEADER;
    private View _parentView;
    String _selectedIp;

    /**
     * Default constructor
     */
    public ListOpponentPan(View parent)
    {
        _parentView = parent;
        _model = new ListOpponentTableModel();
        initComponents();


    }

    /**
     * Components initialization and positioning in the panel
     *
     */
    private void initComponents()
    {
        this.setLayout(new BorderLayout());

        _opponentsList = new JTable(_data, _title);
        _opponentsList.setAutoCreateColumnsFromModel(false);
        _model.setPlayerList(NetworkImplementation.getListOfProfiles());
        _opponentsList.setModel(_model);
        _opponentsList.setColumnSelectionAllowed(false);
        _opponentsList.setDragEnabled(false);
        _opponentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _opponentsList.addMouseListener(new TableMouseListener());
        _opponentsList.setDragEnabled(false);
        _opponentsList.getTableHeader().setReorderingAllowed(false);
        _opponentsList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        _opponentsList.getColumnModel().getColumn(0).setPreferredWidth(LobbyConstant.LIST_OPP_TAB_COL_PSEUDO);
        _opponentsList.getColumnModel().getColumn(1).setPreferredWidth(LobbyConstant.LIST_OPP_TAB_COL_STATE);
        _opponentsList.getColumnModel().getColumn(2).setPreferredWidth((int) (LobbyConstant.LIST_OPP_PAN__PROFILE_OPP_PAN_WX * LobbyConstant.MAIN_VIEW_SIZE_W) - LobbyConstant.LIST_OPP_TAB_COL_PSEUDO - LobbyConstant.LIST_OPP_TAB_COL_STATE);
        _opponentsList.getTableHeader().setResizingAllowed(false);
        _opponentsList.getTableHeader().setFont(new Font(null, Font.BOLD, 12));
        _opponentsList.setRowSelectionAllowed(true);
        this.add(_opponentsList, BorderLayout.CENTER);
        // this.add(_opponentsList);
        this.setVisible(true);
        this.add(new JScrollPane(_opponentsList), BorderLayout.CENTER);


    }

    /**
     * Called when the list of connected players changes Keep the same selected
     * row and update the JTable
     *
     * @param o
     * @param arg
     */
    public void update(Observable o, Object arg)
    {


        int _selectedRow = -1;
        ArrayList<IpProfile> pl = _model.getList().getPlayerList();
        // Update the JTable
        this.removeAll();
        initComponents();

        // Keep the selected line when updating
//        String ip = "0";
//        for (int i = 0; i < pl.size(); i++)
//        {
//                if (_selectedIp.equals(pl.get(i).getIp()))
//                {
//                    _selectedRow = i;
//                    System.out.println("ligne selectionne: " + _selectedRow);
//                    break;
//                }
//        }

        /*       int _selectedRow = -1;
         // Keep the same selected row in the JTable 
         if (_opponentsList.getSelectedRow() != -1)
         {
         ip = (String) _opponentsList.getValueAt(_opponentsList.getSelectedRow(), 3);
         System.out.println("ip selectionne: " +ip);
         for (int i = 0; i < _model.getList().getPlayerList().size(); i++)
         {
         if (ip.equals(_opponentsList.getValueAt(i, 3).toString()))
         {
         _selectedRow = i;
         System.out.println("ligne selectionne: " + _selectedRow);
         break;
         }
         }
         }*/



        // Refresh it
        boolean view = ((MainView) this._parentView).isHistoricalView();
        ((MainView) this._parentView).setHistoricalView(view);

        // Make the row selected
        if (_selectedRow != -1)
        {
            _opponentsList.getSelectionModel().setSelectionInterval(_selectedRow, _selectedRow);
        }
    }

    private class TableMouseListener implements MouseListener
    {
        public void mouseClicked(MouseEvent me)
        {
        }

        public void mousePressed(MouseEvent me)
        {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Send information to the ProfileOpponentPan
         *
         * @param me
         */
        public void mouseReleased(MouseEvent me)
        {
            _selectedIp = _model.getList().getPlayerList().get(_opponentsList.getSelectedRow()).getIp();
            System.out.println("pseudo " + _opponentsList.getValueAt(_opponentsList.getSelectedRow(), 0) + " " + _selectedIp);
            ((MainView) _parentView).setOppDetail(_model.getList().getPlayerList().get(_opponentsList.getSelectedRow()).getProfile());
        }

        public void mouseEntered(MouseEvent me)
        {
            //   throw new UnsupportedOperationException("Not supported yet.");
        }

        public void mouseExited(MouseEvent me)
        {
            //  throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
