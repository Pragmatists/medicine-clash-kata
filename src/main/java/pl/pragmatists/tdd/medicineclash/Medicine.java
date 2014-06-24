package pl.pragmatists.tdd.medicineclash;

import java.util.ArrayList;
import java.util.Collection;

public class Medicine {

    private Collection<Prescription> prescriptions = new ArrayList<>();
    
    public final String name;

    public Medicine(String name) {
        this.name = name;
    }
    
    public void addPrescription(Prescription prescription) {
        this.prescriptions.add(prescription);
    }

}
