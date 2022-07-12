package com.example.noteappwithhilt.repository

import androidx.lifecycle.MutableLiveData
import com.example.noteappwithhilt.data.remote.NoteAPI
import com.example.noteappwithhilt.data.remote.response.NoteRequest
import com.example.noteappwithhilt.data.remote.response.NoteResponse
import com.example.noteappwithhilt.utils.NetworkResult
import retrofit2.Response
import org.json.JSONObject
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteApi: NoteAPI) {

    private val _noteLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val noteLiveData get() = _noteLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<Pair<Boolean, String>>>()
    val statusLiveData get() = _statusLiveData

    suspend fun getNotes(){
        _noteLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.getNotes()
        //move this to general class
        if (response.isSuccessful && response.body() != null) {
            _noteLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _noteLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }
        else{
            _noteLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    suspend fun createNote(noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.createNote(noteRequest)
        handleResponse(response, "Note Created")
    }

    suspend fun updateNote(id: String, noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.updateNote(id, noteRequest)
        handleResponse(response, "Note Updated")
    }

    suspend fun deleteNote(noteId: String) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.deleteNote(noteId)
        handleResponse(response, "Note Deleted")
    }

    private fun handleResponse(response: Response<NoteResponse>, message: String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(Pair(true, message)))
        } else {
            _statusLiveData.postValue(NetworkResult.Success(Pair(false, "Something went wrong")))
        }
    }

}