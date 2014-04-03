package com.example.crystalgame.library.events;

import com.example.crystalgame.library.data.states.State;

public class StateChangeEvent extends Event {

	private State<?, ?> previous, current;
	
	public StateChangeEvent(State<?, ?> previous, State<?, ?> current) {
		this.previous = previous;
		this.current = current;
	}

	public State<?, ?> getPrevious() {
		return previous;
	}

	public State<?, ?> getCurrent() {
		return current;
	}
	
}
