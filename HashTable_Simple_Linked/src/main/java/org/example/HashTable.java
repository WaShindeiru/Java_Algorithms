package org.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HashTable <T, V> {
    class Record<T, V>{
        private T key;
        private V value;
        private int hash;

        public T getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public int getHash() {
            return hash;
        }

        public Record(T key, V value){
            this.key = key;
            this.value = value;
            this.hash = key.hashCode();
        }

        public boolean equals(Record<T, V> other){
            if(other.hashCode() != hash) return false;
            return key.equals(other.key);
        }

        @Override
        public String toString() {
            StringBuilder text = new StringBuilder();
            text.append(key).append(" -> ").append(value);
            return text.toString();
        }
    }

    private static final int DEFAULT_CAPACITY = 5;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private List<LinkedList<Record<T, V>>> buckets;
    private int capacity;
    private int size;
    private int threshold;
    private double maxLoadFactor;

    public HashTable(){
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashTable(int capacity, double maxLoadFactor){
        this.capacity = capacity;
        this.maxLoadFactor = maxLoadFactor;
        this.threshold = (int) (maxLoadFactor*capacity);

        buckets = new ArrayList<>(capacity);
        for(int i=0; i<capacity; i++){
            buckets.add(new LinkedList<>());
        }
    }

    private int normalizeIndex(int keyHash){
        return (keyHash & 0x7FFFFFFF) % capacity;
    }

    public V search(T key){
        int hash = normalizeIndex(key.hashCode());
        Record<T, V> entry = searchBucket(hash, key);
        if(entry != null){
            return entry.getValue();
        }
        return null;
    }

    private Record<T, V> searchBucket(int index, T key){
        LinkedList<Record<T, V>> bucket = buckets.get(index);
        if(bucket == null){return null;}
        for(Record<T, V> entry : bucket){
            if(entry.getKey().equals(key)){
                return entry;
            }
        }
        return null;
    }

    public V remove(T key){
        if(key == null){return null;}
        int hash = normalizeIndex(key.hashCode());
        return removeFromBucket(hash, key);
    }

    private V removeFromBucket(int index, T key){
        Record<T, V> entry= searchBucket(index, key);
        if(entry != null){
            LinkedList<Record<T, V>> bucket = buckets.get(index);
            bucket.remove(entry);
            size--;
            return entry.getValue();
        }
        return null;
    }

    public Record<T, V> insert(T key, V value){
        int hash = normalizeIndex(value.hashCode());

        LinkedList<Record<T, V>> bucket = buckets.get(hash);
        for(Record<T, V> record : bucket){
            if(record.getKey().equals(key)){
                return record;
            }
        }

        Record<T, V> record = new Record<>(key, value);
        bucket.add(record);
        size++;
        if(size > threshold)
            ResizeTable();

        return null;
    }

    private void ResizeTable(){
        capacity = capacity*2;
        threshold = (int) (maxLoadFactor * capacity);

        List<LinkedList<Record<T, V>>> newBuckets = new ArrayList<>();
        for(int i=0; i<capacity; i++)
            newBuckets.add(new LinkedList<>());

        for(LinkedList<Record<T, V>> bucket : buckets){
            bucket.forEach((Record<T, V> e) -> {
                int hash =  normalizeIndex(e.getKey().hashCode());
                newBuckets.get(hash).add(e);
            });

            bucket.clear();
            bucket = null;
        }
        buckets = newBuckets;
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();

        for(int i=0; i<capacity; i++){
            text.append("Hash: ").append(i).append(". ");
            for(Record<T, V> record : buckets.get(i)){
                text.append(record).append(", ");
            }
            text.append("\n");
        }
        return text.toString();
    }

    public static void main(String[] args) {
        HashTable<Integer, String> example = new HashTable<Integer, String>();

        example.insert(4, "an");
        example.insert(10, "example");
        example.insert(11, "of");
        example.insert(55, "a");
        example.insert(5, "hashing");
        example.insert(1221, "class");

        System.out.println(example);

        System.out.println(example.search(5));
        example.remove(55);

        System.out.println(example);
    }
}
