package org.coursera.capstone.client.core.entities;


public class ImageStatus {

	public enum ImageState {
		READY, PROCESSING
	}

	private ImageState state;

	public ImageStatus(ImageState state) {
		super();
		this.state = state;
	}

	public ImageState getState() {
		return state;
	}

	public void setState(ImageState state) {
		this.state = state;
	}
}