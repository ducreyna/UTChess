/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.grid.board;

import fr.utc.data.Grid;
import fr.utc.data.Position;
import fr.utc.data.piece.Piece;
import fr.utc.exceptions.PositionConstructorLineException;
import fr.utc.lobby.LobbyConstant;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 * BoardController is the grid itself represented by a JPanel and rendering the differents cells of the game board
 * @author Cédric
 */
public class BoardController extends JPanel{
    
    // Objet grille liant l'IHM au modèle Data
    private Grid _grid;
    //
    private JPanel _GridTable;
    private GridController _gridController;
    private ArrayList<BoardCell> _cellsCollection;
    
    final int COMPONENT_WIDTH = 726;
    final int COMPONENT_HEIGHT = 728;
    
    public BoardController(GridController gc, Grid g) throws PositionConstructorLineException
    {
        _gridController = gc;
        this._grid = g;
        _cellsCollection = new ArrayList<BoardCell>(64);
        
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(COMPONENT_WIDTH, COMPONENT_HEIGHT));
        
        _GridTable = new JPanel();
        /*_GridTable = new JTable(8,8) {

         public Component prepareRenderer(BoardCell renderer, int row, int column) {
            Component c = super.prepareRenderer( renderer, row, column);
            // We want renderer component to be transparent
            // so background image is visible
            if( c instanceof JComponent )
             {
                 ((JComponent)c).setOpaque(false);
             }
               return c;
            }
        };
        */
        _GridTable.setOpaque(false);
        _GridTable.setBounds(58,45,640,640);
        GridLayout grille = new GridLayout(8,8,0,0);
        _GridTable.setLayout(grille);
        
        for (int lin = 1; lin < 9; lin++)
        {    
            for (int col = 1; col < 9; col++)
            {
                BoardCell bc = new BoardCell(new Position(col,lin),this);
                this.addCellsToCollection(bc);
                _GridTable.add(bc);
            }
        }
        
        JLabel background = new JLabel(new ImageIcon(LobbyConstant.chargeFichier("fr/utc/ressources/chessboard_bg.png")));
        background.setBounds(0, 0, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        
        

        layeredPane.add(_GridTable, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);
        
        this.UpdateGrid();
        
        this.add(layeredPane);
        
        //this._gridController = gc;
        //this._cellsCollection = new ArrayList<BoardCell>();  
    }

    public void UpdateGrid() throws PositionConstructorLineException
    {
        for (int lin = 1; lin < 9; lin++)
        {    
            for (int col = 1; col < 9; col++)
            {
                
                Piece p = this._grid.getAt(new Position(col, lin));
                if(p == null)
                {
                    this.getCellsCollection().get(((lin-1)*8+(col-1))).UpdateCell();
                }
                else
                {
                    this.getCellsCollection().get(((lin-1)*8+(col-1))).UpdateCell(p);
                }
            }
        }
        
         //Regarder si la partie est finie car gagnant
         if(!this.getGridController()._currentGame.getWinner().equals("none")&& !this.getGridController()._currentGame.isCompleted()) 
         {
               this.getGridController().endGame("Le joueur"+this.getGridController()._currentGame.getWinner()+"a gagné!!");
          }
   
    }
    
    public void changePiece(Piece piece,BoardCell bc)
    {
       
    }

    /**
     * @return the grid
     */
    public Grid getGrid() {
        return _grid;
    }

    /**
     * @param grid the grid to set
     */
    public void setGrid(Grid grid) {
        this._grid = grid;
    }

    /**
     * @return the gridController
     */
    public GridController getGridController() {
        return _gridController;
    }

    /**
     * @param gridController the gridController to set
     */
    public void setGridController(GridController gridController) {
        this._gridController = gridController;
    }

    /**
     * @return the cellsCollection
     */
    public ArrayList<BoardCell> getCellsCollection() {
        return _cellsCollection;
    }

    /**
     * @param cellsCollection the cellsCollection to set
     */
    public void setCellsCollection(ArrayList<BoardCell> cellsCollection) {
        this._cellsCollection = cellsCollection;
    }
    
    public void addCellsToCollection(BoardCell bc)
    {
        this.getCellsCollection().add(bc);
    }

    /**
     * @return the _GridTable
     */
    public JPanel getGridTable()
    {
        return _GridTable;
    }

    /**
     * @param GridTable the _GridTable to set
     */
    public void setGridTable(JPanel GridTable)
    {
        this._GridTable = GridTable;
    }
}
