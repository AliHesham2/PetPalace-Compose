package com.alagamb.petcompose.repo.request

import com.alagamb.petcompose.data.model.product.ProductItem
import com.alagamb.petcompose.data.model.request.PetRequestItem
import com.alagamb.petcompose.util.ResultCallBack
import kotlinx.coroutines.flow.Flow

interface RequestRepository {
    suspend fun sendRequest(product: ProductItem, quantity: Int = 1): ResultCallBack<String>
    fun getAllRequests(): Flow<List<PetRequestItem>>
    fun getRequestById(id: String): Flow<PetRequestItem?>
    suspend fun deleteRequest(requestId: String)
    suspend fun clearAllRequests()
}
