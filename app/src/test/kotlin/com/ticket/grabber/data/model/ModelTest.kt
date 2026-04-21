package com.ticket.grabber.data.model

import org.junit.Test
import org.junit.Assert.*

/**
 * 数据模型单元测试
 */
class ModelTest {

    @Test
    fun stationCreation_isCorrect() {
        val station = Station(
            stationCode = "BJP",
            stationName = "北京",
            pinyin = "beijing",
            pinyinShort = "bj"
        )
        
        assertEquals("BJP", station.stationCode)
        assertEquals("北京", station.stationName)
        assertEquals("beijing", station.pinyin)
        assertEquals("bj", station.pinyinShort)
    }

    @Test
    fun trainCreation_isCorrect() {
        val train = Train(
            trainNo = "G123",
            trainCode = "G123",
            fromStation = "北京",
            toStation = "上海",
            departureTime = "08:00",
            arrivalTime = "12:00",
            duration = "4小时"
        )
        
        assertEquals("G123", train.trainCode)
        assertEquals("北京", train.fromStation)
        assertEquals("上海", train.toStation)
    }

    @Test
    fun grabTaskStatusValues_areCorrect() {
        assertEquals(0, GrabTaskStatus.PENDING.code)
        assertEquals(1, GrabTaskStatus.RUNNING.code)
        assertEquals(2, GrabTaskStatus.SUCCESS.code)
        assertEquals(3, GrabTaskStatus.FAILED.code)
        assertEquals(4, GrabTaskStatus.CANCELLED.code)
    }

    @Test
    fun grabTaskCreation_isCorrect() {
        val task = GrabTask(
            taskId = "task_123",
            trainNo = "G123",
            trainCode = "G123",
            fromStation = "北京",
            toStation = "上海",
            departureDate = "2024-05-01",
            seatType = "二等座",
            passengerIds = "passenger_1",
            status = GrabTaskStatus.PENDING.code,
            retryCount = 0,
            maxRetryCount = 100,
            queryInterval = 5000L
        )
        
        assertEquals("task_123", task.taskId)
        assertEquals(GrabTaskStatus.PENDING.code, task.status)
        assertEquals(0, task.retryCount)
    }
}
