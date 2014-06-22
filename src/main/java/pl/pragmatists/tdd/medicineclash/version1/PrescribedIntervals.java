package pl.pragmatists.tdd.medicineclash.version1;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PrescribedIntervals {
    List<Range> intervals;

    public PrescribedIntervals(List<Range> intervals) {
        this.intervals = intervals;
    }

    public Collection<LocalDate> findClashes(PrescribedIntervals secondPrescribedIntervals) {
        return intervals.stream()
                .map(r -> clashWithAllFrom(r, secondPrescribedIntervals.intervals))
                .flatMap(s -> s.stream())
                .collect(Collectors.toList());
    }

    private List<LocalDate> clashWithAllFrom(Range range, List<Range> others) {
        return others.stream()
                .map(r -> clashDatesBetween(range, r))
                .flatMap(s -> s.stream())
                .collect(Collectors.toList());
    }

    private Set<LocalDate> clashDatesBetween(Range second, Range first) {
        if (first.isConnected(second)) {
            return allDatesWithin(first.intersection(second));
        }
        return Collections.emptySet();
    }

    private Set<LocalDate> allDatesWithin(Range range) {
        return ContiguousSet.create(range, new DaysDiscreteDomain());
    }

    private class DaysDiscreteDomain extends DiscreteDomain {
        @Override
        public Comparable next(Comparable comparable) {
            LocalDate date = (LocalDate) comparable;
            return date.plusDays(1);
        }

        @Override
        public Comparable previous(Comparable comparable) {
            LocalDate date = (LocalDate) comparable;
            return date.minusDays(1);
        }

        @Override
        public long distance(Comparable comparable, Comparable comparable2) {
            LocalDate date1 = (LocalDate) comparable;
            LocalDate date2 = (LocalDate) comparable2;
            return ChronoUnit.DAYS.between(date1, date2);
        }
    }
}