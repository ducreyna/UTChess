/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.data;

import java.io.File;
import java.util.UUID;
import fr.utc.data.historique.Move;
import fr.utc.data.piece.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import fr.utc.data.piece.*;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import fr.utc.data.historique.*;
import fr.utc.network.services.NetworkImplementation;
import java.util.GregorianCalendar;
import fr.utc.data.Position;
import fr.utc.exceptions.PositionConstructorColumnException;
import fr.utc.exceptions.PositionConstructorLineException;
import fr.utc.lobby.IHMConnexion2Reseau;
import fr.utc.network.services.NetworkImplementation;
import java.text.DateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Groupe Data
 */
public class Game implements Serializable
{
    private static final long serialVersionUID = 1;
    /*
     * Game identification
     */
    private UUID _id;
    /*
     * Black player
     */
    protected Player _localPlayer;
    /*
     * white player
     */
    protected Player _distantPlayer;
    
    /*
     * Winner
     */
    public static enum winner { none ,blackP, whiteP, draw}
    protected winner _gameWinner ;
    
    /*
     * Step counter
     */
    protected int _counter;
    /*
     * Turn to play
     */
    protected boolean _turn;
    /*
     * Game Historic
     */
    protected GameHistoric _histo;
    /*
     *Game grid
     */
    protected Grid _grid;
    /*
     * if the game is over
     */
    protected boolean _completed;
    /*
     * if the game is over
     */
    protected Date _dateCreat;

    /*
     * List of piece in which a pawn can be converted
     */
    static protected ArrayList<String> _transformablePiece;
   
    private Game()
    {
    }

    /*
     * Constructor of class Game
     * @param Player1
     * @param Player2
     */
    public Game(Player player1, Player player2)
    {
        // if (!player1.getColor())                          //Vérification la couleur du joueur
        // {
        _localPlayer = player1;//par défaut _localPlayer est le joueur 1
        _distantPlayer = player2;// par défaut distantPlayer est le joureur 2
        _gameWinner=winner.none;
        //}
    /*    else{
         _blackPlayer=player1;
         _whitePlayer=player2;
         }*/
        _id = genererUUID();
        _counter = 0;
        _turn = Color.white;                              //Joueur blanc commence le jeu.
        _completed = false;

        
        _histo = new GameHistoric();
        _grid = new Grid();

        ArrayList<String> _transformablePiece = new ArrayList<String>();
        setDateCreat();
        //fillTransformablePiece();
    }

    private void fillTransformablePiece()
    {
        try
        {
            Position p = new Position(1, 1);
            Queen q = new Queen(p, true);
            Bishop b = new Bishop(p, true);
            Rook r = new Rook(p, true);
            Knight k = new Knight(p, true);
            _transformablePiece.add(q.toString());
            _transformablePiece.add(b.toString());
            _transformablePiece.add(r.toString());
            _transformablePiece.add(k.toString());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*
     * The function return the current player
     */
    private Player joueurCourant()
    {
        if (_turn==_localPlayer.getColor())
        {
            return _localPlayer;
        }
        else
        {
            return _distantPlayer;
        }
    }


    /*
     * The function verify if the move is valid for the commun rules.
     * @param displacement: The displacement the players wants to do.
     */
    protected boolean commonRules(Grid grid, Position oldPos, Position newPos, boolean colorOfPlayer)
    {
        Piece oldPiece;               //The checker on the old position
        Piece newPiece;               //The checker on the new position
        Grid gridAfterMove;          //The temporer grid after movement

        //initialisation
        oldPiece = grid.getAt(oldPos);
        newPiece = grid.getAt(newPos);

        if (oldPiece.getColor() != colorOfPlayer)    // if the check's color is not the actual player's color
        {
            return false;                                       //the player has no right to place the checker
        }
        if (newPos == oldPos)                                //No displacement
        {
            return false;
        }

        gridAfterMove = grid.clone();
        gridAfterMove.doMove(oldPos, newPos);                   //The Grid after deplacement

        if (casEchec(gridAfterMove, colorOfPlayer)!=null)                        //Check, after the displacement, if the actual player will not lose the game
        {
            return false;
        }

        if(newPiece!=null){
            if (oldPiece.getColor() == newPiece.getColor())       //if the piece on the destination cell is of the same color as the player
            {
                return false;                                   //Then he can not do the move
            }
        }
        //Else everything is ok
        return true;

    }

    /*
     * The private function verify if it is the case that the king will be eaten.
     * @param displacement: The grid.
     */
    protected LinkedList<Move> casEchec(Grid grid, boolean colorOfPlayer)
    {
        LinkedList<Move> displacements = null ;                       //The result
        LinkedList<Piece> adversaire;       //The List of piece of adversairy
        Position roi;                      //King's position
        int i;

        if (colorOfPlayer == Color.black)
        {                        //black player
            adversaire = grid.getWhitePieces();
            roi = grid.getBlackPieces().get(0).getPosition();
        }
        else                               //white player
        {
            adversaire = grid.getBlackPieces();
            roi = grid.getWhitePieces().get(0).getPosition();
        }
        
        for(Piece tempPiece : adversaire)
        {
            if(tempPiece.specificRules(roi, grid) && commonRules(grid, tempPiece.getPosition(), roi, !colorOfPlayer)){      //!colorOfPlayer car on teste le déplacement des pièces adverses
                if(displacements==null)
                    displacements = new LinkedList<Move>();
                displacements.add(new Move(null,null,tempPiece.getPosition(), roi));
               
            }

        }

        return displacements;
    }

    public Player getPlayerLocal()
    {
        return _localPlayer;
    }

    ;
 
  public Player getPlayerDistant()
    {
        return _distantPlayer;
    }
  
  
  public String getWinner()
  {
      return _gameWinner.toString();
  }

    ;




  /*
     * The function realize the movement on the board.
     * @param displacement: The deplacement.
     */

 public Move doMove(Position oldPos, Position newPos) throws PositionConstructorColumnException
    {

        Piece oldPiece;
        Grid board;
        Piece roi;
        Piece rook;
        boolean echecEtMat = false;

        board = _grid.clone();
        oldPiece = _grid.getAt(oldPos);
        roi = this._grid.getAt(oldPos);
        rook = this._grid.getAt(oldPos);

        Move result = null;
        
        if(oldPiece == null)
            return result;
        
        if (oldPiece.specificRules(newPos, _grid) && this.commonRules(_grid, oldPos, newPos, _turn))
        {
            _grid.doMove(oldPos, newPos);
            
            Date d = new Date();
            result = new Move(d, _localPlayer, oldPos, newPos);
            echecEtMat = this.completed(_grid);//test if player a player won
            if(echecEtMat)
                setWinner(_turn);
            System.out.println(echecEtMat);
            _turn = !_turn;
            return result;
        }
        else if (roque(oldPos, newPos))
        {
            if (rook.getPosition().getColumnNumber() == 8)      //Small rock
            {
                try
                {
                    Position newPositionKing = new Position(oldPos.getColumnNumber() - 2, oldPos.getLine());
                    this._grid.doMove(oldPos, newPositionKing);                   //We move the King
                    this._grid.doMove(newPos, new Position(newPositionKing.getColumnNumber() + 1, newPositionKing.getLine())); //we move the Rook
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
             }
            else                                                //Big rock
            {
             try
                {
                    Position newPositionKing = new Position(oldPos.getColumnNumber() + 2, oldPos.getLine());
                    this._grid.doMove(oldPos, newPositionKing);                   //We move the King
                    this._grid.doMove(newPos, new Position(newPositionKing.getColumnNumber() - 1, newPositionKing.getLine())); //we move the Rook
                } catch (Exception e)
                {
                    e.printStackTrace();
                }   
            }

        //add move and date in historic action
            Date d = new Date();
            result = new Move(d, _localPlayer, oldPos, newPos);
            _histo.pushBack(result);
            //changer de joueur
            _turn=!_turn;
            echecEtMat = this.completed(_grid);//test if player a player won
            if(echecEtMat)
                setWinner(_turn);
            return result;
        }
        else
        {
            return result;
        }

    }
 
 /**
     * This function realizes a move with the transformation of a pawn.
     *
     * @param oldPos The old position of the pawn.
     * @param newPos The new position of the pawn.
     * @param newPieceName The name of the piece in which the pawn will be
     * converted.
     */
    public Transformation doMoveWithTransformation(Position oldPos, Position newPos, String newPieceName) throws PositionConstructorColumnException
    {
        Transformation result = null;
        Move tempMove = null;
        
        if(_grid.getAt(oldPos) != null && _grid.getAt(oldPos) instanceof Pawn && (tempMove = doMove(oldPos,newPos))!=null ){         
            Piece newPiece = createPieceFromString(newPieceName, newPos, !_turn);
            _grid.setAt(newPiece, newPos);
           //add historic
            result = new Transformation(tempMove);
            result.setNewPieceName(newPieceName);
            _histo.pushBack(result);
            boolean echecEtMat = this.completed(_grid);//test if player a player won
            if(echecEtMat)
                setWinner(_turn);
        }
        
        return result;
    }

    protected boolean roque(Position oldPos, Position newPos)
    {
        Piece roi;
        Piece rook;
        Grid gridAfterMove;

        roi = this._grid.getAt(oldPos);
        rook = this._grid.getAt(newPos);

        if (roi == null || rook == null)
        {
            return false;
        }

        if (!(roi instanceof King))        //si c'est pas le roi sur l'ancienne position
        {
            return false;                  //On ne peut pas faire le roque
        }
        if (!(rook instanceof Rook))       //si c'est pas le rock sur la nouvelle position
        {
            return false;                  //On ne peut pas faire le roque
        }
        if (((King) roi).getSpecificMoved())//si le roi est déjà déplacé
        {
            return false;                   //on ne peut pas faire le roque
        }
        if (((Rook) rook).getSpecificMoved())//si le roque est déjà déplacé
        {
            return false;                    //On ne peut pas faire le roque
        }
        if (!(rook.getPosition().getColumnNumber() == 8 || rook.getPosition().getColumnNumber() == 1))
        {
            return false;
        }

        gridAfterMove = _grid.clone();
        if (rook.getPosition().getColumnNumber() == 8)
        {
            try
            {
                Position newPositionKing = new Position(oldPos.getColumnNumber() + 2, oldPos.getLine());
                gridAfterMove.doMove(oldPos, newPositionKing);                   //We move the King
                gridAfterMove.doMove(newPos, new Position(newPositionKing.getColumnNumber() - 1, newPositionKing.getLine())); //we move the Rook
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                Position newPositionKing = new Position(oldPos.getColumnNumber() - 2, oldPos.getLine());
                gridAfterMove.doMove(oldPos, newPositionKing);                   //We move the King
                gridAfterMove.doMove(newPos, new Position(newPositionKing.getColumnNumber() + 1, newPositionKing.getLine())); //we move the Rook
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        if (casEchec(gridAfterMove, _turn)!=null)                        //Check, after the displacement, if the actual player will not lose the game
        {
            return false;
        }

        return true;
    }

    /*
     This method saves a game and adds it in the list of Completed Games of the two players if the game is completed and  adds it in the list of non
     * completed Games if it is not.
  
     */
    public void save()
    {
        if (this._completed)
        {
            this._localPlayer._player._gamesCompleted.add(this);
            //this._distantPlayer._player._gamesCompleted.add(this);
        }
        else
        {
            this._localPlayer._player._gamesSaved.add(this);
            //this._distantPlayer._player._gamesSaved.add(this);
        }
    }

    public void Serializable()
    {
        String curDir = System.getProperty("user.dir"); //Get current directory path
        HashtableProfile hash = new HashtableProfile();
        hash = fr.utc.data.HashtableProfile.readHashtable();
        String num = hash.getHashtable(this._localPlayer._player._pseudo + "," + this._localPlayer._player._pwd); //get the repertory number/name
        //java.util.GregorianCalendar calendar = new GregorianCalendar();
        //java.util.Date time  = calendar.getTime();
        //System.out.println("time" + time); 
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);// It transforms Date in the format " Mardi 01 Janvier 2012 10h 00 CEST" 
        String today_date = df.format(this._dateCreat);
        if (this._completed)
        {
            File f = new File(curDir + "\\" + num + "\\GamesCompleted");
            if (!f.exists())
            {//Repertory doesn't exist ==> no game has been saved
                f.mkdir();
            }
            // Check if the game is completed and add it to the list of completedGames)
            try
            {
                //game serialization

                File newGameRepertory = new File(curDir + "\\" + num + "\\GamesCompleted\\" + this._distantPlayer._player._pseudo + "_" + today_date);
                newGameRepertory.mkdir();
                FileOutputStream fichier = new FileOutputStream(curDir + "\\" + num + "\\GamesCompleted\\" + this._distantPlayer._player._pseudo + "_" + today_date + "\\game.ser");
                this._distantPlayer._player.saveProfileIn(curDir + "\\" + num + "\\GamesCompleted\\" + this._distantPlayer._player._pseudo + "_" + today_date);
                ObjectOutputStream oos = new ObjectOutputStream(fichier);
                oos.writeObject(this);
                oos.flush();
                oos.close();
            } catch (java.io.IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            File f = new File(curDir + "\\" + num + "\\GamesNonCompleted");
            if (!f.exists())
            {//Repertory doesn't exist ==> no game has been saved
                f.mkdir();
            }
            try
            {
                //game serialization
                File newGameRepertory = new File(curDir + "\\" + num + "\\GamesNonCompleted\\" + this._distantPlayer._player._pseudo + "_" + today_date);
                newGameRepertory.mkdir();
                FileOutputStream fichier = new FileOutputStream(curDir + "\\" + num + "\\GamesNonCompleted\\" + this._distantPlayer._player._pseudo + "_" + today_date + "\\game.ser");
                this._distantPlayer._player.saveProfileIn(curDir + "\\" + num + "\\GamesNonCompleted\\" + this._distantPlayer._player._pseudo + "_" + today_date );
                ObjectOutputStream oos = new ObjectOutputStream(fichier);
                oos.writeObject(this);
                oos.flush();
                oos.close();
            } catch (java.io.IOException e)
            {
                e.printStackTrace();

            }
        }


    }

  
    public static UUID genererUUID()
    {
        UUID id = UUID.randomUUID();
        return id;
    }


    /*
     * The function will verify if game is over
     * @param grid
     */
 protected boolean completed(Grid grid) throws PositionConstructorColumnException
 {
        boolean ans = true;
        Piece roi;
        
        LinkedList<Move> myMovesToMat;
        myMovesToMat = casEchec(grid,!_turn);   //je détecte si je met en déplacement mon adversaire
                                                                        //retourne tous mes déplacements qui mettent en échec le roi ennemi
        
        if(myMovesToMat != null){
            LinkedList<Piece> adversaryPieces;
            if(_turn == Color.black)                                
                adversaryPieces = grid.getWhitePieces(); 
            else
                adversaryPieces = grid.getBlackPieces();
                     
            for(Piece advPiece : adversaryPieces){
                for(char column = 'A'; column<='H';column++){
                    for(int line = 1; line<=8;line++){
                        try{
                            
                            Position arrival = new Position(column, line);
                            if(advPiece.getPosition().getColumnLetter() == 'G' && advPiece.getPosition().getLine() == 7 && arrival.getColumnLetter()=='G' && arrival.getLine()==6){
                                System.out.println("BLA");
                            }
                            if(advPiece.specificRules(arrival, grid) && commonRules(grid,advPiece.getPosition(), arrival , !_turn) ){         //if legal move
                                Grid gridCountered = grid.clone();
                                gridCountered.doMove(advPiece.getPosition(), arrival);
                                if(casEchec(gridCountered, !_turn) == null){
                                    return false;
                                }
                            }   
                        }
                        catch(Exception e){
                            System.out.println(e.getMessage());
                        }  
                    }
                }
            }
            
            
        }
        else
            return false;
       
        return ans;
    }


    /**
     * getter blackplayer
     *
     * @return the player who play black
     */
    public Player getBlackPlayer()
    {
        if (_distantPlayer.getColor() == Color.black)
        {
            return _distantPlayer;
        }
        else
        {
            return _localPlayer;
        }
    }

    /**
     * getter Completed
     *
     * @return a boolean, true if the game is completed
     */
    public boolean isCompleted()
    {
        return _completed;
    }

    /**
     * getter Counter
     *
     * @return the counter
     */
    public int getCounter()
    {
        return _counter;
    }

    /**
     * getter grid
     *
     * @return the grid
     */
    public Grid getGrid()
    {
        return _grid;
    }

    /**
     * getter GameHistoric
     *
     * @return the Historic of ther game
     */
    public GameHistoric getHisto()
    {
        return _histo;
    }

    /**
     * getter turn
     *
     * @return the turn
     */
    public boolean getTurn()
    {
        return _turn;
    }

    /**
     * getter whiteplayer
     *
     * @return the player who play white
     */
    public Player getWhitePlayer()
    {
        if (_distantPlayer.getColor() == Color.white)
        {
            return _distantPlayer;
        }
        else
        {
            return _localPlayer;
        }
    }

    /**
     * getter creation date of the game
     *
     * @return creation date of the game
     */
    public String getDateCreat()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        return sdf.format(_dateCreat);

    }

    /**
     * set creation date of the game set this time is now
     */
    public void setDateCreat()
    {
        _dateCreat = new Date(System.currentTimeMillis());
    }

    /**
     * set creation date of the game
     *
     * @param Date : creation date
     */
    public void setDateCreat(Date d)
    {
        _dateCreat = d;
    }

    /**
     * @return the _id
     */
    public UUID getId()
    {
        return _id;
    }
    
        /**
     * setId for the other player
     */
    public void setId(UUID id)
    {
        _id=id;
    }

    public String getNameGame()
    {
        return getWhitePlayer().getPlayer().getPseudo() + " VS " + getWhitePlayer().getPlayer().getPseudo() + "(" + getDateCreat() + ")";
    }

    /**
     * This function is called when the local player type something on the chat.
     * His message is created here, added to the historic and returned to be
     * sent via the network.
     *
     * @param msg The string the player entered.
     * @return The message created.
     */
    public Message createMessage(String msg)
    {
        Date now = new Date();
        Message m = new Message(now, _localPlayer, msg);
        _histo.pushBack(m);
        return m;

    }

    /**
     * This function must be called when the distant player has created a
     * message and we receive it via the network. Here we add the message to the
     * historic.
     *
     * @param msg The message received from the distant player.
     * @return The string included in the message that will be displayed.
     */
    public String receiveMessage(Message msg)
    {
        _histo.pushBack(msg);
        return msg.getMsg();
    }

    /**
     * This function adds a move to the local historic and send it over the
     * network.
     *
     * @param oldPos The old position of the piece.
     * @param newPos The new position of the piece.
     */
    protected void sendMove(Position oldPos, Position newPos)
    {
        Date now = new Date();
        Move m = new Move(now, _localPlayer, oldPos, newPos);   //We create a new move
        _histo.pushBack(m);                                     //We add it to the historic.
        
    }

    /**
     * This function is called when we receive a move from the distant player.
     * It realizes it locallly and add it to the historic.
     *
     * @param m The move the distant player did.
     */
    public void receiveMove(Move m) throws PositionConstructorColumnException
    {
        doMoveReceived(m);                      //Do the move
        _histo.pushBack(m);             //Add the move to the historic
    }

    /**
     * This function should only be called by receiveMove, e.g. it is called
     * when we receive a move from the distant player. It does not add the move
     * to the historic.
     *
     * @param displacement The move of the distant player.
     */
    private void doMoveReceived(Move displacement) throws PositionConstructorColumnException
    {
        Position oldPos = displacement.getOldPos();
        Position newPos = displacement.getNewPos();

        if (roque(oldPos, newPos))           //We check if it's a rock
        {   //There are 2 different rock.
            if (newPos.getColumnNumber() == 8)
            {
                try
                {
                    Position newPositionKing = new Position(oldPos.getColumnNumber() + 2, oldPos.getLine());
                    _grid.doMove(oldPos, newPositionKing);                   //We move the King
                    _grid.doMove(newPos, new Position(newPositionKing.getColumnNumber() - 1, newPositionKing.getLine())); //we move the Rook
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                try
                {
                    Position newPositionKing = new Position(oldPos.getColumnNumber() - 2, oldPos.getLine());
                    _grid.doMove(oldPos, newPositionKing);                   //We move the King
                    _grid.doMove(newPos, new Position(newPositionKing.getColumnNumber() + 1, newPositionKing.getLine())); //we move the Rook
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        else if (displacement instanceof Transformation) //We check if it's a displacement with a transformation
        {
            doMoveWithTransformation(oldPos, newPos, ((Transformation) displacement).getNewPieceName());
        }
        else                                            //Else it is a simple move
        {
            _grid.doMove(oldPos, newPos);
        }

        this._turn = !_turn;
    }

    /**
     * This function creates a new piece from a string containing the name of
     * the piece.
     *
     * @param pieceName Piece which has to be created.
     * @param pos Position of the piece.
     * @param color Color of the piece.
     * @return A new piece.
     */
    private Piece createPieceFromString(String pieceName, Position pos, boolean color)
    {
        if (pieceName.equals(Bishop.className()))
        {
            return new Bishop(pos, color);
        }
        else if (pieceName.equals(Knight.className()))
        {
            return new Knight(pos, color);
        }
        else if (pieceName.equals(Queen.className()))
        {
            return new Queen(pos, color);
        }
        else
        {
            return new Rook(pos, color, true);
        }
    }

    

    public void setGrid(Grid grid)
    {
        _grid = grid;
        _grid.populateListsPieces();
    }
    
    public void setTurn(boolean turn){
        _turn = turn;
    }

    public void swapPlayer(){
        Player temp =  _localPlayer;
        _localPlayer = _distantPlayer;
        _distantPlayer = temp ;
    }


    //call in 2 cases : time out (temps écoulé) /
        /**
     * This function set the atribut winner. It's called when the time of one
     * of the player is out or when the two players want to have a draw (matche nulle) game
     *
     * @param winner
     */
    public void setWinner(winner w){
        this._gameWinner=w;
        this._completed=true;
    }
    
    public void setWinner(boolean color){
        if(color == Color.black)
            _gameWinner = winner.blackP;
        else
            _gameWinner = winner.whiteP;
       
        _completed = true;
    }

    /**
     * This function verify if the game is a draw
     * @return boolean stand for if the game is a draw
     */
    public boolean draw(){

        //king versus king
        //because _grid._blackPieces.get(0) (_grid._whitePieces.get(0)) is the king
        if (this._grid._blackPieces.size()==1 && this._grid._whitePieces.size()==1)
            return true;


        //black king and black bishop versus white king
        if (this._grid._blackPieces.size()==2 && this._grid._blackPieces.get(1) instanceof Bishop
                && this._grid._whitePieces.size()==1)
            return true;

        //white king and white bishop versus black king
        if (this._grid._whitePieces.size()==2 && this._grid._whitePieces.get(1) instanceof Bishop
                && this._grid._blackPieces.size()==1)
            return true;

        //black king and black knight versus white king
        if (this._grid._blackPieces.size()==2 && this._grid._blackPieces.get(1) instanceof Knight
                && this._grid._whitePieces.size()==1)
            return true;

        //white king and white knight versus black king
        if (this._grid._whitePieces.size()==2 && this._grid._whitePieces.get(1) instanceof Knight
                && this._grid._blackPieces.size()==1)
            return true;

        //king and bishop versus king and bishop with the bishops on the same colour.
        if (this._grid._whitePieces.size()==2 && this._grid._whitePieces.get(1) instanceof Bishop
                && this._grid._blackPieces.size()==2 && this._grid._blackPieces.get(1) instanceof Bishop
                &&
                //if the two cases have the same colours
                //then
                //the line number mod 2 + column number mode 2 for the 1st case always equals
                //the line number mod 2 + column number mode 2 for the 2nd case
                //for exemple
                //for the white cases: the line number mod 2 + column number mode 2=1
                //for the black cases: the line number mod 2 + column number mode 2=0
                (this._grid._blackPieces.get(1).getPosition().getColumnNumber() % 2 +
                this._grid._blackPieces.get(1).getPosition().getLine() %2 ==
                this._grid._whitePieces.get(1).getPosition().getColumnNumber() % 2 +
                this._grid._whitePieces.get(1).getPosition().getLine() %2))
            return true;

        return false;
    }


    
    public void surrender(){
        this._completed = true;
        
        if(_localPlayer.getColor() == Color.black)
            this._gameWinner = winner.whiteP;
        else
            this._gameWinner = winner.blackP;
    }
    
    public void receiveSurrender(){
        this._completed = true;
        
        if(_localPlayer.getColor() == Color.black )
            this._gameWinner = winner.blackP;
        else
            this._gameWinner = winner.whiteP;
    }

}
