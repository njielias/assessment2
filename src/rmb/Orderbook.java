package rmb;

import java.math.BigDecimal;
import java.util.*;

public class Orderbook {

    // TreeMap to store the Orders, with the price as the key and a LinkedList of Orders at that price as the value
    private TreeMap<BigDecimal, LinkedList<Order>> bidOrders;
    private TreeMap<BigDecimal, LinkedList<Order>> askOrders;

    public Orderbook() {
        this.bidOrders = new TreeMap<>(Collections.reverseOrder());
        this.askOrders = new TreeMap<>();
    }

    // Method to add a new rmb.Order to the Orderbook
    public void addOrder(Order order) {
        TreeMap<BigDecimal, LinkedList<Order>> orders;
        if (order.getSide() == Side.BUY) {
            orders = bidOrders;
        } else {
            orders = askOrders;
        }
        BigDecimal price = order.getPrice();
        LinkedList<Order> ordersAtPrice = orders.get(price);
        if (ordersAtPrice == null) {
            ordersAtPrice = new LinkedList<>();
            orders.put(price, ordersAtPrice);
        }
        ordersAtPrice.addLast(order);
    }

    // Method to delete an existing rmb.Order from the Orderbook
    public void deleteOrder(int id) {
        for (LinkedList<Order> ordersAtPrice : bidOrders.values()) {
            ordersAtPrice.removeIf(order -> order.getId() == id);
        }
        for (LinkedList<Order> ordersAtPrice : askOrders.values()) {
            ordersAtPrice.removeIf(order -> order.getId() == id);
        }
    }

    // Method to modify an existing rmb.Order in the Orderbook
    public void modifyOrder(int id, int quantity) {
        for (LinkedList<Order> ordersAtPrice : bidOrders.values()) {
            for (Order order : ordersAtPrice) {
                if (order.getId() == id) {
                    order.setQuantity(quantity);
                    ordersAtPrice.remove(order);
                    addOrder(order);
                    return;
                }
            }
        }
        for (LinkedList<Order> ordersAtPrice : askOrders.values()) {
            for (Order order : ordersAtPrice) {
                if (order.getId() == id) {
                    order.setQuantity(quantity);
                    ordersAtPrice.remove(order);
                    addOrder(order);
                    return;
                }
            }
        }
    }

    // Method to get all the Orders at a given price level and side
    public List<Order> getOrdersAtPriceLevel(BigDecimal price, Side side) {
        TreeMap<BigDecimal, LinkedList<Order>> orders;
        if (side == Side.BUY) {
            orders = bidOrders;
        } else {
            orders = askOrders;
        }
        LinkedList<Order> ordersAtPrice = orders.get(price);
        if (ordersAtPrice == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(ordersAtPrice);
    }

    // Method to get the best bid and ask prices
    public BigDecimal getBestBidPrice() {
        return bidOrders.isEmpty() ? BigDecimal.ZERO : bidOrders.firstKey();
    }

    public BigDecimal getBestAskPrice() {
        return askOrders.isEmpty() ? BigDecimal.ZERO : askOrders.firstKey();
    }


    // Method to get the total quantity at the best bid and ask prices
    public int getTotalQuantityAtBestBidPrice() {
        LinkedList<Order> ordersAtPrice = bidOrders.get(getBestBidPrice());
        if (ordersAtPrice == null) {
            return 0;
        }
        int totalQuantity = 0;
        for (Order order : ordersAtPrice) {
            totalQuantity += order.getOriginalQuantity();
        }
        return totalQuantity;
    }

    public int getTotalQuantityAtBestAskPrice() {
        LinkedList<Order> ordersAtPrice = askOrders.get(getBestAskPrice());
        if (ordersAtPrice == null) {
            return 0;
        }
        int totalQuantity = 0;
        for (Order order : ordersAtPrice) {
            totalQuantity += order.getOriginalQuantity();
        }
        return totalQuantity;
    }

    public Order getOrder(int id) {
        for (LinkedList<Order> ordersAtPrice : bidOrders.values()) {
            for (Order order : ordersAtPrice) {
                if (order.getId() == id) {
                    return order;
                }
            }
        }
        for (LinkedList<Order> ordersAtPrice : askOrders.values()) {
            for (Order order : ordersAtPrice) {
                if (order.getId() == id) {
                    return order;
                }
            }
        }
        return null;
    }


}

