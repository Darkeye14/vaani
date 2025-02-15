package com.sayaha.vani.service;

import com.sayaha.vani.model.Complaint;
import com.sayaha.vani.repo.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintService {
    @Autowired
    private ComplaintRepository complaintRepository;

    public Complaint createComplaint(Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id).orElse(null);
    }

    public Iterable<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

}
