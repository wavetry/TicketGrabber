package com.ticket.grabber.data.repository.local

import com.ticket.grabber.data.local.StationDao
import com.ticket.grabber.data.model.Station
import com.ticket.grabber.data.repository.StationRepository
import kotlinx.coroutines.flow.Flow

/**
 * 车站本地仓库实现
 * 仅使用本地数据库
 */
class LocalStationRepository(
    private val dao: StationDao
) : StationRepository {
    
    private val stationNameMap = mutableMapOf<String, String>()
    private val stationCodeMap = mutableMapOf<String, String>()
    
    override suspend fun getAllStations(): List<Station> {
        return dao.getAll().also {
            buildStationMaps(it)
        }
    }
    
    override suspend fun getStationByCode(code: String): Station? {
        return dao.getByCode(code)
    }
    
    override suspend fun getStationCodeByName(name: String): String? {
        buildStationMapsIfEmpty()
        return stationNameMap.entries.find { it.value == name }?.key
    }
    
    override fun searchStations(keyword: String): Flow<List<Station>> {
        return dao.search(keyword)
    }
    
    override suspend fun saveStations(stations: List<Station>) {
        dao.insertAll(stations)
        buildStationMaps(stations)
    }
    
    override suspend fun getStationNameMap(): Map<String, String> {
        buildStationMapsIfEmpty()
        return stationNameMap
    }
    
    override suspend fun getStationCodeMap(): Map<String, String> {
        buildStationMapsIfEmpty()
        return stationCodeMap
    }
    
    private fun buildStationMapsIfEmpty() {
        if (stationNameMap.isEmpty()) {
            buildStationMaps(dao.getAll())
        }
    }
    
    private fun buildStationMaps(stations: List<Station>) {
        stationNameMap.clear()
        stationCodeMap.clear()
        stations.forEach { station ->
            stationNameMap[station.code] = station.name
            stationCodeMap[station.name] = station.code
        }
    }
}
