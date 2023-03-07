package test;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rmb.MatchingEngine;
import rmb.Order;
import rmb.Orderbook;
import rmb.Side;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatchEngineTest {

    private Orderbook orderbook;
    private MatchingEngine matchingEngine;

    @BeforeEach
    void setUp() {
        orderbook = new Orderbook();
        matchingEngine = new MatchingEngine(orderbook);
    }

    @Test
    void addOrder_NewOrderAtBestBidPrice_FillsExistingOrderFully() {

        // given
        orderbook.addOrder(new Order(1, new BigDecimal("100"), 10, Side.BUY)); // best bid price
        orderbook.addOrder(new Order(2, new BigDecimal("101"), 10, Side.SELL)); // best ask price

        // when
        matchingEngine.addOrder(new Order(3, new BigDecimal("100"), 20, Side.SELL));

        // then
        assertEquals(1, orderbook.getOrdersAtPriceLevel(new BigDecimal("100"), Side.BUY).size());
        assertEquals(1, orderbook.getOrdersAtPriceLevel(new BigDecimal("101"), Side.SELL).size());
        assertEquals(10, orderbook.getOrder(1).getFilledQuantity());
        assertEquals(10, orderbook.getOrder(3).getFilledQuantity());
    }

    @Test
    void addOrder_NewOrderAtBestAskPrice_FillsExistingOrderFully() {
        // given
        orderbook.addOrder(new Order(1, new BigDecimal("100"), 10, Side.BUY));
        orderbook.addOrder(new Order(2, new BigDecimal("99"), 10, Side.SELL)); // best ask price

        // when
        matchingEngine.addOrder(new Order(3, new BigDecimal("99"), 20, Side.BUY));

        // then
        assertEquals(1, orderbook.getOrdersAtPriceLevel(new BigDecimal("99"), Side.SELL).size());
        assertEquals(1, orderbook.getOrdersAtPriceLevel(new BigDecimal("100"), Side.BUY).size());
        assertEquals(10, orderbook.getOrder(1).getFilledQuantity());
        assertEquals(0, orderbook.getOrder(3).getFilledQuantity());
    }

    @Test
    void addOrder_NewOrderAtBestBidPrice_FillsExistingOrderPartially() {
        // given
        orderbook.addOrder(new Order(1, new BigDecimal("100"), 10, Side.BUY)); // best bid price
        orderbook.addOrder(new Order(2, new BigDecimal("101"), 20, Side.SELL)); // best ask price

        // when
        matchingEngine.addOrder(new Order(3, new BigDecimal("100"), 15, Side.SELL));

        // then
        assertEquals(1, orderbook.getOrdersAtPriceLevel(new BigDecimal("100"), Side.BUY).size());
        assertEquals(1, orderbook.getOrdersAtPriceLevel(new BigDecimal("101"), Side.SELL).size());
        assertEquals(20, orderbook.getOrder(2).getOriginalQuantity());
//        assertEquals(15, orderbook.getOrder(3).getFilledQuantity());
    }

}