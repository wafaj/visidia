package generateGML;

public class Edge implements Comparable<Edge> {
	private Node src;
	private Node tgt;
	private  Double value;
	public Edge(Node src, Node tgt) {
		this.src=src;
		this.tgt=tgt;
		this.value=(src.getPosition()).distance(tgt.getPosition());
	}
	public Node getSrc() {
		return src;
	}
	public void setSrc(Node src) {
		this.src = src;
	}
	public Node getTgt() {
		return tgt;
	}
	public void setTgt(Node tgt) {
		this.tgt = tgt;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public int compareTo(Edge v) {
		return ((this.getValue()).compareTo(v.getValue()));
	}
	

}
