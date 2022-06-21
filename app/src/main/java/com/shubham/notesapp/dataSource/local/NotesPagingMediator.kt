package com.shubham.notesapp.dataSource.local

//@OptIn(ExperimentalPagingApi::class)
//class NotesPagingMediator(
//    private val notesDao: NotesDao
//): RemoteMediator<Int, Note>() {
//    override suspend fun load(loadType: LoadType, state: PagingState<Int, Note>): MediatorResult {
//        return try {
//
//            val nextPage = params.key ?: 1
//
//        } catch (e: Exception) {
//
//        }
//    }
//}