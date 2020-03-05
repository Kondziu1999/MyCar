package com.KOndziu.notificationservice.dao;

import com.KOndziu.notificationservice.modules.UserPreference;
import com.KOndziu.notificationservice.specifications.SearchCriteria;

import java.util.List;

public interface IUserPreferenceDAO {

    public List<UserPreference> searchUserPreference(List<SearchCriteria> params);
    List<UserPreference> findAll();
}
