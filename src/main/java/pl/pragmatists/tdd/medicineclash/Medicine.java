package pl.pragmatists.tdd.medicineclash;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class Medicine {

    Collection<Prescription> prescriptions = new ArrayList<>();
    
    public final String name;

    public Medicine(String name) {
        this.name = name;
    }
    
    public void addPrescription(Prescription prescription) {
        this.prescriptions.add(prescription);
    }

}
