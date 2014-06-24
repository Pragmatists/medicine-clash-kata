package pl.pragmatists.tdd.medicineclash;

import com.google.common.collect.Range;

import java.time.LocalDate;
import java.util.Date;

public class Prescription {
    
    public final LocalDate dispenseDate;
    public final int daysSupply;
    
    public Prescription(LocalDate dispenseDate, int daysSupply) {
        this.dispenseDate = dispenseDate;
        this.daysSupply = daysSupply;
    }

}
