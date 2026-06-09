# Scalable Notification Platform Design

## 1. Requirements

### Functional
* Support multiple notification channels: Email, SMS, Push (iOS/Android), and In-App.
* Users can set notification preferences (opt-in/opt-out per channel and topic).
* Allow scheduled notifications and recurring notifications.
* Expose REST APIs to send messages programmatically.
* Delivery status tracking and webhooks for status updates.

### Non-Functional
* **High Availability**: 99.99% uptime.
* **High Throughput**: Capable of sending millions of notifications per day.
* **Low Latency**: Time-critical notifications (like OTPs) must be delivered in under a second.
* **Scalability**: Ability to scale horizontally during peak loads (e.g., promotional campaigns).
* **Reliability**: No messages should be lost. Ensure at-least-once delivery semantics.

## 2. Architecture

The system uses a microservices-based, event-driven architecture.

### Components
1. **API Gateway**: Entry point for all clients. Handles rate limiting, authentication, and routing.
2. **Notification Service**: Core service that receives requests, validates payloads, and drops messages into a message queue.
3. **Preference Service**: Stores and retrieves user preferences for channels and topics.
4. **Message Broker / Queue**: Asynchronous processing queue (e.g., Apache Kafka or RabbitMQ).
5. **Workers / Senders**: Independent worker services for each channel (Email Worker, SMS Worker, Push Worker).
6. **Third-Party Providers**: Integration with external vendors (SendGrid, Twilio, Firebase Cloud Messaging).
7. **Callback/Webhook Service**: Receives delivery receipts from vendors and updates the status.

## 3. Database Choices

* **Relational DB (PostgreSQL)**: For user preferences, configuration, and tenant/client details. ACID properties ensure that preference updates are consistent.
* **NoSQL DB (Cassandra / MongoDB)**: For storing the actual notification logs and delivery statuses. This data grows exponentially and requires high write throughput.
* **Cache (Redis)**: To cache user preferences and provider configurations for fast access by the workers.

## 4. Queue Design

We will use **Apache Kafka** due to its high throughput and durability.
* **Topics**: Separate topics based on priority and channel (e.g., `email-high-priority`, `sms-otp`, `push-promotional`).
* **Partitioning**: Partition by `userId` to ensure that messages for the same user are processed in order (if required) and to evenly distribute the load across consumers.
* **Dead Letter Queue (DLQ)**: Failed messages that cannot be processed after multiple retries are pushed to a DLQ for manual inspection or later reprocessing.

## 5. APIs

* `POST /v1/notifications/send` - Send an immediate or scheduled notification.
* `GET /v1/notifications/{id}/status` - Check the delivery status.
* `PUT /v1/preferences/{userId}` - Update user preferences.
* `GET /v1/preferences/{userId}` - Get user preferences.

## 6. Scaling Strategy

* **Horizontal Scaling**: All services (API Gateway, Notification Service, Workers) are stateless and can be scaled horizontally using Kubernetes and Horizontal Pod Autoscalers (HPA).
* **Queue Scaling**: Increase the number of Kafka partitions and worker consumers to handle increased message throughput.
* **Database Scaling**: Use read replicas for the PostgreSQL database. Use sharding for the Cassandra NoSQL database based on timestamp and user ID to ensure even distribution.

## 7. Failure Handling

* **Retry Mechanism**: Workers implement exponential backoff with jitter for transient errors from third-party providers.
* **Circuit Breaker**: Use patterns like Resilience4j to stop calling a failing third-party provider and fail fast, preventing system overload.
* **Failover Providers**: If the primary SMS provider (e.g., Twilio) goes down, automatically route traffic to a secondary provider (e.g., AWS SNS).

## 8. Rate Limiting

* Implemented at the API Gateway level (e.g., using Redis fixed-window or token bucket algorithms).
* Rate limits are applied per tenant/API key to prevent abuse.
* Additional rate limits per user/channel (e.g., max 3 SMS per minute per user) enforced by the Notification Service to avoid spamming end users.

## 9. Observability

* **Metrics**: Track throughput, latency, error rates, and queue depths using Prometheus.
* **Dashboards**: Visualize metrics using Grafana (e.g., delivery success rate by provider).
* **Distributed Tracing**: Use OpenTelemetry and Jaeger to trace a notification from the API Gateway all the way to the vendor callback.
* **Logging**: Centralized logging via ELK (Elasticsearch, Logstash, Kibana) or similar stacks for debugging issues.
