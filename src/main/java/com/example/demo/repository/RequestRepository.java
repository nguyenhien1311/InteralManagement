package com.example.demo.repository;

import com.example.demo.constant.RequestStatus;
import com.example.demo.constant.RequestType;
import com.example.demo.domain.Request;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends MongoRepository<Request,String> {
    List<Request> findAllByRequestStatusAndReceiverEmail(RequestStatus requestStatus,String email);

    List<Request> findRequestsByUserIdOrderByCreatedAtDesc(String userId);

    List<Request> findRequestsByReceiverEmailAndRequestStatus(String email,RequestStatus status);

    Optional<Request> findRequestsByIdAndReceiverEmail(String requestId,String email);

    Request findFirstByUserIdOrderByIdDesc(String userId);

    List<Request> findRequestsByYearRequestAndRequestTypeAndUserId(int year, RequestType type,String userId);
}
