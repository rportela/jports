package jports.data;

/**
 * This enumeration defines the types of columns that the system can handle;
 * 
 * @author rportela
 *
 */
public enum ColumnType {

	/**
	 * The regular, sometimes indexed column.
	 */
	REGULAR,

	/**
	 * The column that increments itself on the database side, or key that's
	 * generated in the server side.
	 */
	IDENTITY,

	/**
	 * A column that has it's value set on the client side but should be unique. Can
	 * be an e-mail or a tax payer id. It is used on saving operations and
	 * determines if a record should be updated or inserted;
	 */
	UNIQUE,

	/**
	 * A column that's a part of a composite key. Using the same principles as the
	 * unique column, it also has it's value set on the client side and along with
	 * other columns is used on saving operations to determine if a record should be
	 * updated or inserted;
	 */
	COMPOSITE_KEY
}
