package com.laioffer.onlineorder.service;

import com.laioffer.onlineorder.entity.MenuItemEntity;
import com.laioffer.onlineorder.entity.RestaurantEntity;
import com.laioffer.onlineorder.model.MenuItemDto;
import com.laioffer.onlineorder.model.RestaurantDto;
import com.laioffer.onlineorder.repository.MenuItemRepository;
import com.laioffer.onlineorder.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public List<RestaurantDto> getRestaurants() {
        List<RestaurantEntity> restaurantEntities = restaurantRepository.findAll();
        List<MenuItemEntity> menuItemEntities = menuItemRepository.findAll();

//        Map<Long, List<MenuItemDto>> groupedMenuItems = new HashMap<>();
//        for (MenuItemEntity menuItemEntity : menuItemEntities) {
//            List<MenuItemDto> group = groupedMenuItems.computeIfAbsent(menuItemEntity.restaurantId(), k -> new ArrayList<>());
//            MenuItemDto menuItemDto = new MenuItemDto(menuItemEntity);
//            group.add(menuItemDto);
//        }

        Map<Long, List<MenuItemEntity>> groupedMenuItems = menuItemEntities
                .stream()
                .collect(Collectors.groupingBy(MenuItemEntity::restaurantId));

//        List<RestaurantDto> results = new ArrayList<>();
//        for (RestaurantEntity restaurantEntity : restaurantEntities) {
//            RestaurantDto restaurantDto = new RestaurantDto(restaurantEntity, groupedMenuItems.get(restaurantEntity.id()));
//            results.add(restaurantDto);
//        }

        List<RestaurantDto> results = restaurantEntities
                .stream()
                .map(restaurantEntity ->
                        new RestaurantDto(restaurantEntity, groupedMenuItems.get(restaurantEntity.id())
                                .stream()
                                .map(MenuItemDto::new)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());

        return results;
    }

}