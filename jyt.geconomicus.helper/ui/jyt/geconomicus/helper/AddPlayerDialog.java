package jyt.geconomicus.helper;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.persistence.EntityManager;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class AddPlayerDialog extends JDialog
{
	private class CancelAction extends AbstractAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent pEvent)
		{
			setVisible(false);
		}
	}

	private class AddAction extends AbstractAction implements ActionListener
	{
		private final JTextField mNameTF;

		private AddAction(JTextField pNameTF)
		{
			mNameTF = pNameTF;
		}

		@Override
		public void actionPerformed(ActionEvent pEvent)
		{
			mEntityManager.getTransaction().begin();
			if (mPlayer == null)
				mPlayer = new Player(mGame, mNameTF.getText());
			else
				mPlayer.setName(mNameTF.getText());
			mEntityManager.getTransaction().commit();
			setVisible(false);
		}
	}

	private EntityManager mEntityManager;
	private Game mGame;
	private Player mPlayer = null;

	public AddPlayerDialog(final JFrame pParent, final Game pGame, final EntityManager pEntityManager, Player pExistingPlayer)
	{
		super(pParent, "Renommer un joueur");
		mPlayer = pExistingPlayer;
		initWindow(pGame, pEntityManager);
	}

	public AddPlayerDialog(final JFrame pParent, final Game pGame, final EntityManager pEntityManager)
	{
		super(pParent, "Ajouter un joueur");
		initWindow(pGame, pEntityManager);
	}

	public void initWindow(final Game pGame, final EntityManager pEntityManager)
	{
		mGame = pGame;
		mEntityManager = pEntityManager;
		setSize(500, 300);
		setLocation(200, 200);
		setModal(true);
		final JPanel mainPanel = new JPanel(new GridBagLayout());
		mainPanel.add(new JLabel("Nom"), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		final JTextField nameTF = new JTextField(50);
		if (mPlayer != null)
			nameTF.setText(mPlayer.getName());
		mainPanel.add(nameTF, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		nameTF.getInputMap().put(KeyStroke.getKeyStroke((char)10), "enter");
		nameTF.getActionMap().put("enter", new AddAction(nameTF));
		nameTF.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
		nameTF.getActionMap().put("escape", new CancelAction());
		final JPanel buttonsPanel = new JPanel(new FlowLayout());
		final JButton addButton = new JButton(mPlayer != null ? "Renommer" : "Ajouter");
		buttonsPanel.add(addButton);
		addButton.addActionListener(new AddAction(nameTF));
		final JButton cancelButton = new JButton("Annuler");
		buttonsPanel.add(cancelButton);
		cancelButton.addActionListener(new CancelAction());
		mainPanel.add(buttonsPanel, new GridBagConstraints(0, 10, 2, 1, 1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		getContentPane().add(mainPanel);
		addFocusListener(new FocusListener()
		{
			@Override
			public void focusLost(FocusEvent pE)
			{
			}
			
			@Override
			public void focusGained(FocusEvent pE)
			{
				nameTF.requestFocus();
			}
		});
	}

	public Player getNewPlayer()
	{
		return mPlayer;
	}
}
