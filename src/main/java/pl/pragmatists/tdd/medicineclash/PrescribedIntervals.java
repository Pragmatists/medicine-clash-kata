package pl.pragmatists.tdd.medicineclash;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.Range;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrescribedIntervals {
    List<Range<LocalDate>> intervals;

    public PrescribedIntervals(List<Range<LocalDate>> intervals) {
        this.intervals = intervals;
    }

    public Collection<LocalDate> findClashes(PrescribedIntervals secondPrescribedIntervals) {
        return flatList(intervals.stream()
                .map(r -> clashWithAllFrom(r, secondPrescribedIntervals.intervals)));

    }

    private Collection<LocalDate> clashWithAllFrom(Range<LocalDate> range, List<Range<LocalDate>> others) {
        return flatList(others.stream()
                .map(r -> clashDatesBetween(range, r)));
    }

    private Collection<LocalDate> flatList(Stream<Collection<LocalDate>> stream) {
        return stream.flatMap(Collection::stream).collect(Collectors.toList());
    }

    private Set<LocalDate> clashDatesBetween(Range<LocalDate> second, Range<LocalDate> first) {
        if (first.isConnected(second)) {
            return allDatesWithin(first.intersection(second));
        }
        return Collections.emptySet();
    }

    @SuppressWarnings("unchecked")
    private Set<LocalDate> allDatesWithin(Range<LocalDate> range) {
        return ContiguousSet.create(range, new DaysDiscreteDomain());
    }

}