package com.ccx.corecarbon.room_util;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface FormDao {

    @Query("SELECT * FROM  form")
    List<Form> getAllForms();

    @Query("DELETE FROM form")
    void ClearData();

    @Insert
    void insertForm(Form... forms);

    @Query("DELETE FROM form WHERE form_id = :form_id")
    void deleteByUserId(long form_id);

    @Query("SELECT * FROM form WHERE isOnlineRecord = :isOnlineRecord")
    List<Form> getOfflineRecords(boolean isOnlineRecord);

    @Query("SELECT * FROM form ORDER BY isOnlineRecord DESC")
    List<Form> getOfflineRecordsOrderByOfflineRecords();

    @Query("SELECT count(1) FROM form ORDER BY isOnlineRecord DESC")
    long getOfflineRecordCount();

    @Query("DELETE FROM form WHERE isOnlineRecord = :isOnlineRecord")
    void deleteAssignedOnlineList(boolean isOnlineRecord);
}
