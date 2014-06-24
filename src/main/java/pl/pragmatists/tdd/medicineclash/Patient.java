package pl.pragmatists.tdd.medicineclash;

import com.google.common.collect.Sets;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

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

        List<Medicine> important = newArrayList();
        for (Medicine medicine : medicines) {
            if (medicineNames.contains(medicine.name))
                important.add(medicine);
        }

        if (important.isEmpty())
            return newArrayList();

        List<LocalDate> all = newArrayList();

        for (Medicine medicine : important) {
            for (Prescription prescription : medicine.prescriptions) {
                LocalDate start = prescription.dispenseDate;
                for (int i = 0; i < prescription.daysSupply; i++) {
                    all.add(start.plusDays(i));
                }
            }
        }

        Set<LocalDate> clash = newHashSet();

        for (Medicine medicine : important) {
            for (Prescription prescription : medicine.prescriptions) {
                LocalDate start = prescription.dispenseDate;
                for (int i = 0; i < prescription.daysSupply; i++) {
                    LocalDate potentialClashDay = start.plusDays(i);
                    all.remove(potentialClashDay);
                    if (all.contains(potentialClashDay) && timeProvider.getCurrentDate().minusDays(daysBack).isBefore(potentialClashDay))
                        clash.add(potentialClashDay);
                }
            }
        }

        return clash;
    }


}
