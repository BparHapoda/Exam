
import java.util.ArrayList;
import java.util.Scanner;


public class Menu {
    private final String name;
    private boolean exit;
    private final ArrayList<MenuPunkt> menuList = new ArrayList<>();
    private final boolean horizontal;

    public Menu(String name, boolean horizontal) {
        this.name = name;
        this.horizontal = horizontal;
    }

    public void add(String name, MenuPunkt.MenuAction menuAction) {

        menuList.add(new MenuPunkt(name, menuAction));
    }


    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public void printMenu() {
        if (!horizontal) {
            System.out.println(name);
            for (int i = 0; i < menuList.size(); i++) {
                System.out.print((i + 1) + "." + menuList.get(i).getName() + "\n");
            }
        } else {
            for (int i = 0; i < menuList.size(); i++) {
                System.out.print((i + 1) + "." + menuList.get(i).getName() + "  ");
            }
        }
    }

    public Integer inputNumber() {
        int number = 0;
        Scanner scanner = new Scanner(System.in);

        try {

            number = scanner.nextInt();
        } catch (NumberFormatException e) {
            System.out.println("Введите номер пункта меню");
        }

        return number;

    }

    public Integer inputMenuNumber() {
        Integer number;
        do {
            number = inputNumber();
        }
        while (number < 1 || number > menuList.size());
        return number - 1;
    }

    public void run() {

        while (!exit) {
            System.out.print("\n");
            printMenu();
            menuList.get(inputMenuNumber()).getMenuAction().actionRun();
        }
    }
}