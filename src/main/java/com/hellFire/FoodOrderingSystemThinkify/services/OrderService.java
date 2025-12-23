package com.hellFire.FoodOrderingSystemThinkify.services;

import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.OrderItemRequest;
import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.OrderRequest;
import com.hellFire.FoodOrderingSystemThinkify.dtos.responses.OrderDto;
import com.hellFire.FoodOrderingSystemThinkify.dtos.responses.OrderedItemDto;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.OrderNotFoundException;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.UserNotFoundException;
import com.hellFire.FoodOrderingSystemThinkify.models.*;
import com.hellFire.FoodOrderingSystemThinkify.models.enums.OrderStatus;
import com.hellFire.FoodOrderingSystemThinkify.respositories.IOrderRepository;
import com.hellFire.FoodOrderingSystemThinkify.respositories.IOrderedItemRepository;
import com.hellFire.FoodOrderingSystemThinkify.strategies.IRestaurantSelectionStrategy;
import com.hellFire.FoodOrderingSystemThinkify.strategies.factory.StrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.hellFire.FoodOrderingSystemThinkify.utils.Utils.normalize;

@Service
public class OrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderedItemRepository orderedItemRepository;

    @Autowired
    private MenuService menuService;

    @Autowired
    private StrategyFactory factory;

    @Autowired
    private AppUserService userService;


    public OrderDto placeOrder(OrderRequest request) throws UserNotFoundException {

        AppUser appUser = userService.getAppUserById(request.getUserId());
        Order order = new Order();
        order.setUser(appUser);

        List<Menu> allMenus = new ArrayList<>();
        for (OrderItemRequest itemReq : request.getOrderItems()) {
            allMenus.addAll(menuService.getMenuMapByItemName(itemReq.getItemName()));
        }

        Map<Restaurant, List<Menu>> restaurantMenuMap =
                allMenus.stream().collect(Collectors.groupingBy(Menu::getRestaurant));

        Map<Restaurant, List<Menu>> eligibleRestaurantMap = new HashMap<>();

        for (Restaurant restaurant : restaurantMenuMap.keySet()) {
            List<Menu> menus = restaurantMenuMap.get(restaurant);

            boolean allItemsPresent = true;

            for (OrderItemRequest reqItem : request.getOrderItems()) {
                boolean found = menus.stream()
                        .anyMatch(m -> Objects.equals(
                                normalize(m.getItemName()),
                                normalize(reqItem.getItemName())
                        ));

                if (!found) {
                    allItemsPresent = false;
                    break;
                }
            }

            if (allItemsPresent) {
                eligibleRestaurantMap.put(restaurant, menus);
            }
        }

        if (eligibleRestaurantMap.isEmpty()) {
            throw new RuntimeException("No restaurant can fulfill the entire order.");
        }

        IRestaurantSelectionStrategy strategy = factory.getStrategy(request.getStrategy());

        Restaurant selectedRestaurant = strategy.selectRestaurant(
                eligibleRestaurantMap, request.getOrderItems()
        );

        if (selectedRestaurant == null) {
            throw new RuntimeException("Strategy failed to select a restaurant.");
        }

        selectedRestaurant.acceptOrder();

        List<OrderedItem> orderedItems = new ArrayList<>();
        List<Menu> selectedMenus = eligibleRestaurantMap.get(selectedRestaurant);

        for (OrderItemRequest reqItem : request.getOrderItems()) {

            Menu matchedMenu = selectedMenus.stream()
                    .filter(Objects::nonNull)
                    .filter(menu -> Objects.equals(
                            normalize(menu.getItemName()),
                            normalize(reqItem.getItemName())
                    ))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Menu mismatch"));

            OrderedItem oi = new OrderedItem();
            oi.setMenu(matchedMenu);
            oi.setQuantity(reqItem.getQuantity());

            orderedItems.add(oi);
        }

        List<OrderedItem> savedOrderedItems = orderedItemRepository.saveAll(orderedItems);

        order.setOrderedItems(savedOrderedItems);
        order.setStatus(OrderStatus.ACCEPTED);

        return toDto(orderRepository.save(order));
    }


    public List<OrderDto> getAllOrders() {
        return toDtoList(orderRepository.findAll());
    }

    public OrderDto completeOrder(Long orderId) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId);
        if(Objects.isNull(order)){
            throw new OrderNotFoundException("Order not found with id " + orderId);
        }
        for (OrderedItem orderedItem : order.getOrderedItems()) {
            orderedItem.getMenu().getRestaurant().completeOrder();
        }
        order.setStatus(OrderStatus.COMPLETED);
        return toDto(order);
    }

    public OrderDto toDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setUser(userService.toDto(order.getUser()));
        orderDto.setOrderedItems(toOrderedItemDtoList(order.getOrderedItems()));
        orderDto.setStatus(order.getStatus());
        return orderDto;
    }

    public List<OrderDto> toDtoList(List<Order> orderList) {
        return orderList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public OrderedItemDto toOrderedItemDto(OrderedItem orderedItem) {
        OrderedItemDto orderedItemDto = new OrderedItemDto();
        orderedItemDto.setId(orderedItem.getId());
        orderedItemDto.setMenu(menuService.toDto(orderedItem.getMenu()));
        orderedItemDto.setQuantity(orderedItem.getQuantity());
        return orderedItemDto;
    }

    public List<OrderedItemDto> toOrderedItemDtoList(List<OrderedItem> orderedItems) {
        return orderedItems.stream().map(this::toOrderedItemDto).collect(Collectors.toList());
    }
}
