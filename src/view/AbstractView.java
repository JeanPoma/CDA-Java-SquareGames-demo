package view;

import java.util.Scanner;

public abstract class AbstractView implements Viewable{

    private final Scanner scanner = new Scanner(System.in);


    public String waitForAnswer(){
        return scanner.nextLine();
    }

    @Override
    public void show(String message) {
        System.out.println(message);
    }

    @Override
    public void showError(String message) {
        System.err.println(message);
    }
}
