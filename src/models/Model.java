package models;

import har.Labels;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.TreeMap;

public abstract class Model implements Serializable {
    public abstract void train(TreeMap<Integer, Labels> trainSet);

    public abstract Labels test(int videoNumber);

    public abstract void save(String address);

    public static Model load(String address) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                    new File(address)));
            Model m = (Model) ois.readObject();
            ois.close();
            return m;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
