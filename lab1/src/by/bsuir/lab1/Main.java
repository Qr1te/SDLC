package by.bsuir.lab1;

import by.bsuir.lab1.controller.Controller;
import by.bsuir.lab1.model.PressureModel;
import by.bsuir.lab1.view.MainFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PressureModel model = new PressureModel();
            MainFrame view = new MainFrame();
            new Controller(model, view);
            view.setVisible(true);
        });
    }
}
