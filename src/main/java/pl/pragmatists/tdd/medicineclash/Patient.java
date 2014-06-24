package pl.pragmatists.tdd.medicineclash;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;

public class Patient {

    private Collection<Medicine> medicines = new ArrayList<Medicine>();

    public void addMedicine(Medicine medicine) {
        this.medicines.add(medicine);
    }

    public Collection<LocalDate> clash(Collection<String> medicineNames) {
        return clash(medicineNames, 90);
    }

    public Collection<LocalDate> clash(final Collection<String> medicineNames, int daysBack) {
        return newArrayList();
    }

}
