package com.github.tim91690.eclipse.misc;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/**Permet de tirer avec un certain poids différents éléments
 * code original : https://stackoverflow.com/questions/6409652/random-weighted-selection-in-java
 * j'aurais pas pu écrire quelque chose de mieux
 */
public class WeightCollection<E> {
    private final NavigableMap<Double,E> map = new TreeMap<>();
    private final Random random;
    private double total = 0;

    public WeightCollection() {
        this(new Random());
    }

    public WeightCollection(Random rdm) {
        this.random = rdm;
    }

    //La clé est le total poids+total précédent, la valeur est result
    public WeightCollection<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total,result);
        return this;
    }

    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }


}
