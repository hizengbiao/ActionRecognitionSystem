package evaluations;


import java.util.TreeMap;

import data.Labels;

import models.Model;

public abstract class Evaluation {
    Model model;

    public Evaluation(Model model) {
        this.model = model;
    }

    public abstract double evaluate(TreeMap<Integer, Labels> testSet);
}
