package com.example.crystalgame.library.data.states;

public abstract class State<SUBJECT, VALUE> {

	public final SUBJECT subject;
	public final VALUE value;
	public final StateType type;
	
	public State(StateType type, SUBJECT subject, VALUE value) {
		this.subject = subject;
		this.value = value;
		this.type = type;
	}
	
}
