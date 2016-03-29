package visidia.misc.colorpalette;

import java.awt.Color;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomColorPalette allows the user to generate a new color palette.
 */
public class CustomColorPalette extends ColorPalette {

	private static final long serialVersionUID = 1541305199807078631L;

	/**
	 * Instantiates a new custom color palette.
	 * 
	 * @param nbColors the number of colors
	 */
	public CustomColorPalette(int nbColors) {
		super();
		buildPalette(nbColors);
	}

	/**
	 * Builds the palette.
	 * 
	 * @param nbColors the number of colors
	 */
	private void buildPalette(int nbColors) {
		// use CIE 709 recommendation about non-linear color representation on screen:
		// gray = 0,299.red + 0,587.green + 0,114.blue
		double rho = 0.299;
		double gamma = 0.587;
		double beta = 0.114;

		int nbRed = (int) Math.round(Math.cbrt(rho*rho/beta/gamma * nbColors));
		int nbBlue = (int) Math.round(Math.cbrt(beta*beta/gamma/rho * nbColors));
		int nbGreen = (int) Math.ceil(((double)nbColors) / ((double)nbRed) / (double)nbBlue);
		nbColors = nbRed * nbGreen * nbBlue; // can generate more than the requested number of colors

		float maxValue = 1f;

		float rDelta = (nbRed > 1) ? maxValue / (float) (nbRed-1) : 0f;
		float gDelta = (nbGreen > 1) ? maxValue / (float) (nbGreen-1) : 0f;
		float bDelta = (nbBlue > 1) ? maxValue / (float) (nbBlue-1) : 0f;

		float red = (nbRed == 1) ? 0.5f : 0f;
		for (int k = 0, cpt = 0; k < nbRed; ++k) {
			float green = (nbGreen == 1) ? 0.5f : 0f;
			for (int j = 0; j < nbGreen; ++j) {
				float blue = (nbBlue == 1) ? 0.5f : 0f;
				for (int i = 0; i < nbBlue; ++i) {
					Color c = new Color(red, green, blue);
					String key = "Clr" + cpt++;
					table.put(key, c);
					blue += bDelta;
					if (blue > 1) blue = 1;
				}
				green += gDelta;
				if (green > 1) green = 1;
			}
			red += rDelta;
			if (red > 1) red = 1;
		}
	}

}
