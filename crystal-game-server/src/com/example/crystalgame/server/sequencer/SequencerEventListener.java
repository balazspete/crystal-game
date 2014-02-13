package com.example.crystalgame.server.sequencer;

import java.util.EventListener;

public abstract class SequencerEventListener implements EventListener {

	public abstract void sequencerEvent(SequencerEvent event);
	
}
