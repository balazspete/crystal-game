/**
 * 
 */
package com.example.crystalgame.library.events;

import com.example.crystalgame.library.data.Artifact;
import com.example.crystalgame.library.data.Artifact.ArtifactType;

/**
 * Proximity Event class to signify the object 
 * that has raised a proximity alert
 * 
 * @author Allen Thomas Varghese, Rajan Verma
 */
public class ProximityEvent {

	private ArtifactType type;
	private Artifact artifact;
	
	/**
	 * 
	 */
	public ProximityEvent(ArtifactType type, Artifact artifact) {
		this.type = type;
		this.artifact = artifact;
	}

}
