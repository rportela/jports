package jports.data;

/**
 * Something that can store objects of a specific kind;
 * 
 * @author rportela
 *
 * @param <T>
 */
public interface Storage<T> {

	public void save(T entity);

	public void insert(T entity);

	public void delete(T entity);

	public void update(T entity);

	public Select<T> select();
}
