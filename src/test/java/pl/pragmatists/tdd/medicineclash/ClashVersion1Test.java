package pl.pragmatists.tdd.medicineclash;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import pl.pragmatists.tdd.medicineclash.Medicine;
import pl.pragmatists.tdd.medicineclash.Patient;
import pl.pragmatists.tdd.medicineclash.Prescription;
import pl.pragmatists.tdd.medicineclash.TimeProvider;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static java.time.LocalDate.now;

public class ClashVersion1Test {

    @Test
    public void testClashSimple() throws Exception {
        Patient patient = new Patient(new DefaultTimeProvider());
        Medicine fluoxetine = new Medicine("Fluoxetine");
        fluoxetine.addPrescription(new Prescription(now().minusDays(30), 1));
        patient.addMedicine(fluoxetine);
        Medicine codeine = new Medicine("Codeine");
        codeine.addPrescription(new Prescription(now().minusDays(30), 1));
        patient.addMedicine(codeine);

        Collection<LocalDate> clashDates = patient.clash(Arrays.asList("Fluoxetine", "Codeine"));

        Assertions.assertThat(clashDates).containsExactly(now().minusDays(30));
    }

    @Test
    public void testClashAdvanced() throws Exception {
        Patient patient = new Patient(new DefaultTimeProvider());
        Medicine fluoxetine = new Medicine("Fluoxetine");
        fluoxetine.addPrescription(new Prescription(now().minusDays(20), 1));
        patient.addMedicine(fluoxetine);
        Medicine codeine = new Medicine("Codeine");
        codeine.addPrescription(new Prescription(now().minusDays(30), 2));
        patient.addMedicine(codeine);
        Medicine paracetamol = new Medicine("Paracetamol");
        paracetamol.addPrescription(new Prescription(now().minusDays(21), 2));
        patient.addMedicine(paracetamol);
        Medicine fourthMedicine = new Medicine("Fourth");
        fourthMedicine.addPrescription(new Prescription(now().minusDays(30), 122));
        patient.addMedicine(fourthMedicine);

        Collection<LocalDate> clashDates = patient.clash(Arrays.asList("Fluoxetine", "Codeine", "Paracetamol"), 50);

        Assertions.assertThat(clashDates).containsExactly(now().minusDays(20));
    }

    @Test
    public void testClashRange() throws Exception {
        Patient patient = new Patient(new DefaultTimeProvider());
        Medicine fluoxetine = new Medicine("Fluoxetine");
        fluoxetine.addPrescription(new Prescription(now().minusDays(20), 1));
        patient.addMedicine(fluoxetine);
        Medicine codeine = new Medicine("Codeine");
        codeine.addPrescription(new Prescription(now().minusDays(100), 20));
        patient.addMedicine(codeine);
        Medicine paracetamol = new Medicine("Paracetamol");
        paracetamol.addPrescription(new Prescription(now().minusDays(120), 40));
        patient.addMedicine(paracetamol);

        Collection<LocalDate> clashDates = patient.clash(Arrays.asList("Fluoxetine", "Codeine", "Paracetamol"));

        Assertions.assertThat(clashDates).hasSize(10);
    }

    @Test
    public void clash_more_than_queried_days_before() throws Exception {
        Patient patient = new Patient(createTimeProvider(LocalDate.of(2010, 3, 17)));
        Medicine fluoxetine = new Medicine("Fluoxetine");
        fluoxetine.addPrescription(new Prescription(LocalDate.of(2010, 3, 1), 31));
        patient.addMedicine(fluoxetine);
        Medicine codeine = new Medicine("Codeine");
        codeine.addPrescription(new Prescription(LocalDate.of(2010, 3, 15), 1));
        patient.addMedicine(codeine);

        Collection<LocalDate> clashDates = patient.clash(Arrays.asList("Codeine", "Fluoxetine"), 1);

        Assertions.assertThat(clashDates).isEmpty();
    }

    private TimeProvider createTimeProvider(LocalDate currentDate) {
        return () -> currentDate;
    }
}
