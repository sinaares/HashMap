public class PAFDH<K , V> extends MotherHashMap<K , V> {

    public PAFDH(){          //Default constructor initializing the hash map with default capacity and load factor.
        super();
    }
    public PAFDH(int cap , double load){  //Constructor allowing customization of initial capacity and load factor.
        super(cap , load);
    }

    @Override
    public V add(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or value cannot be null");
        }

        if (getSize() >= getLoadFactor() * table.length) {  // Resizes the internal table when the load factor is exceeded
            resizeTable();
        }

        int index = hash(key);
        int i = 0;
        int startIndex = index;

        while (table[index] != null && !table[index].key.equals(key)) {
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

    private void resizeTable() { //make a new table and add each element from the old table to the new one
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

    public int hash(K key){  //Computes the hash code for a given key using a custom hashing algorithm.

        int hash = 0;
        int prime = 31;
        for (int i = key.toString().length() - 1 ; i >= 0 ; i--){
            hash = hash +  key.toString().charAt(i) * prime;
            prime = prime * prime;
        }
        if (hash % table.length < 0){
            return (hash % table.length) + table.length;
        }
        return hash % table.length;
    }
    private int hash2(K key) {  //Computes the second hash code for double hashing using a custom algorithm.

        int sum = 0;
        int prime = 31;
        int mode = findPrimeLessThan(table.length / 2);
        for (int i = key.toString().length() - 1 ; i >= 0 ; i--){
            sum =( sum +  key.toString().charAt(i) * prime ) % mode;
            prime = prime * prime;
        }
        int hash2 = mode - (sum % mode);
        if (hash2 < 0){
            hash2 = hash2 + mode;
        }
        return hash2;
    }


    public V remove(K key) { //Removes a key-value pair from the hash map based on the provided key.
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


    public V getValue(K key) {  //Retrieves the value associated with a given key in the hash map.

        int index = findIndex(key);
        return (index != -1) ? table[index].value : null;
    }


    public boolean contains(K key) { //Checks if the hash map contains a specific key.

        return findIndex(key) != -1;
    }

    private int findIndex(K key) {  // search the index by using hash functions
        int index = hash(key);
        int stepSize = hash2(key);
        int startIndex = index;
        int iterations = 0;

        while (table[index] != null) {
            if (table[index].key.equals(key)) {
                return index; // Key found
            }

            index = (index + stepSize) % table.length;

            if (index == startIndex || iterations >= table.length) {
                break; // Reached starting point or too many iterations, key not found
            }

            iterations++;
        }

        return -1; // Key not found
    }
    private void rehashAfterRemoval(int removedIndex) {  // when an entry removed reshash the table
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

    private static int findPrimeLessThan(int number) { // find a prime number less than the given length
        for (int i = number - 1; i > 1; i--) {
            if (isPrime(i)) {
                return i;
            }
        }
        return -1; // No prime number found
    }
}
