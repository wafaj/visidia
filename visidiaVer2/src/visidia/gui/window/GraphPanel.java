package visidia.gui.window;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Enumeration;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import visidia.gui.graphview.GraphView;
import visidia.gui.graphview.VertexView;

// TODO: Auto-generated Javadoc
/**
 * GraphPanel offers a graph representation with which the user can interact.
 */
public class GraphPanel extends JPanel {

	private static final long serialVersionUID = -5023939289474154395L;

	/** The graph view. */
	protected GraphView graphView = null;

	/** The selected items. */
	Selection selection = null;

	/** The selected area. */
	protected Rectangle selectedArea = null;

	/** The drawing panel. */
	DrawingPanel drawingPanel = null;
	
	/** The scroll pane. */
	JScrollPane scroller = null;
	
	/** The maximum position of a vertex on screen (higher x and y coordinates) . */
	protected Point vertexMaxPos = null;

	/** The minimum position of a vertex on screen (lower x and y coordinates) . */
	protected Point vertexMinPos = null;

	/** The boolean indicating if graph display is active. */
	protected boolean displayGraph;

	/**
	 * The DrawingPanelMode enumeration.
	 */
	protected enum DrawingPanelMode {
		/** The EDITION. */
		EDITION,
		/** The SIMULATION. */
		SIMULATION
	};

	/** The drawing mode. */
	private DrawingPanelMode drawingMode;

	/**
	 * The Class DrawingPanel.
	 */
	protected class DrawingPanel extends JPanel {

		/** The Constant BORDER. */
		public static final int BORDER = 50;

        /** The zoom factor. */
        private double zoomFactor = 1.0;
        
        /** The zoom percentage. */
        private final double ZOOM_PERCENTAGE = 0.1; 
        
        /** The info label. */
        private JLabel info;
        
		/**
		 * Instantiates a new drawing panel.
		 */
		public DrawingPanel() {
			super(new BorderLayout());
			info = new JLabel();
			this.add(info, BorderLayout.CENTER);
		}

    	/**
	     * Sets the info text.
	     * 
	     * @param infoText the new info text
	     */
	    public void setInfoText(String infoText) {
    		info.setText(infoText);
    		repaint();
    	}
    	
		/**
		 * Display a text line.
		 * 
		 * @param g the Graphics context
		 * @param msg the message to display
		 * @param ypos the message position along y axis
		 */
		private void displayTextLine(Graphics g, String msg, int ypos) {
			Dimension d = this.getSize();				
			FontMetrics fm = g.getFontMetrics();
			int msgWidth = fm.stringWidth(msg);
			int xpos = d.width / 2 - msgWidth / 2;
							
			g.drawString(msg, xpos, ypos);
		}

		/* (non-Javadoc)
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (displayGraph) {
				info.setVisible(false);
				setLayout(new BorderLayout());
				Graphics2D g2 = (Graphics2D) g;
				g2.scale(zoomFactor, zoomFactor);
				graphView.drawGraph(g2);

				if (selectedArea != null) {
					g2.setColor(Color.black);
					g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {9}, 0));
					g2.drawRect(selectedArea.x, selectedArea.y, selectedArea.width, selectedArea.height);
				}
			} else if (drawingMode == DrawingPanelMode.EDITION) {
				info.setVisible(false);
				String nbVertices = new Integer(graphView.getNbVertices()).toString();
				String nbEdges = new Integer(graphView.getNbEdges()).toString();
				
				displayTextLine(g, "Current graph properties:", 100);
				displayTextLine(g, "Nb vertices: " + nbVertices, 150);
				displayTextLine(g, "Nb edges: " + nbEdges, 200);
			} else if (drawingMode == DrawingPanelMode.SIMULATION) {
				setLayout(new FlowLayout());
				info.setVisible(true);
			}
		}

		/**
		 * Zoom in.
		 */
		public void zoomIn() {
			zoomFactor += ZOOM_PERCENTAGE;
		}

		/**
		 * Zoom out.
		 */
		public void zoomOut() {
			zoomFactor -= ZOOM_PERCENTAGE;

			if (zoomFactor < ZOOM_PERCENTAGE) {
				if (ZOOM_PERCENTAGE > 1.0) zoomFactor = 1.0;
				else zoomIn();
			} 
		}

		/**
		 * Zoom to original size.
		 */
		public void zoomToOriginal() {
			zoomFactor = 1;
		}

		/**
		 * Gets the zoom factor.
		 * 
		 * @return the zoom factor
		 */
		public double getZoomFactor() {
			return zoomFactor;
		}
	}

	/**
	 * Instantiates a new graph panel.
	 * 
	 * @param graphView the graph view
	 * @param drawingMode the drawing mode
	 * @param displayGraph the display graph
	 */
	protected GraphPanel(GraphView graphView, DrawingPanelMode drawingMode, boolean displayGraph) {
		super(new BorderLayout());

		this.graphView = graphView;
		this.drawingMode = drawingMode;
		this.displayGraph = displayGraph;
		selection = new Selection();
		
		drawingPanel = new DrawingPanel();
		drawingPanel.setFocusable(true);
		drawingPanel.setFocusTraversalKeysEnabled(false);
		drawingPanel.requestFocusInWindow();

		scroller = new JScrollPane(drawingPanel);
		add(scroller, BorderLayout.CENTER);
		
		setGraphBounds();
	}

	/**
	 * Sets the boolean indicating if the graph is to be displayed.
	 * 
	 * @param displayGraph true, if graph is to be displayed
	 */
	public void setDisplayGraph(boolean displayGraph) {
		this.displayGraph = displayGraph;
		repaint();
	}

	/**
	 * Gets the graph view.
	 * 
	 * @return the graph view
	 */
	public GraphView getGraphView() {
		return graphView;
	}

	public void setGraphView(GraphView gv) {
		graphView = gv;
	}
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawingPanel.repaint();
	}
	
    /**
     * This method adjusts the layout after zooming.
     */
	private void adjustLayout() {
		int b = DrawingPanel.BORDER;
		double scale = drawingPanel.getZoomFactor();
		int xSize = (int) (vertexMaxPos.x*scale + b);
		int ySize = (int) (vertexMaxPos.y*scale + b);
		drawingPanel.setPreferredSize(new Dimension(xSize, ySize));
		drawingPanel.revalidate();
		drawingPanel.repaint();
	}

	/**
	 * Zoom in.
	 */
	public void zoomIn() {
		if (!displayGraph) return;
		drawingPanel.zoomIn();
		adjustLayout();
	}

	/**
	 * Zoom out.
	 */
	public void zoomOut() {
		if (!displayGraph) return;
		drawingPanel.zoomOut();
		adjustLayout();
	}

	/**
	 * Zoom to original size.
	 */
	public void zoomToOriginal() {
		if (!displayGraph) return;
		drawingPanel.zoomToOriginal();
		adjustLayout();
	}

	/**
	 * Gets the zoom factor.
	 * 
	 * @return the zoom factor
	 */
	public double getZoomFactor() {
		return drawingPanel.getZoomFactor();
	}
	
	/**
	 * Moves vertex items.
	 * 
	 * @param deltaX the displacement along x axis
	 * @param deltaY the displacement along y axis
	 */
	private void moveVertexItems(int deltaX, int deltaY) {
		Enumeration<VertexView> vertices = graphView.getVertexItems();
		while (vertices.hasMoreElements()) {
			vertices.nextElement().move(deltaX, deltaY);
		}
		
		vertexMinPos.x += deltaX;
		vertexMinPos.y += deltaY;
		vertexMaxPos.x += deltaX;
		vertexMaxPos.y += deltaY;

		updateDrawingPanelSize();
	}

	/**
	 * Updates drawing panel size.
	 */
	protected void updateDrawingPanelSize() {
		if (vertexMinPos.x < 0) {
			if (vertexMinPos.y < 0) moveVertexItems(-vertexMinPos.x, -vertexMinPos.y);
			else moveVertexItems(-vertexMinPos.x, 0);
		} else if (vertexMinPos.y < 0) moveVertexItems(0, -vertexMinPos.y);

		int b = DrawingPanel.BORDER;
		if (vertexMinPos.x < b) {
			if (vertexMinPos.y < b) moveVertexItems(b-vertexMinPos.x, b-vertexMinPos.y);
			else moveVertexItems(b-vertexMinPos.x, 0);
		} else if (vertexMinPos.y < b) moveVertexItems(0, b-vertexMinPos.y);

		adjustLayout();
	}

	/**
	 * Specifies that a vertex is at the (x,y) position on screen.
	 * 
	 * @param x the x position
	 * @param y the y position
	 */
	protected void setVertexPosition(int x, int y) {
		if (x < vertexMinPos.x) vertexMinPos.x = x;
		if (x > vertexMaxPos.x) vertexMaxPos.x = x;
		if (y < vertexMinPos.y) vertexMinPos.y = y;
		if (y > vertexMaxPos.y) vertexMaxPos.y = y;
		updateDrawingPanelSize();
	}

	/**
	 * Sets the graph bounds (the min/max position of vertices on x and y axis).
	 */
	protected void setGraphBounds() {
		vertexMaxPos = new Point(Short.MIN_VALUE, Short.MIN_VALUE);
		vertexMinPos = new Point(Short.MAX_VALUE, Short.MAX_VALUE);
		
		if (graphView == null) return;
		
		Enumeration<VertexView> vertices = graphView.getVertexItems();
		while (vertices.hasMoreElements()) {
			Point pos = vertices.nextElement().getPosition();
			setVertexPosition(pos.x, pos.y);
		}
	}
	
}
