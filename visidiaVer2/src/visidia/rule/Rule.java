package visidia.rule;

import java.io.Serializable;
import java.util.Collection;

import visidia.simulation.process.synchronization.SynCT;

public class Rule implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8426659041259128360L;

	protected Star befor = new Star();

	protected Star after = new Star();

	protected RuleVector forbContexts;

	protected int type = SynCT.GENERIC; // normal rule (default)

	protected boolean simpleRule;

	/**
	 * default constructor. default values are defined in class: Star, RuleVector
	 * 
	 */
	public Rule() {
		this(new Star(), new Star(), new RuleVector());
		this.type = SynCT.GENERIC;
	}

	/**
	 * a constructor a Rule without forbidden contexts.
	 */
	public Rule(Star b, Star a) {
		this(b, a, new RuleVector());
	}

	/**
	 * 
	 * @param b the star before
	 * @param a the star after
	 * @param fc a RuleVector of forbidden contexts.
	 */
	public Rule(Star b, Star a, RuleVector fc) {
		this.befor = b;
		this.after = a;
		this.forbContexts = fc;
		this.simpleRule = false;
	}

	/**
	 * return the star before
	 */
	public Star befor() {
		return this.befor;
	}

	/**
	 * return the star after.
	 */
	public Star after() {
		return this.after;
	}

	/**
	 * return the RuleVector of forbidden contexts.
	 */
	public RuleVector forbContexts() {
		return this.forbContexts;
	}

	/**
	 * this method check if the rule contains any forbidden contexts or no.
	 * 
	 * @return true if the rule contains any forbidden context, false
	 *         otherwise.
	 */
	public boolean withForbContexts() {
		return (this.forbContexts().count() > 0);
	}

	public String toString() {
		return "\n<Rule>=\n  Bef= " + this.befor.toString() + "\n" + "  Aft= "
		+ this.after.toString() + " \n  <Forbidden>"
		+ this.forbContexts.toString()
		+ "<End of Forbidden>\n<End of Rule>}";
	}

	/**
	 * sets the type of the rule. possible values are defined in class SynCT. no
	 * verification is done.
	 */
	public void setType(int t) {
		this.type = t;
	}

	/**
	 * return the type of the rule. possible types are defined in class SynCT.
	 */

	public int getType() {
		return this.type;
	}

	/**
	 * return true if the rule is simple, false otherwise.
	 */
	public boolean isSimpleRule() {
		return this.simpleRule;
	}

	/**
	 * sets the kind of rule, tue if simple, false if not simple.
	 */
	public void setSimpleRule(boolean b) {
		this.simpleRule = b;
	}

	/**
	 * return true if the rules are equals. warning: forbidden contexts are not
	 * compared. (out of model).
	 * 
	 * @param r rule.
	 */
	public boolean equals(Rule r) {
		if ((this.befor.equals(r.befor())) && (this.after.equals(r.after()))) {
			return true;
		}
		return false;
	}

	/* returns rdv, lc1, or lc2 whenever needs RDV, LC1, or LC2 */
	/**
	 * this method returns an integer RDV LC1 RDV_LC1 or LC2, indicating witch
	 * synchronization algorithms are supported by the rule.
	 * 
	 * @return RDV if only RDV is possible (resp LC1, LC2). RDV_LC1 if both
	 *         RDV_LC1 are possibles. the LC2 algorithm is supposed to be
	 *         acceptable.
	 */
	public int defaultSynchDegree() {
		boolean rdvposs = true;
		boolean lc1poss = true;
		if (this.withForbContexts() || (this.befor.arity() > 1)) {
			rdvposs = false;
		}

		Star a = (Star) this.befor.clone();
		Star b = (Star) this.after.clone();
		a.setCenterState(b.centerState());
		if (!a.containsLabels(b))// in fact it tests equality
		{
			lc1poss = false;
		}
		if (rdvposs && !lc1poss) {
			return SynCT.RDV;
		}
		if (lc1poss && !rdvposs) {
			return SynCT.LC1;
		}
		if (lc1poss && rdvposs) {
			return SynCT.RDV_LC1;
		} else {
			return SynCT.LC2;
		}
	}

	/**
	 * this method decides if the rule can be applied to the context
	 * neighborhood.
	 * 
	 * @param neighbourhood the context.
	 * @return true if rule is applicable, false if it isn't.
	 */

	public boolean isApplicableTo(Star neighbourhood) {
		// int j = 0;
		// int k=0;
		int l = 0;
		boolean boucle = true;
		Star context = new Star();
		Star s = (Star) this.befor.clone();
		if (neighbourhood.contains(s)) {
			/* if rule context is convenable */
			if (this.withForbContexts()) {
				/* testing forbidden contexts */
				RuleVector v = new RuleVector(this.forbContexts());
				boucle = true;
				l = 0;
				Star n = new Star();
				while (boucle && (l < v.count())) {
					context = (Star) ((Star) v.elementAt(l)).clone();
					n = (Star) neighbourhood.clone();

					if (n.contains((Star) context.clone())) {
						// System.out.println("rejet");
						boucle = false;
						l++;
					} else {
						l++;
					}
				}
				if (boucle) {
					/* if ok */
					// System.out.println(" rule "+j+" Accepted after
					// examination of forbidden contexts");
					return true;
				} else {
					/* not ok -> try next rule */
					// System.out.println(" rule"+j+"refused because of forb
					// context N "+ k );
					return false;
				}
			}
			/* if there is no forbidden contexts */
			else {
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * return the inverse of a simple rule. example: A-N --> U-V {fc} becomes
	 * N-A --> V-U {fc}
	 */
	public Rule inverseSimpleRule() {
		Star b;
		Star a;
		Neighbor nb = this.befor.neighbor(0);
		Neighbor na = this.after.neighbor(0);
		b = new Star(nb.state(), 1);
		b.setState(0, new Neighbor(this.befor().centerState(), nb.mark(), nb.doorNum()));
		a = new Star(na.state(), 1);
		a.setState(0, new Neighbor(this.after().centerState(), na.mark(), na.doorNum()));
		Rule r = new Rule(b, a, (RuleVector) this.forbContexts.clone());
		r.setSimpleRule(this.isSimpleRule());
		return r;
	}

	/**
	 * clones the rule.
	 */
	public Object clone() {
		Rule r = new Rule();
		r.befor = (Star) this.befor.clone();
		r.after = (Star) this.after.clone();
		r.forbContexts = new RuleVector(this.forbContexts.size());
		r.forbContexts.addAll((Collection) this.forbContexts.clone());
		r.type = this.type;
		r.setSimpleRule(this.isSimpleRule());
		return r;
	}

}
