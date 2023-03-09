package com.template.service;

import com.template.model.common.TemplateResponce;
import com.template.model.common.FetchAllTemplateResponce;
import com.template.model.common.FetchTableAtributeRequest;
import com.template.model.common.TemplateRequest;

public interface ITemplateService {

	public TemplateResponce saveTemplateModel(TemplateRequest tempRequest) ;

	public FetchAllTemplateResponce getAllTemplate();
	
	public TemplateResponce getTemplateById(Integer id);
	
	public TemplateResponce deleteTemplate(Integer id);
	
	public FetchAllTemplateResponce fetchTemplateByAtribute(FetchTableAtributeRequest atributeRequest);
}
