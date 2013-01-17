/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.exceptions;

/**
 *
 * @author lo23a024
 */
public class PositionConstructorColumnException extends Exception{
    private String message;

     public PositionConstructorColumnException(){
        message = "The column attribute of Position must be between A and H or 1 and 8.";
    }

    @Override
     public String getMessage(){
         return message;
     }

}
