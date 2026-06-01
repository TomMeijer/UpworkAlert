package com.tm.upwork.domain.job;

import com.tm.upwork.domain.job.entity.JobStatus;
import com.tm.upwork.domain.job.model.JobDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping
    public Page<JobDto> getPage(@RequestParam(defaultValue = "publishedOn.desc") String sort,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int pageSize) {
        String[] sortParams = sort.split("\\.");
        String sortField = sortParams[0];
        Sort.Direction direction = Sort.Direction.DESC;
        if (sortParams.length > 1 && "asc".equalsIgnoreCase(sortParams[1])) {
            direction = Sort.Direction.ASC;
        }
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(direction, sortField));
        return jobService.getPage(pageable);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        jobService.delete(id);
    }

    @PatchMapping("/{id}/status")
    public void updateStatus(@PathVariable int id, @RequestParam JobStatus status) {
        jobService.updateStatus(id, status);
    }
}
