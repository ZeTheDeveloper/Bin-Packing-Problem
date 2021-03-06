import java.util.InputMismatchException;
import java.util.Scanner;

public class MainSystemUI {

	private TaskController control;
	private Scanner scanner;

	public MainSystemUI(TaskController control, Scanner scanner) {
		this.control = control;
		this.scanner = scanner;
	}

	public void start() {
		boolean exit = false;
		while (!exit) {
			int choice = 0;
			boolean error = true; // to initial the first loop of Menu
			while (error) {
				try {
					System.out.println("-----------------------");
					System.out.println("   TruckBinPackSystem");
					System.out.println("-----------------------");
					System.out.println("1. Create new Task");
					System.out.println("2. Display all tasks");
					System.out.println("3. Exit");
					System.out.print("Pick option (1-4) ----> ");
					choice = scanner.nextInt();
					scanner.nextLine();
					error = false;
					switch (choice) {
					case 1:
						createTask();
						break;
					case 2:
						displayAllTasks();
						break;
					case 3:
						exit = true;
						break;
					default:
						System.out.println("Invalid option.\n");
					}
				} catch (InputMismatchException e) {
					scanner.nextLine();
					System.out.println("Please enter a valid number.\n");
				} catch (IndexOutOfBoundsException e) {
					scanner.nextLine();
					System.out.println("Option cannot be negative!\n");
				} finally {
					control.updateFile();
				}
			}
		}
	}

	private void createTask() {
		control.createTask();
		System.out.println("-----------------------");
		System.out.println("   Adding new Task");
		System.out.println("-----------------------");
		System.out.println("Kindly enter the parcel's weight accordingly: ");

		boolean repeat = true;
		while (repeat) {
			try {
				System.out.println("Current Parcel List: " + control.getTask().getParcelList().toString());
				if (control.getTask().getParcelList().size() != 0) {
					System.out.println("#enter \"n\" if you have no more parcel to enter.");
				}
				System.out.print("Parcel weight -> ");
				if (scanner.hasNextDouble()) {
					try {
						control.addParcel(new Parcel(scanner.nextDouble()));
					} catch (InputMismatchException e) {
						System.out.println("Parcel weight cannot exceed " + Parcel.MAX_WEIGHT + "!");
					}

				} else if (scanner.next().equalsIgnoreCase("n") && control.getTask().getParcelList().size() != 0) {
					repeat = false;
				} else {
					throw new InputMismatchException();
				}
			} catch (InputMismatchException e) {
				scanner.nextLine();
				System.out.println("Please enter a valid weight.\n");
				break;
			}
			System.out.println();
		}
		System.out.println();

		control.generateAlgorithmResults();
		control.updateTaskToList();
		printResult(control.getTask());
	}

	private void displayAllTasks() {
		System.out.println("-----------------------");
		System.out.println(" Displaying all Tasks");
		System.out.println("-----------------------");

		for (Task task : control.getTaskList()) {// fetch data from file
			System.out.println("Task ID: " + task.getId());
			System.out.println("Total Parcels: " + task.getParcelList().size());
			System.out.println("Parcel List: " + task.getParcelList().toString());
			System.out.println();
		}

		int input;
		Task theTask;
		try {
			System.out.println("Enter \"0\" to return to the main menu.");
			System.out.print("Select a task (taskID) -> ");
			input = scanner.nextInt();
			if (input == 0) {
				// skip to the end
			} else {
				theTask = control.getTask(input);
				control.setTask(theTask);
				control.generateAlgorithmResults();
				printResult(control.getTask());
			}
		} catch (InputMismatchException e) {
			System.out.println("Please enter a valid task ID!");
			System.out.println();
		} catch (NullPointerException e) {
			System.out.println("Task ID not found!");
			System.out.println();
		}

		System.out.println();
	}

	private void printResult(Task task) {
		Algorithm algo;

		for (int algoType = 1; algoType <= 2; algoType++) {
			if (algoType == 1) {
				algo = task.getBFDResult();
				System.out.println("---------Best Fit Decreasing---------");
			} else { // algoType == 2
				algo = task.getFFDResult();
				System.out.println("---------First Fit Decreasing---------");
			}

			System.out.println("Number of truck allocated: " + algo.getAllocatedTrucks().size());

			int count = 0;
			int truckNumber = 0;
			for (Truck truck : algo.getAllocatedTrucks()) {
				truckNumber++;
				System.out.println("Truck#" + truckNumber + " : " + truck.getContainedParcel().toString());
				if (truck.getRemainingCapacity() == 0)
					count++;
			}

			System.out.println("Trucks fully loaded with parcels: " + count);
			System.out.println("Time Allocated: " + algo.getTimeAllocated() + " nanosecond");
			System.out.println("Remaining Capacity: " + algo.getRemainingCapacity());
			System.out.println();
		}
		System.out.println("Return to menu");
		System.out.println();
	}
}
