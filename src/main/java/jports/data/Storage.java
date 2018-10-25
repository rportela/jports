package jports.data;

/**
 * Something that can store objects of a specific kind;
 * 
 * @author rportela
 *
 * @param <T>
 */
public interface Storage<T> {

	public int save(T entity);

	public int insert(T entity);

	public int delete(T entity);

	public int update(T entity);

	public Select<T> select();
}
