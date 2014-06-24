package pl.pragmatists.tdd.medicineclash;

import java.time.LocalDate;

public interface TimeProvider {
    LocalDate getCurrentDate();
}
