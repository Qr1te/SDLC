package by.bsuir.lab1.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Диалог ввода даты рождения, веса и текущего давления.
 * Дата рождения вводится тремя обычными JComboBox (день / месяц / год),
 * без использования стандартного компонента выбора даты.
 */
public class InputDialog extends JDialog {

    private final JComboBox<Integer> dayBox;
    private final JComboBox<String> monthBox;
    private final JComboBox<Integer> yearBox;
    private final JTextField weightField = new JTextField(8);
    private final JTextField systolicField = new JTextField(5);
    private final JTextField diastolicField = new JTextField(5);
    private final JButton okButton = new JButton("ОК");
    private final JButton cancelButton = new JButton("Отмена");

    public InputDialog(JFrame owner) {
        super(owner, "Ввод данных", true);
        setLayout(new GridBagLayout());
        setResizable(false);

        Integer[] days = new Integer[31];
        for (int i = 0; i < 31; i++) {
            days[i] = i + 1;
        }
        dayBox = new JComboBox<>(days);

        String[] months = new String[12];
        for (int i = 0; i < 12; i++) {
            months[i] = capitalize(Month.of(i + 1).getDisplayName(TextStyle.FULL, new Locale("ru")));
        }
        monthBox = new JComboBox<>(months);

        int currentYear = LocalDate.now().getYear();
        Integer[] years = new Integer[100];
        for (int i = 0; i < 100; i++) {
            years[i] = currentYear - i;
        }
        yearBox = new JComboBox<>(years);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        addRow(c, 0, "Дата рождения:", datePanel());
        addRow(c, 1, "Вес (кг):", weightField);
        addRow(c, 2, "Верхнее (систолическое) давление:", systolicField);
        addRow(c, 3, "Нижнее (диастолическое) давление:", diastolicField);

        JPanel buttons = new JPanel();
        buttons.add(okButton);
        buttons.add(cancelButton);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        add(buttons, c);

        cancelButton.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(owner);
    }

    private static String capitalize(String s) {
        return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private JPanel datePanel() {
        JPanel p = new JPanel();
        p.add(dayBox);
        p.add(monthBox);
        p.add(yearBox);
        return p;
    }

    private void addRow(GridBagConstraints c, int row, String label, Component field) {
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = row;
        add(new JLabel(label), c);
        c.gridx = 1;
        add(field, c);
    }

    /** Восстанавливает ранее введённые значения (требование задания). */
    public void setInitialValues(int day, int month, int year, double weight, int systolic, int diastolic) {
        dayBox.setSelectedItem(day);
        monthBox.setSelectedIndex(month - 1);
        yearBox.setSelectedItem(year);
        weightField.setText(trimNumber(weight));
        systolicField.setText(String.valueOf(systolic));
        diastolicField.setText(String.valueOf(diastolic));
    }

    private static String trimNumber(double value) {
        if (value == Math.floor(value)) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    public int getSelectedDay() {
        return (Integer) dayBox.getSelectedItem();
    }

    public int getSelectedMonth() {
        return monthBox.getSelectedIndex() + 1;
    }

    public int getSelectedYear() {
        return (Integer) yearBox.getSelectedItem();
    }

    public String getWeightText() {
        return weightField.getText().trim();
    }

    public String getSystolicText() {
        return systolicField.getText().trim();
    }

    public String getDiastolicText() {
        return diastolicField.getText().trim();
    }

    public JButton getOkButton() {
        return okButton;
    }
}
