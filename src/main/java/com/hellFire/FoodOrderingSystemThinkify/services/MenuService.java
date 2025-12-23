package com.hellFire.FoodOrderingSystemThinkify.services;

import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.CreateMenuRequest;
import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.UpdateMenuRequest;
import com.hellFire.FoodOrderingSystemThinkify.dtos.responses.MenuDto;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.MenuItemAlreadyExistException;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.MenuItemNotFoundException;
import com.hellFire.FoodOrderingSystemThinkify.models.Menu;
import com.hellFire.FoodOrderingSystemThinkify.models.Restaurant;
import com.hellFire.FoodOrderingSystemThinkify.models.enums.Currency;
import com.hellFire.FoodOrderingSystemThinkify.models.pojo.ItemPrice;
import com.hellFire.FoodOrderingSystemThinkify.respositories.IMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.hellFire.FoodOrderingSystemThinkify.utils.Utils.normalize;

@Service
public class MenuService {

    @Autowired
    private IMenuRepository menuRepository;

    public List<Menu> createMenuList(List<CreateMenuRequest> requests, Restaurant restaurant) throws MenuItemAlreadyExistException {
        List<Menu> menuList = new ArrayList<>();

        Map<String, Menu> existingMenuMap = menuRepository
                .findByRestaurantId(restaurant.getId())
                .stream()
                .collect(Collectors.toMap(
                        m -> normalize(m.getItemName()),
                        Function.identity()
                ));
        for(CreateMenuRequest request : requests){
            if(existingMenuMap.containsKey(normalize(request.getItemName()))){
                throw new MenuItemAlreadyExistException("Menu item already exist with name "+ request.getItemName());
            }
            Menu menu = new Menu();
            if(Objects.isNull(request.getItemPrice().getCurrency())){
                request.getItemPrice().setCurrency(Currency.INR);
            }
            menu.setItemPrice(request.getItemPrice());
            menu.setItemName(request.getItemName().trim());
            menu.setRestaurant(restaurant);
            menuList.add(menu);
        }
        return menuRepository.saveAll(menuList);
    }

    public Menu getMenuById(Long id) {
        return menuRepository.findById(id);
    }

    public String updateMenuById(Long restaurantId, UpdateMenuRequest request) throws MenuItemNotFoundException {

        Menu menu = getMenuById(request.getId());

        if(Objects.nonNull(menu) && Objects.equals(menu.getRestaurant().getId(), restaurantId)) {
            ItemPrice itemPrice = menu.getItemPrice();

            if(Objects.nonNull(request.getItemName())){
                menu.setItemName(request.getItemName().trim());
            }
            if(Objects.isNull(request.getItemPrice().getCurrency())){
                request.getItemPrice().setCurrency(itemPrice.getCurrency());
            }
            if(Objects.nonNull(request.getItemPrice())){
                menu.setItemPrice(request.getItemPrice());
            }
            return "Menu updated successfully";
        }else {
            throw new MenuItemNotFoundException("Menu with id " + request.getId() + " does not exist");
        }
    }

    public List<Menu> getMenuMapByItemName(String itemName) {
        return menuRepository.findAll().stream().filter(menu -> Objects.equals(normalize(menu.getItemName()), normalize(itemName))).collect(Collectors.toList());
    }

    public List<Menu> getMenuListByRestaurantId(Long restaurantId) {
        return menuRepository.findByRestaurantId(restaurantId);
    }

    public MenuDto toDto(Menu menu) {
        MenuDto menuDto = new MenuDto();
        menuDto.setId(menu.getId());
        menuDto.setItemName(menu.getItemName());
        menuDto.setItemPrice(menu.getItemPrice());
        return menuDto;
    }

    public List<MenuDto> toDtoList(List<Menu> menuList) {
        List<MenuDto> menuDtoList = new ArrayList<>();
        for(Menu menu : menuList){
            menuDtoList.add(toDto(menu));
        }
        return menuDtoList;
    }

}
