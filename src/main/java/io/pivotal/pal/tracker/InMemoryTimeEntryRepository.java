package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> timeEntries = new HashMap<>();
    private AtomicLong idSequence = new AtomicLong(1);

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(idSequence.getAndIncrement());
        timeEntries.put(timeEntry.getId(), timeEntry);
        return timeEntry;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return timeEntries.get(timeEntryId);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(timeEntries.values());
    }

    @Override
    public TimeEntry update(long timeEntryId, TimeEntry timeEntry) {
        timeEntry.setId(timeEntryId);
        if (timeEntries.replace(timeEntryId, timeEntry) == null) {
            return null;
        } else {
            return timeEntry;
        }
    }

    @Override
    public void delete(long timeEntryId) {
        timeEntries.remove(timeEntryId);
    }
}
