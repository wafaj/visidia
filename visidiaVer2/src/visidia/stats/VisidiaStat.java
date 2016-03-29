package visidia.stats;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractStat.
 */
public abstract class VisidiaStat {

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		return ((o != null) && (o.getClass() == this.getClass()));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int hash;

		hash = this.getClass().hashCode();
		return hash;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.descriptionName();
	}

	/**
	 * Description name.
	 * 
	 * @return the string
	 */
	protected abstract String descriptionName();

}
