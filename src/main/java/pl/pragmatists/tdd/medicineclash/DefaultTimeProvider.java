package pl.pragmatists.tdd.medicineclash;

import java.time.LocalDate;

public class DefaultTimeProvider implements TimeProvider {
    @Override
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}
