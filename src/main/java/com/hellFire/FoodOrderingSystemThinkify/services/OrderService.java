package com.hellFire.FoodOrderingSystemThinkify.services;

import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.OrderItemRequest;
import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.OrderRequest;
import com.hellFire.FoodOrderingSystemThinkify.dtos.responses.OrderDto;
import com.hellFire.FoodOrderingSystemThinkify.dtos.responses.OrderedItemDto;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.OrderNotFoundException;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.RestaurantNotFoundException;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.UserNotFoundException;
import com.hellFire.FoodOrderingSystemThinkify.models.*;
import com.hellFire.FoodOrderingSystemThinkify.models.enums.OrderFullFilledBy;
import com.hellFire.FoodOrderingSystemThinkify.models.enums.OrderItemStatus;
import com.hellFire.FoodOrderingSystemThinkify.models.enums.OrderStatus;
import com.hellFire.FoodOrderingSystemThinkify.respositories.IOrderRepository;
import com.hellFire.FoodOrderingSystemThinkify.respositories.IOrderedItemRepository;
import com.hellFire.FoodOrderingSystemThinkify.strategies.IRestaurantSelectionStrategy;
import com.hellFire.FoodOrderingSystemThinkify.strategies.factory.StrategyFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.hellFire.FoodOrderingSystemThinkify.utils.Utils.normalize;

@Log4j2
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
    @Autowired
    private RestaurantService restaurantService;


    public OrderDto placeOrder(OrderRequest request) throws UserNotFoundException {

        AppUser appUser = userService.getAppUserById(request.getUserId());
        if(Objects.isNull(request.getOrderFullFilledBy())){
            request.setOrderFullFilledBy(OrderFullFilledBy.ONE_RESTAURANT);
        }

        List<Menu> allMenus = new ArrayList<>();
        for (OrderItemRequest itemReq : request.getOrderItems()) {
            allMenus.addAll(menuService.getMenuMapByItemName(itemReq.getItemName()));
        }

        Map<Restaurant, List<Menu>> restaurantMenuMap =
                allMenus.stream().collect(Collectors.groupingBy(Menu::getRestaurant));

        if(OrderFullFilledBy.MANY_RESTAURANTS.equals(request.getOrderFullFilledBy())){
            return fulfillOrderByManyRestaurants(request, restaurantMenuMap);
        }

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
            oi.setOrderItemStatus(OrderItemStatus.ACCEPTED);
            orderedItems.add(oi);
        }

        List<OrderedItem> savedOrderedItems = orderedItemRepository.saveAll(orderedItems);
        Order order = new Order();
        order.setUser(appUser);
        order.setFullFilledBy(OrderFullFilledBy.ONE_RESTAURANT);
        order.setOrderedItems(savedOrderedItems);
        order.setStatus(OrderStatus.ACCEPTED);
        return toDto(orderRepository.save(order));
    }

    public OrderDto fulfillOrderByManyRestaurants(
            OrderRequest request,
            Map<Restaurant, List<Menu>> restaurantMenuListMap
    ) throws UserNotFoundException {

        AppUser user = userService.getAppUserById(request.getUserId());

        IRestaurantSelectionStrategy strategy = factory.getStrategy(request.getStrategy());

        List<OrderedItem> orderedItems = new ArrayList<>();

        Set<Restaurant> usedRestaurants = new HashSet<>();

        for (OrderItemRequest itemReq : request.getOrderItems()) {

            List<Menu> menusForItem =
                    restaurantMenuListMap.values().stream()
                            .flatMap(List::stream)
                            .filter(menu ->
                                    normalize(menu.getItemName()).equals(
                                            normalize(itemReq.getItemName())
                                    )
                            )
                            .toList();

            if (menusForItem.isEmpty()) {
                throw new RuntimeException("Item not available anywhere: " + itemReq.getItemName());
            }
            Map<Restaurant, List<Menu>> seletedRestaurantMap = menusForItem.stream().collect(Collectors.groupingBy(Menu::getRestaurant));
            Restaurant selectedRestaurant = strategy.selectRestaurant(seletedRestaurantMap, List.of(itemReq));

            if(Objects.isNull(selectedRestaurant)){
                throw new RuntimeException("No restaurant available for item: " + itemReq.getItemName());
            }

            Menu selectedMenu = seletedRestaurantMap.get(selectedRestaurant).stream()
                    .filter(Objects::nonNull)
                    .filter(menu -> normalize(menu.getItemName()).equals(normalize(itemReq.getItemName())))
                    .findFirst().orElse(null);

            if (selectedMenu == null) {
                throw new RuntimeException("No restaurant available for item: " + itemReq.getItemName());
            }

            if (!usedRestaurants.contains(selectedRestaurant)) {
                selectedRestaurant.acceptOrder();
                usedRestaurants.add(selectedRestaurant);
            }

            OrderedItem oi = new OrderedItem();
            oi.setMenu(selectedMenu);
            oi.setOrderItemStatus(OrderItemStatus.ACCEPTED);
            oi.setQuantity(itemReq.getQuantity());

            orderedItems.add(oi);
        }

        List<OrderedItem> savedItems = orderedItemRepository.saveAll(orderedItems);
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.ACCEPTED);
        order.setFullFilledBy(OrderFullFilledBy.MANY_RESTAURANTS);
        order.setOrderedItems(savedItems);
        return toDto(orderRepository.save(order));
    }

    public List<OrderDto> getAllOrders() {
        return toDtoList(orderRepository.findAll());
    }

    @Deprecated
    public OrderDto completeOrder(Long orderId) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId);
        if(Objects.isNull(order)){
            throw new OrderNotFoundException("Order not found with id " + orderId);
        }
        if(OrderFullFilledBy.MANY_RESTAURANTS.equals(order.getFullFilledBy())){
            for(OrderedItem oi : order.getOrderedItems()){
                if(!OrderItemStatus.COMPLETED.equals(oi.getOrderItemStatus())){
                    throw new RuntimeException("All order items are not completed yet");
                }
            }
        }
        boolean allOrderItemCompleted = true;
        for (OrderedItem orderedItem : order.getOrderedItems()) {
            if (!OrderItemStatus.COMPLETED.equals(orderedItem.getOrderItemStatus())) {
                allOrderItemCompleted = false;
                break;
            }
        }
        if(allOrderItemCompleted){
            order.getOrderedItems().get(0).getMenu().getRestaurant().completeOrder();
        }
        order.setStatus(OrderStatus.COMPLETED);
        return toDto(order);
    }

    public OrderedItemDto completeOrderItem(Long orderId, Long orderItemId, Long restaurantId)
            throws OrderNotFoundException, RestaurantNotFoundException {

        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new OrderNotFoundException("Order not found with id " + orderId);
        }

        Restaurant restaurant = restaurantService.getById(restaurantId);

        OrderedItem orderedItem = order.getOrderedItems().stream()
                .filter(oi -> oi.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException("Order item not found with id " + orderItemId));

        if (!orderedItem.getMenu().getRestaurant().getId().equals(restaurantId)) {
            throw new RuntimeException("Restaurant not allowed to complete this item");
        }

        orderedItem.setOrderItemStatus(OrderItemStatus.COMPLETED);
        orderedItemRepository.save(orderedItem);

        checkRestaurantCompletion(order, restaurant);
        checkFullOrderCompletion(order);

        return toOrderedItemDto(orderedItem);
    }

    private void checkFullOrderCompletion(Order order) {

        boolean fullCompleted = order.getOrderedItems().stream()
                .allMatch(oi -> oi.getOrderItemStatus() == OrderItemStatus.COMPLETED);

        if (fullCompleted) {
            order.setStatus(OrderStatus.COMPLETED);
        }
    }

    private void checkRestaurantCompletion(Order order, Restaurant restaurant) {

        List<OrderedItem> itemsOfRestaurant = order.getOrderedItems().stream()
                .filter(oi -> oi.getMenu().getRestaurant().equals(restaurant))
                .toList();

        boolean allCompleted = itemsOfRestaurant.stream()
                .allMatch(oi -> oi.getOrderItemStatus() == OrderItemStatus.COMPLETED);

        if (allCompleted) {
            restaurant.completeOrder();
        }
    }


    public OrderDto toDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setUser(userService.toDto(order.getUser()));
        orderDto.setOrderedItems(toOrderedItemDtoList(order.getOrderedItems()));
        orderDto.setStatus(order.getStatus());
        orderDto.setFullFilledBy(order.getFullFilledBy());
        return orderDto;
    }

    public List<OrderDto> toDtoList(List<Order> orderList) {
        return orderList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public OrderedItemDto toOrderedItemDto(OrderedItem orderedItem) {
        OrderedItemDto orderedItemDto = new OrderedItemDto();
        orderedItemDto.setId(orderedItem.getId());
        orderedItemDto.setRestaurant(restaurantService.toDto(orderedItem.getMenu().getRestaurant()));
        orderedItemDto.setMenu(menuService.toDto(orderedItem.getMenu()));
        orderedItemDto.setQuantity(orderedItem.getQuantity());
        orderedItemDto.setOrderItemStatus(orderedItem.getOrderItemStatus());
        return orderedItemDto;
    }

    public List<OrderedItemDto> toOrderedItemDtoList(List<OrderedItem> orderedItems) {
        return orderedItems.stream().map(this::toOrderedItemDto).collect(Collectors.toList());
    }
}
