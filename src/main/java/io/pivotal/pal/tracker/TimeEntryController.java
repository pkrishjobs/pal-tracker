package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository,
                               MeterRegistry meterRegistry) {
        this.timeEntryRepository = timeEntryRepository;
        this.timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        this.actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry createdTimeEntry = timeEntryRepository.create(timeEntryToCreate);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTimeEntry);
    }

    @GetMapping("/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
        TimeEntry createTimeEntry = timeEntryRepository.find(timeEntryId);

        if(createTimeEntry != null){
            actionCounter.increment();
            return ResponseEntity.ok(createTimeEntry);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity list() {
        actionCounter.increment();
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @PutMapping("/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry updateTimeEntry = timeEntryRepository.update(timeEntryId, expected);
        if (updateTimeEntry != null)
        {
            actionCounter.increment();
            return new ResponseEntity(updateTimeEntry, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{timeEntryId}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity<TimeEntry>(HttpStatus.NO_CONTENT);
    }
}
