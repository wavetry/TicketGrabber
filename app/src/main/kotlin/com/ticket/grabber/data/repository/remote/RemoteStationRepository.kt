package com.ticket.grabber.data.repository.remote

import com.ticket.grabber.data.api.TicketApi
import com.ticket.grabber.data.local.StationDao
import com.ticket.grabber.data.model.Station
import com.ticket.grabber.data.model.StationNameMap
import com.ticket.grabber.data.repository.StationRepository
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject

/**
 * 车站远程仓库实现
 * 结合API和本地数据库
 */
class RemoteStationRepository(
    private val api: TicketApi,
    private val dao: StationDao
) : StationRepository {
    
    private val stationNameMap = mutableMapOf<String, String>()
    private val stationCodeMap = mutableMapOf<String, String>()
    
    override suspend fun getAllStations(): List<Station> {
        // 优先从本地获取
        val localStations = dao.getAll()
        if (localStations.isNotEmpty()) {
            buildStationMaps(localStations)
            return localStations
        }
        
        // 从API获取并保存到本地
        return try {
            val apiResponse = api.getStations()
            // 解析并保存
            val stations = parseStations(apiResponse.string())
            dao.insertAll(stations)
            buildStationMaps(stations)
            stations
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    override suspend fun getStationByCode(code: String): Station? {
        return dao.getByCode(code) ?: run {
            // 如果本地没有，从API获取
            null
        }
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
            try {
                val localStations = dao.getAll()
                if (localStations.isNotEmpty()) {
                    buildStationMaps(localStations)
                } else {
                    val apiResponse = api.getStations()
                    val stations = parseStations(apiResponse.string())
                    dao.insertAll(stations)
                    buildStationMaps(stations)
                }
            } catch (e: Exception) {
                // 使用静态数据作为回退
                buildDefaultStationMaps()
            }
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
    
    private fun buildDefaultStationMaps() {
        // 默认车站映射
        val defaultMap = mapOf(
            "BJP" to "北京",
            "SHH" to "上海",
            "GZQ" to "广州",
            "SZQ" to "深圳",
            "CQ" to "重庆",
            "CD" to "成都",
            "HZH" to "杭州",
            "NJ" to "南京",
            "WH" to "武汉",
            "CX" to "长沙",
            "ZZ" to "郑州",
            "TX" to "天津",
            "SHZ" to "上海虹桥",
            "SHH" to "上海南",
            "GZ" to "广州南",
            "SZ" to "深圳北"
        )
        stationNameMap.clear()
        stationCodeMap.clear()
        defaultMap.forEach { (code, name) ->
            stationNameMap[code] = name
            stationCodeMap[name] = code
        }
    }
    
    private fun parseStations(json: String): List<Station> {
        val stations = mutableListOf<Station>()
        
        try {
            // 12306 API 返回的格式通常是: "code":"success","data":{"stations":[{"code":"BJP","name":"北京","pinyin":"beijing","city":"北京","province":"北京","isCapital":true}]}
            val jsonObject = JSONObject(json)
            if (jsonObject.getString("code") == "success") {
                val data = jsonObject.getJSONObject("data")
                val stationsArray = data.getJSONArray("stations")
                
                for (i in 0 until stationsArray.length()) {
                    val stationObj = stationsArray.getJSONObject(i)
                    val station = Station(
                        code = stationObj.getString("code"),
                        name = stationObj.getString("name"),
                        pinyin = stationObj.getString("pinyin", ""),
                        city = stationObj.getString("city", ""),
                        province = stationObj.getString("province", ""),
                        isCapital = stationObj.optBoolean("isCapital", false)
                    )
                    stations.add(station)
                }
            }
        } catch (e: Exception) {
            // 如果解析失败，尝试其他格式
            parseStationsFallback(json)?.let { stations.addAll(it) }
        }
        
        return stations
    }
    
    private fun parseStationsFallback(json: String): List<Station>? {
        // 尝试解析其他格式
        if (json.contains("stations")) {
            val stations = mutableListOf<Station>()
            try {
                val arr = json.removePrefix("\"").removeSuffix("\"").toCharArray().joinToString("") {
                    when {
                        it == '\\' -> ""
                        else -> it.toString()
                    }
                }
                // 尝试JSON数组格式
                if (arr.startsWith("[")) {
                    val array = JSONArray(arr)
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        stations.add(Station(
                            code = obj.optString("code", obj.optString("station_no", "")),
                            name = obj.optString("name", obj.optString("station_name", "")),
                            pinyin = obj.optString("pinyin", ""),
                            city = obj.optString("city", ""),
                            province = obj.optString("province", "")
                        ))
                    }
                }
            } catch (e: Exception) {
                return null
            }
            return stations
        }
        return null
    }
}
