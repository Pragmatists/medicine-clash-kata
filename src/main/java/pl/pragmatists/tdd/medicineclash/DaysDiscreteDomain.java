package pl.pragmatists.tdd.medicineclash;

import com.google.common.collect.DiscreteDomain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

class DaysDiscreteDomain extends DiscreteDomain<LocalDate> {

    @Override
    public LocalDate next(LocalDate value) {
        return value.plusDays(1);
    }

    @Override
    public LocalDate previous(LocalDate value) {
        return value.minusDays(1);
    }

    @Override
    public long distance(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }
}
