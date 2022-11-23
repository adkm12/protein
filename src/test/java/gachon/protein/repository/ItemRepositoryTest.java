package gachon.protein.repository;

import com.querydsl.core.BooleanBuilder;
import gachon.protein.constant.ItemSellStatus;
import gachon.protein.entity.Item;
import gachon.protein.entity.QItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;
import org.springframework.test.context.TestPropertySource;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;



@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품저장 테스트")
    public void createItemTest() {
        Item item = new Item();
        item.setItemName("테스트상품");
        item.setPrice(10000);
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setItemDetail("테스트 상품 설명");
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }

    public void createItemList() {
        for (int i = 0; i < 10; i++) {
            Item item = new Item();
            item.setItemName("테스트상품" + i);
            item.setPrice(10000 + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setItemDetail("테스트 상품 설명" + i);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }

    public void createItemList2() {
        for (int i = 0; i < 5; i++) {
            Item item = new Item();
            item.setItemName("테스트상품" + i);
            item.setPrice(10000 + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setItemDetail("테스트 상품 설명" + i);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
        for (int i = 5; i < 10; i++) {
            Item item = new Item();
            item.setItemName("테스트상품" + i);
            item.setPrice(10000 + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setItemDetail("테스트 상품 설명" + i);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNameTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemName("테스트상품1");
        for (Item i :
                itemList) {
            System.out.println(i.toString());

        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNameOrItemDetailTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNameOrItemDetail("테스트상품1", "테스트 상품 설명5");
        for (Item i :
                itemList) {
            System.out.println(i.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10003);
        for (Item i :
                itemList) {
            System.out.println(i.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 내림차순 테스트")
    public void findByLessThanOrderByPriceDescTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10004);
        for (Item i :
                itemList) {
            System.out.println(i.toString());
        }
    }

    @Test
    @DisplayName("@Query 상품상세설명 검색 테스트")
    public void findByItemDetailTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 설명");
        for (Item i : itemList) {
            System.out.println(i.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest() {
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem).where(qItem.itemSellStatus.eq(ItemSellStatus.SELL)).where(qItem.itemDetail.like("%" +
                "테스트 상품 설명" +
                "%")).orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();

        for (Item i :
                itemList) {
            System.out.println(i.toString());
        }
    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트 2")
    public void queryDslTest2() {
        this.createItemList2();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String itemDetail = "테스트 상품 설명";
        int price = 10003;
        String itemSellStat = "SELL";
        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));

        if (StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) {
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0, 5);
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + itemPagingResult.getTotalElements());

        List<Item> resultItemList = itemPagingResult.getContent();
        for (Item i :
                resultItemList) {
            System.out.println(i.toString());
        }
    }
}