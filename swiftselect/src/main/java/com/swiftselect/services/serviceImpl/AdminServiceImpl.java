package com.swiftselect.services.serviceImpl;

import com.swiftselect.domain.entities.employer.Employer;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.payload.response.APIResponse;
import com.swiftselect.payload.response.employerresponse.EmployerResponse;
import com.swiftselect.payload.response.employerresponse.EmployerResponsePage;
import com.swiftselect.repositories.EmployerRepository;
import com.swiftselect.services.AdminService;
import com.swiftselect.services.EmployerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final EmployerService employerService;
    private final EmployerRepository employerRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<EmployerResponsePage> getAllEmployers(int pageNo, int pageSize, String sortBy, String sortDir) {
        // Delegate the call to EmployerService's getAllEmployers method
        return employerService.getAllEmployers(pageNo, pageSize, sortBy, sortDir);
    }

    @Override
    public ResponseEntity<APIResponse<EmployerResponse>> getEmployerById(Long employerId) {
        Optional<Employer> employerOptional = employerRepository.findById(employerId);
        if (employerOptional.isEmpty()) {
            throw new ApplicationException("Employer With "+ employerId + "Does Not Exist");
        }
            Employer employer = employerOptional.get();
            EmployerResponse employerResponse = modelMapper.map(employer, EmployerResponse.class);
            return ResponseEntity.ok(new APIResponse<>("Search Successful",employerResponse));
        }
}

