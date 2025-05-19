package com.se1020.secondhandcarplatform.service;

import com.se1020.secondhandcarplatform.model.Bid;
import com.se1020.secondhandcarplatform.model.Car;
import com.se1020.secondhandcarplatform.util.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BidService {
    private static final String BIDS_FILE = "data/bids.txt";
    private List<Bid> bids;

    @Autowired
    private CarService carService;

    public BidService() {
        loadBids();
    }

    private void loadBids() {
        bids = new ArrayList<>();
        List<String> lines = FileHandler.readFromFile(BIDS_FILE);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 6) {
                Bid bid = new Bid();
                bid.setId(parts[0]);
                bid.setCarId(parts[1]);
                bid.setBidderId(parts[2]);
                bid.setAmount(Double.parseDouble(parts[3]));
                bid.setBidTime(LocalDateTime.parse(parts[4], formatter));
                bid.setStatus(parts[5]);
                bids.add(bid);
            }
        }
    }

    private void saveBids() {
        FileHandler.saveToFile(bids, BIDS_FILE);
    }

    public List<Bid> getAllBids() {
        return new ArrayList<>(bids);
    }

    public Optional<Bid> getBidById(String id) {
        return bids.stream()
                .filter(bid -> bid.getId().equals(id))
                .findFirst();
    }

    public List<Bid> getBidsByCarId(String carId) {
        return bids.stream()
                .filter(bid -> bid.getCarId().equals(carId))
                .collect(Collectors.toList());
    }

    public List<Bid> getBidsByBidderId(String bidderId) {
        return bids.stream()
                .filter(bid -> bid.getBidderId().equals(bidderId))
                .collect(Collectors.toList());
    }

    public Bid placeBid(Bid bid) {
        bids.add(bid);
        saveBids();
        return bid;
    }

    public boolean acceptBid(String bidId) {
        Optional<Bid> optionalBid = getBidById(bidId);
        if (optionalBid.isPresent()) {
            Bid bid = optionalBid.get();
            bid.setStatus("ACCEPTED");

            // Mark car as sold
            Optional<Car> optionalCar = carService.getCarById(bid.getCarId());
            if (optionalCar.isPresent()) {
                Car car = optionalCar.get();
                car.setSold(true);
                carService.saveCar(car);

                // Reject all other bids for this car
                bids.stream()
                        .filter(b -> b.getCarId().equals(bid.getCarId()) && !b.getId().equals(bidId))
                        .forEach(b -> b.setStatus("REJECTED"));
            }

            saveBids();
            return true;
        }
        return false;
    }

    public boolean rejectBid(String bidId) {
        Optional<Bid> optionalBid = getBidById(bidId);
        if (optionalBid.isPresent()) {
            Bid bid = optionalBid.get();
            bid.setStatus("REJECTED");
            saveBids();
            return true;
        }
        return false;
    }
}
