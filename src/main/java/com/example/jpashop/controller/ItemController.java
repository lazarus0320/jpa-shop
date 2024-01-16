package com.example.jpashop.controller;

import com.example.jpashop.dto.ItemDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class ItemController {

    @GetMapping("/ex01")
    public ResponseEntity<ItemDto> viewExample() {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemNm("테스트 상품3");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());

        return ResponseEntity.ok(itemDto);
    }
}
