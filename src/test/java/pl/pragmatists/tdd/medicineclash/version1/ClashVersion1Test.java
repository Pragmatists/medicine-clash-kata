package pl.pragmatists.tdd.medicineclash.version1;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class ClashVersion1Test {

    @Test
    public void empty_input() throws Exception {
        Patient patient = new Patient(createTimeProvider(LocalDate.of(2010, 3, 17)));

        Collection<LocalDate> result = patient.clash(Collections.<String>emptyList());

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void straightforward_clash() throws Exception {
        Patient patient = new Patient(createTimeProvider(LocalDate.of(2010, 3, 17)));
        Medicine fluoxetine = new Medicine("Fluoxetine");
        fluoxetine.addPrescription(new Prescription(LocalDate.of(2010, 1, 1), 1));
        patient.addMedicine(fluoxetine);
        Medicine codeine = new Medicine("Codeine");
        codeine.addPrescription(new Prescription(LocalDate.of(2010, 1, 1), 1));
        patient.addMedicine(codeine);

        Collection<LocalDate> clashDates = patient.clash(Arrays.asList("Fluoxetine", "Codeine"));

        Assertions.assertThat(clashDates).containsExactly(LocalDate.of(2010, 1, 1));
    }

    @Test
    public void straightforward_clash_on_another_date() throws Exception {
        Patient patient = new Patient(createTimeProvider(LocalDate.of(2010, 3, 17)));
        Medicine fluoxetine = new Medicine("Fluoxetine");
        fluoxetine.addPrescription(new Prescription(LocalDate.of(2010, 2, 1), 1));
        patient.addMedicine(fluoxetine);
        Medicine codeine = new Medicine("Codeine");
        codeine.addPrescription(new Prescription(LocalDate.of(2010, 2, 1), 1));
        patient.addMedicine(codeine);

        Collection<LocalDate> clashDates = patient.clash(Arrays.asList("Fluoxetine", "Codeine"));

        Assertions.assertThat(clashDates).containsExactly(LocalDate.of(2010, 2, 1));
    }

    @Test
    public void non_overlapping_single_days() throws Exception {
        Patient patient = new Patient(createTimeProvider(LocalDate.of(2010, 3, 17)));
        Medicine fluoxetine = new Medicine("Fluoxetine");
        fluoxetine.addPrescription(new Prescription(LocalDate.of(2010, 3, 1), 1));
        patient.addMedicine(fluoxetine);
        Medicine codeine = new Medicine("Codeine");
        codeine.addPrescription(new Prescription(LocalDate.of(2010, 2, 1), 1));
        patient.addMedicine(codeine);

        Collection<LocalDate> clashDates = patient.clash(Arrays.asList("Fluoxetine", "Codeine"));

        Assertions.assertThat(clashDates).isEmpty();
    }

    @Test
    public void single_day_in_longer_range() throws Exception {
        Patient patient = new Patient(createTimeProvider(LocalDate.of(2010, 3, 17)));
        Medicine fluoxetine = new Medicine("Fluoxetine");
        fluoxetine.addPrescription(new Prescription(LocalDate.of(2010, 3, 1), 31));
        patient.addMedicine(fluoxetine);
        Medicine codeine = new Medicine("Codeine");
        codeine.addPrescription(new Prescription(LocalDate.of(2010, 3, 15), 1));
        patient.addMedicine(codeine);

        Collection<LocalDate> clashDates = patient.clash(Arrays.asList("Fluoxetine", "Codeine"));

        Assertions.assertThat(clashDates).containsExactly(LocalDate.of(2010, 3, 15));
    }

    @Test
    public void second_range_within_first_range() throws Exception {
        Patient patient = new Patient(createTimeProvider(LocalDate.of(2010, 3, 17)));
        Medicine fluoxetine = new Medicine("Fluoxetine");
        fluoxetine.addPrescription(new Prescription(LocalDate.of(2010, 3, 1), 31));
        patient.addMedicine(fluoxetine);
        Medicine codeine = new Medicine("Codeine");
        codeine.addPrescription(new Prescription(LocalDate.of(2010, 3, 15), 2));
        patient.addMedicine(codeine);

        Collection<LocalDate> clashDates = patient.clash(Arrays.asList("Fluoxetine", "Codeine"));

        Assertions.assertThat(clashDates).containsExactly(LocalDate.of(2010, 3, 15), LocalDate.of(2010, 3, 16));
    }

    @Test
    public void first_range_within_second_range() throws Exception {
        Patient patient = new Patient(createTimeProvider(LocalDate.of(2010, 3, 17)));
        Medicine fluoxetine = new Medicine("Fluoxetine");
        fluoxetine.addPrescription(new Prescription(LocalDate.of(2010, 3, 15), 2));
        patient.addMedicine(fluoxetine);
        Medicine codeine = new Medicine("Codeine");
        codeine.addPrescription(new Prescription(LocalDate.of(2010, 3, 1), 31));
        patient.addMedicine(codeine);

        Collection<LocalDate> clashDates = patient.clash(Arrays.asList("Fluoxetine", "Codeine"));

        Assertions.assertThat(clashDates).containsExactly(LocalDate.of(2010, 3, 15), LocalDate.of(2010, 3, 16));
    }

    @Test
    public void clash_on_second_interval() throws Exception {
        Patient patient = new Patient(createTimeProvider(LocalDate.of(2010, 3, 17)));
        Medicine fluoxetine = new Medicine("Fluoxetine");
        fluoxetine.addPrescription(new Prescription(LocalDate.of(2010, 1, 1), 1));
        fluoxetine.addPrescription(new Prescription(LocalDate.of(2010, 2, 10), 1));
        patient.addMedicine(fluoxetine);
        Medicine codeine = new Medicine("Codeine");
        codeine.addPrescription(new Prescription(LocalDate.of(2010, 2, 9), 2));
        patient.addMedicine(codeine);

        Collection<LocalDate> clashDates = patient.clash(Arrays.asList("Fluoxetine", "Codeine"));

        Assertions.assertThat(clashDates).containsExactly(LocalDate.of(2010, 2, 10));
    }

    @Test
    public void clash_between_first_and_third_medicine() throws Exception {
        Patient patient = new Patient(createTimeProvider(LocalDate.of(2010, 3, 17)));
        Medicine fluoxetine = new Medicine("Fluoxetine");
        fluoxetine.addPrescription(new Prescription(LocalDate.of(2010, 2, 10), 1));
        patient.addMedicine(fluoxetine);
        Medicine codeine = new Medicine("Codeine");
        codeine.addPrescription(new Prescription(LocalDate.of(2010, 1, 1), 2));
        patient.addMedicine(codeine);
        Medicine thirdMedicine = new Medicine("Third");
        thirdMedicine.addPrescription(new Prescription(LocalDate.of(2010, 2, 9), 2));
        patient.addMedicine(thirdMedicine);

        Collection<LocalDate> clashDates = patient.clash(Arrays.asList("Fluoxetine", "Codeine", "Third"));

        Assertions.assertThat(clashDates).containsExactly(LocalDate.of(2010, 2, 10));
    }

    @Test
    public void clashing_medicine_not_in_query() throws Exception {
        Patient patient = new Patient(createTimeProvider(LocalDate.of(2010, 3, 17)));
        Medicine fluoxetine = new Medicine("Fluoxetine");
        fluoxetine.addPrescription(new Prescription(LocalDate.of(2010, 3, 1), 31));
        patient.addMedicine(fluoxetine);
        Medicine codeine = new Medicine("Codeine");
        codeine.addPrescription(new Prescription(LocalDate.of(2010, 3, 15), 1));
        patient.addMedicine(codeine);

        Collection<LocalDate> clashDates = patient.clash(Arrays.asList("Codeine"));

        Assertions.assertThat(clashDates).isEmpty();
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
