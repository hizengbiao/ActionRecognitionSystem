package data;

public class VideoConfidence {
	private int VideoType;
	private int id;
	private float confidence;

	public VideoConfidence() {
		// TODO Auto-generated constructor stub
		VideoType = -1;
		this.confidence = 0;
	}

	public int getVideoType() {
		return VideoType;
	}

	public void setVideoType(int videoType) {
		VideoType = videoType;
	}

	public float getConfidence() {
		return confidence;
	}

	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
