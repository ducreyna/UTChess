/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.data;

/**
 *
 * @author cnzengan
 */


public class Player implements java.io.Serializable
{

   protected Profile _player;
   protected boolean _color;
   protected int _timer;

     /*
     * Constructeur de Player
     * @param Player
     *          Désigne  un joueur défini par tous ses paramètres: Nom, prénom,Age
     * @param color
     *          Désigne la couleur du joueur (noire , blache)
     * @param timer
     *          Désigne le temps qu'il reste à jouer à un joueur
     */

   public Player (Profile player, boolean color, int timer)
   {
         _color = color;
         _timer = timer;
         _player = player;

   }

   //Getter

      public Profile  getPlayer(){return _player;}
      public boolean   getColor(){return _color;}
      public int   getTimer(){return _timer;}

     //Setter

       public void     setPlayer(Profile player1){_player = player1;}
       public void     setColor(boolean color){_color = color;}
       public void     setTimer(int timer){_timer = timer;}





}

           



