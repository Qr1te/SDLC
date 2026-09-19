package by.bsuir.lab1.controller;

import by.bsuir.lab1.model.InvalidInputException;
import by.bsuir.lab1.model.PressureModel;
import by.bsuir.lab1.view.InputDialog;
import by.bsuir.lab1.view.MainFrame;
import java.time.LocalDate;
import javax.swing.*;

public class Controller {

    private final PressureModel model;
    private final MainFrame view;

    public Controller(PressureModel model, MainFrame view) {
        this.model = model;
        this.view = view;

        model.addPropertyChangeListener(view);

        view.getEnterDataButton().addActionListener(e -> openInputDialog());
    }

    private void openInputDialog() {
        InputDialog dialog = new InputDialog(view);

        if (model.hasData()) {
            dialog.setInitialValues(model.getBirthDay(), model.getBirthMonth(), model.getBirthYear(),
                    model.getWeight(), model.getSystolic(), model.getDiastolic());
        } else {
            LocalDate defaultDate = LocalDate.now().minusYears(25);
            dialog.setInitialValues(defaultDate.getDayOfMonth(), defaultDate.getMonthValue(),
                    defaultDate.getYear(), 70, 120, 80);
        }

        dialog.getOkButton().addActionListener(e -> handleSubmit(dialog));
        dialog.setVisible(true);
    }

    private void handleSubmit(InputDialog dialog) {
        try {
            int day = dialog.getSelectedDay();
            int month = dialog.getSelectedMonth();
            int year = dialog.getSelectedYear();
            double weight = Double.parseDouble(dialog.getWeightText().replace(',', '.'));
            int systolic = Integer.parseInt(dialog.getSystolicText());
            int diastolic = Integer.parseInt(dialog.getDiastolicText());

            model.updateData(day, month, year, weight, systolic, diastolic);
            dialog.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog,
                    "Вес и давление должны быть числами.",
                    "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(dialog, ex.getMessage(),
                    "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
        }
    }
}
