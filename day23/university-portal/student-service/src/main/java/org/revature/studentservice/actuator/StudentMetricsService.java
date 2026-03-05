package org.revature.studentservice.actuator;

import io.micrometer.core.instrument.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class StudentMetricsService {

    // ── Counter: Counts UP forever, never resets ─────────────────────
    // Use for: How many times did X happen?
    private final Counter studentCreatedCounter;
    private final Counter studentNotFoundCounter;
    private final Counter duplicateEmailCounter;

    // ── Gauge: Snapshot of a current value, can go up AND down ───────
    // Use for: What is the current count of X right now?
    private final AtomicInteger activeStudents = new AtomicInteger(0);

    // ── Timer: Measures duration of operations ────────────────────────
    // Use for: How long does operation X take?
    private final Timer studentCreationTimer;

    // ── DistributionSummary: Tracks value distribution (min/max/avg) ──
    // Use for: What's the distribution of student year values?
    private final DistributionSummary yearDistribution;

    // MeterRegistry is auto-injected by Spring Boot Actuator
    public StudentMetricsService(MeterRegistry registry) {

        // ── Register Counters ────────────────────────────────────────
        this.studentCreatedCounter = Counter.builder("student.created.total")
                .description("Total number of students created since app startup")
                .tag("service", "student-service")   // Tags for filtering in dashboards
                .register(registry);

        this.studentNotFoundCounter = Counter.builder("student.not_found.total")
                .description("Total number of Student Not Found errors")
                .tag("service", "student-service")
                .register(registry);

        this.duplicateEmailCounter = Counter.builder("student.duplicate_email.total")
                .description("Total number of duplicate email rejections")
                .tag("service", "student-service")
                .register(registry);

        // ── Register Gauge ────────────────────────────────────────────
        // Gauge reads from the AtomicInteger automatically — no manual updates needed
        Gauge.builder("student.active.count", activeStudents, AtomicInteger::get)
                .description("Current number of active students in the system")
                .tag("service", "student-service")
                .register(registry);

        // ── Register Timer ────────────────────────────────────────────
        this.studentCreationTimer = Timer.builder("student.creation.duration")
                .description("Time taken to create a student (DB write + validation)")
                .tag("service", "student-service")
                .publishPercentiles(0.5, 0.95, 0.99) // Track 50th, 95th, 99th percentile
                .register(registry);

        // ── Register Distribution Summary ─────────────────────────────
        this.yearDistribution = DistributionSummary.builder("student.year.distribution")
                .description("Distribution of student year values (1st, 2nd, 3rd, 4th year)")
                .tag("service", "student-service")
                .register(registry);
    }

    // ── Public methods called by the service layer ───────────────────

    public void recordStudentCreated(int year) {
        studentCreatedCounter.increment();       // Counter goes up by 1
        activeStudents.incrementAndGet();        // Gauge goes up by 1
        yearDistribution.record(year);           // Record this year value
    }

    public void recordStudentDeleted() {
        activeStudents.decrementAndGet();        // Gauge goes DOWN by 1
    }

    public void recordStudentNotFound() {
        studentNotFoundCounter.increment();
    }

    public void recordDuplicateEmail() {
        duplicateEmailCounter.increment();
    }

    // Wrap an operation in a timer — records how long the lambda takes
    public <T> T timeStudentCreation(java.util.concurrent.Callable<T> operation) throws Exception {
        return studentCreationTimer.recordCallable(operation);
    }
}