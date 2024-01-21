package com.example.jpashop.entity;

import com.example.jpashop.constant.ItemSellStatus;
import com.example.jpashop.constant.OrderStatus;
import com.example.jpashop.repository.ItemRepository;
import com.example.jpashop.repository.MemberRepository;
import com.example.jpashop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@Transactional
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem() {
        return Item.builder()
                .itemNm("테스트 상품")
                .price(10000)
                .itemDetail("상세설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .stockNumber(100)
                .regTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now()).build();
    }

    public OrderItem createOrderItem(Order order, Item item) {
        return OrderItem.builder()
                .item(item)
                .order(order)
                .count(10)
                .orderPrice(1000)
                .regTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    void cascadeTest() {

        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.ORDER)
                .regTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();


        for (int i = 0; i < 3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);

            OrderItem orderItem = createOrderItem(order, item);
            order.getOrderItems().add(orderItem);
        }
        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, savedOrder.getOrderItems().size());
    }


    public Order createOrder() {
        Member member = Member.builder()
                .build();
        memberRepository.save(member);

        Order order = Order.builder()
                .member(member)
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.ORDER)
                .regTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        for (int i = 0; i < 3; i++) {
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = createOrderItem(order, item);
            order.getOrderItems().add(orderItem);
        }

        return order;
    }

    @Test
    @DisplayName("고아 객체 제거 테스트")
    void orphanRemovalTest() {
        Order order = createOrder();
        order.getOrderItems().remove(0);
        em.flush();
    }
}
