package controller;

import com.se1020.secondhandcarplatform.model.Bid;
import com.se1020.secondhandcarplatform.model.Car;
import com.se1020.secondhandcarplatform.model.User;
import com.se1020.secondhandcarplatform.service.BidService;
import com.se1020.secondhandcarplatform.service.CarService;
import com.se1020.secondhandcarplatform.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService;

    @Autowired
    private BidService bidService;

    @GetMapping
    public String listCars(Model model, @RequestParam(required = false) String search) {
        List<Car> cars;
        if (search != null && !search.isEmpty()) {
            cars = carService.searchCars(search);
            model.addAttribute("searchTerm", search);
        } else {
            cars = carService.getAvailableCars();
        }
        model.addAttribute("cars", cars);
        return "cars/list";
    }



    @GetMapping("/{id}")
    public String viewCar(@PathVariable String id, Model model, HttpSession session) {
        Optional<Car> optionalCar = carService.getCarById(id);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            model.addAttribute("car", car);

            // Get seller information
            Optional<User> seller = userService.getUserById(car.getSellerId());
            seller.ifPresent(user -> model.addAttribute("seller", user));

            // Get bids for this car
            List<Bid> bids = bidService.getBidsByCarId(id);
            model.addAttribute("bids", bids);

            // Check if current user is the seller
            String currentUserId = (String) session.getAttribute("userId");
            boolean isOwner = currentUserId != null && currentUserId.equals(car.getSellerId());
            model.addAttribute("isOwner", isOwner);

            return "cars/view";
        }

        return "redirect:/cars";
    }

    @GetMapping("/add")
    public String showAddCarForm(Model model, HttpSession session) {
        // Check if user is logged in
        if (session.getAttribute("userId") == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("car", new Car());
        return "cars/add";
    }



    @PostMapping("/add")
    public String addCar(@ModelAttribute Car car, HttpSession session) {
        // Set the seller ID from the session
        String sellerId = (String) session.getAttribute("userId");
        if (sellerId == null) {
            return "redirect:/users/login";
        }

        car.setSellerId(sellerId);
        carService.saveCar(car);
        return "redirect:/cars";
    }

    @GetMapping("/edit/{id}")
    public String showEditCarForm(@PathVariable String id, Model model, HttpSession session) {
        // Check if user is logged in
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/users/login";
        }

        Optional<Car> optionalCar = carService.getCarById(id);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();

            // Check if current user is the seller
            if (!car.getSellerId().equals(userId)) {
                return "redirect:/cars";
            }

            model.addAttribute("car", car);
            return "cars/edit";
        }
        return "redirect:/cars";
    }

    @PostMapping("/edit/{id}")
    public String updateCar(@PathVariable String id, @ModelAttribute Car car, HttpSession session) {
        // Check if user is logged in
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/users/login";
        }

        Optional<Car> optionalCar = carService.getCarById(id);
        if (optionalCar.isPresent()) {
            Car existingCar = optionalCar.get();

            // Check if current user is the seller
            if (!existingCar.getSellerId().equals(userId)) {
                return "redirect:/cars";
            }

            // Preserve the seller ID and sold status
            car.setSellerId(existingCar.getSellerId());
            car.setSold(existingCar.isSold());

            carService.saveCar(car);
        }
        return "redirect:/cars";
    }

    @GetMapping("/delete/{id}")
    public String deleteCar(@PathVariable String id, HttpSession session) {
        // Check if user is logged in
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/users/login";
        }

        Optional<Car> optionalCar = carService.getCarById(id);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();

            // Check if current user is the seller
            if (!car.getSellerId().equals(userId)) {
                return "redirect:/cars";
            }

            carService.deleteCar(id);
        }
        return "redirect:/cars";
    }

    @PostMapping("/{id}/bid")
    public String placeBid(@PathVariable String id, @RequestParam double amount, HttpSession session) {
        // Check if user is logged in
        String bidderId = (String) session.getAttribute("userId");
        if (bidderId == null) {
            return "redirect:/users/login";
        }

        Optional<Car> optionalCar = carService.getCarById(id);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();

            // Check if car is not sold and bidder is not the seller
            if (!car.isSold() && !car.getSellerId().equals(bidderId)) {
                Bid bid = new Bid(id, bidderId, amount);
                bidService.placeBid(bid);
            }
        }
        return "redirect:/cars/" + id;
    }

    @GetMapping("/bid/{bidId}/accept")
    public String acceptBid(@PathVariable String bidId, HttpSession session) {
        // Check if user is logged in
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/users/login";
        }

        Optional<Bid> optionalBid = bidService.getBidById(bidId);
        if (optionalBid.isPresent()) {
            Bid bid = optionalBid.get();

            // Get the car
            Optional<Car> optionalCar = carService.getCarById(bid.getCarId());
            if (optionalCar.isPresent()) {
                Car car = optionalCar.get();

                // Check if current user is the seller
                if (car.getSellerId().equals(userId)) {
                    bidService.acceptBid(bidId);
                }
            }
        }
        return "redirect:/cars";
    }

    @GetMapping("/bid/{bidId}/reject")
    public String rejectBid(@PathVariable String bidId, HttpSession session) {
        // Check if user is logged in
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/users/login";
        }

        Optional<Bid> optionalBid = bidService.getBidById(bidId);
        if (optionalBid.isPresent()) {
            Bid bid = optionalBid.get();

            // Get the car
            Optional<Car> optionalCar = carService.getCarById(bid.getCarId());
            if (optionalCar.isPresent()) {
                Car car = optionalCar.get();

                // Check if current user is the seller
                if (car.getSellerId().equals(userId)) {
                    bidService.rejectBid(bidId);
                }
            }
        }
        return "redirect:/cars";
    }
}
