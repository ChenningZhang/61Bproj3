/* HashTableChained.java */

package dict;

import list.*;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

  private int size;
  private DList[] table;
  private int buckets;
  private int collisions;

  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/

  public HashTableChained(int sizeEstimate) {
    // Your solution here.
  	size = 0;
  	buckets = findPrime((sizeEstimate*100) / 77);
  	if (((sizeEstimate*1.0) / buckets) > 1.0) {
  		buckets = (sizeEstimate*20) / 17;
  	}
  	table = new DList[buckets];
  	for (int i = 0; i < buckets; i++) {
  		table[i] = new DList();
  	}
  }

  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
    // Your solution here.
  	size = 0;
  	buckets = 101;
  	table = new DList[buckets];
  	for (int i = 0; i < buckets; i++) {
  		table[i] = new DList();
  	}
  }
  
  //Helper functions for obtaining primes.
  
  private boolean isPrime(int n) {
	  if (n < 2) {
		  return false;
	  }
	  for (int div = 2; (div*div <= n); div++) {
		  if (n % div == 0) {
			  return false;
		  }
	  }
	  return true;
  }
  
  /**
   * Finds a prime number somewhat close to x.
   * @param x the integer of interest
   * @return either a prime number in the range of x, or (x*101)/100
   */
  private int findPrime(int x) {
	  if (isPrime(x)) {
		  return x;
	  } else {
		  int range = x / 10;
		  for (int i = 1; i <= range; i++) {
			  if (isPrime(x+i)) {
				  return x+i;
			  } else if (isPrime(x-i)) {
				  return x-i;
			  }
		  }
		  return (x*101)/100;
	  }
  }

  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  int compFunction(int code) {
  	int a = 6,b = 29;
  	long c = (a*code) + b;
  	int p = findPrime(buckets*50000);
    int result = (int) ((c % p) % buckets);
    if (result < 0) {
    	result = result + buckets;
    }
    return result;
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    return size;
  }
  
  /**
   * Returns the number of buckets in this hash table.
   */
  
  private int buckets() {
	  return buckets;
  }
  
  /**
   * Returns the number of collisions in this hash table so far.
   * @return number of collisions
   */
  
  private int collisionCount() {
	  return collisions;
  }

  /**
  * Returns the load factor of this hash table.
  */
  
  private double loadFactor() {
	  return (size*1.0)/ buckets;
  }
  
  /**
   * Prints out a histogram of the Hash Table. X's represent entries.
   */
  
  public void histogram() {
	  String chart;
	  System.out.println("Entry Distribution: ");
	  for (int i = 0; i < table.length; i++) {
		  ListNode current = table[i].front();
		  chart = "hash index " + i + ": ";
		  while (current.isValidNode()) {
			  chart = chart + "X ";
			  try {
				  current = current.next();
			  } catch(InvalidNodeException e) {
				  System.out.println("That shouldn't happen.");
			  }
		  }
		  System.out.println(chart);
	  }
  }

  /**
   * Resizes the hash table by doubling and re-hashing every entry.
   * New number of buckets is not quite 2*N, but the closest prime, 
   * calculated with the above findPrime helper method.
   */
  
  private void resize() {
	  int doubled = findPrime(2*buckets);
    DList[] newTable = new DList[doubled];
    for (int i = 0; i < doubled; i++) {
      newTable[i] = new DList();
    }
    buckets = doubled;
    for (DList l : table) {
      if (l.size() != 0) {
        ListNode current = l.front();
        while (current.isValidNode()) {
          try {
            Entry x = (Entry) current.item();
            int comp2 = compFunction(x.key().hashCode());
            newTable[comp2].insertBack(x);
            current = current.next();
          } catch(InvalidNodeException e) {
            System.out.println("That shouldn't happen.");
          }
        }
      }
    }
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  public boolean isEmpty() {
    return (size == 0);
  }

  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  public Entry insert(Object key, Object value) {
  	int comped = compFunction(key.hashCode());
  	Entry temp = new Entry();
  	temp.key = key;
  	temp.value = value;
  	DList bucket = table[comped];
  	if (bucket.length() != 0) {
  		collisions++;
  	}
  	bucket.insertBack(temp);
  	size++;
    if (loadFactor() > 1.0) {
      resize();
    }
    return temp;
  }

  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/

  public Entry find(Object key) {
  	int comped = compFunction(key.hashCode());
  	DList bucket = table[comped];
  	ListNode current = bucket.front();
  	while (current.isValidNode()) {
  		try {
  			if (((Entry) current.item()).key.equals(key)) {
  				return (Entry) current.item();
  			} else {
  				current = current.next();
  			}
  		} catch(InvalidNodeException e) {
  			System.out.println("That shouldn't happen.");
  		}
  	}
    return null;
  }

  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */

  public Entry remove(Object key) {
  	int comped = compFunction(key.hashCode());
  	DList bucket = table[comped];
  	ListNode current = bucket.front();
  	while (current.isValidNode()) {
  		try {
  			Entry temp = (Entry) current.item();
  			if (temp.key.equals(key)) { 
  				current.remove();
  				size--;
  				return temp;
  			} else {
  				current = current.next();
  			}
  		} catch(InvalidNodeException e) {
  			System.out.println("That shouldn't happen.");
  		}
  	}
  	return null;
  }

  /**
   *  Remove all entries from the dictionary.
   */
  public void makeEmpty() {
  	size = 0;
  	table = new DList[buckets];
  	for (int i = 0; i < buckets; i++) {
  		table[i] = new DList();
  	}
  }

}
