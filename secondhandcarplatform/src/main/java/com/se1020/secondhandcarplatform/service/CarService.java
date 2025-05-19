package com.se1020.secondhandcarplatform.service;

import com.se1020.secondhandcarplatform.model.Car;
import com.se1020.secondhandcarplatform.util.FileHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {
    private static final String CARS_FILE = "data/cars.txt";
    private List<Car> cars;

    public CarService() {
        loadCars();
    }

    private void loadCars() {
        cars = new ArrayList<>();
        List<String> lines = FileHandler.readFromFile(CARS_FILE);

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 11) {
                Car car = new Car();
                car.setId(parts[0]);
                car.setMake(parts[1]);
                car.setModel(parts[2]);
                car.setYear(Integer.parseInt(parts[3]));
                car.setPrice(Double.parseDouble(parts[4]));
                car.setDescription(parts[5]);
                car.setSellerId(parts[6]);
                car.setCondition(parts[7]);
                car.setMileage(Integer.parseInt(parts[8]));
                car.setImageUrl(parts[9]);
                car.setSold(Boolean.parseBoolean(parts[10]));
                cars.add(car);
            }
        }
    }

    private void saveCars() {
        FileHandler.saveToFile(cars, CARS_FILE);
    }

    public List<Car> getAllCars() {
        return new ArrayList<>(cars);
    }

    public List<Car> getAvailableCars() {
        return cars.stream()
                .filter(car -> !car.isSold())
                .collect(Collectors.toList());
    }

    public Optional<Car> getCarById(String id) {
        return cars.stream()
                .filter(car -> car.getId().equals(id))
                .findFirst();
    }

    public List<Car> getCarsBySellerId(String sellerId) {
        return cars.stream()
                .filter(car -> car.getSellerId().equals(sellerId))
                .collect(Collectors.toList());
    }

    public Car saveCar(Car car) {
        // Remove existing car if it's an update
        cars.removeIf(c -> c.getId().equals(car.getId()));
        cars.add(car);
        saveCars();
        return car;
    }

    public boolean deleteCar(String id) {
        boolean removed = cars.removeIf(car -> car.getId().equals(id));
        if (removed) {
            saveCars();
        }
        return removed;
    }

    public List<Car> searchCars(String keyword) {
        String searchTerm = keyword.toLowerCase();
        return cars.stream()
                .filter(car -> !car.isSold() &&
                        (car.getMake().toLowerCase().contains(searchTerm) ||
                                car.getModel().toLowerCase().contains(searchTerm) ||
                                car.getDescription().toLowerCase().contains(searchTerm)))
                .collect(Collectors.toList());
    }
}
