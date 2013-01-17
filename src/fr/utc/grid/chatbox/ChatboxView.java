package fr.utc.grid.chatbox;

import fr.utc.data.historique.Action;
import fr.utc.data.historique.Message;
import fr.utc.data.historique.Move;
import fr.utc.data.piece.Piece;
import fr.utc.grid.board.GridController;
import fr.utc.network.exceptions.ErrorSendingMessageException;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

/**
 * View which represent the Chatbox, implements all the functionalities of the Chatbox
 * 
 * @author Nicolas CHAPUT, Amaury LAVERGNE
 */
public class ChatboxView extends JPanel {

	private static final long serialVersionUID = 2894607318287043664L;
	/**
	 * 	the date formatter, use to display the date of a newly received message
	 */
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
	/**
	 * represent the text displayed in the chatbox
	 */
	private JTextPane _textBox;
	/**
	 * enables the scroll on all the text displayed in _textBox
	 */
	private JScrollPane _postingPane;
	/**
	 * the input where to write the text
	 */
	private JTextArea _writingTexBox;
	/**
	 * the current text in formatting state before being displayed in _textBox
	 */
	private String _currentText = "";
	/**
	 *  a reference to the GridController
	 */
	private GridController _gridController;

	/**
	 * Constructor by default
	 * 
	 * @param gridController
	 *            the GridController reference
	 */
	public ChatboxView(GridController gridController) {
		// set the size of the JPanel
		this.setMinimumSize(new Dimension(385, 250));
		// the vertical layout for the ChatBoxView
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// instance of the gridController
		_gridController = gridController;
		// instantiation of the textBox
		_textBox = new JTextPane();
		// indicate that the text displayed in _textBox should be considered as HTML
		_textBox.setContentType("text/html");
		// prevent _textBox from being edited
		_textBox.setEditable(false);
		// set the size of _textBox
		_textBox.setPreferredSize(new Dimension(500, 250));
		// instantiation of the scroll pane
		_postingPane = new JScrollPane(_textBox);

		// instantiation of the JTextArea
		_writingTexBox = new JTextArea();
		// indicate that the text will be wrap horizontally
		_writingTexBox.setLineWrap(true);
		// set the wrapping will be effective after each word
		_writingTexBox.setWrapStyleWord(true);
		// instantiation of the font used in _writingTexBox
		Font font = new Font("Verdana", Font.BOLD, 12);
		// set the font to _writingTexBox
		_writingTexBox.setFont(font);

		// instantiation of the scroll pane used to enable the scroll in _writingTexBox
		JScrollPane writingPane = new JScrollPane(_writingTexBox);
		// set the size of _writingTexBox
		writingPane.setPreferredSize(new Dimension(305, 45));

		// instantiation of the button "Envoyer"
		JButton btEnvoyer = new JButton("Envoyer");
		// set the size of the button "Envoyer"
		btEnvoyer.setPreferredSize(new Dimension(97, 44));
		// the listener of the click on the button "Envoyer"
		btEnvoyer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// send the post to the other player
				sendPostPlayer(false);
			}
		});

		// the key listener of _writingTexBox
		_writingTexBox.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent keyEvent) {
			}

			// when the key is released
			@Override
			public void keyReleased(KeyEvent keyEvent) {
				// if the key pressed is "Enter"
				if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
					// send the post to the other player
					sendPostPlayer(true);
				}
			}

			@Override
			public void keyPressed(KeyEvent keyEvent) {
			}
		});

		// End pane : contains _writingBox + the button "Envoyer"
		JPanel endPane = new JPanel();
		// set a horizontal layout for this panel
		endPane.setLayout(new BoxLayout(endPane, BoxLayout.LINE_AXIS));
		// add the writing pane to this panel
		endPane.add(writingPane);
		// A rigidArea used to do the margin between the button "Envoyer" and _writingBox
		endPane.add(Box.createRigidArea(new Dimension(5, 0)));
		// add the button "Envoyer" to this panel
		endPane.add(btEnvoyer);

		// add _postingPane
		this.add(_postingPane);
		// A rigidArea used to do the margin between the "display" zone and the endPane
		this.add(Box.createRigidArea(new Dimension(0, 5)));
		// add the endPane to the ChatBoxView
		this.add(endPane);
	}

	/**
	 * send the post written by the player to the other player
	 * 
	 * @param fromEntree
	 *            boolean which indicate if the action used is the click (false) or the key "Enter" (true)
	 */
	void sendPostPlayer(boolean fromEntree) {
		// we get back what is written in _writingTexBox
		String postPlayer = _writingTexBox.getText();
		// if the string is valid
		if (postPlayer != null && postPlayer.length() > 0 && postPlayer.compareTo("\n") != 0) {
			// if the action used is the key "Enter"
			if (fromEntree) {
				// we remove the "\n" at the end of the string
				postPlayer = postPlayer.substring(0, postPlayer.length() - 1);
			}

			// we create a new message
			Message msg = new Message(new Date(), GridController._currentGame.getPlayerLocal(), postPlayer);
			writePostPlayer(1, msg);
			try {
				// if the GridController and the network are ok
				if (_gridController != null && _gridController.getNetwork() != null && msg != null) {
					// we send the message
					_gridController.getNetwork().sendMessage(msg);

				}
			} catch (ErrorSendingMessageException ex) {
				Logger.getLogger(ChatboxView.class.getName()).log(Level.SEVERE, null, ex);
			}

		}
		// we empty the _writingTexBox
		_writingTexBox.setText("");
	}

	/**
	 * write the post written or received by the local player in _textBox in order to display it
	 * @param typePost
	 *            the type of post (1 : the author is the local player, 2 : the author is the opponent, 3 : system or move message)
	 * @param msg
	 * 			  the message to display
	 */
	public void writePostPlayer(int typePost, Action msg) {
		// string which contains the header of the html text
		String header = "<html>" + "<style type='text/css'>" + "     body { font-family:'Verdana'; }"
				+ "     h1 {Â font-size:10px; text-align:center }" + "     ul { list-style-type: none; margin-left: 10px; padding: 0; }"
				+ "</style>" + "<body>" + "     <ul id='messagesList'></ul>";
		// string which contains the footer of the html text
		String footer = "</body>" + "</html>";

		// if msg is a Message or a Move
		if (msg instanceof Message || msg instanceof Move) {
			// we get back the author of the message
			String playerName = msg.getEmitter().getPlayer().getPseudo();
			// we initialize the String postPlayer which contains the message to display
			String postPlayer = "";
			// if msg is a Message
			if (msg instanceof Message) {
				// we get back the message as a String
				postPlayer = ((Message) msg).getMsg();
			} 
			// if the message is a Move
			else if (msg instanceof Move) {
				// we cast msg as a Move
				Move move = (Move) msg;
				// if all the Object used are ok
				if (move != null && GridController._currentGame != null && GridController._currentGame.getGrid() != null
						&& GridController._currentGame.getGrid() != null
						&& GridController._currentGame.getGrid().getAt(move.getNewPos()) != null
						&& ((Piece) GridController._currentGame.getGrid().getAt(move.getNewPos())) != null
						&& ((Piece) GridController._currentGame.getGrid().getAt(move.getNewPos())).getClass() != null
						&& ((Piece) GridController._currentGame.getGrid().getAt(move.getNewPos())).getClass().getName() != null) {
					// we get back the class name of the pawn which moved
					String name = ((Piece) GridController._currentGame.getGrid().getAt(move.getNewPos())).getClass().getName();
					// if the name is ok
					if (name != null && name.length() > 0) {
						// we remove all except the name of the pawn
						name = name.toLowerCase().replaceAll("^.+\\.", "");
						// we write the post player with the name of the pawn, the original location, the new location
						postPlayer = new String(name + " de " + ((Move) msg).getOldPos().getColumnLetter()
								+ ((Move) msg).getOldPos().getLine() + " a  " + ((Move) msg).getNewPos().getColumnLetter()
								+ ((Move) msg).getNewPos().getLine());
					}
				}
			}
			// we get the system date as a String
			String currentTime = dateFormat.format(msg.getTime());

			// if it is a message from the local player 
			if (typePost == 1) {
				// we write the name of the local player in red + the post player in regular font
				_currentText = _currentText + "<font color=\"red\"><strong>" + "[" + currentTime + "] " + playerName
						+ "</strong></font> : " + postPlayer + "<br/>";
			} 
			// if it is a message from the opponent player 
			else if (typePost == 2) {
				// we write the name of the opponent player in blue + the post player in regular font
				_currentText = _currentText + "<font color=\"blue\"><strong>" + "[" + currentTime + "] " + playerName
						+ "</strong></font> : " + postPlayer + "<br/>";
			} 
			// if it is a system message
			else {
				// we write the Move in gray + the post player in regular font
				_currentText = _currentText + "<font color=\"gray\"><strong>" + "[" + currentTime + "] " + playerName
						+ "</strong></font> : " + postPlayer + "<br/>";
			}
			// we write the html text with the header and the footer
			_textBox.setText(header + _currentText + footer);
			// we display the HTML text
			_postingPane.setViewportView(_textBox);

			// if the Game and the Historic are ok
			if (GridController._currentGame != null && GridController._currentGame.getHisto() != null && msg != null) {
				// we push the move or the message in the historic stack
				GridController._currentGame.getHisto().pushBack(msg);
			}
		}

		// set the scrolling down, at the bottom of the text displayed in _textBox
		_textBox.setCaretPosition(_textBox.getDocument().getLength());
	}
}
