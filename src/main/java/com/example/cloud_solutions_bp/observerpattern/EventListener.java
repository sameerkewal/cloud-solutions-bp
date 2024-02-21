package com.example.cloud_solutions_bp.observerpattern;

public interface EventListener {

    void update(String eventType, String outgoingMessage, String phoneNumber);


}
