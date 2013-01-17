/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.grid.board.player;

import fr.utc.data.Player;
import fr.utc.lobby.LobbyConstant;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

/**
 * The class displaying the player informations
 * @author Maxime Bornemann & Etienne Gadeski
 */
public class PlayerInfoView extends JPanel implements ActionListener {

    /**
     * State whether the player is the white player
     */
    boolean _isBlack;

    /**
     * Label displaying the player name
     */
    JLabel _playerNameLabel;

    /**
     * Label displaying the player remaining time
     */
    JLabel _timerLabel;

    /**
     * The timer
     */
    Timer _timer;

    /**
     * The player represented in this info view
     */
    Player _player;

    public final int COMPONENT_WIDTH = 405;
    public final int COMPONENT_HEIGHT = 148;

    /**
     * Default constructor
     * @param playerName The player name
     * @param isReplay If true, this is a replay
     */
    public PlayerInfoView(Player player, boolean isReplay)
    {
        _player = player;

        this.setPreferredSize(new Dimension(COMPONENT_WIDTH, COMPONENT_HEIGHT));
        _isBlack = player.getColor();
        _playerNameLabel = new JLabel(player.getPlayer().getPseudo());
        _timerLabel = new JLabel(normalizeTime(player.getTimer()));
        JLayeredPane layeredPane = new JLayeredPane();

        // Background
        JLabel background = new JLabel(new ImageIcon(LobbyConstant.chargeFichier("fr/utc/ressources/player_info_bg.png")));
        background.setBounds(0, 0, COMPONENT_WIDTH, COMPONENT_HEIGHT);

        // Player label
        _playerNameLabel.setFont(new Font("Georgia", Font.BOLD, 33));
        _playerNameLabel.setForeground(_isBlack ? Color.black : Color.white);
        _playerNameLabel.setHorizontalAlignment(JLabel.CENTER);

        // 0 = no timer
        if(player.getTimer() == -1 || isReplay) {
            _playerNameLabel.setVerticalAlignment(JLabel.CENTER);
            _playerNameLabel.setBounds(15, 0, COMPONENT_WIDTH - 30, COMPONENT_HEIGHT);
        }
        else {
            _playerNameLabel.setVerticalAlignment(JLabel.TOP);
            _playerNameLabel.setBounds(15, 35, COMPONENT_WIDTH - 30, COMPONENT_HEIGHT);
            _timerLabel.setVerticalAlignment(JLabel.TOP);
            _timerLabel.setHorizontalAlignment(JLabel.CENTER);
            _timerLabel.setFont(new Font("Georgia", Font.BOLD, 28));
            _timerLabel.setForeground(new Color(137, 78, 33));
            _timerLabel.setBounds(0, 80, COMPONENT_WIDTH, COMPONENT_HEIGHT);
            layeredPane.add(_timerLabel, JLayeredPane.DEFAULT_LAYER);
            this.initTimer();
        }

        layeredPane.add(_playerNameLabel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(layeredPane, GroupLayout.DEFAULT_SIZE, COMPONENT_WIDTH, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(layeredPane, GroupLayout.DEFAULT_SIZE, COMPONENT_HEIGHT, Short.MAX_VALUE));
    }

    /**
     * Normalize a time to be displayed in mm:ss format
     *
     * @param time The time to normalize
     * @return The normalized time
     */
    private String normalizeTime(int time)
    {
        long minutes = TimeUnit.SECONDS.toMinutes(time);
        long seconds = time - minutes * 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Initializes the timer
     */
    private void initTimer()
    {
        _timer = new Timer(1000, this);
    }

    public void actionPerformed(ActionEvent e)
    {
        _player.setTimer(_player.getTimer() - 1);
        if (_player.getTimer() >= 0) {
            _timerLabel.setText(normalizeTime(_player.getTimer()));
        }
        else {
            // The game ain't in me no mo'
            System.out.println(_player.getPlayer().getPseudo() + " is out of time.");
            _timer.stop();
        }
    }

    /**
     * Pauses the timer
     */
    public void pauseTimer()
    {
        _timer.stop();
    }

    /**
     * Resumes the timer
     */
    public void resumeTimer()
    {
        this.initTimer();
        _timer.start();
    }
}
