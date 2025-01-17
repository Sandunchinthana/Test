import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Collections;


public class BusReservationSystem {
    public static void main(String[] args) {
        List<Bus> registeredBuses = new ArrayList<>();
        List<Customer> registeredCustomers = new ArrayList<>();

        System.out.println("Welcome to the Bus Reservation System");
        Scanner scanner = new Scanner(System.in);

        boolean exit = false; // Add an exit flag

        while (!exit) { // Use the exit flag to control the loop
            try {
                System.out.println("1. Register a Bus");
                System.out.println("2. Register a Customer");
                System.out.println("3. Search for Buses");
                System.out.println("4. Reserve a Seat");
                System.out.println("5. Cancel Reservation");
                System.out.println("6. Request a New Seat");
                System.out.println("7. Display All Reservations");
                System.out.println("8. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                     
                    case 1:
                        Bus bus = registerBus(scanner);
                        registeredBuses.add(bus);
                        break;
                    case 2:
                        Customer customer = registerCustomer(scanner);
                        registeredCustomers.add(customer);
                        break;
                    case 3:
                        System.out.print("Enter your desired ending point: ");
                        String endingPoint = scanner.nextLine();
                        searchBusesByEndingPoint(registeredBuses, endingPoint);
                        break;
                    case 4:
                        reserveSeat(scanner, registeredBuses, registeredCustomers);
                        break;
                    case 5:
                        cancelReservation(scanner, registeredBuses);
                        break;
                    case 6:
                        requestNewSeat(scanner, registeredBuses);
                        break;
                    case 7:
                       
                        Collections.sort(registeredBuses, (bus1, bus2) -> bus1.getBusNumber().compareTo(bus2.getBusNumber()));
                        displayAllReservations(registeredBuses);
                        break;
                    case 8:
                        System.out.println("Thank you for using the Bus Reservation System.");
                        scanner.close();
                        exit = true; // Set the exit flag to true
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid option.");
                scanner.nextLine(); // Clear the input buffer
            }
        }
    }



    private static Bus registerBus(Scanner scanner) {
        System.out.print("Enter Bus Number: ");
        String busNumber = scanner.nextLine();
        System.out.print("Enter Total Seats: ");
        int totalSeats = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Starting Point: ");
        String startingPoint = scanner.nextLine();
        System.out.print("Enter Ending Point: ");
        String endingPoint = scanner.nextLine();
        System.out.print("Enter Starting Time: ");
        String startingTime = scanner.nextLine();
        System.out.print("Enter Fare: ");
        double fare = scanner.nextDouble();
        scanner.nextLine();

        return new Bus(busNumber, totalSeats, startingPoint, endingPoint, startingTime, fare);
    }

    private static Customer registerCustomer(Scanner scanner) {
        System.out.print("Enter Customer Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Mobile Number: ");
        String mobileNumber = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter City: ");
        String city = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        return new Customer(name, mobileNumber, email, city, age);
    }

    private static void searchBusesByEndingPoint(List<Bus> buses, String endingPoint) {
        List<Bus> matchingBuses = new ArrayList<>();
        for (Bus bus : buses) {
            if (bus.getEndingPoint().equalsIgnoreCase(endingPoint)) {
                matchingBuses.add(bus);
            }
        }
        if (matchingBuses.isEmpty()) {
            System.out.println("No buses found with the ending point: " + endingPoint);
        } else {
            System.out.println("Buses ending at " + endingPoint + ":");
            for (Bus bus : matchingBuses) {
                bus.displayBusInfo();
            }
        }
    }

    private static void reserveSeat(Scanner scanner, List<Bus> buses, List<Customer> customers) {
        System.out.print("Enter the Bus Registration ID to book: ");
        int busRegistrationId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Bus bookedBus = getBusById(buses, busRegistrationId);

        if (bookedBus != null) {
            System.out.print("Enter Customer's Name: ");
            String name = scanner.nextLine();
            Customer customer = getCustomerByname(customers, name);

            if (customer != null) {
                bookedBus.reserveSeat(customer);
                bookedBus.saveReservation();
            } else {
                System.out.println("Customer not found. Reservation failed.");
            }
        } else {
            System.out.println("Invalid Bus Registration ID. Reservation failed.");
        }
    }

    private static void cancelReservation(Scanner scanner, List<Bus> buses) {
        System.out.print("Enter the Bus Registration ID to cancel reservation: ");
        int busRegistrationId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Bus bookedBus = getBusById(buses, busRegistrationId);

        if (bookedBus != null) {
            System.out.print("Enter Customer's Name: ");
            String name= scanner.nextLine();
            Customer customer = getCustomerByname(bookedBus.getReservedCustomers(), name);

            if (customer != null) {
                bookedBus.cancelReservation(customer);
                bookedBus.saveReservation();
            } else {
                System.out.println("Customer not found. Cancellation failed.");
            }
        } else {
            System.out.println("Invalid Bus Registration ID. Cancellation failed.");
        }
    }

    private static void requestNewSeat(Scanner scanner, List<Bus> buses) {
        System.out.print("Enter the Bus Registration ID to request a new seat: ");
        int busRegistrationId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Bus bookedBus = getBusById(buses, busRegistrationId);

        if (bookedBus != null) {
            System.out.print("Enter Customer's Name: ");
            String name = scanner.nextLine();
            Customer customer = getCustomerByname(bookedBus.getReservedCustomers(), name);

            if (customer != null) {
                bookedBus.requestNewSeat(customer);
            } else {
                System.out.println("Customer not found. Seat request failed.");
            }
        } else {
            System.out.println("Invalid Bus Registration ID. Seat request failed.");
        }
    }

    private static void displayAllReservations(List<Bus> buses) {
        for (Bus bus : buses) {
            bus.displayAllReservations();
        }
    }

    private static Bus getBusById(List<Bus> buses, int busRegistrationId) {
        for (Bus bus : buses) {
            if (bus.getBusRegistrationId() == busRegistrationId) {
                return bus;
            }
        }
        return null;
    }

    private static Customer getCustomerByname(List<Customer> customers, String name) {
        for (Customer customer : customers) {
            if (customer.getName().equals(name)) {
                return customer;
            }
        }
        return null;
    }
}

class Bus {
    private int busRegistrationId;
    private String busNumber;
    private int totalSeats;
    private String startingPoint;
    private String endingPoint;
    private String startingTime;
    private double fare;
    private List<Integer> reservedSeats = new ArrayList<>();
    private Queue<Integer> reservedSeatsQueue = new LinkedList<>();
    private List<Customer> reservedCustomers = new ArrayList<>();

    private static int nextBusRegistrationId = 1;

    public Bus(String busNumber, int totalSeats, String startingPoint, String endingPoint, String startingTime, double fare) {
        this.busRegistrationId = nextBusRegistrationId++;
        this.busNumber = busNumber;
        this.totalSeats = totalSeats;
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
        this.startingTime = startingTime;
        this.fare = fare;
    }
     public String getBusNumber() {
        return busNumber;
    }

    public void displayBusInfo() {
        System.out.println("Bus Information:");
        System.out.println("Bus Registration ID: " + busRegistrationId);
        System.out.println("Bus Number: " + busNumber);
        System.out.println("Total Seats: " + totalSeats);
        System.out.println("Starting Point: " + startingPoint);
        System.out.println("Ending Point: " + endingPoint);
        System.out.println("Starting Time: " + startingTime);
        System.out.println("Fare: " + fare);
        System.out.println();
    }

    public int getBusRegistrationId() {
        return busRegistrationId;
    }

    public String getEndingPoint() {
        return endingPoint;
    }

    public List<Customer> getReservedCustomers() {
        return reservedCustomers;
    }

    public void reserveSeat(Customer customer) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the seat number to reserve: ");
        int seatNumber = scanner.nextInt();
        if (seatNumber > 0 && seatNumber <= totalSeats) {
            if (!reservedSeats.contains(seatNumber)) {
                reservedSeats.add(seatNumber);
                reservedSeatsQueue.offer(seatNumber);
                reservedCustomers.add(customer);
                System.out.println("Seat " + seatNumber + " reserved successfully for " + customer.getName());
                if (reservedSeats.size() > totalSeats) {
                    int removedSeat = reservedSeatsQueue.poll();
                    reservedSeats.remove(Integer.valueOf(removedSeat));
                    reservedCustomers.remove(0);
                    System.out.println("Reservation for Seat " + removedSeat + " is canceled (FIFO) to accommodate the new reservation.");
                }
            } else {
                System.out.println("Seat " + seatNumber + " is already reserved.");
            }
        } else {
            System.out.println("Invalid seat number.");
        }
    }

    public void saveReservation() {
        System.out.println("Reservation saved for the following seats: " + reservedSeats);
    }

    public void cancelReservation(Customer customer) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the seat number to cancel the reservation: ");
        int seatNumber = scanner.nextInt();
        if (reservedSeats.contains(seatNumber)) {
            reservedSeats.remove(Integer.valueOf(seatNumber));
            reservedSeatsQueue.remove(seatNumber);
            reservedCustomers.remove(customer);
            System.out.println("Reservation for Seat " + seatNumber + " is canceled for " + customer.getName());
        } else {
            System.out.println("Seat " + seatNumber + " is not reserved. Cancellation failed.");
        }
    }

    public void requestNewSeat(Customer customer) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the new seat number to request: ");
        int seatNumber = scanner.nextInt();
        if (seatNumber > 0 && seatNumber <= totalSeats) {
            if (!reservedSeats.contains(seatNumber)) {
                reservedSeats.add(seatNumber);
                reservedSeatsQueue.offer(seatNumber);
                reservedCustomers.add(customer);
                System.out.println("Seat " + seatNumber + " reserved successfully for " + customer.getName());
                if (reservedSeats.size() > totalSeats) {
                    int removedSeat = reservedSeatsQueue.poll();
                    reservedSeats.remove(Integer.valueOf(removedSeat));
                    reservedCustomers.remove(0);
                    System.out.println("Reservation for Seat " + removedSeat + " is canceled (FIFO) to accommodate the new reservation.");
                }
            } else {
                System.out.println("Seat " + seatNumber + " is already reserved.");
            }
        } else {
            System.out.println("Invalid seat number.");
        }
    }

    public void displayAllReservations() {
        System.out.println("Bus Registration ID: " + busRegistrationId);
        System.out.println("Bus Number: " + busNumber);
        System.out.println("Ending Point: " + endingPoint);
        System.out.println("Reserved Seats: " + reservedSeats);
        System.out.println("Reservations: ");
        for (Customer customer : reservedCustomers) {
            System.out.println("Customer: " + customer.getName() + ", Mobile Number: " + customer.getMobileNumber());
        }
        System.out.println();
    }
}

class Customer {
    private String name;
    private String mobileNumber;
    private String email;
    private String city;
    private int age;

    public Customer(String name, String mobileNumber, String email, String city, int age) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.city = city;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }
}