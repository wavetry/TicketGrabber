package com.ticket.grabber.data.local

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ticket.grabber.data.model.Station

/**
 * 车站数据初始化器
 * 从assets目录加载车站数据到数据库
 */
object StationDataInitializer {
    
    private const val TAG = "StationDataInitializer"
    private const val STATION_JSON_FILE = "stations.json"
    
    /**
     * 初始化车站数据
     * 从assets加载，如果数据库为空则加载
     */
    suspend fun initializeData(context: Context, dao: StationDao) {
        try {
            // 检查是否已存在数据
            val existingCount = dao.count()
            if (existingCount > 0) {
                Log.d(TAG, "Station data already initialized: $existingCount records")
                return
            }
            
            // 从assets加载JSON
            val json = loadStationJson(context.assets)
            if (json.isNullOrBlank()) {
                Log.e(TAG, "Failed to load station JSON from assets")
                return
            }
            
            // 解析JSON
            val stations = parseStationsFromJson(json)
            if (stations.isEmpty()) {
                Log.e(TAG, "Failed to parse stations from JSON")
                return
            }
            
            // 插入数据库
            dao.insertAll(stations)
            Log.d(TAG, "Initialized ${stations.size} stations")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing station data", e)
        }
    }
    
    /**
     * 从assets加载JSON文件
     */
    private fun loadStationJson(assetManager: AssetManager): String? {
        return try {
            val inputStream = assetManager.open(STATION_JSON_FILE)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer)
        } catch (e: Exception) {
            Log.e(TAG, "Error reading station JSON", e)
            null
        }
    }
    
    /**
     * 解析JSON格式的车站数据
     */
    private fun parseStationsFromJson(json: String): List<Station> {
        return try {
            val gson = Gson()
            val type = object : TypeToken<List<Station>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing station JSON", e)
            emptyList()
        }
    }
}
