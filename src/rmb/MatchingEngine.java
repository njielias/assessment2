package rmb;

import java.math.BigDecimal;
import java.util.List;

public class MatchingEngine {

    private Orderbook orderbook;

    public MatchingEngine(Orderbook orderbook) {
        this.orderbook = orderbook;
    }

    public void addOrder(Order order) {
        orderbook.addOrder(order);
        matchOrders();
    }

    public void matchOrders(){

        BigDecimal bestBidPrice = orderbook.getBestBidPrice();
        BigDecimal bestAskPrice = orderbook.getBestAskPrice();

        if(bestBidPrice != null && bestAskPrice != null && bestBidPrice.compareTo(bestAskPrice) >= 0){
            List<Order> bestBidOrders = orderbook.getOrdersAtPriceLevel(bestBidPrice, Side.BUY);
            List<Order> bestAskOrders = orderbook.getOrdersAtPriceLevel(bestAskPrice, Side.SELL);

            for(Order bidOrder : bestBidOrders){
                for(Order askOrder : bestAskOrders){
                    if(bidOrder.getFilledQuantity() == bidOrder.getOriginalQuantity()){
                        break; // all of this bid order has been filled
                    }
                    if(askOrder.getFilledQuantity() == askOrder.getOriginalQuantity()){
                        continue; // this ask order has already been filled, move to the next ask order
                    }
                    int quantityToFill = Math.min(bidOrder.getRemainingQuantity(), askOrder.getRemainingQuantity());
                    BigDecimal price = bidOrder.getPrice();
                    if(quantityToFill > 0){
                        bidOrder.fill(quantityToFill);
                        askOrder.fill(quantityToFill);
                        System.out.println("Matched " + quantityToFill + " shares at $" + price);
                    }
                }
            }
        }


    }

}



