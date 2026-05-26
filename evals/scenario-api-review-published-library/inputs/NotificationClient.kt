package com.acme.notify

/**
 * Represents a single notification record.
 */
data class Notification(
    val id: String,
    val channel: String,
    val recipient: String,
    val body: String
)

data class DeliveryReport(
    val notificationId: String,
    val status: String,
    val attemptCount: Int
)

class NotificationClient(private val apiKey: String) {

    fun send(notification: Notification, retries: Int = 3) = dispatch(notification, retries)

    fun sendBatch(notifications: List<Notification>, retries: Int = 3) = dispatchBatch(notifications, retries)

    fun statusFor(notificationId: String) = fetchStatus(notificationId)

    fun version() = "1.4.0"

    private fun dispatch(notification: Notification, retries: Int): DeliveryReport {
        TODO("implementation")
    }

    private fun dispatchBatch(notifications: List<Notification>, retries: Int): List<DeliveryReport> {
        TODO("implementation")
    }

    private fun fetchStatus(id: String): DeliveryReport? {
        TODO("implementation")
    }
}
