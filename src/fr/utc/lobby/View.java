/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lobby;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author lo23a014
 */
public class View extends JPanel
{
    public JFrame popup;
    private int _sizeViewHeight;
    private int _sizeViewWidth;
    private GameFrame _parent;

    /**
     * Parameterized constructor
     *
     * @param sizeViewHeight the height of the view
     * @param sizeViewWidth the width of the view
     * @param frame which contain this view
     */
    public View(int sizeViewHeight, int sizeViewWidth, GameFrame frame)
    {
        this._sizeViewHeight = sizeViewHeight;
        this._sizeViewWidth = sizeViewWidth;

        this._parent = frame;
    }

    public View()
    {
        this(LobbyConstant.DEFAULT_SIZE_H, LobbyConstant.DEFAULT_SIZE_W, null);
    }

    public View(int sizeViewHeight, int sizeViewWidth)
    {
        this(sizeViewHeight, sizeViewWidth, null);
    }

    public GameFrame getParentFrame()
    {
        return this._parent;
    }

    public void setParentFrame(GameFrame frame)
    {
        this._parent = frame;
    }

    /**
     * Getter of _sizeViewHeight
     *
     * @return int the height of the view
     */
    public int getSizeViewHeight()
    {
        return this._sizeViewHeight;
    }

    /**
     * Getter of _sizeViewWidth
     *
     * @return int the width of the view
     */
    public int getSizeViewWidth()
    {
        return this._sizeViewWidth;
    }
}
