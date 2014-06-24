package pl.pragmatists.tdd.medicineclash;

import com.google.common.collect.Range;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Medicine {

    private Collection<Prescription> prescriptions = new ArrayList<>();
    
    public final String name;

    public Medicine(String name) {
        this.name = name;
    }
    
    public void addPrescription(Prescription prescription) {
        this.prescriptions.add(prescription);
    }

    private PrescribedIntervals asIntervals() {
        return new PrescribedIntervals(prescriptionsAsIntervals());
    }

    private List<Range<LocalDate>> prescriptionsAsIntervals() {
        return prescriptions.stream().map(Prescription::asInterval).collect(toList());
    }

    public Collection<LocalDate> clashesWith(Collection<Medicine> medicines) {
        return medicines.stream()
                .map(m -> m.clashesWith(this))
                .flatMap(Collection::stream).collect(toList());
    }

    private Collection<LocalDate> clashesWith(Medicine second) {
        PrescribedIntervals prescribedIntervals = asIntervals();
        PrescribedIntervals secondPrescribedIntervals = second.asIntervals();
        return prescribedIntervals.findClashes(secondPrescribedIntervals);
    }
}
