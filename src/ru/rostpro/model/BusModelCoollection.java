package ru.rostpro.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BusModelCoollection extends ArrayList<BusModel> implements Iterable<BusModel> {

	private static final long serialVersionUID = 107101491624069070L;
	
	List<BusModel> buses = new ArrayList<BusModel>(); 
	
	@Override
	public Iterator<BusModel> iterator() {
		Iterator<BusModel> bus = buses.iterator();
        return bus;
	}
		
}
