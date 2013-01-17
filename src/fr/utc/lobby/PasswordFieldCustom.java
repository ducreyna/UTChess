/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lobby;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Custom component, link a passwordField with a label
 * @author lo23a014
 */
public class PasswordFieldCustom extends JPanel
{
    private JLabel _JLname;
    private JPasswordField _JTFvalue;
    private ActionListener _CallBackListener;

    public PasswordFieldCustom(String name)
    {
        _CallBackListener = null;
        initComponents(name, LobbyConstant.DEFAULT_COLUMNS_FIELD);
    }

    public PasswordFieldCustom(String name, ActionListener callBack)
    {
        _CallBackListener = callBack;
        initComponents(name, LobbyConstant.DEFAULT_COLUMNS_FIELD);
    }

    public PasswordFieldCustom(String name, int Columns, ActionListener callBack)
    {
        _CallBackListener = callBack;
        initComponents(name, Columns);
    }

    public PasswordFieldCustom(String name, int Columns)
    {
        _CallBackListener = null;
        initComponents(name, Columns);
    }

    /**
     * Components initialization and positioning in the panel
     *
     * @param name Name of the field
     * @param limit Size of the editing field
     */
    private void initComponents(String name, int Columns)
    {
        this.setBackground(Color.gray);
        this.setLayout(new GridBagLayout());
        GridBagConstraints contrainte = new GridBagConstraints();
        contrainte.gridy = 0;
        contrainte.gridheight = 1;
        contrainte.anchor = GridBagConstraints.BASELINE_LEADING;
        contrainte.fill = GridBagConstraints.HORIZONTAL;
        contrainte.insets = new Insets(0, 15, 0, 15);
        contrainte.gridwidth = 1;
        contrainte.gridx = 0;
        contrainte.weightx = 0.5;
        contrainte.weighty = 0;

        _JLname = new JLabel(name);
        this.add(_JLname, contrainte);

        contrainte.gridwidth = GridBagConstraints.REMAINDER;
        contrainte.anchor = GridBagConstraints.BASELINE_TRAILING;

        contrainte.gridx = 1;
        _JTFvalue = new JPasswordField();

        //Ajouter un Action listerner 
        if (_CallBackListener != null)
        {
            _JTFvalue.addActionListener(_CallBackListener);
        }
        else
        {
            _JTFvalue.addKeyListener(new TreatmentKonami());
        }

        this.add(_JTFvalue, contrainte);
    }

    public String getValue()
    {
        return new String(this._JTFvalue.getPassword());
    }

    /**
     * Functions to define the preferred size of the information field
     *
     * @param d preferred size you want set to the field
     */
    public void setSizeTextField(Dimension d)
    {
        this._JTFvalue.setPreferredSize(d);
    }

    /**
     * Functions to get the size of the text field
     *
     * @return the preferred size of the field
     */
    public Dimension getPreferredSizeText()
    {
        return _JLname.getPreferredSize();
    }

    /**
     * Functions to get the minimum size of the text field
     *
     * @return the minimum size of the text field
     */
    public Dimension getMinimumSizeText()
    {
        return _JLname.getMinimumSize();
    }

    /**
     * Functions to define the size of the information field
     *
     * @param mini minimum size you want set to the field
     * @param pref preferred size you want set to the field
     */
    public void setMiniPrefSizeText(Dimension mini, Dimension pref)
    {
        _JLname.setPreferredSize(pref);
        _JLname.setMinimumSize(mini);
    }

    /**
     * Functions to get the text field
     *
     * @return the text field
     */
    public JTextField getTextField()
    {
        return this._JTFvalue;
    }

    /**
     * Handler class for 'Konami'
     */
    private class TreatmentKonami implements KeyListener
    {
        int niveau;

        /**
         * Handle the key typed event from the text field.
         *
         * @param e event linking to the Action performed
         */
        @Override
        public void keyTyped(KeyEvent e)
        {
        }

        /**
         * Handle the key-pressed event from the text field.
         *
         * @param e event linking to the Action performed
         */
        @Override
        public void keyPressed(KeyEvent e)
        {
        }

        /**
         * Handle the key-released event from the text field.
         *
         * @param e event linking to the Action performed
         */
        @Override
        public void keyReleased(KeyEvent e)
        {
            if ((niveau == 0 || niveau == 1) && (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP))
            {
                niveau++;
            }
            else if ((niveau == 2 || niveau == 3) && (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_KP_DOWN))
            {
                niveau++;
            }
            else if ((niveau == 4 || niveau == 6) && (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_KP_LEFT))
            {
                niveau++;
            }
            else if ((niveau == 5 || niveau == 7) && (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_KP_RIGHT))
            {
                niveau++;
            }
            else if (niveau == 8 && e.getKeyCode() == KeyEvent.VK_A)
            {
                niveau++;
            }
            else if (niveau == 9 && e.getKeyCode() == KeyEvent.VK_B)
            {
                lancerKonami();
                niveau = 0;
            }
            else
            {
                niveau = 0;
            }
        }

        /**
         * Handle the key-released event from the text field.
         *
         * @param e event linking to the Action performed
         */
        private void lancerKonami()
        {
            LobbyConstant.toggleActivedKonami();
        }
    }
}
