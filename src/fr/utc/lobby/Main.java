/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lobby;

import fr.utc.network.common.Constant;

/**
 * Main fonction, launch the UTChess appliaction
 *
 * @author lo23a015
 */
public class Main
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        UniqueInstance uniqueInstance = new UniqueInstance(Constant.DEFAULT_PORT_UNIQUE_INSTANCE);

        if (uniqueInstance.launch())
        {
            GameFrame window = new GameFrame();
            window.loadView(new LogView(window));
        }
        else
        {
            System.out.println("ERREUR : il y a déjà une instance lancée");
        }
    }
}
