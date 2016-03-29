package visidia.examples.algo.Mazurkiewicz;

import java.util.Vector;

public class Knowledge {

	private int maxNumber=0; /* The maximum number in the set */
	private Vector[] setKnowledge     ; 
	private int myName=0; 


	public void initial(int graphS) {
		setKnowledge=new Vector[graphS+1];
		setKnowledge[0]=new Vector();
		setKnowledge[0].add(new Integer(0));
		for (int i=1;i<=graphS;i++) {
			setKnowledge[i]=null;
		}
	}

	public boolean maxSet(Vector v1,Vector v2) {
		int i,j;
		i=0;
		j=0;

		if (v2==null)
			return true;
		if (v1==null)
			return false;

		while (true) {
			if (v1.size()>i) {
				if (v2.size()<=j) 
					return true;
				else
					if (((Integer) v1.elementAt(i)).intValue()==((Integer) v2.elementAt(j)).intValue()) {
						i++;
						j++;
					}
					else
						if (((Integer) v1.elementAt(i)).intValue()>((Integer) v2.elementAt(j)).intValue()) {
							return true;
						}
						else 
							return false;

			}
			else 	    
				return false;

		}
	}

	public void changeKnowledge(Vector newVector) {  
		Vector intVector=newVector;
		int numNoeud=((Integer) newVector.elementAt(0)).intValue();

		intVector.removeElementAt(0);
		if (maxSet(intVector,setKnowledge[numNoeud])) {
			setKnowledge[numNoeud]=intVector;
		}
		if (numNoeud > maxNumber) {
			maxNumber=numNoeud;
		}
	}

	public void changeNeighbours(Vector newName) {
		int longKnow=setKnowledge[0].size();

		if ( ((Integer)newName.elementAt(0)).intValue() != ((Integer)newName.elementAt(1)).intValue() ) {
			boolean b=setKnowledge[0].remove(newName.elementAt(1));
			longKnow=setKnowledge[0].size();

			if (setKnowledge[0].isEmpty())
				setKnowledge[0].add(newName.elementAt(0));
			else {
				if (longKnow>1) {
					if (((Integer)setKnowledge[0].elementAt(0)).intValue()<((Integer)newName.elementAt(0)).intValue()) {
						setKnowledge[0].insertElementAt(newName.elementAt(0),0);
					}
					else {
						if (((Integer)setKnowledge[0].elementAt(longKnow-1)).intValue() > ((Integer)newName.elementAt(0)).intValue()) 
							setKnowledge[0].addElement(newName.elementAt(0));
						else {
							for (int i=0;i<longKnow-1;i++)
								if ((((Integer)setKnowledge[0].elementAt(i)).intValue()>((Integer)newName.elementAt(0)).intValue()) && (((Integer)setKnowledge[0].elementAt(i+1)).intValue()<=((Integer)newName.elementAt(0)).intValue())) {
									setKnowledge[0].insertElementAt(newName.elementAt(0),i+1);
									break;
								}
						}
					}
				}
				else {
					if (((Integer)setKnowledge[0].elementAt(0)).intValue()>((Integer)newName.elementAt(0)).intValue())
						setKnowledge[0].addElement(newName.elementAt(0));
					else
						setKnowledge[0].insertElementAt(newName.elementAt(0),0);
				}
			}
		} 
	}

	public Vector neighbourNode(int neighbourName) {
		return setKnowledge[neighbourName];
	}
	public Vector neighbour() { /* Returns a vector of neighbours */
		return setKnowledge[0];
	}

	public int max() { /* Returns the maximum number */
		return maxNumber;
	}

	public void changeName(int newName) {
		myName=newName;
		if (myName > maxNumber)
			maxNumber=myName;
	}

	public int myName() {
		return myName;
	}

	public boolean endKnowledge(int graphS) {
		boolean var=true;
		int k;

		for (int i=1;i<=graphS;i++) {
			if (setKnowledge[i]==null) {
				var=false;
				break;
			}
			else
				if (setKnowledge[i].contains(new Integer(0))) {
					var=false;
					break;
				}
		}

		if (var) {
			for (int i=1;i<=graphS;i++)
				for (int j=0;j<setKnowledge[i].size();j++) {
					k=((Integer)setKnowledge[i].elementAt(j)).intValue();
					if (!setKnowledge[k].contains(new Integer(i))) {
						var=false;
						break;
					}
				}

		}

		return var;
	}

}

