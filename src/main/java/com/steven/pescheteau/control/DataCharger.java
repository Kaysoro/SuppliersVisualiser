package com.steven.pescheteau.control;

import com.steven.pescheteau.domain.*;

import java.util.*;

/**
 * Created by steve on 03/10/2016.
 */
public class DataCharger {

    public static Object[] getCities(){
        List<String> list =  new ArrayList<String>(City.getCities().keySet());
        Collections.sort(list);
        list.add(0, "All");
        return list.toArray();
    }

    public static Object[] getCountries(){
        List<String> list =  new ArrayList<String>(Country.getCountries().keySet());
        Collections.sort(list);
        list.add(0, "All");
        return list.toArray();
    }

    public static Object[] getZones(){
        List<String> list =  new ArrayList<String>(Zone.getZones().keySet());
        Collections.sort(list, new Comparator<String>(){
            @Override
            public int compare(String o1, String o2) {
                if (o1.matches("^\\d+$") && o2.matches("^\\d+$"))
                    return Integer.parseInt(o1) - Integer.parseInt(o2);
                return o1.compareTo(o2);
            }
        });
        list.add(0, "All");
        return list.toArray();
    }

    public static Object[] getTrucks(){
        List<String> list =  new ArrayList<String>(Truck.getTrucks().keySet());
        Collections.sort(list);
        list.add(0, "All");
        return list.toArray();
    }

    public static Object[] getSuppliers(){
        List<String> list =  new ArrayList<String>(Supplier.getSuppliers().keySet());
        Collections.sort(list);
        list.add(0, "All");
        return list.toArray();
    }

    public static Object[] getYears(){
        List<String> list =  new ArrayList<String>();
        int yearMax = Calendar.getInstance().get(Calendar.YEAR) + 1;
        for(int i = 2010; i <= yearMax; i++)
            list.add(String.valueOf(i));
        Collections.sort(list, new Comparator<String>(){
            @Override
            public int compare(String o1, String o2) {
                return Integer.parseInt(o2) - Integer.parseInt(o1);
            }
        });
        list.add(0, "All");
        return list.toArray();
    }
}
