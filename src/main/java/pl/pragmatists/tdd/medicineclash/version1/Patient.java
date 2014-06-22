package pl.pragmatists.tdd.medicineclash.version1;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

public class Patient {

    private Collection<Medicine> medicines = new ArrayList<Medicine>();
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

        List<Medicine> filtered = medicines.stream().filter(m -> medicineNames.contains(m.name)).collect(Collectors.toList());
        return filtered.stream()
                .map(m -> m.clashesWith(medicinesWithout(m, filtered)))
                .flatMap(Collection::stream)
                .filter(d -> timeProvider.getCurrentDate().minusDays(daysBack).isBefore(d))
                .collect(Collectors.toSet());
    }

    private Collection<Medicine> medicinesWithout(Medicine medicine, Collection<Medicine> medicineCollection) {
        return medicineCollection.stream().filter(m -> !m.equals(medicine)).collect(Collectors.toList());
    }


}
