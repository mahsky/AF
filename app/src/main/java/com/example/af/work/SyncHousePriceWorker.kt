package com.example.af.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.af.framework.net.NetworkResponse
import com.af.repository.LianJiaRepository
import com.af.repository.common.onFailure
import com.af.repository.common.onSuccess
import com.example.af.db.DB
import com.example.af.main.viewmodel.HtmlParse
import kotlinx.coroutines.delay

/**
 * Created by mah on 2022/3/16.
 */
class SyncHousePriceWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        DB.db.houseDao().getAllSuspend().forEach { house ->
            val response = LianJiaRepository()
                .dongYiQu(house.id)
                .onFailure { error ->
                    println("---${house.id} ${error.status} ${error.id}")
                }.onSuccess { text ->
                    println("---${house.id}  http request success")
                }

            val html = when (response) {
                is NetworkResponse.Success -> response.data
                else -> {
                    return@forEach
                }
            }

            HtmlParse.parseHtml(house, html)
            println("---${house.id}  parseHtml")
            delay((1000..2000).random().toLong())
        }
        return Result.success()
    }
}