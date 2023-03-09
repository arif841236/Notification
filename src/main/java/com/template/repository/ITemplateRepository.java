package com.template.repository;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.template.model.TemplateModel;

@Transactional
@Repository("postgres")
public interface ITemplateRepository extends JpaRepository<TemplateModel, Integer> {
	List<TemplateModel> findByNotificationTypeAndProcessName(String notificationType, String processName);
	List<TemplateModel> findByNotificationTypeOrProcessName(String notificationType, String processName);
	List<TemplateModel> findByNotificationType(String notificationType);
	List<TemplateModel> findByProcessName(String processName);
}
