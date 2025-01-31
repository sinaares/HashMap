public class MotherHashMap <K , V>  implements HashMapInterface<K , V>
{

    private static final int DEFAULT_CAPACITY = 401;
    private static double LOAD_FACTOR ;

    protected Entry<K, V>[] table;
    private Long CountCollision;

    private int size;
    public MotherHashMap() // constructer when user dont enter any cap and load factor

    {
        this(DEFAULT_CAPACITY ,0.8);
    }
    public MotherHashMap(int initialCapacity , double load) // cunstructor when user enter cap and load factor

    {


        if(initialCapacity < DEFAULT_CAPACITY){
            initialCapacity = DEFAULT_CAPACITY;
        }

        if(!isPrime(initialCapacity)){
            initialCapacity = getNextPrime(initialCapacity);
        }


        table = new Entry[initialCapacity];
        size = 0;
        CountCollision = 0L;
        LOAD_FACTOR = load;
    }
    public int getTablelenghth()
    {
        return this.table.length;
    }
    public void setLoadFactor(double loadFactor)
    {
        LOAD_FACTOR = loadFactor;
    }
    public double getLoadFactor()
    {
        return LOAD_FACTOR;
    }


    public static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) { //entry constructor
            this.key = key;
            this.value = value;
            this.next = null;
        }

        // Getter for 'next'
        public Entry<K, V> getNext() {
            return next;
        }
    }

    @Override
    public V add(K key, V value) { //add the entry to the table by using the hach code and the collisions
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or value cannot be null");
        }

        if (size >= LOAD_FACTOR * table.length)
        {
            resizeTable();
        }

        int index = hash(key);
        while (table[index] != null && !table[index].key.equals(key)) {
            CountCollision++;
            index = (index + 1) % table.length;
        }

        if (table[index] == null)
        {
            table[index] = new Entry<>(key, value);
            size++;
            return null;
        }
        else
        {

            V oldValue = table[index].value;
            table[index].value = value;
            return oldValue; // Entry was replaced
        }
    }


    @Override
    public V remove(K key) {  // remove an entry from the table and rehash after removing
        int index = findIndex(key);
        if (index == -1) {
            return null; // Key not found
        }

        V removedValue = table[index].value;
        table[index] = null;
        size--;

        // Rehash to fix any subsequent entries affected by the removal
        rehashAfterRemoval(index);

        return removedValue;
    }

    @Override
    public V getValue(K key)
    {
        int index = findIndex(key);
        return (index != -1) ? table[index].value : null;
    }

    @Override
    public boolean contains(K key)

    {
        return findIndex(key) != -1;
    }

    @Override
    public boolean isEmpty()

    {
        return size == 0;
    }

    @Override
    public int getSize()

    {
        return size;
    }

    @Override
    public void clear()// clear the all entries from the table

    {
        size = 0;

        for (int i = 0; i < table.length; i++)
        {
            table[i] = null;
        }
    }

    private int findIndex(K key)    // search the the key int table by using hash code if the key exsits return index

    {
        int index = hash(key);
        int startIndex = index;

        while (table[index] != null) {
            if (table[index].key.equals(key)) {
                return index; // Key found
            }

            index = (index + 1) % table.length;

            if (index == startIndex) {
                break; // Reached starting point, key not found
            }
        }

        return -1; // Key not found
    }

    public int hash(K key){ // get a hashcode by getting the key from user

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




    private void resizeTable() {  // resize the table and add all entry from old table and add them to the new
        Entry<K, V>[] oldTable = table;
        int newSize = getNextPrime(2 * oldTable.length);
        table = new Entry[newSize];
        size = 0;

        for (Entry<K, V> entry : oldTable) {
            if (entry != null) {
                add(entry.key, entry.value);
            }
        }
    }

    private void rehashAfterRemoval(int removedIndex) {  // rehash the table after removing the entry
        int currentIndex = (removedIndex + 1) % table.length;

        while (table[currentIndex] != null) {
            Entry<K, V> entryToRehash = table[currentIndex];
            table[currentIndex] = null;
            size--;

            // Re-add the entry to the table
            add(entryToRehash.key, entryToRehash.value);

            currentIndex = (currentIndex + 1) % table.length;
        }
    }

    public static int getNextPrime(int n) {  // getting a prime number biger than the length
        while (!isPrime(n)) {
            n++;
        }
        return n;
    }

    static boolean isPrime(int n) {  // checking a number is prime or not
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public long getCountCollision()
    {
        return CountCollision;
    }
    public void print()  // print the all entry in the table
    {
        System.out.println("Hash Dictionary Contents:");
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                System.out.println(table[i].value.toString());
            }
        }
        System.out.println("Size: " + size);
    }

    public void setCountCollision(long count){
        this.CountCollision = count;
    }

    public void setSize(int size){
        this.size = size;
    }


    public double GetloadFactor(){
        return LOAD_FACTOR;
    }

}
