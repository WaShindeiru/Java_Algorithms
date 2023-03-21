package org.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MaxHeap <T extends Comparable<T>>{
    private int size;
    private List<T> heap;

    public MaxHeap(int size){
        heap = new ArrayList<>(size);
        size = 0;
    }

    public MaxHeap(){
        this(5);
    }

    public MaxHeap(T[] arr){
        size = arr.length;
        heap = new ArrayList<>(size);
        Collections.addAll(heap, arr);
    }

    public MaxHeap(Collection<T> container){
        size = container.size();
        heap = new ArrayList<>(size);
        heap.addAll(container);
    }

    static private int getParent(int i){
        return (i-1)/2;
    }

    static private int getLeftChild(int i){
        return (2*i)+1;
    }

    static private int getRightChild(int i){
        return (2*i) + 2;
    }

    private void MaxHeapify(int i){
        int l = getLeftChild(i);
        int r = getRightChild(i);
        int largest;
        if(l < size && heap.get(l).compareTo(heap.get(i)) > 0){
            largest = l;
        }
        else{
            largest = i;
        }
        if(r < size && heap.get(r).compareTo(heap.get(largest)) > 0){
            largest = r;
        }

        if(largest != i){
            T temp = heap.get(i);
            heap.set(i, heap.get(largest));
            heap.set(largest, temp);
            MaxHeapify(largest);
        }
    }

    public void BuildMaxHeap(){
        for(int a=size/2; a>=0; a--){
            MaxHeapify(a);
        }
    }

    private void swap(int i, int x){
        T temp = heap.get(i);
        heap.set(i, heap.get(x));
        heap.set(x, temp);
    }

    public void Heapsort(){
        BuildMaxHeap();
        for(int i=size-1; i>0; i--){
            swap(0, i);
            size--;
            MaxHeapify(0);
        }
    }

    @Override
    public String toString() {
        return heap.toString();
    }

    public T Maximum(){
        if(size < 1){
            throw new RuntimeException("Wrong size");
        }
        return heap.get(0);
    }

    public T ExtractMax(){
        if(size < 1){
            throw new RuntimeException("Wrong size");
        }
        T max = heap.get(0);
        heap.set(0, heap.get(size - 1));
        size--;
        heap.remove(size);
        MaxHeapify(0);
        return max;
    }

    public void IncreaseKey(int i, T key){
        if(heap.get(i).compareTo(key) > 0){
            throw new RuntimeException("Wrong key");
        }
        heap.set(i, key);
        while(i >= 0 && heap.get(getParent(i)).compareTo(heap.get(i)) < 0){
            swap(getParent(i), i);
            i = getParent(i);
        }
    }

    public void Insert(T key){
        heap.add(key);
        int index=size;
        size++;
        while(index >= 0 && heap.get(getParent(index)).compareTo(heap.get(index)) < 0){
            swap(getParent(index), index);
            index = getParent(index);
        }
    }

    public static void main(String[] args) {
        MaxHeap<Integer> example = new MaxHeap<>(new Integer[] {10, 45, 20, 11, 55, 11});
        example.BuildMaxHeap();
        System.out.println(example);
        example.Insert(23);
        System.out.println(example);
    }
}
