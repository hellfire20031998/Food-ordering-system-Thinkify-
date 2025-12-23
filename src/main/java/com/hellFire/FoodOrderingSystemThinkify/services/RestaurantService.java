package com.hellFire.FoodOrderingSystemThinkify.services;

import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.CreateMenuRequest;
import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.CreateRestaurantRequest;
import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.UpdateMenuRequest;
import com.hellFire.FoodOrderingSystemThinkify.dtos.responses.MenuDto;
import com.hellFire.FoodOrderingSystemThinkify.dtos.responses.RestaurantDto;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.MenuItemAlreadyExistException;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.MenuItemNotFoundException;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.RestaurantAlreadyExistException;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.RestaurantNotFoundException;
import com.hellFire.FoodOrderingSystemThinkify.models.Menu;
import com.hellFire.FoodOrderingSystemThinkify.models.Restaurant;
import com.hellFire.FoodOrderingSystemThinkify.respositories.IRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private IRestaurantRepository restaurantRepository;

    @Autowired
    private MenuService menuService;

    public RestaurantDto onboard(CreateRestaurantRequest request) throws RestaurantAlreadyExistException, MenuItemAlreadyExistException {
        Restaurant restaurant = restaurantRepository.findByName(request.getName());
        if(restaurant == null) {
            restaurant = new Restaurant();
            restaurant.setName(request.getName().trim());
            restaurant.setMaxOrders(request.getMaxOrders());
            restaurant.setRating(request.getRating());
            Restaurant savedRestaurant = restaurantRepository.save(restaurant);

            List<Menu> menuList = menuService.createMenuList(request.getMenuRequests(), savedRestaurant);
            RestaurantDto restaurantDto = toDto(savedRestaurant);
            restaurantDto.setMenuItems(menuService.toDtoList(menuList));
            return restaurantDto;
        }else{
            throw new RestaurantAlreadyExistException("Restaurant with name " + request.getName() + " already exists");
        }
    }

    public String updateMenu(Long id, UpdateMenuRequest request) throws RestaurantNotFoundException, MenuItemNotFoundException {
        Restaurant r = restaurantRepository.findById(id);
        if(r != null) {
            return menuService.updateMenuById(r.getId(), request);
        }else {
            throw new RestaurantNotFoundException("Restaurant with id " + id + " does not exist");
        }
    }

    public List<MenuDto> addMenu(Long id, List<CreateMenuRequest> request) throws RestaurantNotFoundException, MenuItemNotFoundException, MenuItemAlreadyExistException {
        Restaurant r = restaurantRepository.findById(id);
        if(r != null) {
            return menuService.toDtoList(menuService.createMenuList(request, r));
        }else {
            throw new RestaurantNotFoundException("Restaurant with id " + id + " does not exist");
        }
    }

    public List<RestaurantDto> getAllRestaurants() {
        return restaurantRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public Restaurant getById(Long id) throws RestaurantNotFoundException {
        Restaurant r = restaurantRepository.findById(id);
        if(r == null) {
            throw new RestaurantNotFoundException("Restaurant with id " + id + " does not exist");
        }
        return r;
    }

    public RestaurantDto toDto(Restaurant restaurant) {
        RestaurantDto response = new RestaurantDto();
        response.setId(restaurant.getId());
        response.setName(restaurant.getName());
        response.setRating(restaurant.getRating());
        response.setMenuItems(menuService.toDtoList(menuService.getMenuListByRestaurantId(restaurant.getId())));
        return response;
    }

    public List<RestaurantDto> toDtoList(List<Restaurant> restaurants) {
        return restaurants.stream().map(this::toDto).collect(Collectors.toList());
    }
}
