/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.grid.board;
import fr.utc.data.Color;
import fr.utc.data.Position;
import fr.utc.data.historique.Move;
import fr.utc.data.piece.Pawn;
import fr.utc.data.piece.Piece;
import fr.utc.exceptions.PositionConstructorColumnException;
import fr.utc.exceptions.PositionConstructorLineException;
import fr.utc.lobby.LobbyConstant;
import fr.utc.network.exceptions.ErrorSendingMoveException;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.TransferHandler;

/**
 * BoardCell is a cell of the board where the players can move their pawn. It implements a TableCellRenderer in order to be receive as a component from the JTable BoardController.
 * @author lo23a017
 */
public class BoardCell extends JButton implements Transferable,DragSourceListener, DragGestureListener,DropTargetListener, Serializable {
    /*
     * The boardController of the BoardCell
     */
    private BoardController _boardController;
    /*
     * The position of the BoardCell
     */
    private Position _position;
    /*
     * The view of this BoardCell
     */
    private PieceView _pieceView;
    /*
     * This is use to the drag 
     */
    private DragSource _dragSource;
    /*
     * This is the objet transfered by the drag
     */
    private TransferHandler _transfertHandler;
    /*
     * This is use to the drop
     */
    private DropTarget _target;
    /*
     * The cursor for the drag of the boardCell
     */
    private Cursor _dragCursor;


/*
 * Constructor
 * @param Position the cell position
 * @param BoardController 
 */
    public BoardCell(Position p, BoardController bc) {
        this._position = p;
        this._boardController = bc;
        this._pieceView = new PieceView();

        this.setIcon(null);
        this.setFocusPainted(false);
        this.setOpaque(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        
       /* AbstractAction action = new AbstractAction("test")
        {
            public void actionPerformed(ActionEvent evt)
            {
                // Perform action
                System.out.println("plop");
            }
        };
        
        this.addActionListener(action);*/
        
         //The TransferHandler returns a new DnDButton
        //to be transferred in the Drag
        _transfertHandler = new TransferHandler(){
              public Transferable createTransferable(BoardCell bc){
                return bc;
              }
        };

        setTransferHandler(_transfertHandler);
        
        //The Drag will copy the DnDButton rather than moving it
        _dragSource = new DragSource();
        _dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);
        _target = new DropTarget(this,this);
        
    }

    /*
     * Update the view of a empty BoardCell
     */
    public void UpdateCell()
    {
        this._pieceView.deletePawn();
        this.setIcon(null);
        
      }
    /*
     * Update the view of BoardCell whith his piece view
     */
    public void UpdateCell(Piece p)
    {
            
        this._pieceView.setPawn(p);
        this.setIcon(new ImageIcon(LobbyConstant.chargeFichier(this._pieceView.imageForPiece())));
        this.setVisible(true);
    }
    
    /**
     * @return the gridController
     */
    public BoardController getBoardController() {
        return _boardController;
    }

    /**
     * @param gridController the gridController to set
     */
    public void setBoardController(BoardController boardController) {
        this._boardController = boardController;
    }

    /**
     * @return the position
     */
    public Position getPosition() {
        return _position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Position position) {
        this._position = position;
    }

    /**
     * @return the pieceView
     */
    public PieceView getPieceView() {
        return _pieceView;
    }

    /**
     * @param pieceView the pieceView to set
     */
    public void setPieceView(PieceView pieceView) {
        this._pieceView = pieceView;
    }

    
    public DataFlavor[] getTransferDataFlavors()
    {
       // throw new UnsupportedOperationException("Not supported yet.");
        return new DataFlavor[]{new DataFlavor(Position.class, "Position")};
    }

    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        return true;
    }

    public Object getTransferData(DataFlavor flavor)throws UnsupportedFlavorException, IOException
    {
       // throw new UnsupportedOperationException("Not supported yet.");
        return this.getPosition();
    }

    public void dragEnter(DragSourceDragEvent dsde)
    {
        dsde.getDragSourceContext().setCursor(this._dragCursor);
    }

    public void dragOver(DragSourceDragEvent dsde)
    {
       // System.out.println("dragOver");
    }

    public void dropActionChanged(DragSourceDragEvent dsde)
    {
    }

    public void dragExit(DragSourceEvent dse)
    {
       dse.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
    }

    public void dragDropEnd(DragSourceDropEvent dsde)
    {
        this.setVisible(true);
        
    }

    public void dragGestureRecognized(DragGestureEvent dge)
    {
        if(this.isDragable())
        {
            this.generateDragCursor();
            _dragSource.startDrag(dge,this._dragCursor,this,this); 
            this.setVisible(false);
        }
    }

    public void dragEnter(DropTargetDragEvent dtde) {}

    public void dragOver(DropTargetDragEvent dtde) {}

    public void dropActionChanged(DropTargetDragEvent dtde) {}

    public void dragExit(DropTargetEvent dte) {}

    public void drop(DropTargetDropEvent dtde)
    {
            Transferable t = dtde.getTransferable();
            DataFlavor[] d = t.getTransferDataFlavors();
            
        try
        {
           Position tp = (Position)t.getTransferData(d[0]);
           System.out.println("couleur "+_boardController.getGrid().getAt(tp).getColor());
           System.out.println("position départ "+tp.getLine()+tp.getColumnLetter());
           System.out.println("position arrivée "+this.getPosition().getLine()+this.getPosition().getColumnLetter());
            // Voir avec Data si on envoie pas plutot un objet move
            //Move displacement = new Move(new Date(),this._boardController.getGridController().currentGame.getPlayerLocal(),tp,this.getPosition());
           
           if(this.isTransformable(tp))
           {
               //Voir pourquoi erreur java null pointer exception
               GridController gc = _boardController.getGridController();
               Position pos = this.getPosition();
               boolean col = GridController._currentGame.getPlayerLocal().getColor();
               gc.transformPawn(tp, pos, col);
                try
                {
                    _boardController.UpdateGrid();
                } catch (PositionConstructorLineException ex)
                {
                    Logger.getLogger(BoardCell.class.getName()).log(Level.SEVERE, null, ex);
                }
           }
           else
           {
                Move displacement = this._boardController.getGridController()._currentGame.doMove(tp,this.getPosition());
                System.out.println("Déplacement autorisé "+displacement);
                if(displacement!=null)
                {
                    try
                    {
                        this._boardController.UpdateGrid();
                       try
                       {
                          this._boardController.getGridController().setFreeze(true);
                           this._boardController.getGridController().getNetwork().sendMove(displacement);
                         
                       } catch (ErrorSendingMoveException ex)
                       {
                           Logger.getLogger(BoardCell.class.getName()).log(Level.SEVERE, null, ex);
                       }

                    } catch (PositionConstructorLineException ex)
                    {
                        Logger.getLogger(BoardCell.class.getName()).log(Level.SEVERE, null, ex);
                    }
               }
           }
           
        } catch (PositionConstructorColumnException ex)
        {
            Logger.getLogger(BoardCell.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedFlavorException ex)
        {
            Logger.getLogger(BoardCell.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(BoardCell.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    /*
     * This method tests if we can drag the piece
     * @Return boolean if we could drag the piece
     */
    public boolean isDragable()
    {
        System.out.println("tour de jouer : "+this._boardController.getGridController()._currentGame.getTurn());
        System.out.println("Joueur local : "+this._boardController.getGridController()._currentGame.getPlayerLocal().getColor() );
// rajouter test freeze
        if(!this._boardController.getGridController()._currentGame.isCompleted() && !this._boardController.getGridController().isFreeze() && this._pieceView.getPawn()!=null && this._pieceView.getPawn().getColor() == this._boardController.getGridController()._currentGame.getPlayerLocal().getColor()
                && (this._boardController.getGridController()._currentGame.getTurn()== this._boardController.getGridController()._currentGame.getPlayerLocal().getColor() ))
        {
            return true;
        }
        else
        {
            return false;
        //return true;
        }
    }
    /*
     * This method allows to generate the dragCursor of the piece
     */
    public void generateDragCursor()
    {
         Toolkit tk = Toolkit.getDefaultToolkit();
         ImageIcon imageIcon = new ImageIcon(LobbyConstant.chargeFichier(this._pieceView.imageForPiece(true)));
         Image image = imageIcon.getImage();
         Cursor cursor = tk.createCustomCursor(image, new Point(0,0), "img");
         _dragCursor=cursor;  
    }
    
    /*
     * This methode tests if we could transform the piece
     * @Param Position the position of the piece
     * @Return boolean if we can tansform the piece
     */
    public boolean isTransformable(Position p)
    {
        if( (this._boardController.getGrid().getAt(p) instanceof Pawn)
       && ((this.getPosition().getLine()==1 && this._boardController.getGridController()._currentGame.getPlayerLocal().getColor()== Color.black)
                || (this.getPosition().getLine()==8 && this._boardController.getGridController()._currentGame.getPlayerLocal().getColor()==Color.white)) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
