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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Custom component class, link a textField with a label
 * @author lo23a015
 */
public class InformationField extends JPanel
{
    private JLabel _JLname;
    private JTextField _JTFvalue;

    public InformationField(String name)
    {
        initComponents(name, LobbyConstant.DEFAULT_COLUMNS_FIELD);
    }

    public InformationField(String name, int Columns)
    {
        initComponents(name, Columns);
    }

    /**
     * Components initialization and positioning in the panel
     *
     * @param name Name of the field
     * @param Columns Size of the editing field
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
        _JTFvalue = new JTextField();
        this.add(_JTFvalue, contrainte);

    }

    public String getValue()
    {

        return this._JTFvalue.getText();


    }

    /**
     * Functions to define the size of the information field
     */
    public void setSizeTextField(Dimension d)
    {
        this._JTFvalue.setPreferredSize(d);
    }

    public Dimension getPreferredSizeText()
    {
        return _JLname.getPreferredSize();
    }

    public Dimension getMinimumSizeText()
    {
        return _JLname.getMinimumSize();
    }

    public void setMiniPrefSizeText(Dimension mini, Dimension pref)
    {
        _JLname.setPreferredSize(pref);
        _JLname.setMinimumSize(mini);
    }

    /**
     * @return
     */
    public JTextField getTextField()
    {
        return this._JTFvalue;
    }
}
