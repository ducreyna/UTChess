
package fr.utc.data;
import fr.utc.exceptions.*;


/**
 *
 * @author group DATA
 */
/*
 * The class Position represents a position on the board. 
 * Its first attribute is the position on the width of the board. It can be a letter between A and H or a number between 1 and 8.
 * Its second attribute is the position on the height of the board. Its value must be between 1 and 8.
 */

public class Position implements java.io.Serializable
{
    /**
     * Represents the position on the width of the board.
     */
    protected char _column;  
    /**
     * Represents the position on the height of the board.
     */
    protected int _line;
    
    /**
     * Constructor of Position
     * @param columnLetter
     *          Represents the position on the width of the board. Must be between A and H.
     * @param line
     *          Represents the position on the height of the board. Must be between 1 and 8.
     * @throws PositionConstructorLetterException if the letter is not between A and H.
     * @throws PositionConstructorLineException if the number is not between 1 and 8.
     */
    public Position(char columnLetter, int line) 
            throws PositionConstructorColumnException, PositionConstructorLineException
    {
        if(columnLetter<'A' || columnLetter>'H')
            throw new PositionConstructorColumnException();
        else
            _column = columnLetter;

        if(line<1 || line>8)
            throw new PositionConstructorLineException();
        else
            _line = line;
    }

    /*
     * Contructor of Position
     * @param indexWidth
     *          Represents the position on the width of the board. Must be between 1 and 8.
     * @param indexHeight
     *          Represents the position on the height of the board. Must be between 1 and 8.8.
     * @throws PositionConstructorLineException if the number is not between 1 and 8.
     */
    public Position(int indexWidth, int indexHeight)
            throws PositionConstructorLineException, PositionConstructorLineException
    {
        if(indexWidth<1 || indexWidth>8)
            throw new PositionConstructorLineException();
        else
            _column = (char)(indexWidth+ (int)'A'-1);

        if(indexHeight<1 || indexHeight>8)
            throw new PositionConstructorLineException();
        else
            _line = indexHeight;

    }

    /**
     * Empty constructor of Position. Initialized at A1.
     */
    public Position()
    {
        _column = 'A';
        _line = 1;
    }

    /**
     * This constructor creates a copy of a Position.
     * @param clone The position to be cloned.
     */
    public Position(Position clone)
    {
        _column = clone._column;
        _line = clone._line;
    }
    /**
     * Return the column of this position as a letter.
     * @return the column of this position as a letter.
     */
    public char getColumnLetter(){
        return _column;
    };
    
    /**
     * Return the column of this position as a number.
     * @return the column of this position as a number
     */
    public int getColumnNumber(){
        return ((int)_column-(int)'A' +1);  //We dont want to get a nul column number. For the column A we want the number 1, for column B we want 2,etc.
    }

    /**
     * Return the line of this position as a number.
     * @return return the line of this position as a number.
     */
    public int getLine(){
        return _line; 
    };

    /**
     * Allows to modify the column of this position.
     * @param column the new column of the position. Must be between 1 and 8.
     * @throws PositionConstructorColumnException if not between 1 and 8.
     */
    public void setColumn(int column) throws PositionConstructorColumnException{
        if(column < 1 || column>8)                  
            throw new PositionConstructorColumnException();
        else
            _column = (char)(column+(int)'A'-1);    //For number 1 we want column A, for number 2 we want column B, etc.
    };
    
    /**
     * Allows to modify te column of this position.
     * @param column the new column of this position. Must be between A and H.
     * @throws PositionConstructorColumnException if not between A and H.
     */
    public void setColumn(char column) throws PositionConstructorColumnException{
        if(column<'A' || column>'H')
            throw new PositionConstructorColumnException();
        else
            _column = column;
    }
    
    /**
     * Allows to modify the line of this position
     * @param line new line of the position. Must be between 1 and 8.
     * @throws PositionConstructorLineException if not between 1 and 8.
     */
    public void setLine(int line) throws PositionConstructorLineException{
        if(line < 1 || line>8)
            throw new PositionConstructorLineException();
        else
            _line = line;
    };
    
    /**
     * Check if two Position are equals. Firstly by verifying if they are the same reference.
     * Secondly, if they are not the same reference, it checks attributes' value.
     * @param obj The second object to check with this. 
     * @return true if same reference or same attributes' value. Else it returns false.
     */
    @Override
    public boolean equals(Object obj) {
        // Check if references are the same
        if (obj==this) {
            return true;
        }
        
        // Check if class of Position
        if (obj instanceof Position) {
            // In case of heritage
            Position other = (Position) obj;
            
           
            if (this._column != other._column) {
                return false;           // columns are different
            }
            
            if(this._line != other._line){
                return false;           //lines are different
            }
            
            
            // Then objects are equal
            return true;
        }
        
        return false;
    }
}
