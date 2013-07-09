package br.itabits.cerberus.network;
/**
 * link with the receiver of NetworkManager
 * @author Marcelo
 */
public interface NetworkResponse {
	/**
	 * called in every network connection change 
	 * @param active exists a active connection if true
	 */
	public void onChangesDetected(boolean active);
}
