package visidia.gui.window.rule;

import java.awt.Color;

/**
 * Stores margins and color data for drawing star rules.
 */
public final class RuleConstants {

	public static Color ruleColor = new Color(225, 225, 185);

	public static Color contextColor = new Color(187, 225, 185);

	public static int ray = 100;

	public static int rule_top = 60;

	public static int rule_left = 40;

	public static int rule_right = 40;

	public static int rule_bottom = 35;

	public static int rule_center = 110;

	public static int arrow_x1 = RuleConstants.ray * 2 + RuleConstants.rule_left + RuleConstants.rule_center / 4;

	public static int arrow_x2 = RuleConstants.ray * 2 + RuleConstants.rule_left + 3 * RuleConstants.rule_center / 4;

	public static int arrow_length = RuleConstants.rule_center / 2;

	public static int ctxt_top = 35;

	public static int ctxt_left = 45;

	public static int ctxt_right = 45;

	public static int ctxt_bottom = 35;

}
