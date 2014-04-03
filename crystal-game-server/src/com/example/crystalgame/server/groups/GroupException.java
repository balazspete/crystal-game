package com.example.crystalgame.server.groups;

/**
 * An exception related to group errors
 * @author Balazs Pete, Rajan verma
 *
 */
public class GroupException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3493252071816513060L;
	
	public static final GroupException
		LAST_MEMBER = new GroupException("Cannot remove, last member of group!"),
		CANNOT_JOIN = new GroupException("Cannot join group!"),
		NO_SUCH_GROUP = new GroupException("No such group!"),
		CANNOT_CREATE_LIMIT = new GroupException("Cannot create groups! Maximum limit reached."),
		ALREADY_IN_GROUP = new GroupException("Cannot create group, already member of another! Leave the other group first!");
	
	/**
	 * Create a group exception
	 * @param name The error message
	 */
	public GroupException(String name) {
		super(name);
	}
	
}
