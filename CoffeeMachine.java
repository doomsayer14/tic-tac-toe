package machine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CoffeeMachine {

    //fields
    private int water;
    private int milk;
    private int beans;
    private int cups;
    private int money;

    //buy, fill, take, remaining, exit
    private String action;

    private BufferedReader reader;

    //Constructor
    /*
    At the start, the coffee machine has $550,
    400 ml of water, 540 ml of milk,
    120 g of coffee beans, and 9 disposable cups.
     */
    public CoffeeMachine() {
        water = 400;
        milk = 540;
        beans = 120;
        cups = 9;
        money = 550;

        action = "";
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    //getter and setters
    //----------------------------------------------------
    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public int getMilk() {
        return milk;
    }

    public void setMilk(int milk) {
        this.milk = milk;
    }

    public int getBeans() {
        return beans;
    }

    public void setBeans(int beans) {
        this.beans = beans;
    }

    public int getCups() {
        return cups;
    }

    public void setCups(int cups) {
        this.cups = cups;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
    //----------------------------------------------------

    public static void main(String[] args) {
        new CoffeeMachine().startCoffeeMachine();
    }

    public void startCoffeeMachine() {
        while (true) {
            System.out.println("Write action (buy, fill, take, remaining, exit):");
            try {
                setAction(reader.readLine());       //saving action
                if (getAction().equals("exit")) {   //condition of losing program
                    return;
                }
            } catch (IOException e) {
                System.out.println("Wrong action!");
            }
            doAction();
        }
    }

    //depends on user's choice of action performs it
    private void doAction() {
        switch (getAction()) {
            case "buy":
                buy();
                break;
            case "fill":
                fill();
                break;
            case "take":
                take();
                break;
            case "remaining":
                showStatus();
            default:
                System.out.println("Please, type right action");
        }
    }

    //choose, buy and take chosen coffee
    private void buy() {
        String option;
        try {
            System.out.println("What do you want to buy? 1 - espresso," +
                    " 2 - latte, 3 - cappuccino, back - to main menu:");
            option = reader.readLine();
            switch (option) {
                case "1":       //espresso
                    makeCoffee(250, 0, 16, 4);
                    break;
                case "2":       //latte
                    makeCoffee(350, 75, 20, 7);
                    break;
                case "3":       //cappuccino
                    makeCoffee(200, 100, 12, 6);
                    break;
                case "break":   //to main menu
                    return;
                default:
                    System.out.println("Please, choose number of an option to buy a coffee");
                    break;
            }
        } catch (IOException e) {
            System.out.println("Please, choose right option to buy a coffee");
        }
    }

    //literally makes coffee and refreshes coffee machine status
    private void makeCoffee(int water, int milk, int beans, int money) {
        if (checkIngredients(water, milk, beans, money)) {
            System.out.println("I have enough resources, making you a coffee!");
            setWater(getWater() - water);
            setMilk(getMilk() - milk);
            setBeans(getBeans() - beans);
            setCups(getCups() - 1);
            setMoney(getMoney() + money);
            return;
        }
        System.out.println("Sorry, not enough water!");
    }

    //checks if the coffee machine can make coffee
    //true if can
    //false if not
    private boolean checkIngredients(int water, int milk, int beans, int money) {
        return water <= getWater() && milk <= getMilk() &&
                beans <= getBeans() && money <= getMoney() &&
                getCups() > 0;
    }

    //method for a man, who want to fill coffee machine with the ingredients
    private void fill() {
        int temp;
        try {
            System.out.println("Write how many ml of water you want to add: ");
            temp = Integer.parseInt(reader.readLine());
            setWater(getWater() + temp);
            System.out.println("Write how many ml of milk you want to add: ");
            temp = Integer.parseInt(reader.readLine());
            setMilk(getMilk() + temp);
            System.out.println("Write how many grams of coffee beans you want to add: ");
            temp = Integer.parseInt(reader.readLine());
            setBeans(getBeans() + temp);
            System.out.println("Write how many disposable cups of coffee you want to add: ");
            temp = Integer.parseInt(reader.readLine());
            setCups(getCups() + temp);
        } catch (IOException e) {
            System.out.println("Something went wrong, try to fill on more time");
        }
    }

    //withdraw money
    private void take() {
        System.out.println("I gave you $" + getMoney());
        setMoney(0);
    }

    //prints actual values of the coffee machine fields
    public void showStatus() {
        System.out.println("The coffee machine has:");
        System.out.println(getWater() + " ml of water");
        System.out.println(getMilk() + " ml of milk");
        System.out.println(getBeans() + " g of coffee beans");
        System.out.println(getCups() + " disposable cups");
        System.out.println("$" + getMoney() + "  of money");
    }
}
