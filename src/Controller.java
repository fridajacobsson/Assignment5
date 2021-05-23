public class Controller {

    private OrderClient orderClient;

    public Controller(OrderClient orderClient) {
        this.orderClient = orderClient;
        createTestOrder();
    }

    private void createTestOrder() {
        Order order = new Order();
        order.addItemToOrder(new OrderItem("Sandwich", "Bread, meat, cheese, salad, vegetables, sause", 23));
        order.addItemToOrder(new OrderItem("Borscht", "Beetroot, cabbage potato, beef", 84));
        order.addItemToOrder(new OrderItem("Coffe", "Hot, black, good", 18));
        orderClient.sendRequest(order);
    }
}
