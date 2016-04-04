package graphBuilderViSiDiA;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;

import javax.swing.JPanel;

public class BuildGraphTPanel extends JPanel {
	private int topologyIndex;
	private int d1;
	private int d2;

	public BuildGraphTPanel(int D1, int D2) {
		setBackground(Color.WHITE);
		setOpaque(true);
		topologyIndex = 0;
		d1 = D1;
		d2 = D2;
	}

	public int getTopologyIndex() {
		return topologyIndex;
	}

	public void setTopologyIndex(int topologyIndex, int D1, int D2) {
		this.topologyIndex = topologyIndex;
		d1 = D1;
		d2 = D2;
		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		int topologyIndedx2 = (int) topologyIndex;
		switch (topologyIndedx2) {

		case 0:
			DrawFormThinH(g);
			break; // ok
		case 1:
			DrawFormThinCross(g);
			break;// oki
		case 2:
			DrawFormS(g);
			break;// oki
		case 3:
			DrawFormLargeCross(g);
			break;// ok
		case 4:
			DrawFormL(g);
			break;
		case 5:
			DrawFormH(g);
			break;
		case 6:
			DrawGrille(g);
			break;
		case 7:
			DrawRectangle(g);
			break;

		/**/
		}

	}

	private void DrawRectangle(Graphics g) {

		int panelWidth = getWidth();
		int panelHeight = getHeight();
		int grapWidth = getGWidth();
		int graphHeight = getGHeight();

		int boxSize = panelWidth > panelHeight ? panelHeight : panelWidth;
		double unite = (boxSize / (Math.max(d1, d2) + 2));
		Path2D path = new Path2D.Double();
		double x = 2 * unite;
		double y = 1 * unite;
		path.moveTo(x, y);
		x = unite;
		y = unite;
		path.lineTo(x, y);
		x = (1 + d1) * unite;
		y = unite;
		path.lineTo(x, y);
		x = (1 + d1) * unite;
		y = (1 + d2) * unite;
		path.lineTo(x, y);
		x = unite;
		y = (1 + d2) * unite;
		path.lineTo(x, y);
		x = unite;
		y = unite;
		path.lineTo(x, y);
		for (int i = 2; i < (d2 + 1); i++) {
			x = unite;
			y = i * unite;
			path.moveTo(x, y);
			x = (d1 + 1) * unite;
			path.lineTo(x, y);
		}
		for (int i = 2; i < (d1 + 1); i++) {
			x = i * unite;
			y = unite;
			path.moveTo(x, y);
			y = (d2 + 1) * unite;
			path.lineTo(x, y);
		}

		path.closePath();

		Graphics2D g2d = (Graphics2D) g;

		// Activates "antialiasing" to have a good quality.
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Fills the path.
		g2d.setColor(Color.YELLOW);
		g2d.fill(path);

		// Draws the path.
		g2d.setColor(Color.RED);
		g2d.draw(path);
	}

	private int getGWidth() {
		return d1;
	}

	private int getGHeight() {
		return d2;
	}

	private void DrawGrille(Graphics g) {
		int panelWidth = getWidth();
		int panelHeight = getHeight();
		int boxSize = panelWidth > panelHeight ? panelHeight : panelWidth;
		double unite = (boxSize / 10);
		Path2D path = new Path2D.Double();
		Path2D path1 = new Path2D.Double();

		for (int i = 2; i < 9; i++) {
			double x = unite;
			double y = i * unite;
			path.moveTo(x, y);
			x = 9 * unite;
			/* y =i*unite; */ path.lineTo(x, y);
		}
		for (int i = 2; i < 9; i++) {
			double x = i * unite;
			double y = unite;
			path.moveTo(x, y);
			/* x=9*unite; */y = 9 * unite;
			path.lineTo(x, y);
		}

		path.closePath();

		Graphics2D g2d = (Graphics2D) g;

		// Activates "antialiasing" to have a good quality.
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Fills the path.
		g2d.setColor(Color.WHITE);
		g2d.fill(path);

		// Draws the path.
		g2d.setColor(Color.RED);
		g2d.draw(path);

	}

	private void DrawFormThinH(Graphics g) {

		int panelWidth = getWidth();
		int panelHeight = getHeight();
		int boxSize = panelWidth > panelHeight ? panelHeight : panelWidth;
		double unite = (boxSize / 10);
		Path2D path = new Path2D.Double();
		Path2D path1 = new Path2D.Double();
		// point 1
		double x = 1 * unite;
		double y = 1 * unite;
		path.moveTo(x, y);
		x = 7 * unite;
		y = 1 * unite;
		path.lineTo(x, y);
		x = 7 * unite;
		y = 8 * unite;
		path.lineTo(x, y);
		x = 1 * unite;
		y = 8 * unite;
		path.lineTo(x, y);
		path.closePath();
		x = 2 * unite;
		y = 2 * unite;
		path1.moveTo(x, y);
		x = 3 * unite;
		y = 2 * unite;
		path1.lineTo(x, y);
		x = 3 * unite;
		y = 4 * unite;
		path1.lineTo(x, y);
		x = 5 * unite;
		y = 4 * unite;
		path1.lineTo(x, y);
		x = 5 * unite;
		y = 2 * unite;
		path1.lineTo(x, y);
		x = 6 * unite;
		y = 2 * unite;
		path1.lineTo(x, y);
		x = 6 * unite;
		y = 7 * unite;
		path1.lineTo(x, y);
		x = 5 * unite;
		y = 7 * unite;
		path1.lineTo(x, y);
		x = 5 * unite;
		y = 5 * unite;
		path1.lineTo(x, y);
		x = 3 * unite;
		y = 5 * unite;
		path1.lineTo(x, y);
		x = 3 * unite;
		y = 7 * unite;
		path1.lineTo(x, y);
		x = 2 * unite;
		y = 7 * unite;
		path1.lineTo(x, y);

		path1.lineTo(x, y);

		path1.closePath();

		Graphics2D g2d = (Graphics2D) g;

		// Activates "antialiasing" to have a good quality.
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Fills the path.
		g2d.setColor(Color.YELLOW);
		g2d.fill(path);

		// Draws the path.
		g2d.setColor(Color.RED);
		g2d.draw(path);
		// Fills the path1.
		g2d.setColor(Color.WHITE);
		g2d.fill(path1);

		// Draws the path1.
		g2d.setColor(Color.RED);
		g2d.draw(path1);

	}

	private void DrawFormThinCross(Graphics g) {

		int panelWidth = getWidth();
		int panelHeight = getHeight();
		int boxSize = panelWidth > panelHeight ? panelHeight : panelWidth;
		double unite = (boxSize / 7);
		Path2D path = new Path2D.Double();
		Path2D path1 = new Path2D.Double();
		// point 1
		double x = 1 * unite;
		double y = 1 * unite;
		path.moveTo(x, y);
		x = 6 * unite;
		y = 1 * unite;
		path.lineTo(x, y);
		x = 6 * unite;
		y = 6 * unite;
		path.lineTo(x, y);
		x = 1 * unite;
		y = 6 * unite;
		path.lineTo(x, y);
		path.closePath();

		//////////////////////////////////////////////////////////////////////////////////////
		x = 3 * unite;
		y = 2 * unite;
		path1.moveTo(x, y);

		x = 4 * unite;
		y = 2 * unite;
		path1.lineTo(x, y);
		x = 4 * unite;
		y = 3 * unite;
		path1.lineTo(x, y);
		x = 5 * unite;
		y = 3 * unite;
		path1.lineTo(x, y);
		x = 5 * unite;
		y = 4 * unite;
		path1.lineTo(x, y);
		x = 4 * unite;
		y = 4 * unite;
		path1.lineTo(x, y);
		x = 4 * unite;
		y = 5 * unite;
		path1.lineTo(x, y);
		x = 3 * unite;
		y = 5 * unite;
		path1.lineTo(x, y);
		x = 3 * unite;
		y = 4 * unite;
		path1.lineTo(x, y);
		x = 2 * unite;
		y = 4 * unite;
		path1.lineTo(x, y);
		x = 2 * unite;
		y = 3 * unite;
		path1.lineTo(x, y);
		x = 3 * unite;
		y = 3 * unite;
		path1.lineTo(x, y);
		// x =3*unite;y =2*unite;path1.lineTo(x, y);

		path1.closePath();

		Graphics2D g2d = (Graphics2D) g;

		// Activates "antialiasing" to have a good quality.
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Fills the path.
		g2d.setColor(Color.YELLOW);
		g2d.fill(path);

		// Draws the path.
		g2d.setColor(Color.RED);
		g2d.draw(path);
		// Fills the path1.
		g2d.setColor(Color.WHITE);
		g2d.fill(path1);

		// Draws the path1.
		g2d.setColor(Color.RED);
		g2d.draw(path1);

	}

	private void DrawFormS(Graphics g) {
		int panelWidth = getWidth();
		int panelHeight = getHeight();
		int boxSize = panelWidth > panelHeight ? panelHeight : panelWidth;
		double unite = (boxSize / 7);
		Path2D path = new Path2D.Double();
		double x = 1 * unite;
		double y = 1 * unite;
		path.moveTo(x, y);

		x = 4 * unite;
		y = 1 * unite;
		path.lineTo(x, y);
		x = 4 * unite;
		y = 2 * unite;
		path.lineTo(x, y);
		x = 2 * unite;
		y = 2 * unite;
		path.lineTo(x, y);
		x = 2 * unite;
		y = 3 * unite;
		path.lineTo(x, y);
		x = 4 * unite;
		y = 3 * unite;
		path.lineTo(x, y);
		x = 4 * unite;
		y = 6 * unite;
		path.lineTo(x, y);
		x = 1 * unite;
		y = 6 * unite;
		path.lineTo(x, y);
		x = 1 * unite;
		y = 5 * unite;
		path.lineTo(x, y);
		x = 3 * unite;
		y = 5 * unite;
		path.lineTo(x, y);
		x = 3 * unite;
		y = 4 * unite;
		path.lineTo(x, y);
		x = 1 * unite;
		y = 4 * unite;
		path.lineTo(x, y);

		path.closePath();

		Graphics2D g2d = (Graphics2D) g;

		// Activates "antialiasing" to have a good quality.
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Fills the path.
		g2d.setColor(Color.YELLOW);
		g2d.fill(path);

		// Draws the path.
		g2d.setColor(Color.RED);
		g2d.draw(path);

	}

	private void DrawFormL(Graphics g) {
		int panelWidth = getWidth();
		int panelHeight = getHeight();
		int boxSize = panelWidth > panelHeight ? panelHeight : panelWidth;
		double unite = (boxSize / 5);
		Path2D path = new Path2D.Double();
		double x = 1 * unite;
		double y = 1 * unite;
		path.moveTo(x, y);

		x = 2 * unite;
		y = 1 * unite;
		path.lineTo(x, y);
		x = 2 * unite;
		y = 3 * unite;
		path.lineTo(x, y);
		x = 3 * unite;
		y = 3 * unite;
		path.lineTo(x, y);
		x = 3 * unite;
		y = 4 * unite;
		path.lineTo(x, y);

		x = 1 * unite;
		y = 4 * unite;
		path.lineTo(x, y);

		path.closePath();

		Graphics2D g2d = (Graphics2D) g;

		// Activates "antialiasing" to have a good quality.
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Fills the path.
		g2d.setColor(Color.YELLOW);
		g2d.fill(path);

		// Draws the path.
		g2d.setColor(Color.RED);
		g2d.draw(path);

	}

	private void DrawFormLargeCross(Graphics g) {

		int panelWidth = getWidth();
		int panelHeight = getHeight();
		int boxSize = panelWidth > panelHeight ? panelHeight : panelWidth;
		double unite = (boxSize / 5);
		Path2D path = new Path2D.Double();
		double x = 2 * unite;
		double y = 1 * unite;
		path.moveTo(x, y);
		x = 3 * unite;
		y = 1 * unite;
		path.lineTo(x, y);
		x = 3 * unite;
		y = 2 * unite;
		path.lineTo(x, y);
		x = 4 * unite;
		y = 2 * unite;
		path.lineTo(x, y);
		x = 4 * unite;
		y = 3 * unite;
		path.lineTo(x, y);
		x = 3 * unite;
		y = 3 * unite;
		path.lineTo(x, y);
		x = 3 * unite;
		y = 4 * unite;
		path.lineTo(x, y);
		x = 2 * unite;
		y = 4 * unite;
		path.lineTo(x, y);
		x = 2 * unite;
		y = 3 * unite;
		path.lineTo(x, y);
		x = 1 * unite;
		y = 3 * unite;
		path.lineTo(x, y);
		x = 1 * unite;
		y = 2 * unite;
		path.lineTo(x, y);
		x = 2 * unite;
		y = 2 * unite;
		path.lineTo(x, y);
		x = 2 * unite;
		y = 1 * unite;
		path.lineTo(x, y);

		path.closePath();

		Graphics2D g2d = (Graphics2D) g;

		// Activates "antialiasing" to have a good quality.
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Fills the path.
		g2d.setColor(Color.YELLOW);
		g2d.fill(path);

		// Draws the path.
		g2d.setColor(Color.RED);
		g2d.draw(path);

	}

	private void DrawFormH(Graphics g) {

		int panelWidth = getWidth();
		int panelHeight = getHeight();
		int boxSize = panelWidth > panelHeight ? panelHeight : panelWidth;
		double unite = (boxSize / 10);
		// Path2D path = new Path2D.Double();
		Path2D path1 = new Path2D.Double();
		// point 1
		double x = 2 * unite;
		double y = 2 * unite;
		path1.moveTo(x, y);
		//////////////////////////////////////////////////////////////////////////////////////
		// x =2*unite; y=2*unite; path1.moveTo(x, y);

		x = 3 * unite;
		y = 2 * unite;
		path1.lineTo(x, y);
		x = 3 * unite;
		y = 4 * unite;
		path1.lineTo(x, y);
		x = 5 * unite;
		y = 4 * unite;
		path1.lineTo(x, y);
		x = 5 * unite;
		y = 2 * unite;
		path1.lineTo(x, y);
		x = 6 * unite;
		y = 2 * unite;
		path1.lineTo(x, y);
		x = 6 * unite;
		y = 7 * unite;
		path1.lineTo(x, y);
		x = 5 * unite;
		y = 7 * unite;
		path1.lineTo(x, y);
		x = 5 * unite;
		y = 5 * unite;
		path1.lineTo(x, y);
		x = 3 * unite;
		y = 5 * unite;
		path1.lineTo(x, y);
		x = 3 * unite;
		y = 7 * unite;
		path1.lineTo(x, y);
		x = 2 * unite;
		y = 7 * unite;
		path1.lineTo(x, y);

		path1.lineTo(x, y);

		path1.closePath();

		Graphics2D g2d = (Graphics2D) g;

		// Activates "antialiasing" to have a good quality.
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Fills the path1.
		g2d.setColor(Color.YELLOW);
		g2d.fill(path1);

		// Draws the path1.
		g2d.setColor(Color.RED);
		g2d.draw(path1);
		path1.closePath();

	}

}