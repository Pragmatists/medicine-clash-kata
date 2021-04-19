package pl.pragmatists.tdd.medicineclash;

import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public class ClashTest {

    @Test
    public void empty_input() throws Exception {
        Patient patient = createPatient();

        Collection<LocalDate> result = patient.clash(Collections.<String>emptyList());

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void straightforward_clash() throws Exception {
        Patient patient = createPatient();
        given(patient).hasPrescriptionFrom(2010, 1, 1).forDays(1).forMedicine("Fluoxetine");
        given(patient).hasPrescriptionFrom(2010, 1, 1).forDays(1).forMedicine("Codeine");

        Collection<LocalDate> clashDates = patient.clash(asList("Fluoxetine", "Codeine"));

        Assertions.assertThat(clashDates).containsExactly(LocalDate.of(2010, 1, 1));
    }

    @Test
    public void straightforward_clash_on_another_date() throws Exception {
        Patient patient = createPatient();
        given(patient).hasPrescriptionFrom(2010, 2, 1).forDays(1).forMedicine("Fluoxetine");
        given(patient).hasPrescriptionFrom(2010, 2, 1).forDays(1).forMedicine("Codeine");

        Collection<LocalDate> clashDates = patient.clash(asList("Fluoxetine", "Codeine"));

        Assertions.assertThat(clashDates).containsExactly(LocalDate.of(2010, 2, 1));
    }

    @Test
    public void non_overlapping_single_days() throws Exception {
        Patient patient = createPatient();
        given(patient).hasPrescriptionFrom(2010, 3, 1).forDays(1).forMedicine("Fluoxetine");
        given(patient).hasPrescriptionFrom(2010, 2, 1).forDays(1).forMedicine("Codeine");

        Collection<LocalDate> clashDates = patient.clash(asList("Fluoxetine", "Codeine"));

        Assertions.assertThat(clashDates).isEmpty();
    }

    @Test
    public void single_day_in_longer_range() throws Exception {
        Patient patient = createPatient();
        given(patient).hasPrescriptionFrom(2010, 3, 1).forDays(31).forMedicine("Fluoxetine");
        given(patient).hasPrescriptionFrom(2010, 3, 15).forDays(1).forMedicine("Codeine");

        Collection<LocalDate> clashDates = patient.clash(asList("Fluoxetine", "Codeine"));

        Assertions.assertThat(clashDates).containsExactly(LocalDate.of(2010, 3, 15));
    }

    @Test
    public void second_range_within_first_range() throws Exception {
        Patient patient = createPatient();
        given(patient).hasPrescriptionFrom(2010, 3, 1).forDays(31).forMedicine("Fluoxetine");
        given(patient).hasPrescriptionFrom(2010, 3, 15).forDays(2).forMedicine("Codeine");

        Collection<LocalDate> clashDates = patient.clash(asList("Fluoxetine", "Codeine"));

        Assertions.assertThat(clashDates).containsExactly(LocalDate.of(2010, 3, 15), LocalDate.of(2010, 3, 16));
    }

    @Test
    public void first_range_within_second_range() throws Exception {
        Patient patient = createPatient();
        given(patient).hasPrescriptionFrom(2010, 3, 15).forDays(2).forMedicine("Fluoxetine");
        given(patient).hasPrescriptionFrom(2010, 3, 1).forDays(31).forMedicine("Codeine");

        Collection<LocalDate> clashDates = patient.clash(asList("Fluoxetine", "Codeine"));

        Assertions.assertThat(clashDates).containsExactly(LocalDate.of(2010, 3, 15), LocalDate.of(2010, 3, 16));
    }

    @Test
    public void clash_on_second_interval() throws Exception {
        Patient patient = createPatient();
        given(patient)
                .hasPrescriptionFrom(2010, 1, 1).forDays(1)
                .hasPrescriptionFrom(2010, 2, 10).forDays(1).forMedicine("Fluoxetine");
        given(patient)
                .hasPrescriptionFrom(2010, 2, 9).forDays(2).forMedicine("Codeine");

        Collection<LocalDate> clashDates = patient.clash(asList("Fluoxetine", "Codeine"));

        Assertions.assertThat(clashDates).containsExactly(LocalDate.of(2010, 2, 10));
    }

    @Test
    public void clash_between_first_and_third_medicine() throws Exception {
        Patient patient = createPatient();
        given(patient).hasPrescriptionFrom(2010, 2, 10).forDays(1).forMedicine("Fluoxetine");
        given(patient).hasPrescriptionFrom(2010, 1, 1).forDays(2).forMedicine("Codeine");
        given(patient).hasPrescriptionFrom(2010, 2, 9).forDays(2).forMedicine("Paracetamol");

        Collection<LocalDate> clashDates = patient.clash(asList("Fluoxetine", "Codeine", "Paracetamol"));

        Assertions.assertThat(clashDates).containsExactly(LocalDate.of(2010, 2, 10));
    }

    @Test
    public void clashing_medicine_not_in_query() throws Exception {
        Patient patient = createPatient();
        given(patient).hasPrescriptionFrom(2010, 3, 1).forDays(31).forMedicine("Fluoxetine");
        given(patient).hasPrescriptionFrom(2010, 3, 15).forDays(1).forMedicine("Codeine");

        Collection<LocalDate> clashDates = patient.clash(asList("Codeine"));

        Assertions.assertThat(clashDates).isEmpty();
    }

    @Test
    public void clash_more_than_queried_days_before() throws Exception {
        Patient patient = new Patient(createTimeProvider(LocalDate.of(2010, 3, 17)));
        given(patient).hasPrescriptionFrom(2010, 3, 1).forDays(31).forMedicine("Fluoxetine");
        given(patient).hasPrescriptionFrom(2010, 3, 15).forDays(1).forMedicine("Codeine");

        Collection<LocalDate> clashDates = patient.clash(asList("Codeine", "Fluoxetine"), 1);

        Assertions.assertThat(clashDates).isEmpty();
    }

    @Test
    public void clash_exactly_at_days_before() throws Exception {
        Patient patient = new Patient(createTimeProvider(LocalDate.of(2010, 3, 20)));
        given(patient).hasPrescriptionFrom(2010, 3, 5).forDays(7).forMedicine("Fluoxetine");
        given(patient).hasPrescriptionFrom(2010, 3, 9).forDays(3).forMedicine("Codeine");

        Collection<LocalDate> clashDates = patient.clash(asList("Codeine", "Fluoxetine"), 10);

        Assertions.assertThat(clashDates).containsExactly(LocalDate.of(2010,3,10), LocalDate.of(2010,3,11));
    }

    private TimeProvider createTimeProvider(LocalDate currentDate) {
        return () -> currentDate;
    }

    private PatientBuilder given(Patient patient) {
        return new PatientBuilder(patient);
    }

    private Patient createPatient() {
        return new Patient(createTimeProvider(LocalDate.of(2010, 3, 17)));
    }

    private class PatientBuilder {
        private Patient patient;
        private List<LocalDate> from = Lists.newArrayList();
        private List<Integer> days = Lists.newArrayList();

        public PatientBuilder(Patient patient) {
            this.patient = patient;
        }

        public void forMedicine(String medicineName) {
            Medicine medicine = new Medicine(medicineName);
            for (int i = 0; i < from.size(); i++) {
                medicine.addPrescription(new Prescription(from.get(i), days.get(i)));
            }
            patient.addMedicine(medicine);
        }

        public PatientBuilder hasPrescriptionFrom(int year, int month, int day) {
            this.from.add(LocalDate.of(year, month, day));
            return this;
        }

        public PatientBuilder forDays(int days) {
            this.days.add(days);
            return this;
        }
    }
}
