package pl.pragmatists.tdd.medicineclash;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class Medicine {

    private Collection<Prescription> prescriptions = new ArrayList<>();
    
    public final String name;

    public Medicine(String name) {
        this.name = name;
    }
    
    public void addPrescription(Prescription prescription) {
        this.prescriptions.add(prescription);
    }

    public Collection<LocalDate> clashesWith(Medicine second) {
        PrescribedIntervals prescribedIntervals = asIntervals();
        PrescribedIntervals secondPrescribedIntervals = second.asIntervals();
        return prescribedIntervals.findClashes(secondPrescribedIntervals);
    }

    private PrescribedIntervals asIntervals() {
        return new PrescribedIntervals(prescriptions.stream().map(Prescription::asInterval).collect(Collectors.toList()));
    }

    public Collection<LocalDate> clashesWith(Collection<Medicine> medicines) {
        return medicines.stream().map(m->m.clashesWith(this)).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
