package edu.hawaii.its.api.service;

import edu.hawaii.its.api.wrapper.Subject;
import edu.hawaii.its.api.wrapper.SubjectCommand;
import edu.hawaii.its.api.wrapper.SubjectResult;
import edu.hawaii.its.api.wrapper.SubjectsCommand;
import edu.hawaii.its.api.wrapper.SubjectsResults;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * SubjectService provides a set of functions for checking the validity of UH identifiers.
 */
@Service
public class SubjectService {
    @Value("${groupings.api.success}")
    private String SUCCESS;

    @Value("${groupings.api.failure}")
    private String FAILURE;

    public boolean isValidIdentifier(String uhIdentifier) {
        return isValidSubject(new SubjectCommand(uhIdentifier).execute());
    }

    private boolean isValidSubject(SubjectResult subjectResult) {
        return subjectResult.getResultCode().equals(SUCCESS);
    }

    /**
     * Fetch all valid UH identifiers and return their corresponding UhUuids.
     */
    public List<String> getValidUhUuids(List<String> uhIdentifiers) {
        SubjectsResults subjectsResults = new SubjectsCommand(uhIdentifiers).execute();
        List<String> results = new ArrayList<>();
        for (Subject subject : subjectsResults.getSubjects()) {
            if (subject.getResultCode().equals("SUBJECT_NOT_FOUND")) {
                continue;
            }
            results.add(subject.getUhUuid());
        }
        return results;
    }

    public String getValidUhUuid(String uhIdentifier) {
        SubjectResult subjectResult = new SubjectCommand(uhIdentifier).execute();
        if (!isValidSubject(subjectResult)) {
            return "";
        }
        return subjectResult.getUhUuid();
    }
}