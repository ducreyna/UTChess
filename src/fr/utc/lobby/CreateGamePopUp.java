/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lobby;

import fr.utc.data.Profile;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * Popup window, which let you creat a new game
 *
 * @author lo23a014
 */
public class CreateGamePopUp extends JFrame
{
    private Profile _opponent;
    private JPanel _mainPanel;
    private JLabel _JLtitle;
    private JLabel _JLcolor;
    private JLabel _JLtimer;
    private JTextField _JTminutes;
    private JRadioButton _JRblack;
    private JRadioButton _JRwhite;
    private ButtonGroup _BGcolor;
    private JCheckBox _JCtimer;
    private JButton _JBcancel;
    private JButton _JBcreate;
    private View _parent;
    private OpponentProfileView _parent2;

    /**
     * Default constructor
     *
     * @param parent view parent which creat this popup
     * @ opponent profile of the opponent you want play with
     */
    public CreateGamePopUp(View parent, Profile opponent)
    {
        this._parent = parent;
        this._parent2 = null;
        initGamePopUp(opponent);
    }

    /**
     * Default constructor
     *
     * @param parent view parent which creat this popup
     * @param opponent profile of the opponent you want play with
     */
    public CreateGamePopUp(OpponentProfileView parent, Profile opponent)
    {
        this._parent2 = parent;
        this._parent = null;
        initGamePopUp(opponent);
    }

    /**
     * initialization and positioning the popup
     *
     * @param opponent profile of the opponent
     *
     */
    private void initGamePopUp(Profile opponent)
    {
        this._opponent = opponent;

        this.setTitle("Creer une partie");
        this.setResizable(false);
        this.setSize(LobbyConstant.CREATE_GAME_POPUP_SIZE_W, LobbyConstant.CREATE_GAME_POPUP_SIZE_H);
        setIconImage(new ImageIcon(LobbyConstant.chargeFichier(LobbyConstant.getPathLogo())).getImage());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        // Center the window
        this.setLocationRelativeTo(this.getParent());

        closingWindow();

        initComponents();

        this.setVisible(true);
    }

    /**
     * Components initialization and positioning in the window
     *
     */
    private void initComponents()
    {
        this._mainPanel = new JPanel();

        this._JLtitle = new JLabel("Création d'une nouvelle partie contre:  " + this._opponent.getPseudo());
        this._JLcolor = new JLabel("Couleur");
        this._JLtimer = new JLabel("Timer");
        this._JTminutes = new JTextField("secondes");
        this._JTminutes.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        this._JTminutes.setEnabled(false);
        this._JRblack = new JRadioButton("Noir");
        this._JRblack.setSelected(true);
        this._JRwhite = new JRadioButton("Blanc");
        this._JCtimer = new JCheckBox("Oui");
        this._JBcancel = new JButton("Annuler");
        this._JBcreate = new JButton("Créer");

        this._BGcolor = new ButtonGroup();
        this._BGcolor.add(_JRblack);
        this._BGcolor.add(_JRwhite);

        // General Layout
        this.setLayout(new GridLayout(1, 0));
        this.add(this._mainPanel);

        /* ================ Set panel ================ */
        this._mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();

        // Title
        constraint.anchor = GridBagConstraints.CENTER;
        constraint.gridwidth = 0;
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.gridheight = 1;
        constraint.insets = new Insets(0, 0, 30, 0);
        this._JLtitle.setFont(new Font(this._JLtitle.getText(), Font.BOLD, 14));
        this._mainPanel.add(this._JLtitle, constraint);

        // Color
        constraint.gridy += 1;
        constraint.insets = new Insets(0, 0, 0, 0);
        this._JLcolor.setFont(new Font(this._JLcolor.getText(), Font.BOLD, 12));
        this._mainPanel.add(this._JLcolor, constraint);

        constraint.fill = GridBagConstraints.NONE;
        constraint.gridwidth = GridBagConstraints.RELATIVE;
        constraint.insets = new Insets(0, 53, 0, 0);
        constraint.gridx = 0;
        constraint.gridy += 1;
        this._mainPanel.add(this._JRblack, constraint);
        constraint.gridx = 1;
        this._mainPanel.add(this._JRwhite, constraint);

        // Timer
        constraint.gridwidth = 0;
        constraint.gridx = 0;
        constraint.gridy += 1;
        constraint.insets = new Insets(20, 0, 0, 0);
        this._JLtimer.setFont(new Font(this._JLtimer.getText(), Font.BOLD, 12));
        this._mainPanel.add(this._JLtimer, constraint);

        constraint.fill = GridBagConstraints.NONE;
        constraint.gridwidth = GridBagConstraints.RELATIVE;
        constraint.insets = new Insets(0, 53, 0, 0);
        constraint.gridx = 0;
        constraint.gridy += 1;
        this._JCtimer.addActionListener(new TreatmentCheckboxTimer());
        this._mainPanel.add(this._JCtimer, constraint);
        constraint.gridx = 1;
        this._mainPanel.add(this._JTminutes, constraint);

        // Buttons
        constraint.anchor = GridBagConstraints.BASELINE_TRAILING;
        constraint.gridx = 0;
        constraint.gridy += 1;
        constraint.insets = new Insets(40, 15, 0, 0);
        // Cancel
        this._JBcancel.addActionListener(new TreatmentButtonCancel());
        this._mainPanel.add(this._JBcancel, constraint);
        // Create
        constraint.gridx = 1;
        this._JBcreate.setPreferredSize(this._JBcancel.getPreferredSize());
        this._JBcreate.setMinimumSize(this._JBcancel.getMinimumSize());
        this._JBcreate.addActionListener(new TreatmentButtonCreate());
        this._mainPanel.add(this._JBcreate, constraint);
    }

    /**
     * Private method to set the default closing of the window
     *
     */
    private void closingWindow()
    {
        // Window listener for the closing window event
        this.addWindowListener((WindowListener) new WindowAdapter()
        {
            // Callback for the right close event
            @Override
            public void windowClosing(WindowEvent e)
            {
                if (_parent != null)
                {
                    _parent.popup = null;
                    _parent.getParentFrame().setEnabled(true);
                }
                if (_parent2 != null)
                {
                    _parent2.popup = null;
                    _parent2.setEnabled(true);
                }
                dispose();
            }
        });
    }

    /**
     * Handler class for 'Timer' checkbox actions
     */
    private class TreatmentCheckboxTimer implements ActionListener
    {
        /**
         * Handler method of the action performed on the checkbox for the timer
         * Required because the class implements the interface ActionListener
         *
         * @param e event linking to the Action performed
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (((JCheckBox) e.getSource()).isSelected())
            {
                _JTminutes.setEnabled(true);
                _JTminutes.setText("");
            }
            else
            {
                _JTminutes.setEnabled(false);
                _JTminutes.setText("secondes");
            }
        }
    }

    /**
     * Handler class for 'Cancel' button actions
     */
    private class TreatmentButtonCancel implements ActionListener
    {
        /**
         * Handler method of the action performed on the button cancel Required
         * because the class implements the interface ActionListener
         *
         * @param e event linking to the Action performed
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (_parent != null)
            {
                _parent.popup = null;
                _parent.getParentFrame().setEnabled(true);
            }
            if (_parent2 != null)
            {
                _parent2.popup = null;
                _parent2.setEnabled(true);
            }
            dispose();

        }
    }

    /**
     * Handler class for 'Create' button actions
     */
    private class TreatmentButtonCreate implements ActionListener
    {
        /**
         * Handler method of the action performed on the button create Required
         * because the class implements the interface ActionListener
         *
         * @param e event linking to the Action performed
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            int timer;
            boolean color;

            //Check all the game's parameters before sending a Game Request
            if (_JCtimer.isSelected())
            {
                if (!_JTminutes.getText().isEmpty() && _JTminutes.getText().matches("[0-9]*"))
                {
                    if (_JRblack.isSelected())
                    {
                        System.out.println("couleur: Noir \t timer: yes \t duree: " + _JTminutes.getText());
                        color = true;
                        timer = Integer.parseInt(_JTminutes.getText());

                    }
                    else
                    {
                        System.out.println("couleur: Blanc \t timer: yes \t duree: " + _JTminutes.getText());
                        color = false;
                        timer = Integer.parseInt(_JTminutes.getText());
                    }

                    //Replace this PopUp by calling waitingRespondePopUp 
                    if (_parent != null)
                    {
                        _parent.popup = new WaitingResponsePopUp(_parent, _opponent, timer, color, true, null);
                    }
                    if (_parent2 != null)
                    {
                        _parent2.popup = null;
                        _parent2.getParent().popup = new WaitingResponsePopUp(_parent2, _opponent, timer, color, true, null);
                    }
                    //Close this PopUp
                    dispose();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Champ des secondes incorrect", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
            else
            {
                timer = -1;
                if (_JRblack.isSelected())
                {
                    System.out.println("couleur: Noir \t timer: no");
                    color = true;
                }
                else
                {
                    System.out.println("couleur: Blanc \t timer: no");
                    color = false;
                }

                //Replace this PopUp by calling waitingRespondePopUp
                if (_parent != null)
                {
                    _parent.popup = new WaitingResponsePopUp(_parent, _opponent, timer, color, true, null);
                }
                if (_parent2 != null)
                {
                    _parent2.popup = null;
                    _parent2.getParent().popup = new WaitingResponsePopUp(_parent2, _opponent, timer, color, true, null);
                }
                //Close this PopUp
                dispose();
            }
        }
    }
}
