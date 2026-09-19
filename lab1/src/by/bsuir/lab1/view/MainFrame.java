package by.bsuir.lab1.view;

import by.bsuir.lab1.model.PressureModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class MainFrame extends JFrame implements PropertyChangeListener {

    private final JButton enterDataButton = new JButton("Ввести данные");
    private final JLabel lastDataLabel = new JLabel("Данные ещё не введены", SwingConstants.CENTER);
    private final JLabel resultLabel = new JLabel(" ", SwingConstants.CENTER);

    public MainFrame() {
        super("Норма артериального давления");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 260);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel();
        top.add(enterDataButton);
        add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 5, 15));
        resultLabel.setFont(resultLabel.getFont().deriveFont(Font.BOLD, 15f));
        center.add(lastDataLabel);
        center.add(resultLabel);
        add(center, BorderLayout.CENTER);

        JPanel padding = new JPanel();
        padding.setPreferredSize(new Dimension(1, 15));
        add(padding, BorderLayout.SOUTH);
    }

    public JButton getEnterDataButton() {
        return enterDataButton;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (PressureModel.PROP_DATA_CHANGED.equals(evt.getPropertyName())) {
            render((PressureModel) evt.getNewValue());
        }
    }

    private void render(PressureModel model) {
        lastDataLabel.setText(String.format(
                "<html>Дата рождения: %02d.%02d.%04d&nbsp;&nbsp;(возраст: %d лет)<br>"
                        + "Вес: %.1f кг&nbsp;&nbsp;&nbsp;Текущее давление: %d/%d</html>",
                model.getBirthDay(), model.getBirthMonth(), model.getBirthYear(), model.getAge(),
                model.getWeight(), model.getSystolic(), model.getDiastolic()));

        resultLabel.setText(String.format(
                "<html>Идеальное давление: %.0f/%.0f&nbsp;&nbsp;—&nbsp;&nbsp;%s</html>",
                model.getIdealSystolic(), model.getIdealDiastolic(), model.getStatus()));
    }
}
