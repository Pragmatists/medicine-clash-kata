package pl.pragmatists.tdd.medicineclash;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import static java.time.LocalDate.now;

public class ClashTest {

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

}
