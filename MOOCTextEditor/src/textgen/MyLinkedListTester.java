/**
 * 
 */
package textgen;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

/**
 * @author UC San Diego MOOC team
 *
 */
public class MyLinkedListTester {

	private static final int LONG_LIST_LENGTH =10; 

	MyLinkedList<String> shortList;
	MyLinkedList<Integer> emptyList;
	MyLinkedList<Integer> longerList;
	MyLinkedList<Integer> list1;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Feel free to use these lists, or add your own
	    shortList = new MyLinkedList<String>();
		shortList.add("A");
		shortList.add("B");
		emptyList = new MyLinkedList<Integer>();
		longerList = new MyLinkedList<Integer>();
		for (int i = 0; i < LONG_LIST_LENGTH; i++)
		{
			longerList.add(i);
		}
		list1 = new MyLinkedList<Integer>();
		list1.add(65);
		list1.add(21);
		list1.add(42);
		
	}

	
	/** Test if the get method is working correctly.
	 */
	/*You should not need to add much to this method.
	 * We provide it as an example of a thorough test. */
	@Test
	public void testGet()
	{
		//test empty list, get should throw an exception
		try {
			emptyList.get(0);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
			
		}
		
		// test short list, first contents, then out of bounds
		assertEquals("Check first", "A", shortList.get(0));
		assertEquals("Check second", "B", shortList.get(1));
		
		try {
			shortList.get(-1);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		try {
			shortList.get(2);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		// test longer list contents
		for(int i = 0; i<LONG_LIST_LENGTH; i++ ) {
			assertEquals("Check "+i+ " element", (Integer)i, longerList.get(i));
		}
		
		// test off the end of the longer array
		try {
			longerList.get(-1); // when calling get(-1), it should throw an exception
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		try {
			// the value here is 10, which is out of bounds because the last index is 9
			longerList.get(LONG_LIST_LENGTH); 
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		}
		
	}
	
	
	/** Test removing an element from the list.
	 * We've included the example from the concept challenge.
	 * You will want to add more tests.  */
	@Test
	public void testRemove()
	{
		int a = list1.remove(0);
		assertEquals("Remove: check a is correct ", 65, a);
		assertEquals("Remove: check element 0 is correct ", (Integer)21, list1.get(0));
		assertEquals("Remove: check size is correct ", 2, list1.size());

		
	}
	
	/** Test adding an element into the end of the list, specifically
	 *  public boolean add(E element)
	 * */
	@Test
	public void testAddEnd()
	{
        // test implementation
		boolean a = list1.add(100);
		assertEquals("Add: check a is correct ", true, a);
		assertEquals("Add: check element 3 is correct ", (Integer)100, list1.get(3));
		assertEquals("Add: check size is correct ", 4, list1.size());
		
		boolean b = list1.add(200);
		assertEquals("Add: check b is correct ", true, b);
		assertEquals("Add: check element 4 is correct ", (Integer)200, list1.get(4));
		assertEquals("Add: check size is correct ", 5, list1.size());
		
	}

	
	/** Test the size of the list */
	@Test
	public void testSize()
	{
		// test implementation
		int a = list1.size();
		assertEquals("Size: check a is correct ", 3, a);
		assertEquals("Size: check size is correct ", 3, list1.size());

	}

	
	
	/** Test adding an element into the list at a specified index,
	 * specifically:
	 * public void add(int index, E element)
	 * */
	@Test
	public void testAddAtIndex()
	{
        // test implementation
		list1.add(0, 100);
		list1.add(1, 200);

		assertEquals("AddAtIndex: check element 0 is correct ", (Integer)100, list1.get(0));
		assertEquals("AddAtIndex: check element 1 is correct ", (Integer)200, list1.get(1));
		assertEquals("AddAtIndex: check element 2 is correct ", (Integer)65, list1.get(2));
		
	}
	
	/** Test setting an element in the list */
	@Test
	public void testSet()
	{
	    // test implementation
		list1.set(2, 100);

		assertEquals("Set: check element 2 is correct ", (Integer)100, list1.get(2));
		assertEquals("Set: check size is correct ", 3, list1.size());

	}
	
	
	// TODO: Optionally add more test methods.

	@Test
	public void testToString() {
		String a = list1.toString();
		assertEquals("ToString: check a is correct ", "[65, 21, 42]", a);
		assertEquals("ToString: check size is correct ", 3, list1.size());
	}
	
}
