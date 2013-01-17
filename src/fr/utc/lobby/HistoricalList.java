/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lobby;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

/**
 * Panel use to show the list of the games finished
 *
 * @author Seg_fault_
 */
public class HistoricalList extends JPanel
{
    private View _parentView;
    Object[][] _data =
    {
        {
            "", "", ""
        },
    };
    JTable _historicalList;
    HistoricalListTableModel _model;
    String[] _title = LobbyConstant.HIST_LIST_HEADER;

    /**
     * Default constructor
     *
     * @param parent View parent where the panel is set
     */
    public HistoricalList(View parent)
    {
        _model = new HistoricalListTableModel(parent);
        _parentView = parent;
        initComponents();
    }

    /**
     * Components initialization and positioning in the window
     */
    private void initComponents()
    {
        this.setLayout(new BorderLayout());
        //création de la table
        _historicalList = new JTable(_data, _title);
        //selection des proprietes de la table
        _historicalList.setAutoCreateColumnsFromModel(false);
        _historicalList.setModel(_model);
        _historicalList.setColumnSelectionAllowed(false);
        _historicalList.setDragEnabled(false);
        _historicalList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _historicalList.addMouseListener(new HistoricalList.TableMouseListener());
        _historicalList.setDragEnabled(false);
        _historicalList.getTableHeader().setReorderingAllowed(false);
        _historicalList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        _historicalList.getColumnModel().getColumn(0).setPreferredWidth(LobbyConstant.LIST_OPP_TAB_COL_PSEUDO);
        _historicalList.getColumnModel().getColumn(1).setPreferredWidth(LobbyConstant.LIST_OPP_TAB_COL_STATE);
        _historicalList.getColumnModel().getColumn(2).setPreferredWidth((int) (LobbyConstant.LIST_OPP_PAN__PROFILE_OPP_PAN_WX * LobbyConstant.MAIN_VIEW_SIZE_W) - LobbyConstant.LIST_OPP_TAB_COL_PSEUDO - LobbyConstant.LIST_OPP_TAB_COL_STATE);
        _historicalList.getTableHeader().setResizingAllowed(false);
        _historicalList.getTableHeader().setFont(new Font(null, Font.BOLD, 12));

        //ajout de la table dans le dans le panel
        this.add(_historicalList, BorderLayout.CENTER);
        this.setVisible(true);
        //ajout d'une Scroll bar
        this.add(new JScrollPane(_historicalList), BorderLayout.CENTER);
    }

    /**
     * Handler class for mouse Click actions
     */
    private class TableMouseListener implements MouseListener
    {
        /**
         * Handler method of the action performed on the table when mouse is
         * Clicked Required because the class implements the interface
         * ActionListener
         *
         * @param me event linking to the Action performed
         */
        public void mouseClicked(MouseEvent me)
        {
        }

        /**
         * Handler method of the action performed on the table when mouse is
         * pressed Required because the class implements the interface
         * ActionListener
         *
         * @param me event linking to the Action performed
         */
        public void mousePressed(MouseEvent me)
        {
        }

        /**
         * Handler method of the action performed on the table when mouse is
         * released Required because the class implements the interface
         * ActionListener
         *
         * @param me event linking to the Action performed
         */
        public void mouseReleased(MouseEvent me)
        {
            //envoyer la partie selectionné
            ((MainView) _parentView).setGameDetail(_model.getList().get(_historicalList.getSelectedRow()));
        }

        /**
         * Handler method of the action performed on the table when mouse is
         * Entered Required because the class implements the interface
         * ActionListener
         *
         * @param me event linking to the Action performed
         */
        public void mouseEntered(MouseEvent me)
        {
        }

        /**
         * Handler method of the action performed on the table when mouse is
         * Existed Required because the class implements the interface
         * ActionListener
         *
         * @param me event linking to the Action performed
         */
        public void mouseExited(MouseEvent me)
        {
        }
    }
}
