package visidia.gui.window;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

// TODO: Auto-generated Javadoc
/**
 * Class ClosableTab represents the "header" of a tab in a JTabbedPane, offering the opportunity to close the tab.
 * Contains a JLabel to show the text and a JButton to close the tab it belongs to.
 * 
 * Requires Java6 (for method JTabbedPane.indexOfTabComponent(Component tabComponent)).
 */ 
public class ClosableTab extends JPanel {

	private static final long serialVersionUID = -8177704165051886688L;

	/** The tabbed pane. */
	private final JTabbedPane pane;
	
	/** Tells if the tab can be closed or not. */
	private boolean isClosable = true;

	/**
	 * Instantiates a new closable tab.
	 * 
	 * @param pane the tabbed pane
	 * @param isClosable tells if the tab can be closed or not
	 */
	public ClosableTab(final JTabbedPane pane, boolean isClosable) {
		//unset default FlowLayout' gaps
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		if (pane == null) throw new NullPointerException("TabbedPane is null");
		this.pane = pane;
		this.isClosable = isClosable;
		setOpaque(false);

		//make JLabel read titles from JTabbedPane
		JLabel label = new JLabel() {
			public String getText() {
				int i = pane.indexOfTabComponent(ClosableTab.this);
				if (i != -1) return pane.getTitleAt(i);
				return null;
			}
		};

		add(label);
		//add more space between the label and the button
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		//tab button
		JButton button = new TabButton();
		add(button);
		//add more space to the top of the component
		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
	}

	/**
	 * Instantiates a new closable tab.
	 * 
	 * @param pane the tabbed pane
	 */
	public ClosableTab(final JTabbedPane pane) {
		this(pane, true);
	}

	/**
	 * The button to close the tab.
	 */
	private class TabButton extends JButton implements ActionListener {
		
		/**
		 * Instantiates a new tab button.
		 */
		public TabButton() {
			int size = 12;
			setPreferredSize(new Dimension(size, size));
			//Make the button looks the same for all Laf's
			setUI(new BasicButtonUI());
			//Make it transparent
			setContentAreaFilled(false);
			//No need to be focusable
			setFocusable(false);
			setBorder(null);
			setBorderPainted(false);
			if (isClosable) {
				setToolTipText("close this tab");
				//Making nice rollover effect
				setRolloverEnabled(true);
				//Close the proper tab by clicking the button
				addActionListener(this);
			}
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			int i = pane.indexOfTabComponent(ClosableTab.this);
			if (i != -1) {
				pane.getComponentAt(i).setVisible(false);
				pane.remove(i);
			}
		}

		/* (non-Javadoc)
		 * @see javax.swing.JButton#updateUI()
		 */
		public void updateUI() {
		}

		/* (non-Javadoc)
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (!isClosable) return;

			Graphics2D g2 = (Graphics2D) g.create();
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.BLACK);
			if (getModel().isRollover()) g2.setColor(Color.RED);
			int delta = 3;
			int offsetH = 1;
			g2.drawLine(delta, delta + offsetH, getWidth() - delta - 1, getHeight() - delta - 1 + offsetH);
			g2.drawLine(getWidth() - delta - 1, delta + offsetH, delta, getHeight() - delta - 1 + offsetH);
			g2.dispose();
		}
	}

}
