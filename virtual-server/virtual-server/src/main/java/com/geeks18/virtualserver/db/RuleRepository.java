package com.geeks18.virtualserver.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.geeks18.virtualserver.hibernate.model.MainRuleModel;
@Repository
public interface RuleRepository extends CrudRepository<MainRuleModel,Integer> {

}
