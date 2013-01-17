/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.grid;

import fr.utc.lobby.LobbyConstant;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout;
import javax.swing.JLayeredPane;

/**
 * The class which constructs the pawn transformation popup
 * his pawn
 * @author lo23a019
 */
public class PawnTransformationPopup extends JPanel
{
    private JLabel _questionText;
    private final int POPUP_WIDTH = 300;
    private final int POPUP_HEIGHT = 160;
    private JButton _queen;

    public JButton getQueen()
    {
        return _queen;
    }
    private JButton _bishop;

    public JButton getBishop()
    {
        return _bishop;
    }

    public JButton getKnight()
    {
        return _knight;
    }

    public JButton getRook()
    {
        return _rook;
    }
    private JButton _rook;
    private JButton _knight;
    boolean _color;
    String _colorText;

    public PawnTransformationPopup(boolean color)
    {
        _color = color;
        _colorText = (_color) ? "black" : "white";
        JLayeredPane layeredPane = new JLayeredPane();
        _questionText = new JLabel("Par quelle pièce sera remplacé votre pion ?");
        _questionText.setForeground(Color.black);
        _questionText.setBounds(0, 20, POPUP_WIDTH, POPUP_HEIGHT);
        _questionText.setHorizontalAlignment(JLabel.CENTER);
        _questionText.setVerticalAlignment(JLabel.TOP);
        initPieces();
        layeredPane.add(_questionText, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(_queen, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(_bishop, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(_rook, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(_knight, JLayeredPane.DEFAULT_LAYER);
        this.setBackground(Color.WHITE);
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(layeredPane, GroupLayout.DEFAULT_SIZE, POPUP_WIDTH, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(layeredPane, GroupLayout.DEFAULT_SIZE, POPUP_HEIGHT, Short.MAX_VALUE));
    }

    /**
     * Inits the button 
     */
    private void initPieces()
    {
         _queen = new JButton();
         _queen.setIcon(new ImageIcon(LobbyConstant.chargeFichier("fr/utc/ressources/piece/queen_"+_colorText+".png")));
         _queen.setBounds(10, 60, 70, 80);
         _queen.setBackground(Color.white);

         _bishop = new JButton();
         _bishop.setIcon(new ImageIcon(LobbyConstant.chargeFichier("fr/utc/ressources/piece/bishop_"+_colorText+".png")));
         _bishop.setBounds(80, 60, 70, 80);
         _bishop.setBackground(Color.white);

         _rook = new JButton();
         _rook.setIcon(new ImageIcon(LobbyConstant.chargeFichier("fr/utc/ressources/piece/rook_"+_colorText+".png")));
         _rook.setBounds(150, 60, 70, 80);
         _rook.setBackground(Color.white);

         _knight = new JButton();
         _knight.setIcon(new ImageIcon(LobbyConstant.chargeFichier("fr/utc/ressources/piece/knight_"+_colorText+".png")));
         _knight.setBounds(220, 60, 70, 80);
         _knight.setBackground(Color.white);

    }

    public int getPOPUP_HEIGHT()
    {
        return POPUP_HEIGHT;
    }

    public int getPOPUP_WIDTH()
    {
        return POPUP_WIDTH;
    }
}
