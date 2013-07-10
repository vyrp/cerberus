package br.itabits.cerberus.util;

import java.util.ArrayList;
import java.util.Date;

/**
 * The transaction model.
 * 
 * @author Croata
 */
public class Transaction {

	/* * Fields * */

	private String start;
	private String name;
	private String end;

	/* * Constructor * */

	/**
	 * Constructs a transaction. Dates as date strings.
	 * 
	 * @param start
	 *            The date of the borrowing
	 * @param name
	 *            The name of the borrower
	 * @param end
	 *            The returning date
	 */
	public Transaction(String start, String name, String end) {
		this.start = start;
		this.name = name;
		this.end = end;
	}

	/* * Getter and Setters * */

	/**
	 * Gets the start date (the date of the borrowing)
	 * 
	 * @return The start date as a date string
	 */
	public String getStart() {
		return start;
	}

	/**
	 * Gets the name of the borrower.
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the end date (the returning date).
	 * 
	 * @return The end date
	 */
	public String getEnd() {
		return end;
	}

	/**
	 * Constructs a list of Transactions from a serialized string.<br>
	 * In the serialized string the transactions should be separated by <b>|</b> and the fields in each transaction
	 * should be separated by <b>;</b>.
	 * 
	 * @param str
	 *            The serialized string
	 * @return The list of Transaction's
	 */
	public static ArrayList<Transaction> fromString(String str) {
		ArrayList<Transaction> result = new ArrayList<Transaction>();

		String[] items = str.split("\\|");
		for (String item : items) {
			String[] fields = item.split(";");
			if (fields.length != 3)
				continue;

			Long start = null;
			Long end = null;

			try {
				start = Long.parseLong(fields[0]);
			} catch (NumberFormatException e) {
			}

			try {
				end = Long.parseLong(fields[2]);
			} catch (NumberFormatException e) {
			}

			result.add(new Transaction(toDate(start), fields[1], toDate(end)));
		}

		return result;
	}

	private static String toDate(Long timestamp) {
		return (timestamp == null || timestamp < 0) ? "-" : new Date(timestamp).toString();
	}
}
