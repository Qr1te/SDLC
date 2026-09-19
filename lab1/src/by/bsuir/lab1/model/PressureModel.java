package by.bsuir.lab1.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;

public class PressureModel {

    public static final String PROP_DATA_CHANGED = "dataChanged";

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private boolean hasData = false;

    private int birthDay;
    private int birthMonth;
    private int birthYear;
    private double weight;
    private int systolic;
    private int diastolic;

    private int age;
    private double idealSystolic;
    private double idealDiastolic;
    private String status;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public boolean hasData() {
        return hasData;
    }

    public int getBirthDay() {
        return birthDay;
    }

    public int getBirthMonth() {
        return birthMonth;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public double getWeight() {
        return weight;
    }

    public int getSystolic() {
        return systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public int getAge() {
        return age;
    }

    public double getIdealSystolic() {
        return idealSystolic;
    }

    public double getIdealDiastolic() {
        return idealDiastolic;
    }

    public String getStatus() {
        return status;
    }

    public void updateData(int day, int month, int year, double weight, int systolic, int diastolic)
            throws InvalidInputException {

        LocalDate birthDate = validateDate(day, month, year);
        validateWeight(weight);
        validatePressure(systolic, diastolic);

        this.birthDay = day;
        this.birthMonth = month;
        this.birthYear = year;
        this.weight = weight;
        this.systolic = systolic;
        this.diastolic = diastolic;

        this.age = Period.between(birthDate, LocalDate.now()).getYears();
        calculateIdealPressure();
        calculateStatus();
        this.hasData = true;

        pcs.firePropertyChange(PROP_DATA_CHANGED, null, this);
    }

    private LocalDate validateDate(int day, int month, int year) throws InvalidInputException {
        try {
            LocalDate date = LocalDate.of(year, month, day);
            if (date.isAfter(LocalDate.now())) {
                throw new InvalidInputException("Дата рождения не может быть в будущем.");
            }
            if (year < 1900) {
                throw new InvalidInputException("Год рождения указан некорректно.");
            }
            return date;
        } catch (DateTimeException e) {
            throw new InvalidInputException("Некорректная дата рождения (например, 31 февраля).");
        }
    }

    private void validateWeight(double weight) throws InvalidInputException {
        if (weight <= 0 || weight > 400) {
            throw new InvalidInputException("Вес указан некорректно (допустимый диапазон: 0–400 кг).");
        }
    }

    private void validatePressure(int systolic, int diastolic) throws InvalidInputException {
        if (systolic <= 0 || systolic > 300 || diastolic <= 0 || diastolic > 200) {
            throw new InvalidInputException("Значения давления указаны некорректно.");
        }
        if (systolic <= diastolic) {
            throw new InvalidInputException("Верхнее (систолическое) давление должно быть больше нижнего.");
        }
    }

    private void calculateIdealPressure() {
        idealSystolic = 109 + 0.5 * age + 0.1 * weight;
        idealDiastolic = 63 + 0.1 * age + 0.15 * weight;
    }

    private void calculateStatus() {
        final double TOLERANCE = 10.0;
        double deltaSys = systolic - idealSystolic;
        double deltaDia = diastolic - idealDiastolic;

        if (deltaSys > TOLERANCE || deltaDia > TOLERANCE) {
            status = "Повышенное давление";
        } else if (deltaSys < -TOLERANCE || deltaDia < -TOLERANCE) {
            status = "Пониженное давление";
        } else {
            status = "Норма";
        }
    }
}
