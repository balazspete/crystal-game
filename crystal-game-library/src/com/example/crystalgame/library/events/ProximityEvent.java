/**
 * 
 */
package com.example.crystalgame.library.events;

import com.example.crystalgame.library.data.Artifact;

/**
 * Proximity Event class to signify the object 
 * that has raised a proximity alert
 * 
 * @author Allen Thomas Varghese, Rajan Verma
 */
public class ProximityEvent {

	private ArtifactType type;
	private Artifact artifact;
	
	public enum ArtifactType {
			CHARACTER 
		,	CRYSTAL
		,	MAGICAL_ITEM
	}
	
	/**
	 * 
	 */
	public ProximityEvent(ArtifactType type, Artifact artifact) {
		this.type = type;
		this.artifact = artifact;
	}

}
