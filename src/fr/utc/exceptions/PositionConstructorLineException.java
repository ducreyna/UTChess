/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.exceptions;

/**
 *
 * @author lo23a024
 */
public class PositionConstructorLineException extends Exception{
    private String _message;

    public PositionConstructorLineException(){
        _message = "The line attribute of Position must be between 1 and 8.";
    }

    public String getMessage()
    {
        return _message;
    }
}
