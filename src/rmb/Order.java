package rmb;

import java.math.BigDecimal;
import java.util.Objects;

public class Order {
    private int id;
    private BigDecimal price;
    private int originalQuantity; // added field to store original quantity
    private int filledQuantity; // added field to store filled quantity
    private Side side;

    public Order(int id, BigDecimal price, int quantity, Side side) {
        this.id = id;
        this.price = price;
        this.originalQuantity = quantity; // initialize original quantity
        this.filledQuantity = 0; // initialize filled quantity to zero
        this.side = side;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getOriginalQuantity() {
        return originalQuantity;
    }

    public int getFilledQuantity() {
        return filledQuantity;
    }

    public void setQuantity(int quantity) {
        this.originalQuantity = quantity;
    }

    public Side getSide() {
        return side;
    }

    public boolean isFilled() {
        return filledQuantity == originalQuantity;
    }


    public int getRemainingQuantity() {
        return originalQuantity - filledQuantity;
    }

    public void fill(int quantity) {
        filledQuantity += quantity;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Order order = (Order) obj;
        return id == order.id &&
                Objects.equals(price, order.price) &&
                side == order.side;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, side);
    }

    @Override
    public String toString() {
        return "rmb.Order{" +
                "id=" + id +
                ", price=" + price +
                ", originalQuantity=" + originalQuantity +
                ", filledQuantity=" + filledQuantity +
                ", side=" + side +
                '}';
    }
}