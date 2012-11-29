package ru.rostpro.exceptions;

public class MException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1518435700732383116L;
	private static final String TAG = MException.class.getName();
	
	
	public MException(){
		super(String.format("Error [%s]", TAG));
	}
	public MException(String message){
		super(String.format("Error [%s] by reason of : [%s]", TAG, message));
	}
	public MException(Exception e){
		super(e);
	}
	public MException(String msg, Exception  e){
		super(msg, e);
	}
	

}
