package com.example.demo.converter;

import com.example.demo.exception.CustomException;
import com.example.demo.request.requests.CreateLateSoonRequest;
import com.example.demo.request.requests.CreateLeaveRequest;
import com.example.demo.response.ResponseObject;

public class RequestChecker {
    public static void checkCreateLateSoonRequest(CreateLateSoonRequest request){
        if (request.getLateSoonType() == null){
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_ACCEPTABLE,ResponseObject.MESSAGE_NULL_LATE_SOON_TYPE);
        }
        if (request.getShift() == null){
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_ACCEPTABLE,ResponseObject.MESSAGE_NULL_SHIFT);
        }
        if (request.getReason() == null){
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_ACCEPTABLE,ResponseObject.MESSAGE_NULL_REASON);
        }
    }
    public static void checkCreateLeaveRequest(CreateLeaveRequest request){
        if (request.getReason() == null){
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_ACCEPTABLE,ResponseObject.MESSAGE_NULL_REASON);
        }
        if (request.getShiftStart() == null  || request.getShiftEnd() == null){
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_ACCEPTABLE,ResponseObject.MESSAGE_NULL_SHIFT);
        }
        if (request.getOffType() == null){
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_ACCEPTABLE,ResponseObject.MESSAGE_NULL_OFF_TYPE);
        }
    }
}
