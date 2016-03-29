package visidia.io.gml.parser;

public class GMLEdge {

	private Integer sourceId;

	private Integer targetId;
	
	private String label="";
	
	private Double weight=1.0;

	public void setSourceId(Integer srcId) {
		this.sourceId = srcId;
	}

	public Integer getSourceId() {
		return this.sourceId;
	}

	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}

	public Integer getTargetId() {
		return this.targetId;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Double getWeight() {
		return this.weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

}
