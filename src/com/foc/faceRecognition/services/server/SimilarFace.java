package com.foc.faceRecognition.services.server;

public class SimilarFace {
	private long   ref      = 0;
	private double distance = 0;
	
	public SimilarFace(long ref, double distance) {
		this.ref      = ref;
		this.distance = distance;
	}

	public long getRef() {
		return ref;
	}

	public double getDistance() {
		return distance;
	}
}
