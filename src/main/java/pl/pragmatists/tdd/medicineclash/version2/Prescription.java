package pl.pragmatists.tdd.medicineclash.version2;

import com.google.common.collect.Range;

import java.time.LocalDate;

public class Prescription {
    
    public final LocalDate dispenseDate;
    public final int daysSupply;
    
    public Prescription(LocalDate dispenseDate, int daysSupply) {
        this.dispenseDate = dispenseDate;
        this.daysSupply = daysSupply;
    }

    public Range asInterval() {
        return Range.closedOpen(dispenseDate, dispenseDate.plusDays(daysSupply));
    }
}
