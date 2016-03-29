package visidia.misc.colorpalette;

import java.awt.Color;

// TODO: Auto-generated Javadoc
/**
 * StandardColorPalette is the default color palette in ViSiDiA. It mainly contains the information to associate colors to graph vertices.
 */
public class NewCustomColorPalette extends ColorPalette {

	private static final long serialVersionUID = 7078366028118723337L;

	/**
	 * Instantiates a new color palette.
	 */
	public NewCustomColorPalette() {
		super();
		buildPalette();
	}

	/**
	 * Builds the palette.
	 */
	private void buildPalette() {
		table.put("A", Color.red);
		table.put("B", new Color(239, 48, 178));
		table.put("C", new Color(188, 123, 234));
		table.put("D", new Color(123, 154, 234));
		table.put("E", Color.yellow);
		table.put("F", Color.blue);
		table.put("G", new Color(217, 60, 115));
		table.put("H", new Color(117, 35, 211));
		table.put("I", new Color(34, 115, 58));
		table.put("J", Color.magenta);
		table.put("K", new Color(170, 206, 237));
		table.put("L", new Color(215, 237, 92));
		table.put("M", new Color(255, 113, 153));
		table.put("N", Color.green);
		table.put("O", new Color(161, 113, 255));
		table.put("P", Color.cyan);
		table.put("Q", Color.white);
		table.put("R", new Color(231, 211, 22));
		table.put("S", Color.gray);
		table.put("T", new Color(255, 0, 140));
		table.put("U", new Color(174, 23, 104));
		table.put("V", new Color(138, 153, 207));
		table.put("W", new Color(108, 155, 159));
		table.put("X", new Color(164, 27, 120));
		table.put("Y", new Color(98, 194, 239));
		table.put("Z", new Color(145, 239, 98));
		table.put("Switched off", Color.lightGray);
	}

	public void setColor(String key, Color color){
		if(table.containsKey(key))
			table.put(key,color);
	}
}
