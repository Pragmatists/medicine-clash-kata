package pl.pragmatists.tdd.medicineclash;

import java.time.LocalDate;

public class Prescription {
    
    public final LocalDate dispenseDate;
    public final int daysSupply;
    
    public Prescription(LocalDate dispenseDate, int daysSupply) {
        this.dispenseDate = dispenseDate;
        this.daysSupply = daysSupply;
    }

}
