package com.swiftselect.services;

import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.jobpostresponse.JobApplicationResponse;

public interface JobApplicationService {
    APIResponse<JobApplicationResponse> jobApplication(long jobPostId);

}
