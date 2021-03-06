import java.util.ArrayList;
import java.util.Collections;

public class FFDAlgorithm extends Algorithm {

	public FFDAlgorithm(ArrayList<Parcel> parcelList) {
		Collections.sort(parcelList); // sort by comparable in reverse order
		this.parcelList = parcelList;
		allocateParcels();
	}

	@Override
	protected void allocateParcels() {
		long startTime, endTime;
		boolean parcelAllocated = false;
		this.allocatedTrucks.add(new Truck()); // initial the 1st empty truck
		startTime = System.nanoTime();
		
		for (Parcel parcel : this.parcelList) {
			parcelAllocated = false;
			for (Truck truck : this.allocatedTrucks) { //
				// check if the truck is able to store the parcel;
				// if (able)
				// ---> add the parcel into the truck
				if (truck.getRemainingCapacity() >= parcel.getWeight()) {
					truck.addParcel(parcel);
					truck.updateRemainingCapacity();
					parcelAllocated = true;
					break;
				}
			}
			// if (the parcel is not allocated into any existing truck)
			// ----> a new truck will be created
			// ----> and the parcel will be assigned to the new truck

			if (!parcelAllocated) {
				Truck newTruck = new Truck();
				newTruck.addParcel(parcel);
				this.allocatedTrucks.add(newTruck);
				this.allocatedTrucks.get(this.allocatedTrucks.size() - 1).updateRemainingCapacity();
			} 
		}

		endTime = System.nanoTime();
		setTimeAllocated(startTime, endTime);
		updateTotalRemainingCapacity();
	}
}