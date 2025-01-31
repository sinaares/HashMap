public class SSFLP <K , V> extends MotherHashMap<K , V>{

    public SSFLP(){   //Default constructor initializing the hash map with default capacity and load factor.
        super();
    }
    public SSFLP(int cap , double load){     //Constructor allowing customization of initial capacity and load factor.
        super(cap , load);
    }

    public V add(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or value cannot be null");
        }

        if (getSize() >= getLoadFactor() * table.length)  // Resizes the internal table when the load factor is exceeded
        {
            resizeTable();
        }

        int index = hash(key);  // creat a hach code from the given key
        while (table[index] != null && !table[index].key.equals(key)) {
            setCountCollision(getCountCollision() + 1);
            index = (index + 1) % table.length;
        }

        if (table[index] == null) //if the index is empty add the entry
        {
            table[index] = new Entry<>(key, value);
            setSize(getSize() + 1);
            return null;
        }
        else
        {

            V oldValue = table[index].value;
            table[index].value = value;
            return oldValue; // Entry was replaced
        }
    }

    public V remove(K key) {  // remove an entry by using search and rehash the table
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

    public V getValue(K key) {  // if the key is exsits return the value
        int index = findIndex(key);
        return (index != -1) ? table[index].value : null;
    }


    public boolean contains(K key) { // return true if thekey is exits
        return findIndex(key) != -1;
    }

    private int findIndex(K key) // find an entry by usuing a hach code and if it exists return
    {
        int index = hash(key);
        int startIndex = index;

        while (table[index] != null) {
            if (table[index].key.equals(key)) {
                return index; // Key found
            }

            index = (index + 1) % table.length;

            if (index == startIndex ) {
                break; // Reached starting point, key not found
            }
        }

        return -1; // Key not found
    }

    public int hash(K key){  // creat a hashcode by given key

        int hash = 0;
        for (int i = key.toString().length() - 1 ; i >= 0 ; i--){
            hash = hash +  key.toString().charAt(i) % table.length;
        }
        if (hash % table.length < 0){
            return (hash) + table.length;
        }
        return hash % table.length;
    }

    private void resizeTable() {  // make a new table and add all entry of old table
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

    private void rehashAfterRemoval(int removedIndex) { // rehash the table after using the remove
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
