package com.alagamb.petcompose.repo.request

import com.alagamb.petcompose.data.db.dao.request.RequestDao
import com.alagamb.petcompose.data.db.table.request.RequestTable
import com.alagamb.petcompose.data.model.product.ProductItem
import com.alagamb.petcompose.data.model.request.PetRequestItem
import com.alagamb.petcompose.data.model.request.toDomain
import com.alagamb.petcompose.util.ResultCallBack
import com.alagamb.petcompose.util.toFailure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestRepositoryImpl @Inject constructor(
    private val requestDao: RequestDao
) : RequestRepository {

    override suspend fun sendRequest(product: ProductItem, quantity: Int): ResultCallBack<String> = withContext(Dispatchers.IO) {
        try {
            val requestId = UUID.randomUUID().toString()
            val priceNumber = product.price.replace("$", "").trim().toDoubleOrNull() ?: 0.0
            val formattedTotalPrice = String.format("$%.2f", priceNumber * quantity)

            val requestTable = RequestTable(
                id = requestId,
                petId = product.id,
                petName = product.name,
                petCategory = product.category,
                petPrice = product.price,
                petDescription = product.description,
                tag = product.tag,
                quantity = quantity,
                totalPrice = formattedTotalPrice,
                status = "Pending",
                createdAt = System.currentTimeMillis()
            )

            requestDao.insertRequest(requestTable)
            ResultCallBack.Success(requestId)
        } catch (e: Exception) {
            e.toFailure()
        }
    }

    override fun getAllRequests(): Flow<List<PetRequestItem>> {
        return requestDao.getAllRequests()
            .map { list -> list.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
    }

    override fun getRequestById(id: String): Flow<PetRequestItem?> {
        return requestDao.getRequestById(id)
            .map { it?.toDomain() }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun deleteRequest(requestId: String) = withContext(Dispatchers.IO) {
        requestDao.deleteRequest(requestId)
    }

    override suspend fun clearAllRequests() = withContext(Dispatchers.IO) {
        requestDao.clearAllRequests()
    }
}
