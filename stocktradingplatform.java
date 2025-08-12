import java.text.DecimalFormat;
import java.util.*;

class Stock {
    private String symbol;
    private double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public void updatePrice(double newPrice) {
        this.price = newPrice;
    }
}

class Portfolio {
    private Map<String, Integer> holdings;
    private double balance;
    private DecimalFormat df = new DecimalFormat("#.00");

    public Portfolio(double startingBalance) {
        holdings = new HashMap<>();
        this.balance = startingBalance;
    }

    public void buyStock(Stock stock, int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }
        double cost = stock.getPrice() * quantity;
        if (cost <= balance) {
            balance -= cost;
            holdings.put(stock.getSymbol(), holdings.getOrDefault(stock.getSymbol(), 0) + quantity);
            System.out.println("Bought " + quantity + " shares of " + stock.getSymbol());
        } else {
            System.out.println("Insufficient funds to buy " + quantity + " shares of " + stock.getSymbol());
        }
    }

    public void sellStock(Stock stock, int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }
        if (holdings.getOrDefault(stock.getSymbol(), 0) >= quantity) {
            holdings.put(stock.getSymbol(), holdings.get(stock.getSymbol()) - quantity);
            if (holdings.get(stock.getSymbol()) == 0) {
                holdings.remove(stock.getSymbol());
            }
            balance += stock.getPrice() * quantity;
            System.out.println("Sold " + quantity + " shares of " + stock.getSymbol());
        } else {
            System.out.println("Not enough shares to sell.");
        }
    }

    public void displayPortfolio(Map<String, Stock> market) {
        System.out.println("\n--- Portfolio ---");
        System.out.println("Cash Balance: $" + df.format(balance));
        double totalValue = balance;
        for (String symbol : holdings.keySet()) {
            int qty = holdings.get(symbol);
            double value = market.get(symbol).getPrice() * qty;
            totalValue += value;
            System.out.println(symbol + ": " + qty + " shares (Value: $" + df.format(value) + ")");
        }
        System.out.println("Total Portfolio Value: $" + df.format(totalValue));
        System.out.println("-----------------\n");
    }
}

public class stocktradingplatform {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DecimalFormat df = new DecimalFormat("#.00");

        Map<String, Stock> market = new HashMap<>();
        market.put("AAPL", new Stock("AAPL", 150.0));
        market.put("GOOG", new Stock("GOOG", 2800.0));
        market.put("TSLA", new Stock("TSLA", 700.0));

        Portfolio portfolio = new Portfolio(5000.0);
        Random rand = new Random();

        int choice;
        do {
            System.out.println("=== Stock Trading Platform ===");
            System.out.println("1. View Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\n--- Market Data ---");
                    for (Stock s : market.values()) {
                        System.out.println(s.getSymbol() + " : $" + df.format(s.getPrice()));
                    }
                    System.out.println("-------------------\n");
                    break;

                case 2:
                    System.out.print("Enter stock symbol to buy: ");
                    String buySymbol = sc.next().toUpperCase();
                    if (market.containsKey(buySymbol)) {
                        System.out.print("Enter quantity: ");
                        int qty = sc.nextInt();
                        portfolio.buyStock(market.get(buySymbol), qty);
                    } else {
                        System.out.println("Stock not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter stock symbol to sell: ");
                    String sellSymbol = sc.next().toUpperCase();
                    if (market.containsKey(sellSymbol)) {
                        System.out.print("Enter quantity: ");
                        int qty = sc.nextInt();
                        portfolio.sellStock(market.get(sellSymbol), qty);
                    } else {
                        System.out.println("Stock not found.");
                    }
                    break;

                case 4:
                    portfolio.displayPortfolio(market);
                    break;

                case 5:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

            // Random price changes after each turn
            for (Stock s : market.values()) {
                double changePercent = (rand.nextDouble() - 0.5) * 0.1; // ±5%
                s.updatePrice(s.getPrice() * (1 + changePercent));
            }

        } while (choice != 5);

        sc.close();
    }
}
