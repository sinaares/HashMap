public class SSFDH<K, V> extends MotherHashMap<K, V> {
    public SSFDH() {   //Constructor allowing customization of initial capacity and load factor.
        super();
    }

    public SSFDH(int initialCapacity, double load) { //Default constructor initializing the hash map with default capacity and load factor.
        super(initialCapacity, load);
    }

    public V add(K key, V value) { // add entry by using the hash function and the collisions
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or value cannot be null");
        }

        if (getSize() >= getLoadFactor() * table.length) {  // resize the table when the size gets to the loadfactor * length
            resizeTable();
        }

        int index = hash(key);
        int stepSize = hash2(key);
        int i = 0;

        while (table[index] != null && !table[index].key.equals(key)) { //checking the collisions
            setCountCollision(getCountCollision() + 1);
            index = (hash(key) + (i * hash2(key))) % table.length;
            i++;
        }

        if (table[index] == null) {
            table[index] = new Entry<>(key, value);
            setSize(getSize() + 1);
            return null; // Entry was added
        } else {
            V oldValue = table[index].value;
            table[index].value = value;
            return oldValue; // Entry was replaced
        }
    }

    public V remove(K key) {  // removng the index by getting the hashcode
        int index = findIndex(key);
        if (index == -1) {
            return null; // Key not found
        }

        V removedValue = table[index].value;
        table[index] = null;
        setSize(getSize() - 1);

        // Rehash to fix any subsequent entries affected by the removal
        rehashAfterRemoval(index);

        return removedValue;
    }

    public V getValue(K key) { // if the value is exsits in the table returns the value
        int index = findIndex(key);
        return (index != -1) ? table[index].value : null;
    }

    public boolean contains(K key) { // if the key exsits in the table returns true
        return findIndex(key) != -1;
    }

    private int findIndex(K key) { // finding the the key if its exits in the table and if its not return -1
        int index = hash(key);
        int stepSize = hash2(key);
        int startIndex = index;
        int iterations = 0;
        int i = 0;

        while (table[index] != null) {
            if (table[index].key.equals(key)) {
                return index; // Key found
            }

            index = (hash(key) + (i * stepSize)) % table.length;
            i++;

            if (index == startIndex || iterations > table.length) {
                break; // Reached starting point or too many iterations, key not found
            }

            iterations++;
        }

        return -1; // Key not found
    }

    public int hash(K key) {  // getting hash code using char
        int hash = 0;
        for (int i = 0; i < key.toString().length(); i++) {
            hash = (hash * 31 + key.toString().charAt(i)) % table.length;
        }
        return hash;
    }

    private int hash2(K key) { // second hashcode for double hashcode
        int sum = 0;

        int mode = findPrimeLessThan(table.length / 2);
        for (int i = key.toString().length() - 1; i >= 0; i--) {
            sum = (sum * + key.toString().charAt(i)) % table.length;
        }
        int hash2 = mode - (sum % mode);
        if (hash2 < 0) {
            hash2 = hash2 + 31;
        }
        return hash2;
    }

    private static int findPrimeLessThan(int number) { //finding the prime number less than the given number
        for (int i = number - 1; i > 1; i--) {
            if (isPrime(i)) {
                return i;
            }
        }
        return -1; // No prime number found
    }

    private void resizeTable() {  //add the each entry to another table whith mucher length
        Entry<K, V>[] oldTable = table;
        int newSize = getNextPrime(2 * oldTable.length);
        table = new Entry[newSize];
        setSize(0);

        for (Entry<K, V> entry : oldTable) {
            if (entry != null) {
                add(entry.key, entry.value);
            }
        }
    }

    private void rehashAfterRemoval(int removedIndex) {  // rehash the table after removing an item
        int currentIndex = (removedIndex + 1) % table.length;

        while (table[currentIndex] != null) {
            Entry<K, V> entryToRehash = table[currentIndex];
            table[currentIndex] = null;
            setSize(getSize() - 1);

            // Re-add the entry to the table
            add(entryToRehash.key, entryToRehash.value);

            currentIndex = (currentIndex + 1) % table.length;
        }
    }
}
