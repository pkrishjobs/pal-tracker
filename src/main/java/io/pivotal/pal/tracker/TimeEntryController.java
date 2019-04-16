package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        //TimeEntry createTimeEntry = timeEntryRepository.create(timeEntryToCreate);
        //ResponseEntity responseEntity = new ResponseEntity(createTimeEntry, HttpStatus.CREATED);

        return ResponseEntity.status(HttpStatus.CREATED).body(timeEntryRepository.create(timeEntryToCreate));
    }

    @GetMapping("/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
        TimeEntry createTimeEntry = timeEntryRepository.find(timeEntryId);

        if(createTimeEntry != null){
            return ResponseEntity.ok(createTimeEntry);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity list() {
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @PutMapping("/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry updateTimeEntry = timeEntryRepository.update(timeEntryId, expected);
        if (updateTimeEntry != null)
        {
            return new ResponseEntity(updateTimeEntry, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{timeEntryId}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long timeEntryId) {
        return new ResponseEntity<TimeEntry>(timeEntryRepository.delete(timeEntryId),HttpStatus.NO_CONTENT);
    }
}
