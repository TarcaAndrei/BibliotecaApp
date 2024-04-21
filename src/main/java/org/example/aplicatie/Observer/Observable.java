package org.example.aplicatie.Observer;


import java.util.ArrayList;

public interface Observable{
    ArrayList<Observer> observers = new ArrayList<>();
    default void addObserver(Observer observer){
        observers.add(observer);
    }
    default void removeObserver(Observer observer){
        observers.remove(observer);
    }
    default void notifyAllObservers(EventType eventUpdate){
        observers.forEach(x->x.update(eventUpdate));
    }
}
