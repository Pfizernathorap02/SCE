package com.pfizer.sce.beans; 

public class ScoringSystem {

	private Integer scoreId;
	private String scoringSystemIdentifier;
	private String scoreValue;
	private String scoreLegend;

	public Integer getScoreId() {
		return scoreId;
	}

	public void setScoreId(Integer scoreId) {
		this.scoreId = scoreId;
	}

	public String getScoringSystemIdentifier() {
		return scoringSystemIdentifier;
	}

	public void setScoringSystemIdentifier(String scoringSystemIdentifier) {
		this.scoringSystemIdentifier = scoringSystemIdentifier;
	}

	public String getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(String scoreValue) {
		this.scoreValue = scoreValue;
	}

	public String getScoreLegend() {
		return scoreLegend;
	}

	public void setScoreLegend(String scoreLegend) {
		this.scoreLegend = scoreLegend;
	}

}
