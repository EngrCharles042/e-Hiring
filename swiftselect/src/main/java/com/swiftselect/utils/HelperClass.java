package com.swiftselect.utils;

import com.swiftselect.domain.entities.admin.Admin;
import com.swiftselect.domain.entities.employer.Employer;
import com.swiftselect.domain.entities.jobseeker.JobSeeker;
import com.swiftselect.domain.entities.jobseeker.profile.Certification;
import com.swiftselect.domain.entities.jobseeker.profile.Education;
import com.swiftselect.domain.entities.jobseeker.profile.Skills;
import com.swiftselect.domain.entities.jobseeker.profile.WorkExperience;
import com.swiftselect.domain.enums.Gender;
import com.swiftselect.domain.enums.JobType;
import com.swiftselect.domain.enums.PayRate;
import com.swiftselect.infrastructure.exceptions.ApplicationException;
import com.swiftselect.payload.response.jsresponse.*;
import com.swiftselect.repositories.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelperClass {
    private final JobSeekerRepository jobSeekerRepository;
    private final EmployerRepository employerRepository;
    private final AdminRepository adminRepository;
    private final EducationRepository educationRepository;
    private final WorkExperienceRepository workExperienceRepository;
    private final SkillsRepository skillsRepository;
    private final CertificationRepository certificationRepository;

    public String getTokenFromHttpRequest(HttpServletRequest request) {
        // Get the bearer token from the http request
        String bearerToken = request.getHeader("Authorization");

        // Extract only the Token excluding the prefix "Bearer "
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            System.out.println(bearerToken);
            return bearerToken.substring(7);
        }

        return null;
    }

    public void sendEmail(
            String firstName,
            String url,
            JavaMailSender mailSender,
            String sendMail,
            String recipient,
            String action,
            String serviceProvider,
            String subject,
            String description
            ) {

        try {
            String mailContent ="<div style='padding: 1rem; background-color: rgb(138, 36, 36); color: white'>"
                    + "<p style='text-align: center'>"
                    + "<img src=" + AppConstants.LOGO + " style='width: 8rem; height: 10rem'></p>"
                    + "<hr style='color: black'>"
                    + "<p> Hi, " + firstName + " </p>"
                    + "<p> " + description + " </p>"
                    + "<a href=" + url + " style='padding: 0.7rem; background-color: #383896; text-decoration: none; border-radius: 0.3rem; color: white'>" + action + "</a> <br>"
                    + "<p> Thank you. <br> " + serviceProvider + " </p>"
                    + "</div>";

            MimeMessage message = mailSender.createMimeMessage();

            var messageHelper = new MimeMessageHelper(message);

            messageHelper.setFrom(sendMail, serviceProvider);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);
            messageHelper.setText(mailContent, true);

            mailSender.send(message);

        } catch (MailException | MessagingException | UnsupportedEncodingException e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    @Async
    public void sendNotificationEmail(
            String firstName,
            String url,
            JavaMailSender mailSender,
            String sendMail,
            String recipient,
            String action,
            String serviceProvider,
            String subject,
            String description
    ) {

        try {
            String mailContent ="<div style='padding: 1rem; background-color: white; color: black'>"
                    + "<p style='text-align: center'>"
                    + "<img src=" + AppConstants.LOGO + " style='width: 8rem; height: 10rem'></p>"
                    + "<hr style='color: black'>"
                    + "<p> Hi, " + firstName + " </p>"
                    + "<p> " + description + " </p>"
                    + "<a href=" + url + " style='padding: 0.7rem; background-color: #383896; text-decoration: none; border-radius: 0.3rem; color: white'>" + action + "</a> <br>"
                    + "<p> Thank you. <br> " + serviceProvider + " </p>"
                    + "</div>";

            MimeMessage message = mailSender.createMimeMessage();

            var messageHelper = new MimeMessageHelper(message);

            messageHelper.setFrom(sendMail, serviceProvider);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);
            messageHelper.setText(mailContent, true);

            mailSender.send(message);

        } catch (MailException | MessagingException | UnsupportedEncodingException e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    public String emailVerification(
            String email,
            String action,
            String buttonName,
            String serviceProvider,
            String description
    ) {

        String firstName = extractFirstName(email);

        return "<head>"
                + "<title> " + action + " </title> "
                + "</head>"
                + "<body style='height: 100vh; overflow: hidden; margin: 0'>"
                + "<div style='padding: 2rem; background-color: white; color: gray; height: 100vh; width: 100vw; font-size: 20px; display: flex; justify-content: center; overflow: hidden'>"
                + "<div style='width: 35vw; color: black'>"
                + "<p style='text-align: center'>"
                + "<img src=" + AppConstants.LOGO + " style='width: 8rem; height: 10rem'></p>"
                + "<hr style='color: black'>"
                + "<p style='font-family: Academy Engraved LET; font-size: 30px'> Hi, " + firstName + " </p>"
                + "<p style='font-family: Cochin; margin-bottom: 1.5rem'> " + description + " </p>"
                + "<a href='http://127.0.0.1:1123/login' style='font-family: Cochin; width: 35vw; height: 4rem; border-radius: 1rem; border: 1px solid saddlebrown; margin-top: 1rem; margin-bottom: 4rem; opacity: 0.8; background-color: rgb(36,36,138); color: white; font-size: 18px; cursor: pointer; padding: 1rem 2rem; text-align: center; text-decoration: none' />"
                + buttonName
                + "</a>"
                + "<p style='font-family: Cochin; margin-top: 1.5rem'> &copy; &nbsp;" + serviceProvider + " </p>"
                + "</div>"
                + "</div>"
                + "</body>";
    }
    @Async
    public void sendNotificationEmailTosubscribers(
            String firstName,
            String url,
            JavaMailSender mailSender,
            String sendMail,
            List<String> recipients,
            String action,
            String serviceProvider,
            String subject,
            String description
    ) {
        for (String recipient : recipients) {
            try {
                String mailContent = "<div style='padding: 1rem; background-color: white; color: black'>"
                        + "<p style='text-align: center'>"
                        + "<img src=" + AppConstants.LOGO + " style='width: 8rem; height: 10rem'></p>"
                        + "<hr style='color: black'>"
                        + "<p> Hi, " + firstName + " </p>"
                        + "<p> " + description + " </p>"
                        + "<a href=" + url + " style='padding: 0.7rem; background-color: #383896; text-decoration: none; border-radius: 0.3rem; color: white'>" + action + "</a> <br>"
                        + "<p> Thank you. <br> " + serviceProvider + " </p>"
                        + "</div>";

                MimeMessage message = mailSender.createMimeMessage();
                var messageHelper = new MimeMessageHelper(message);

                messageHelper.setFrom(sendMail, serviceProvider);
                messageHelper.setTo(recipient);
                messageHelper.setSubject(subject);
                messageHelper.setText(mailContent, true);

                mailSender.send(message);
            } catch (MailException | MessagingException | UnsupportedEncodingException e) {
                log.error("Error sending email to {}: {}", recipient, e.getMessage(), e);
            }
        }
    }


    public String extractFirstName(String email) {
        Optional<JobSeeker> jobSeekerOptional = jobSeekerRepository.findByEmail(email);
        Optional<Employer> employerOptional = employerRepository.findByEmail(email);
        Optional<Admin> adminOptional = adminRepository.findByEmail(email);

        if (jobSeekerOptional.isPresent()) {
            return jobSeekerOptional.get().getFirstName();
        } else if (employerOptional.isPresent()) {
            return employerOptional.get().getFirstName();
        } else {
            return adminOptional.get().getFirstName();
        }
    }

    public JobSeekerInfoResponse jobSeekerToJobSeekerInfoResponse(JobSeeker jobSeeker) {
        return JobSeekerInfoResponse.builder()
                .id(jobSeeker.getId())
                .firstName(jobSeeker.getFirstName())
                .lastName(jobSeeker.getLastName())
                .email(jobSeeker.getEmail())
                .phoneNumber(jobSeeker.getPhoneNumber())
                .profilePicture(jobSeeker.getProfilePicture())
                .country(jobSeeker.getCountry())
                .state(jobSeeker.getState())
                .city(jobSeeker.getCity())
                .address(jobSeeker.getAddress())
                .postalCode(jobSeeker.getPostalCode())
                .facebook(jobSeeker.getFacebook())
                .twitter(jobSeeker.getTwitter())
                .instagram(jobSeeker.getInstagram())
                .gender(jobSeeker.getGender())
                .dateOfBirth(jobSeeker.getDateOfBirth())
                .resume(jobSeeker.getResume())
                .coverLetter(jobSeeker.getCoverLetter())
                .workExperiences(workExperiencesToWorkExperienceResponses(workExperienceRepository.findAllByJobSeeker(jobSeeker)))
                .skills(skillsToSkillResponses(skillsRepository.findAllByJobSeeker(jobSeeker)))
                .certifications(certificationsToCertificationsResponse(certificationRepository.findAllByJobSeeker(jobSeeker)))
                .education(educationsToEducationResponses(educationRepository.findAllByJobSeeker(jobSeeker)))
                .build();
    }

    public List<EducationResponse> educationsToEducationResponses(List<Education> educations) {
        return educations.stream()
                .map(education ->
                    EducationResponse.builder()
                            .id(education.getId())
                            .educationLevel(education.getEducationLevel())
                            .fieldOfStudy(education.getFieldOfStudy())
                            .yearOfGraduation(education.getYearOfGraduation())
                            .build()
                ).toList();
    }

    public List<WorkExperienceResponse> workExperiencesToWorkExperienceResponses(List<WorkExperience> workExperiences) {
        return workExperiences.stream()
                .map(workExperience ->
                        WorkExperienceResponse.builder()
                                .id(workExperience.getId())
                                .companyName(workExperience.getCompanyName())
                                .jobTitle(workExperience.getJobTitle())
                                .startDate(workExperience.getStartDate())
                                .stopDate(workExperience.getStopDate())
                                .build()
                ).toList();
    }

    public List<SkillsResponse> skillsToSkillResponses(List<Skills> skills) {
        return skills.stream()
                .map(skill ->
                        SkillsResponse.builder()
                                .id(skill.getId())
                                .skill(skill.getSkill())
                                .yearsOfExperience(skill.getYearsOfExperience())
                                .build()
                ).toList();
    }

    public List<CertificationsResponse> certificationsToCertificationsResponse(List<Certification> certifications) {
        return certifications.stream()
                .map(certification ->
                        CertificationsResponse.builder()
                                .id(certification.getId())
                                .expiration(certification.getExpiration())
                                .name(certification.getName())
                                .build()
                ).toList();
    }

}
