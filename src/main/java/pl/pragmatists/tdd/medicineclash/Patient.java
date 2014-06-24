package pl.pragmatists.tdd.medicineclash;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class Patient {

    private Collection<Medicine> medicines = new ArrayList<>();
    private TimeProvider timeProvider;

    public Patient(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public void addMedicine(Medicine medicine) {
        this.medicines.add(medicine);
    }

    public Collection<LocalDate> clash(Collection<String> medicineNames) {
        return clash(medicineNames, 90);
    }

    public Collection<LocalDate> clash(final Collection<String> medicineNames, int daysBack) {
        if (medicineNames.isEmpty())
            return newArrayList();

        List<Medicine> filtered = onlyThoseWithNamesIn(medicineNames);
        return filtered.stream()
                .map(m -> m.clashesWith(medicinesWithout(m, filtered)))
                .flatMap(Collection::stream)
                .filter(d -> isAfter(daysBack, d))
                .collect(toSet());
    }

    private boolean isAfter(int daysBack, LocalDate date) {
        return date.isAfter(timeProvider.getCurrentDate().minusDays(daysBack));
    }

    private List<Medicine> onlyThoseWithNamesIn(Collection<String> medicineNames) {
        return medicines.stream().filter(m -> medicineNames.contains(m.name)).collect(toList());
    }

    private Collection<Medicine> medicinesWithout(Medicine medicine, Collection<Medicine> medicineCollection) {
        return medicineCollection.stream().filter(m -> !m.equals(medicine)).collect(toList());
    }


}
