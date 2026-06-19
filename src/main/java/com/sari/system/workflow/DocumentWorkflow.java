package com.sari.system.workflow;

import com.sari.system.domain.DocumentStatus;
import org.springframework.stereotype.Component;

@Component
public class DocumentWorkflow {


    public DocumentStatus next(DocumentStatus current, String action) {

        switch (current) {

            case DRAFT:
                if ("submit".equalsIgnoreCase(action))
                    return DocumentStatus.UNDER_REVIEW;
                break;

            case UNDER_REVIEW:
                if ("approve".equalsIgnoreCase(action))
                    return DocumentStatus.APPROVED;
                break;

            case APPROVED:
                if ("distribute".equalsIgnoreCase(action))
                    return DocumentStatus.DISTRIBUTED;
                break;

            case DISTRIBUTED:
                if ("obsolete".equalsIgnoreCase(action))
                    return DocumentStatus.OBSOLETE;
                break;
        }

        throw new RuntimeException(
                "Invalid transition from " + current + " using action " + action);
    }

}
